package com.bits.hr.domain.enumeration;

/**
 * The EmployeeCategory enumeration.
 */
public enum EmployeeCategory {
    REGULAR_CONFIRMED_EMPLOYEE,
    REGULAR_PROVISIONAL_EMPLOYEE,
    CONTRACTUAL_EMPLOYEE,
    INTERN,
    CONSULTANTS,
    PART_TIME;

    public static String employeeCategoryEnumToNaturalText(EmployeeCategory employeeCategory) {
        if (employeeCategory == null) {
            return "";
        } else if (employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            return "Regular Confirmed";
        } else if (employeeCategory.equals(EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE)) {
            return "Regular Probation";
        } else if (employeeCategory.equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)) {
            return "Contractual";
        } else if (employeeCategory.equals(EmployeeCategory.INTERN)) {
            return "Intern";
        } else if (employeeCategory.equals(EmployeeCategory.CONSULTANTS)) {
            return "Consultants";
        } else if (employeeCategory.equals(EmployeeCategory.PART_TIME)) {
            return "Part Time";
        } else {
            return "";
        }
    }
}
