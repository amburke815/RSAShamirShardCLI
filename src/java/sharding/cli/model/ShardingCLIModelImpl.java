package sharding.cli.model;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sharding.Utils;
import com.codahale.shamir.Scheme;
import java.security.SecureRandom;

public class ShardingCLIModelImpl implements IShardingCLIModel {

  private static int defaultKeySize = 2048;
  private static String keyType = "RSA";
  private Scheme scheme;
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
  public Map<Integer, byte[]> shamirShardKey(KeyPair toShard, int numTotalShards, int minShardsToCreate) {
    scheme = new Scheme(new SecureRandom(), numTotalShards, minShardsToCreate);
    final byte[] privateKeyBytes = toShard.getPrivate().getEncoded();
    shardsMap = scheme.split(privateKeyBytes);
    return shardsMap;

  }

  @Override
  public void writeShards(List<byte[]> shards) {

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
      throw new IllegalStateException("caught an " + e.getClass() + " when encrypting ciphertext" + e.getMessage());
    }
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
      throw new IllegalStateException("caught an " + e.getClass() + " when encrypting ciphertext \n\n" + e.getMessage());
    }
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
    for(int i : shardIndices) {
      if (shardsMap.containsKey(i))
        presentedShards.putIfAbsent(i, shardsMap.get(i));
    }


    return Utils.notNull(scheme).join(presentedShards);


  }

  private final int checkRSAKeySize(int size) throws InsecureRSAKeySizeException {
    if (size % 2048 != 0) {
      throw new InsecureRSAKeySizeException();
    }

    return size;
  }
}
