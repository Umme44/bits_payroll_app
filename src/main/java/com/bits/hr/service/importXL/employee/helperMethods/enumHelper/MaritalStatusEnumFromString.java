package com.bits.hr.service.importXL.employee.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.MaritalStatus;
import java.util.Locale;

public class MaritalStatusEnumFromString {

    public static MaritalStatus get(String s) {
        s = s.trim().toLowerCase(Locale.ROOT);
        switch (s) {
            case "married":
                return MaritalStatus.MARRIED;
            case "divorced":
                return MaritalStatus.DIVORCED;
            case "widowed":
                return MaritalStatus.WIDOWED;
            default:
                return MaritalStatus.SINGLE;
        }
    }
}
