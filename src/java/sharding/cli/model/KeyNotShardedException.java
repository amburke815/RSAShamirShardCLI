package sharding.cli.model;

/**
 * Custom exception intended to indicate to the caller that a method that requires a pre-sharded
 * key was called with no sharded key. Allows for more detailed error handling in view-controller
 */
public class KeyNotShardedException extends IllegalArgumentException {
  public KeyNotShardedException(String message) {
    super(message);
  }

  public KeyNotShardedException() {
    super("Key not yet sharded. Please shard key before trying to manipulate shards map");
  }
}
