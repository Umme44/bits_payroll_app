package com.bits.hr.util;

public class PinUtil {

    public static String formatPin(String pin) {
        try {
            if (pin.length() <= 9) {
                String num = String.valueOf((int) Math.round(Double.parseDouble(pin)));
                return num.trim();
            } else return pin.trim();
        } catch (NumberFormatException e) {
            return pin.trim();
        }
    }
}
