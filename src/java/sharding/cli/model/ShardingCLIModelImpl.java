package sharding.cli.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import sharding.Utils;
import com.codahale.shamir.Scheme;
import java.security.SecureRandom;


/**
 * A simple implementation of the <code>IShardingCLIModel</code> interface that has the following
 * distinguishing features:
 * <ul>
 *   <li>Enforces that RSA keys are of size 2048q, where q >= 1 is a positive integer. This
 *   ensures that RSA keys are secure against brute-force and factoring attacks. i.e. If Eve
 *   were to try and compute Bob's private key modulus N and encryption exponent e, she would
 *   have to do so by guessing all possible combinations of N and e, or factoring N and pairing it
 *   with every possible e, which with a key size of 2048 bits is secure enough for most computers
 *   in 2022</li>
 *   <li>Has two I/O methods that write text files to memory, describing the public key and Shamir
 *   shards. These files are protected by calling a method whose effect is equivalent to "chmod 777"
 *   posix privilege modification. This means that the files written to memory require sudo/admin
 *   access (for linux/windows, resp.) to read, write, and execute. This functionality also guarantees
 *   that only the owner of these files has access to them, making the CLI secure for distributed
 *   systems</li>
 *   <li>Contains a few fields that track the state of an instance of this class, namely
 *   a <code>Scheme, KeyPair, Map, </code> and <code>PrivateKey</code>. These fields allow the
 *   developer to implement twin overloaded method signatures where one takes one or more of these types,
 *   and the other does not explicitly take one of these types, but uses the object encapsulated in
 *   this instance. For example, implementing <code>encrypt(String, KeyPair)</code> and its twin
 *   overloaded method <code>encrypt(String)</code> is simple, since the former just uses the
 *   <code>KeyPair</code> field of this instance that was set in <code>RSAKeyGen()</code>
 *   </li>
 *   <li>In the method signatures that omit objects that appear as states in an instance of this class,
 *   those methods throw custom exceptions that indicate various failures of a sequence-based
 *   operation of this model. For example, if we were to try and encrypt a message <code>m</code>
 *   using the <code>encrypt</code> signature that omits a <code>KeyPair</code> object and instead
 *   uses the <code>KeyPair</code> field of this class. If that field were never set by
 *   <code>RSAKeyGen()</code>, then it would be <code>null</code> and a custom exception would be thrown.
 *   The reason for this is to make these errors easily handleable by a controller/view implementation
 *   that can give specific feedback directly in the CLI.</li>
 * </ul>
 */
public class ShardingCLIModelImpl implements IShardingCLIModel {

