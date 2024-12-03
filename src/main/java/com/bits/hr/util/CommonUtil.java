package com.bits.hr.util;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.time.LocalDate;

public class CommonUtil {

    public static LocalDate getConfirmationDateOrContractEndDate(Employee employee) {
        if (employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
            if (employee.getContractPeriodExtendedTo() != null) {
                return employee.getContractPeriodExtendedTo();
            } else {
                return employee.getContractPeriodEndDate();
            }
        } else {
            return employee.getDateOfConfirmation();
        }
    }
}
