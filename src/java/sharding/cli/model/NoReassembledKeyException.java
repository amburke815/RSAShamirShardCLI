package sharding.cli.model;

/**
 * Custom exception intended to indicate to the caller that a method that requires a private key
 * assembled from Shamir shards, but no such key has been assembled
 * Allows for more detailed error handling in view-controller
 */
public class NoReassembledKeyException extends IllegalArgumentException {
  public NoReassembledKeyException(String message) {
    super(message);
  }

  public NoReassembledKeyException() {
    this("Cannot call this method without having a private RSA assembled from Shamir Shards");
  }
}