  private static int defaultKeySize = 2048;
  private static final String keyType = "RSA";
  // if I were to make the encryption scheme more secure, I would use the following padding scheme
  // to protect against chosen plaintext attacks, as well as using the RSA key to
  // encrypt an AES key to encrypt messages. However, the assignment said to encrypt with the
  // public RSA key, so I will not implement the functionality here.
  private static final String encryptionTypeFullyQualified =
      keyType + "/" + "ECB/OAEPWITHSHA-256ANDMGF1PADDING";
  private static final OAEPParameterSpec oaepParams =
      new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"),
          PSource.PSpecified.DEFAULT);
  private static final String path = "./Keys/";
  private Scheme scheme;
  private KeyPair generatedKeyPair;
  private Map<Integer, byte[]> shardsMap;
  private PrivateKey reassembledPrivateKey;

  /**
   * default ctor
   */
  public ShardingCLIModelImpl() {
  }

  /**
   * ctor to set both key pair and shards map field
   *
   * @param keyPair   the key pair that this model holds
   * @param shardsMap the shard map that this model holds.
   */
  public ShardingCLIModelImpl(KeyPair keyPair, Map<Integer, byte[]> shardsMap) {
    this.generatedKeyPair = keyPair;
    this.shardsMap = shardsMap;
  }


  // getter for shards map
  public Map<Integer, byte[]> getShardsMap() {
    return shardsMap;
  }

  // getter for reassembled private key
  public PrivateKey getReassembledPrivateKey() {
    return reassembledPrivateKey;
  }

  @Override
  public String helpMenu() {
    return null;
  }

  @Override
  public KeyPair RSAKeyGen() {
    return RSAKeyGen(defaultKeySize);
  }

  @Override
  public KeyPair RSAKeyGen(int keySize)
      throws InsecureRSAKeySizeException {
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyType);
      kpg.initialize(Utils.intBetween(1, checkRSAKeySize(keySize),
          Integer.MAX_VALUE));

      return kpg.generateKeyPair();

    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Illegal State: RSA key generation failed.");
    }
  }

  @Override
  public Map<Integer, byte[]> shamirShardKey(KeyPair toShard, int numTotalShards,
      int minShardsToCreate)
      throws IllegalShamirShardingParametersException {
    checkShamirShardingParameters(numTotalShards, minShardsToCreate);
    try {
      scheme = new Scheme(new SecureRandom(), numTotalShards, minShardsToCreate);
    } catch (IllegalArgumentException e) {
      throw new IllegalShamirShardingParametersException();
    }
    final byte[] privateKeyBytes = toShard.getPrivate().getEncoded();
    shardsMap = scheme.split(privateKeyBytes);
    return shardsMap;

  }

  @Override
  public Map<Integer, byte[]> shamirShardKey(int numTotalShards, int minShardsToCreate)
      throws NoSuchKeyException, IllegalShamirShardingParametersException {
    return shamirShardKey(checkKeyPairGenerated(generatedKeyPair), numTotalShards,
        minShardsToCreate);
  }

  @Override
  public void writePublicKey(PublicKey toWrite) {
    File f = new File(path + "Public.TXT");
    Utils.chmod000(f);

    try {
      OutputStream os = new FileOutputStream(f);
      os.write(toWrite.getEncoded());
    } catch (IOException e) {
      System.out.println("Caught an IOException when trying to write Public.TXT to memory");
    }
  }

  @Override
  public void writePublicKey() throws NoSuchKeyException {
    writePublicKey(checkKeyPairGenerated(generatedKeyPair).getPublic());
  }

  @Override
  public void writeShards(Map<Integer, byte[]> shards) {
    for (Entry<Integer, byte[]> e : shards.entrySet()) {
      writeShard(e.getKey(), e.getValue());
    }
  }

  @Override
  public void writeShards() throws NoSuchKeyException {
    writeShards(checkShardsMapGenerated(shardsMap));
  }

  /**
   * Writes a given shard in the <code>Map &lt Integer, byte[]&gt</code> returned from
   * <code>shamirShardKey</code> to a TXT file <i>Shard[k].TXT</i>, where <i>k</i> is the index
   * of the key shard. I.e. the 2nd shard would be written to <i>Shard2.TXT</i>
   *
   * @param shardIdx   the (1-based) index of the shard to be written to memory
   * @param shardBytes the shard's value, as a byte array
   */
  private static void writeShard(int shardIdx, byte[] shardBytes) {
    File toWrite = new File(path + "Shard" + shardIdx + ".TXT");
    Utils.chmod000(toWrite);
    try {
      OutputStream os = new FileOutputStream(toWrite);
      os.write(shardBytes);
      os.close();
    } catch (IOException e) {
      System.out.println("Caught an IOException when trying to write shard " + (shardIdx + 1) +
          " to memory");
    }
  }

  @Override
  public byte[] encrypt(String plainText, PublicKey encryptWith) {
    try {
      Cipher encCipher = Cipher.getInstance(keyType);
      encCipher.init(Cipher.ENCRYPT_MODE, encryptWith/*, oaepParams*/);
      byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
      //System.out.println("\n\n++plaintext size: " + plainTextBytes.length);
      //System.out.println("\n++BLOCK SIZE: " + encCipher.getBlockSize());
      return encCipher.doFinal(plainTextBytes);
    } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
        BadPaddingException | NoSuchPaddingException /*| InvalidAlgorithmParameterException*/ e) {
      // last exception above is necessary for implementing padding
      throw new IllegalStateException(
          "caught an " + e.getClass() + " when encrypting ciphertext" + e.getMessage());
    }
  }

  @Override
  public byte[] encrypt(String plainText) throws NoSuchKeyException {
    return encrypt(plainText, checkKeyPairGenerated(generatedKeyPair).getPublic());
  }

  @Override
  public String decrypt(byte[] cipherTextBytes, PrivateKey decryptWith) {
    try {
      Cipher decCipher = Cipher.getInstance(keyType);
      decCipher.init(Cipher.DECRYPT_MODE, decryptWith/*, oaepParams*/);
      final byte[] plainTextBytes = decCipher.doFinal(cipherTextBytes);

      return new String(plainTextBytes, StandardCharsets.US_ASCII);
    } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
        BadPaddingException | NoSuchPaddingException /*| InvalidAlgorithmParameterException*/ e) {
      // last exception above is necessary for implementing padding
      throw new IllegalStateException(
          "caught an " + e.getClass() + " when encrypting ciphertext \n\n" + e.getMessage());
    }
  }

  @Override
  public String decrypt(byte[] cipherTextBytes) throws NoSuchKeyException {
    return decrypt(cipherTextBytes, checkPrivateKeyReassembled(reassembledPrivateKey));
  }

