package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeResignationRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HafDeductionService {

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    // if month = joining month => pay/cut full
    // if month = resigning month => pay/cut 0
    public double hafDeduction(EmployeeSalary employeeSalary) {
        // 0 if data invalid data given
        if (employeeSalary.getEmployee() == null) return 0.0d;
        if (employeeSalary.getEmployee().getBand() == null) return 0.0d;
        if (employeeSalary.getEmployee().getBand().getWelfareFund() == null) return 0.0d;

        // 0 for ce and i
        if (
            employeeSalary.getEmployee().getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE ||
            employeeSalary.getEmployee().getEmployeeCategory() == EmployeeCategory.INTERN
        ) {
            return 0.0d;
        }

        // full month leave without pay ==> no haf deduction
        if (employeeSalary.getFractionDays().equals(0)) {
            return 0;
        }

        // 0 for resigning employee this month
        int salaryMonth = Month.fromEnum(employeeSalary.getMonth());
        int salaryYear = employeeSalary.getYear();

        int lengthOfSalaryMonth = LocalDate.of(salaryYear, salaryMonth, 1).lengthOfMonth();

        // Month = resigning month haf = 0
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository
            .findEmployeeResignationByEmployeeId(employeeSalary.getEmployee().getId())
            .stream()
            .filter(x -> x.getLastWorkingDay() != null)
            .collect(Collectors.toList());
        if (!employeeResignationList.isEmpty()) {
            int resignationDay = employeeResignationList.get(0).getLastWorkingDay().getDayOfMonth();
            int resignationMonth = employeeResignationList.get(0).getLastWorkingDay().getMonth().getValue();
            int resignationYear = employeeResignationList.get(0).getLastWorkingDay().getYear();
            // full month work pgross == mgross >> pay PF
            if (salaryYear == resignationYear && salaryMonth == resignationMonth && lengthOfSalaryMonth == resignationDay) {
                return employeeSalary.getEmployee().getBand().getWelfareFund();
            }
            // not full month worked,
            else if (salaryYear == resignationYear && salaryMonth == resignationMonth && lengthOfSalaryMonth != resignationDay) {
                return 0;
            } else return employeeSalary.getEmployee().getBand().getWelfareFund();
        }
        // above all condition false means get welfare from band
        else return employeeSalary.getEmployee().getBand().getWelfareFund();
    }
}
