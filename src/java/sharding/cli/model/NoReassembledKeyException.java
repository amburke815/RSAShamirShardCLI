package sharding.cli.model;

public class NoReassembledKeyException extends IllegalArgumentException {
  public NoReassembledKeyException(String message) {
    super(message);
  }

  public NoReassembledKeyException() {
    this("Cannot call this method without having a private RSA assembled from Shamir Shards");
  }
}
