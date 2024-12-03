package com.bits.hr.service.importXL.employee.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.BloodGroup;

public class BloodGroupEnumFromString {

    public static BloodGroup get(String s) {
        s = s.trim();
        switch (s) {
            case "A_POSITIVE":
            case "A+ve":
            case "A+":
                return BloodGroup.A_POSITIVE;
            case "B_POSITIVE":
            case "B+ve":
            case "B+":
                return BloodGroup.B_POSITIVE;
            case "O_POSITIVE":
            case "O+ve":
            case "O+":
                return BloodGroup.O_POSITIVE;
            case "AB_POSITIVE":
            case "AB+ve":
            case "AB+":
                return BloodGroup.AB_POSITIVE;
            case "A_NEGATIVE":
            case "A-ve":
            case "A-":
                return BloodGroup.A_NEGATIVE;
            case "B_NEGATIVE":
            case "B-ve":
            case "B-":
                return BloodGroup.B_NEGATIVE;
            case "O_NEGATIVE":
            case "O-ve":
            case "O-":
                return BloodGroup.O_NEGATIVE;
            case "AB_NEGATIVE":
            case "AB-ve":
            case "AB-":
                return BloodGroup.AB_NEGATIVE;
            default:
                return null;
        }
    }
}
