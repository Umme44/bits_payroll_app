package com.bits.hr.service.importXL.employee.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.Religion;
import java.util.Locale;

public class ReligionEnumFromString {

    public static Religion get(String s) {
        s = s.trim();
        s = s.toLowerCase(Locale.ROOT);
        switch (s) {
            case "islam":
            case "muslim":
                return Religion.ISLAM;
            case "hindu":
            case "hinduism":
                return Religion.HINDU;
            case "buddha":
            case "buddhism":
                return Religion.BUDDHA;
            case "christian":
            case "Christianism":
                return Religion.CHRISTIAN;
            default:
                return Religion.OTHER;
        }
    }
}
