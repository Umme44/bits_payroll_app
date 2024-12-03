package com.bits.hr.service.importXL.pf.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.PfAccountStatus;
import java.util.Locale;

public class GetPfAccountStatusEnumFromString {

    public static PfAccountStatus get(String s) {
        s = s.trim().toLowerCase(Locale.ROOT);
        switch (s) {
            case "closed":
                return PfAccountStatus.CLOSED;
            case "inactive":
                return PfAccountStatus.INACTIVE;
            default:
                return PfAccountStatus.ACTIVE;
        }
    }
}
