package sharding.cli.model;

/**
 * Custom exception intended to indicate to the caller that a method that requires an RSA key pair
 * key was called with no such key pair existing. Allows for more detailed error handling in view-controller
 */
public class NoSuchKeyException extends IllegalArgumentException {
  public NoSuchKeyException(String message) {
    super(message);
  }

  public NoSuchKeyException() {
    super("WARNING: no available key. Use keyGenRSA() to generate key");
  }
}
