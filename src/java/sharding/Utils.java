package sharding;

import java.util.List;

public class Utils {

  public static <X> X notNull(X toCheck) {
    if (toCheck == null)
      throw new IllegalArgumentException("passed a null argument to a non-null parameter");
    return toCheck;
  }

  public static int intBetween(int lowerBoundIncl, int toCheck, int upperBoundIncl) {
    if (toCheck < lowerBoundIncl || toCheck > upperBoundIncl)
      throw new IllegalArgumentException("int provided not within inclusive bounds ["
          + lowerBoundIncl + "," + upperBoundIncl + "]");
    return toCheck;
  }

}
