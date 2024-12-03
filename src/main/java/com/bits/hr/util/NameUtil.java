package com.bits.hr.util;

public class NameUtil {

    public static String makeItProperCase(String name) {
        try {
            if (name == null) return "";

            String properCase = "";
            //splitting by space
            String[] names = name.trim().split(" ");

            for (String value : names) {
                properCase += (value.charAt(0) + "").toUpperCase() + value.substring(1).toLowerCase();
                if (!value.equals(names[names.length - 1])) {
                    // adding space after the value
                    properCase += " ";
                }
            }
            return properCase;
        } catch (Exception e) {
            return name;
        }
    }
}
