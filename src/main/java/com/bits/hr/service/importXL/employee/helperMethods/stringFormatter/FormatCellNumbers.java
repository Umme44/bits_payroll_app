package com.bits.hr.service.importXL.employee.helperMethods.stringFormatter;

import java.math.BigDecimal;

public class FormatCellNumbers {

    public static String format(String phoneNumber) {
        phoneNumber = phoneNumber.replace("-", "");
        if (phoneNumber.length() >= 10) {
            if (phoneNumber.contains("E")) {
                phoneNumber = BigDecimal.valueOf(Double.parseDouble(phoneNumber)).toPlainString();
            }
            if (!phoneNumber.substring(0, 4).equals("+880")) {
                phoneNumber = "+880" + phoneNumber;
            }
        }
        return phoneNumber;
    }
}
