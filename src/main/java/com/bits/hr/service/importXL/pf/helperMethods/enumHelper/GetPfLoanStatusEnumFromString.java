package com.bits.hr.service.importXL.pf.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import java.util.Locale;

public class GetPfLoanStatusEnumFromString {

    public static PfLoanStatus get(String s) {
        s = s.trim().toLowerCase(Locale.ROOT);
        switch (s) {
            case "not fullfilled":
            case "not fulfilled":
            case "not_fullfilled":
            case "not_fulfilled":
                return PfLoanStatus.NOT_FULLFILLED;
            case "paid off":
            case "paid_off":
                return PfLoanStatus.PAID_OFF;
            default:
                return PfLoanStatus.OPEN_REPAYING;
        }
    }
}
