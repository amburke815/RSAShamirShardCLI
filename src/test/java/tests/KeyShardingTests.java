package tests;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Map.Entry;
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
