package sharding.cli;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;

public interface IShardingCLI {

  String helpMenu();

  KeyPair RSAKeyGen();

  KeyPair RSAKeyGen(int keySize);

  Map<Integer, byte[]> shamirShardKey(KeyPair toShard, int numTotalShards, int minShardsToCreate);

  void writeShards(List<byte[]> shards);

  byte[] encrypt(String plainText, PublicKey encryptWith);

  String decrypt(byte[] cipherTextBytes, PrivateKey decryptWith);

  // String decrypt(byte[] cipherTextBytes, List<byte[]> shards);

  byte[] assembleShards(int... shardIndices);



}
