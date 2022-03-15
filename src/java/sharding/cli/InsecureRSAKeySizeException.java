package sharding.cli;

public class InsecureRSAKeySizeException extends IllegalArgumentException {

  public InsecureRSAKeySizeException(String message) {
    super(message);
  }

  public InsecureRSAKeySizeException() {
    this("WARNING: insecure RSA key size. Must be a multiple of 2048 bits in order to be "
        + "considered secure in 2022");
  }

}
