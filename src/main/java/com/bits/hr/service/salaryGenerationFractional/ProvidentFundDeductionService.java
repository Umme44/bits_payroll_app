package com.bits.hr.service.salaryGenerationFractional;

import static com.bits.hr.service.salaryGenerationFractional.SalaryConstants.PF_PERCENT_FROM_BASIC;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.PfArrear;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.PfArrearRepository;
import com.bits.hr.repository.PfLoanRepaymentRepository;
import com.bits.hr.service.finalSettlement.helperMethods.ResignationProcessingService;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProvidentFundDeductionService {

    @Autowired
    private PfLoanRepaymentRepository pfLoanRepaymentRepository;

    @Autowired
    private ResignationProcessingService resignationProcessingService;

    @Autowired
    private PfArrearRepository pfArrearRepository;

    public double getProvidentFundLoanDeductionAmount(Employee employee, int year, int month) {
        if (pfLoanRepaymentRepository.findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin()).isPresent()) {
            return Math.round(
                pfLoanRepaymentRepository.findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin()).get().getAmount()
            );
        } else return 0.0d;
    }

    // employee contribution = company contribution
    // provident fund = per day basic salary * fraction days(31 in full month of 31 days) * pf percent
    // or pf= perMonthPayableBasic * .10
    // if month = joining month => pay/cut full
    // if month = resigning month => pay/cut 0
    public double providentFundContribution(EmployeeSalary employeeSalary) {
        Employee employee = employeeSalary.getEmployee();
        if (!employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            return 0d;
        }

        int salaryMonth = Month.fromEnum(employeeSalary.getMonth());
        int salaryYear = employeeSalary.getYear();
        int lengthOfSalaryMonth = LocalDate.of(salaryYear, salaryMonth, 1).lengthOfMonth();

        // full month leave without pay / fraction days ==0 ===> pf will be zero
        if (employeeSalary.getFractionDays().equals(0)) {
            return 0d;
        }

        // pf- arrears consideration
        List<PfArrear> pfArrearList = pfArrearRepository.findByEmployeeIdAndYearAndMonth(
            employeeSalary.getEmployee().getId(),
            employeeSalary.getYear(),
            employeeSalary.getMonth()
        );

        double pfArrear = pfArrearList.stream().mapToDouble(PfArrear::getAmount).sum();

        /// resigning employee ( not probational )
        /// resigning month == this month ==> no pf this month (only if not worked full month )
        Optional<EmployeeResignation> employeeResignationOptional = resignationProcessingService.getResignation(employee.getId());

        if (employeeResignationOptional.isPresent()) {
            int lastWorkingDay = employeeResignationOptional.get().getLastWorkingDay().getDayOfMonth();
            int lastWorkingMonth = employeeResignationOptional.get().getLastWorkingDay().getMonth().getValue();
            int lastWorkingYear = employeeResignationOptional.get().getLastWorkingDay().getYear();

            boolean isLastMonth = (salaryYear == lastWorkingYear && salaryMonth == lastWorkingMonth);
            boolean isLastMonthFullWorked =
                (salaryYear == lastWorkingYear && salaryMonth == lastWorkingMonth && lengthOfSalaryMonth == lastWorkingDay);

            // last month
            if (isLastMonth && !isLastMonthFullWorked) {
                // not full month worked in last month
                return 0 + pfArrear;
            }
        }

        // default for regular confirmed employee
        return MathRoundUtil.round((employeeSalary.getMainGrossBasicSalary() * PF_PERCENT_FROM_BASIC) + pfArrear);
    }
}
