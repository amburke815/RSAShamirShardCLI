package sharding;

import java.security.KeyPair;
import java.util.List;
import sharding.shards.IKeyShard;

public interface IShardingCLI {

  String helpMenu();

  KeyPair RSAKeyGen();

  List<IKeyShard> shamirShardKey(KeyPair toShard, int numShards);

  void writeShards();

  String encrypt(String plainText, KeyPair encryptWith);

  String encrypt(String plainText, List<IKeyShard> shards);

  String decrypt(String cipherText, KeyPair decryptWith);

  String decrypt(String cipherText, List<IKeyShard> shards);

  KeyPair assembleShards(List<IKeyShard> shards);



}
