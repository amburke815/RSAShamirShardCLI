package sharding.cli.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sharding.Utils;
import com.codahale.shamir.Scheme;
import java.security.SecureRandom;

public class ShardingCLIModelImpl implements IShardingCLIModel {

  private static int defaultKeySize = 2048;
  private static final String keyType = "RSA";
  private static final String path = "./Keys/";
  private Scheme scheme;
  private KeyPair generatedKeyPair;
  private Map<Integer, byte[]> shardsMap;

  @Override
  public String helpMenu() {
    return null;
  }

  @Override
  public KeyPair RSAKeyGen() {
    return RSAKeyGen(defaultKeySize);
  }

  @Override
  public KeyPair RSAKeyGen(int keySize) {
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
      int minShardsToCreate) {
    scheme = new Scheme(new SecureRandom(), numTotalShards, minShardsToCreate);
    final byte[] privateKeyBytes = toShard.getPrivate().getEncoded();
    shardsMap = scheme.split(privateKeyBytes);
    return shardsMap;

  }

  @Override
  public Map<Integer, byte[]> shamirShardKey(int numTotalShards, int minShardsToCreate)
      throws NoSuchKeyException {
    return shamirShardKey(checkKeyPairGenerated(generatedKeyPair), numTotalShards,
        minShardsToCreate);
  }

  @Override
  public void writePublicKey(PublicKey toWrite) {
    File f = new File(path + "Public.TXT");
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
    File toWrite = new File(path + "Shard" + (shardIdx + 1) + ".TXT");
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
      encCipher.init(Cipher.ENCRYPT_MODE, encryptWith);
      byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
      System.out.println("\n\n++plaintext size: " + plainTextBytes.length);
      System.out.println("\n++BLOCK SIZE: " + encCipher.getBlockSize());
      return encCipher.doFinal(plainTextBytes);
    } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
        BadPaddingException | NoSuchPaddingException e) {
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
      decCipher.init(Cipher.DECRYPT_MODE, decryptWith);
      final byte[] plainTextBytes = decCipher.doFinal(cipherTextBytes);

      return new String(plainTextBytes, StandardCharsets.US_ASCII);
    } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
        BadPaddingException | NoSuchPaddingException e) {
      throw new IllegalStateException(
          "caught an " + e.getClass() + " when encrypting ciphertext \n\n" + e.getMessage());
    }
  }

  @Override
  public String decrypt(byte[] cipherTextBytes) throws NoSuchKeyException {
    return decrypt(cipherTextBytes, checkKeyPairGenerated(generatedKeyPair).getPrivate());
  }


//  @Override
//  public String decrypt(byte[] cipherTextBytes, List<byte[]> shards) {
//    return decrypt(cipherTextBytes, assembleShards(shards));
//  }

  @Override
  public byte[] assembleShards(int... shardIndices) {
    if (shardsMap == null) {
      throw new IllegalStateException("Cannot assemble key shards. Never sharded using Shamir");
    }

    Map<Integer, byte[]> presentedShards = new HashMap<>();
    for (int i : shardIndices) {
      if (shardsMap.containsKey(i)) {
        presentedShards.putIfAbsent(i, shardsMap.get(i));
      }
    }

    return Utils.notNull(scheme).join(presentedShards);


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
   * Returns the shards map if this is true, throws a <code>NoSuchKeyException</code> otherwise.
   *
   * @param toCheck the shards map to check for nullity.
   * @return the given shards map if it is not null
   * @throws NoSuchKeyException if the shards map is null
   */
  private static Map<Integer, byte[]> checkShardsMapGenerated(Map<Integer, byte[]> toCheck)
      throws NoSuchKeyException {
    if (toCheck == null) {
      throw new NoSuchKeyException();
    }

    return toCheck;
  }

}
