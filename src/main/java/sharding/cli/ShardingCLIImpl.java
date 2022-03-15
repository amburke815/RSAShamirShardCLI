package sharding.cli;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sharding.Utils;
import sharding.shards.IKeyShard;

public class ShardingCLIImpl implements IShardingCLI {

  private static int defaultKeySize = 2048;
  private static String keyType = "RSA";

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
  public List<IKeyShard> shamirShardKey(KeyPair toShard, int numShards) {
    return null;
  }

  @Override
  public void writeShards(List<IKeyShard> shards) {

  }

  @Override
  public byte[] encrypt(String plainText, KeyPair encryptWith) {
    try {
      Cipher encCipher = Cipher.getInstance(keyType);
      encCipher.init(Cipher.ENCRYPT_MODE, encryptWith.getPublic());
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
  public byte[] encrypt(String plainText, List<IKeyShard> shards) {
    return encrypt(plainText, assembleShards(shards));
  }

  @Override
  public String decrypt(byte[] cipherTextBytes, KeyPair decryptWith) {
    try {
      Cipher decCipher = Cipher.getInstance(keyType);
      decCipher.init(Cipher.DECRYPT_MODE, decryptWith.getPrivate());
      byte[] plainTextBytes = decCipher.doFinal(cipherTextBytes);

      return new String(plainTextBytes, StandardCharsets.US_ASCII);
    } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
        BadPaddingException | NoSuchPaddingException e) {
      throw new IllegalStateException("caught an " + e.getClass() + " when encrypting ciphertext \n\n" + e.getMessage());
    }
  }

  @Override
  public String decrypt(byte[] cipherTextBytes, List<IKeyShard> shards) {
    return decrypt(cipherTextBytes, assembleShards(shards));
  }

  @Override
  public KeyPair assembleShards(List<IKeyShard> shards) {
    return null;
  }

  private final int checkRSAKeySize(int size) throws InsecureRSAKeySizeException {
    if (size % 2048 != 0) {
      throw new InsecureRSAKeySizeException();
    }

    return size;
  }
}
