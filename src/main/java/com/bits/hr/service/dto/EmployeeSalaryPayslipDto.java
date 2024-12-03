package com.bits.hr.service.dto;

import com.bits.hr.domain.Employee;
import lombok.Data;

@Data
public class EmployeeSalaryPayslipDto {

    EmployeeDTO employee;
    EmployeeSalaryDTO employeeSalary;
}
