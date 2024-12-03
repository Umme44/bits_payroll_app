package com.bits.hr.service.salaryGeneration;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.Month;

public interface SalaryVisibilityControlService {
    boolean makeSalaryVisibleToEmployee(int year, int month);

    boolean makeSalaryHideFromEmployee(int year, int month);
    boolean makeSalaryPartiallyVisibleToEmployee(int year, int month);

    boolean makeEmployeeSalaryVisibleToEmployee(Employee employee, int year, Month month);

    boolean makeEmployeeSalaryHideFromEmployee(Employee employee, int year, Month month);
}
