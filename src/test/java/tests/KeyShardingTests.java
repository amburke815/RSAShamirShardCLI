package tests;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import sharding.cli.IShardingCLI;
import sharding.cli.ShardingCLIImpl;

public class KeyShardingTests {
//   RandomStringUtils s;
  RandomStringUtils rsu = new RandomStringUtils();
  int randomStringLength = 256;
  public String randomStringPlainText = RandomStringUtils.random(randomStringLength, true, true);
  private IShardingCLI shardingCLI = new ShardingCLIImpl();
  private KeyPair kp = shardingCLI.RSAKeyGen();


  @Test
  public void programWorksCorrectly() {
    IShardingCLI cliInstance = new ShardingCLIImpl();
    // 1. creates the RSA key pair with a private key broken into 5 shards
    KeyPair generatedRSAKeyPair = cliInstance.RSAKeyGen();
    cliInstance.shamirShardKey(generatedRSAKeyPair, 5, 2);

    // 2. encrypts a random plain text string using the RSA key
    String randomPlainText = RandomStringUtils.random(200, true, true);
    byte[] encryptedBytes = cliInstance.encrypt(randomPlainText, generatedRSAKeyPair.getPublic());

    // 3. reassembles the private key using shards 2 and 5
    byte[] reassembledPrivateKeyBytes = cliInstance.assembleShards(2, 5);
    // TODO: make the above a private key (possibly using KeyFactory)
    try {
      KeyFactory kf = KeyFactory.getInstance("RSA");
      EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(reassembledPrivateKeyBytes);
      PrivateKey privateKey = kf.generatePrivate(privKeySpec);
      // 4. decrypts the cipher text back into the plain text using the reassembled Private Key
      String decrypted = cliInstance.decrypt(encryptedBytes, privateKey);
      // 5. asserts the decrypted plain text is equal to the original plain text in step 2
      Assert.assertEquals(randomPlainText, decrypted);

    } catch (Exception e) {
      System.out.println(e.getMessage());
      Assert.fail();
    }




  }

  @Test
  public void produceRandomString() {
    System.out.println(randomStringPlainText);
  }

  @Test
  public void decryptionIsInverseOfEncryption() {
    String plainText = randomStringPlainText;
    //System.out.println(plainText.length());
    plainText = "the quick brown fox jumps over the lazy dog";
    byte[] encryptedMessage = shardingCLI.encrypt(plainText, kp.getPublic());
    System.out.println("encrypted message from byte arr: " + new String(encryptedMessage, StandardCharsets.US_ASCII));
    String decryptedMessage = shardingCLI.decrypt(encryptedMessage, kp.getPrivate());
    System.out.println(decryptedMessage);

    Assert.assertEquals(plainText, decryptedMessage);
  }


}
