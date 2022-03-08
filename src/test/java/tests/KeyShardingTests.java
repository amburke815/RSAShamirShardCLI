package tests;
import java.security.KeyPair;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.apache.commons.lang3.builder.*;
import org.junit.Test;
import sharding.cli.IShardingCLI;
import sharding.cli.ShardingCLIImpl;

public class KeyShardingTests {
//   RandomStringUtils s;
  RandomStringUtils rsu = new RandomStringUtils();
  int randomStringLength = 100;
  public String randomStringPlainText = RandomStringUtils.random(randomStringLength, true, true);
  private IShardingCLI shardingCLI = new ShardingCLIImpl();
  private KeyPair kp = shardingCLI.RSAKeyGen();

  @Test
  public void produceRandomString() {
    System.out.println(randomStringPlainText);
  }

  @Test
  public void decryptionIsInverseOfEncryption() {
    String plainText = "the quick brown fox jumps over the lazy dog";
    String encryptedMessage = shardingCLI.encrypt(plainText, kp);
    String decryptedMessage = shardingCLI.decrypt(encryptedMessage, kp);

    Assert.assertEquals(plainText, decryptedMessage);
  }
}
