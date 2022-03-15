package sharding.cli.model;

/**
 * A custom exception used to warn developers of the creation of an insecure RSA key.
 * Is essentially an IllegalArgumentException but more informative.
 */
public class InsecureRSAKeySizeException extends IllegalArgumentException {

  public InsecureRSAKeySizeException(String message) {
    super(message);
  }

  public InsecureRSAKeySizeException() {
    this("WARNING: insecure RSA key size. Must be a multiple of 2048 bits in order to be "
        + "considered secure in 2022");
  }

}
