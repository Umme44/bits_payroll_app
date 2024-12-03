package com.bits.hr.util;

public class MathRoundUtil {

    public static int round(Double number) {
        int intPart = (int) Math.floor(number);
        double decimalPart = number - intPart;
        if (decimalPart <= 0.49999999999) {
            return intPart;
        } else {
            return intPart + 1;
        }
    }

    public static double roundUpToTwoDecimal(Double number) {
        return Math.round(number * 100.0d) / 100.0d;
    }
}
