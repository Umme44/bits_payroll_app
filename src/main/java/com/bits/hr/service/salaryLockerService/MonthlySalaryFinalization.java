package com.bits.hr.service.salaryLockerService;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeSalaryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlySalaryFinalization {

    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;

    public Boolean finalizeMonthlySalaryAllActiveEmployee(int year, int month) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findEmployeeSalaryByYearAndMonth(year, Month.fromInteger(month));
        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            if (employeeSalary.getEmployee() != null && employeeSalary.getEmployee().getEmploymentStatus() != null) {
                if (employeeSalary.getEmployee().getEmploymentStatus() == EmploymentStatus.ACTIVE) {
                    employeeSalary.setIsFinalized(true);
                    employeeSalaryRepository.save(employeeSalary);
                }
            }
        }
        return true;
    }
}
