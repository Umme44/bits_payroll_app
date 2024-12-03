package com.bits.hr.service.importXL.employee.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.Gender;
import java.util.Locale;

public class GenderEnumFromString {

    public static Gender get(String s) {
        s = s.toLowerCase(Locale.ROOT);
        if (s.trim().equals("male")) return Gender.MALE; else if (s.trim().equals("female")) return Gender.FEMALE; else return Gender.OTHER;
    }
}
