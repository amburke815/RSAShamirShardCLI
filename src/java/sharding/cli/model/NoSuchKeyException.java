package sharding.cli.model;

public class NoSuchKeyException extends IllegalArgumentException {
  public NoSuchKeyException(String message) {
    super(message);
  }

  public NoSuchKeyException() {
    super("WARNING: no available key. Use keyGenRSA() to generate key");
  }
}
