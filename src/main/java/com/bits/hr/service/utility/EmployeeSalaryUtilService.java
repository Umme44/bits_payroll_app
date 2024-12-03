package com.bits.hr.service.utility;

import com.bits.hr.domain.*;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeSalaryPayslipDto;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSalaryUtilService {

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    public EmployeeSalaryPayslipDto dummySalaryPayslip() {
        EmployeeSalaryPayslipDto dto = new EmployeeSalaryPayslipDto();

        EmployeeSalary employeeSalary = new EmployeeSalary();
        employeeSalary.setPayableGrossBasicSalary(0.0);
        employeeSalary.setPayableGrossHouseRent(0.0);
        employeeSalary.setPayableGrossConveyanceAllowance(0.0);
        employeeSalary.setEntertainment(0.0);
        employeeSalary.setUtility(0.0);
        employeeSalary.setPayableGrossSalary(0.0);
        employeeSalary.setAllowance01(0.0);
        employeeSalary.setAllowance02(0.0);
        employeeSalary.setOtherAddition(0.0);
        employeeSalary.setSalaryAdjustment(0.0);
        employeeSalary.setArrearSalary(0.0);
        employeeSalary.setTotalDeduction(0.0);
        employeeSalary.setMobileBillDeduction(0.0);
        employeeSalary.setWelfareFundDeduction(0.0);
        employeeSalary.setTaxDeduction(0.0);
        employeeSalary.setPfDeduction(0.0);
        employeeSalary.setPfArrear(0.0);
        employeeSalary.setTotalDeduction(0.0);
        employeeSalary.setProvisionForFestivalBonus(0.0);

        dto.setEmployeeSalary(employeeSalaryMapper.toDto(employeeSalary));

        if (currentEmployeeService.getCurrentEmployee().isPresent()) {
            Employee employee = currentEmployeeService.getCurrentEmployee().get();
            dto.setEmployee(employeeMapper.toDto(employee));
        } else {
            Employee employee = new Employee();
            employee.setPin("");
            employee.setFullName("");
            employee.setUnit(new Unit());
            employee.setDepartment(new Department());
            employee.setDesignation(new Designation());
            employee.setDateOfJoining(LocalDate.of(1900, java.time.Month.JANUARY, 1)); //dummy date
            dto.setEmployee(employeeMapper.toDto(employee));
        }
        return dto;
    }
}
