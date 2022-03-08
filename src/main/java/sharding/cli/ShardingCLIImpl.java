package sharding.cli;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import sharding.shards.IKeyShard;

public class ShardingCLIImpl implements IShardingCLI {

  private static int defaultKeySize = 2048;

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
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
      kpg.initialize(keySize);
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
  public String encrypt(String plainText, KeyPair encryptWith) {
    return null;
  }

  @Override
  public String encrypt(String plainText, List<IKeyShard> shards) {
    return null;
  }

  @Override
  public String decrypt(String cipherText, KeyPair decryptWith) {
    return null;
  }

  @Override
  public String decrypt(String cipherText, List<IKeyShard> shards) {
    return null;
  }

  @Override
  public KeyPair assembleShards(List<IKeyShard> shards) {
    return null;
  }
}
