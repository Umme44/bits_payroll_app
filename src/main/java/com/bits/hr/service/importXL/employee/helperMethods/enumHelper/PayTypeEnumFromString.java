package com.bits.hr.service.importXL.employee.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.PayType;

public class PayTypeEnumFromString {

    PayType get(String s) {
        s = s.trim();
        switch (s) {
            case "HOURLY":
            case "hourly":
            case "Hourly":
                return PayType.HOURLY;
            case "UNPAID":
            case "Unpaid":
            case "unpaid":
                return PayType.UNPAID;
            default:
                return PayType.MONTHLY;
        }
    }
}
