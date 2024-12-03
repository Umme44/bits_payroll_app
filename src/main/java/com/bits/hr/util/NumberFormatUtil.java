package com.bits.hr.util;

import java.text.DecimalFormat;

public class NumberFormatUtil {

    /**
     * Method for process number to decimalFormat =>
     * example-> 300000 --> 300,000
     * @return
     */
    public static String processToDecimalFormat(String number) {
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }
}
