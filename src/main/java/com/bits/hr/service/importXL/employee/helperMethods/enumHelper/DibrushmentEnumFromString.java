package com.bits.hr.service.importXL.employee.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.DisbursementMethod;

public class DibrushmentEnumFromString {

    DisbursementMethod get(String s) {
        s = s.trim();
        switch (s) {
            case "CASH":
            case "Cash":
            case "cash":
                return DisbursementMethod.CASH;
            case "MOBILE_BANKING":
            case "Mobile Banking":
            case "mobile Banking":
            case "mobile banking":
                return DisbursementMethod.MOBILE_BANKING;
            default:
                return DisbursementMethod.BANK;
        }
    }
}
