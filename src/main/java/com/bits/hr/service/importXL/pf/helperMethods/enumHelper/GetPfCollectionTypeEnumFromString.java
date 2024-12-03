package com.bits.hr.service.importXL.pf.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.domain.enumeration.PfCollectionType;
import java.util.Locale;

public class GetPfCollectionTypeEnumFromString {

    public static PfCollectionType get(String s) {
        s = s.trim().toLowerCase(Locale.ROOT);
        switch (s) {
            case "arrear":
            case "arrears":
                return PfCollectionType.ARREAR;
            case "cash":
                return PfCollectionType.CASH;
            default:
                return PfCollectionType.MONTHLY;
        }
    }
}
