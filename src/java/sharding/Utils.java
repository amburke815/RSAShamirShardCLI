package sharding;

import java.io.File;

/**
 * Utility class for commonly used operations throughout the project--helps avoid code duplication
 * and enforces a central point of control.
 */
public class Utils {

  /**
   * Checks that the given parameter is not null and returns it if so. Otherwise throws an
   * <code>IllegalArgumentException</code>
   *
   * @param toCheck the parameter to check for nullity
   * @param <X>     the type of <code>toCheck</code>. Allows this method to be used for any data..
   * @return <code>toCheck</code> if it is not null
   * @throws IllegalArgumentException if <code>toCheck</code> is null
   */
  public static <X> X notNull(X toCheck)
      throws IllegalArgumentException {
    if (toCheck == null) {
      throw new IllegalArgumentException("passed a null argument to a non-null parameter");
    }
    return toCheck;
  }

  /**
   * Checks that the given integer is within the inclusive range
   * <code>[lowerBoundIncl,upperBoundIncl]</code>, and returns it if this is true.
   * Otherwise throws an
   * <code>IllegalArgumentException</code>
   *
   * @param toCheck        the integer to for being in the given range
   * @param lowerBoundIncl the lower bound of the range that <code>toCheck</code> is to lie in
   *                       (inclusive)
   * @param upperBoundIncl the upper bound of the range that <code>toCheck</code> is to lie in
   *                       (inclusive)
   * @return <code>toCheck</code> if it lies in the range <code>[lowerBoundIncl,upperBoundIncl]</code>
   * @throws IllegalArgumentException if <code>toCheck</code> does not lie in the range
   *                                  <code>[lowerBoundIncl,upperBoundIncl]</code>
   */
  public static int intBetween(int lowerBoundIncl, int toCheck, int upperBoundIncl)
      throws IllegalArgumentException {
    if (toCheck < lowerBoundIncl || toCheck > upperBoundIncl) {
      throw new IllegalArgumentException("int provided not within inclusive bounds ["
          + lowerBoundIncl + "," + upperBoundIncl + "]");
    }
    return toCheck;
  }

  /**
   * Executes the equivalent of the linux command chmod 000 [f], where f is a file name.
   * Makes the file f non-readable, non-writable, and non-executable without sudo/admin access
   * @param f the file to change the read/write/execute permissions of.
   */
  public static void chmod000(File f) {
    f.setReadable(false);
    f.setWritable(false);
    f.setExecutable(false);
  }

}
