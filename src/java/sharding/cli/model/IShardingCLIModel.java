package sharding.cli.model;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;

/**
 * A model interface to handle the backend operations of the sharding CLI. Exposes functionality
 * that allows the user to:
 * <ol>
 *   <li>Generate an RSA key pair</li>
 *   <li>Generate key shards using Shamir's secret sharing algorithm</li>
 *   <li>Write all key shards to memory</li>
 *   <li>Encrypt a string using the generated RSA key pair's public key</li>
 *   <li>Decrypt an encrypted byte array using the RSA key pair's private key</li>
 *   <li>Assemble the RSA key obtained from Sharding the generated RSA key from a given set of key
 *   shard indices</li>
 * </ol>
 */
public interface IShardingCLIModel {

  String helpMenu();

  /**
   * Generates an RSA Key Pair of default size--typically 2048 to provide security against
   * brute-force attacks--for Alice and Bob to send secure messages to each other. Alice can use the
   * RSA Key Pair's public key to encrypt and send a message to Bob. Bob can use the RSA Key Pair's
   * private key to decrypt ciphertexts that Alice has sent to him.
   * <p>
   * Suppose Eve is spying on Bob. Eve has access to the public key but not the private key. This
   * means that Eve also has access to the ciphertext that Alice sent. To launch an attack on Bob,
   * Eve must efficiently factor the public key modulus, say N = pq, or equivalently compute the
   * euler totient of N. For a sufficiently large N (on the order of 2^11 or greater), this ensures
   * that Eve cannot mount an efficient attack, securing this key against brute-force attacks.
   * <p>
   * Note that this method should be called first when implementing CLI functionality in the
   * view/controller, as it is nonsensical to shard a key or encrypt when a key is not generated.
   *
   * @return The RSA key pair to be used to encrypt and decrypt messages, and break into shards in
   * the CLI.
   */
  KeyPair RSAKeyGen();

  /**
   * Provides the same functionality as <code>RSAKeyGen()</code> but allows the developer to specify
   * a <code>keySize</code> to publish the key with.
   *
   * @param keySize the size (in bits) of the RSA key pair to be generated, as an integer
   * @return The RSA key pair to be used to encrypt and decrypt messages, and break into shards in
   * the CLI.
   * @throws InsecureRSAKeySizeException if the given key size is not a multiple of 2^11=2048
   */
  KeyPair RSAKeyGen(int keySize)
      throws InsecureRSAKeySizeException;

  /**
   * Uses Shamir's Secret Sharing Algorithm to break a <code>KeyPair</code> (intended to be generated
   * by <code>RSAKeyGen</code>) into <code>n := numTotalShards</code> shards, where the
   * key can be reassembled with a minimum of <code>k := minShardsToCreate</code> shards, where
   * <code>1 <= k <= n</code>
   * @param numTotalShards n-- the number of shards to break the <code>KeyPair</code> into
   * @param minShardsToCreate k-- the minimum number of Shamir shards required to reassemble the
   *                          original <code>KeyPair</code>
   * @return a <code>Map&lt Integer, byte[]&gt</code>
   */
  Map<Integer, byte[]> shamirShardKey(KeyPair toShard, int numTotalShards, int minShardsToCreate);

  void writeShards(List<byte[]> shards);

  byte[] encrypt(String plainText, PublicKey encryptWith);

  String decrypt(byte[] cipherTextBytes, PrivateKey decryptWith);

  // String decrypt(byte[] cipherTextBytes, List<byte[]> shards);

  byte[] assembleShards(int... shardIndices);


}
