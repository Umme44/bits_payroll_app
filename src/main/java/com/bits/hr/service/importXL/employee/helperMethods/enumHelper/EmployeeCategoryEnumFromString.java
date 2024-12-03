package com.bits.hr.service.importXL.employee.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.EmployeeCategory;

public class EmployeeCategoryEnumFromString {

    public static EmployeeCategory get(String s) {
        s = s.trim();
        switch (s) {
            case "REGULAR_CONFIRMED_EMPLOYEE":
            case "Regular Confirmed Employee":
            case "Confirmed":
            case "confirmed":
            case "Confirm":
                return EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
            case "REGULAR_PROVISIONAL_EMPLOYEE":
            case "Regular Probational Employee":
            case "Probational":
            case "probational":
            case "Probation":
                return EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE;
            case "CONTRACTUAL_EMPLOYEE":
            case "Contractual Employee":
            case "Contractual":
            case "contractual":
            case "by Contract":
            case "by contract":
                return EmployeeCategory.CONTRACTUAL_EMPLOYEE;
            default:
                return EmployeeCategory.INTERN;
        }
    }
}
