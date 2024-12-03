package com.bits.hr.service.importXL.employee.helperMethods.stringFormatter;

import java.math.BigDecimal;

public class FormatIDNumbers {

    /*
     * passport and nid number correction utility function
     * */
    public static String format(String id) {
        id = id.replace("-", "");
        if (id.contains("E")) {
            id = BigDecimal.valueOf(Double.parseDouble(id)).toPlainString();
        }
        return id;
    }
}
