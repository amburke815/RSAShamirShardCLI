package tests;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.apache.commons.lang3.builder.*;
import org.junit.Test;

public class KeyShardingTests {
//   RandomStringUtils s;
  RandomStringUtils rsu = new RandomStringUtils();
  int randomStringLength = 100;
  public String randomStringPlainText = RandomStringUtils.random(randomStringLength, true, true);

  @Test
  public void produceRandomString() {
    System.out.println(randomStringPlainText);
  }
}
