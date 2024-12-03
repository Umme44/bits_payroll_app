package com.bits.hr.util;

public class ObjectConversationUtil {

    public static double asDouble(Object o) {
        double val = 0;
        if (o instanceof Number) {
            val = ((Number) o).doubleValue();
        }
        return val;
    }
}
