package com.bits.hr.service.incomeTaxManagement.helperMethods;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.service.salaryGenerationFractional.SalaryConstants;

@Deprecated
public class YearlyFestivalBonus {

    public static double getFb(double gross, EmployeeCategory employeeCategory) {
        if (employeeCategory == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
            return gross;
        } else if (
            employeeCategory == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE ||
            employeeCategory == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE
        ) {
            return gross * SalaryConstants.BASIC_PERCENT * 2;
        } else return 0;
    }
}