//  @Override
//  public String decrypt(byte[] cipherTextBytes, List<byte[]> shards) {
//    return decrypt(cipherTextBytes, assembleShards(shards));
//  }

  @Override
  public PrivateKey assembleShards(int... shardIndices) {
    if (shardsMap == null) {
      throw new IllegalStateException("Cannot assemble key shards. Never sharded using Shamir");
    }

    Map<Integer, byte[]> presentedShards = new HashMap<>();
    for (int i : shardIndices) {
      if (shardsMap.containsKey(i)) {
        presentedShards.putIfAbsent(i, shardsMap.get(i));
      }
    }

    byte[] reassembledPrivateKeyBytes = Utils.notNull(scheme).join(presentedShards);

    try {
      KeyFactory kf = KeyFactory.getInstance("RSA");
      EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(reassembledPrivateKeyBytes);
      this.reassembledPrivateKey = kf.generatePrivate(privKeySpec);
      return reassembledPrivateKey;
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IllegalStateException(
          "Caught an " + e.getClass() + ". Could not reassemble shards to private key");
    }

  }

  @Override
  public void setRSAKey(KeyPair newKeyPair) {
    this.generatedKeyPair = newKeyPair;
  }

  @Override
  public void setShardsMap(Map<Integer, byte[]> newShardsMap) {
    this.shardsMap = newShardsMap;
  }

  /**
   * Checks that the given RSA key's size (in number of bits as a base 10 number) is a multiple of
   * 2048. Throws a <code>InsecureKeySizeException</code> if the check fails. Returns the key size
   * if the check passes
   *
   * @param size the key size to check (in number of bits as a base 10 number)
   * @return the key size if the key size is a multiple of 2048
   * @throws InsecureRSAKeySizeException if the key size is not a multiple of 2048
   */
  private static int checkRSAKeySize(int size)
      throws InsecureRSAKeySizeException {
    if (size % 2048 != 0) {
      throw new InsecureRSAKeySizeException();
    }

    return size;
  }

  /**
   * Checks that the given key pair has been generated, i.e. that it is not <code>null</code>.
   * Returns the KeyPair if this is true, throws a <code>NoSuchKeyException</code> otherwise.
   *
   * @param toCheck the <code>KeyPair</code> to check for nullity.
   * @return the given <code>KeyPair</code> if it is not null
   * @throws NoSuchKeyException if the <code>KeyPair</code> is null
   */
  private static KeyPair checkKeyPairGenerated(KeyPair toCheck)
      throws NoSuchKeyException {
    if (toCheck == null) {
      throw new NoSuchKeyException();
    }

    return toCheck;
  }

  /**
   * Checks that the given shards have been generated, i.e. that it is not <code>null</code>.
   * Returns the shards map if this is true, throws a <code>KeyNotSharded</code> otherwise.
   *
   * @param toCheck the shards map to check for nullity.
   * @return the given shards map if it is not null
   * @throws KeyNotShardedException if the shards map is null
   */
  private static Map<Integer, byte[]> checkShardsMapGenerated(Map<Integer, byte[]> toCheck)
      throws KeyNotShardedException {
    if (toCheck == null) {
      throw new KeyNotShardedException();
    }

    return toCheck;
  }

  /**
   * Checks if Shamir's Secret Sharing algorithm was called with valid paramters, i.e. if the
   * algorithm was used to break a key into n pieces where k are required to recover the key, then
   * this method checks that k <= n and n >= 1 and k > 1
   *
   * @param n the number of Shamir shards to be created
   * @param k the minimum number of Shamir shards necessary to recover the sharded key
   * @throws IllegalShamirShardingParametersException If the above conditions were not met and thus
   *                                                  Shamir's Secret Sharing Algorithm was called
   *                                                  with invalid parameters
   */
  private static void checkShamirShardingParameters(int n, int k)
      throws IllegalShamirShardingParametersException {
    if ((k > n || n < 1) && k > 1) {
      throw new IllegalShamirShardingParametersException();
    }
  }

  /**
   * Checks that the private key <code>pk</code> is non-null, and equivalently with respect to the
   * business logic of the CLI, that <code>RSAKeyGen()</code> was called to initialize the RSA key
   * used for decryption and encryption in methods that do not explicitly take an RSA key.
   *
   * @param pk the <code>KeyPair</code> to check for nullity
   * @return <code>pk</code> if it is non-null
   */
  private static PrivateKey checkPrivateKeyReassembled(PrivateKey pk) {
    if (pk == null) {
      throw new NoReassembledKeyException();
    }
    return pk;
  }

}
