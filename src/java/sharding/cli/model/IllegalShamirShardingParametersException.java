package sharding.cli.model;

/**
 * Custom exception to be thrown if the user tries to shard a key using an invalid sharding criteria
 * <br>
 * Consider a private RSA key that will be broken into <code>n</code> shards using Shamir's Secret Sharing
 * Algorithm, where <code>k</code> shards are required to reassemble the private key.
 * An Illegal Shamir Sharding Criteria is one of the following scenarios:
 * <ul>
 *   <li><code>k > n</code></li>
 *   <li><code>n < 1</code></li>
 * </ul>
 */
public class IllegalShamirShardingParametersException extends IllegalArgumentException {

  public IllegalShamirShardingParametersException(String message) {
    super(message);
  }

  public IllegalShamirShardingParametersException() {
    this("Illegal Shamir Sharding paramters. See JavaDoc for IShardingCLI.shamirShardKey() for guidance");
  }
}
