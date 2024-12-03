package com.bits.hr.service.approvalProcess.helperMethods;

import com.bits.hr.domain.EmployeeSalary;

public class SalaryFinalization {

    public static EmployeeSalary processToFinalize(EmployeeSalary employeeSalary) {
        employeeSalary.setIsFinalized(true);
        return employeeSalary;
    }

    public static EmployeeSalary processToRejectFinalize(EmployeeSalary employeeSalary) {
        employeeSalary.setIsFinalized(false);
        return employeeSalary;
    }
}
