package sharding.cli;

import java.security.KeyPair;
import java.util.List;
import sharding.shards.IKeyShard;

public interface IShardingCLI {

  String helpMenu();

  KeyPair RSAKeyGen();

  KeyPair RSAKeyGen(int keySize);

  List<IKeyShard> shamirShardKey(KeyPair toShard, int numShards);

  void writeShards(List<IKeyShard> shards);

  byte[] encrypt(String plainText, KeyPair encryptWith);

  byte[] encrypt(String plainText, List<IKeyShard> shards);

  String decrypt(byte[] cipherTextBytes, KeyPair decryptWith);

  String decrypt(byte[] cipherTextBytes, List<IKeyShard> shards);

  KeyPair assembleShards(List<IKeyShard> shards);



}
