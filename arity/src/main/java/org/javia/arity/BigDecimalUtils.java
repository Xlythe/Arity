package org.javia.arity;

import java.math.BigDecimal;

/**
 * Utilities for Math using BigDecimal
 */
public class BigDecimalUtils {

    private BigDecimalUtils() {}

    private static boolean isSupported(double value) {
        return !Double.isInfinite(value) && !Double.isNaN(value);
    }

    private static BigDecimal getBigDecimalFrom(double value) {
        return new BigDecimal(String.valueOf(value));
    }

    public static double substract(double a, double b) {
        if (isSupported(a) && isSupported(b)) {
            return getBigDecimalFrom(a).subtract(getBigDecimalFrom(b)).doubleValue();
        }

        return a - b;
    }
}
