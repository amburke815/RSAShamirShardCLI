package sharding.cli.model;

public class KeyNotShardedException extends IllegalArgumentException {
  public KeyNotShardedException(String message) {
    super(message);
  }

  public KeyNotShardedException() {
    super("Key not yet sharded. Please shard key before trying to manipulate shards map");
  }
}
