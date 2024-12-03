package com.bits.hr.service.incomeTaxManagement.helperMethods;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.service.incomeTaxManagement.model.TaxQueryConfig;
import com.bits.hr.service.salaryGenerationFractional.SalaryConstants;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.List;

public class ProvidentFundPerIncomeYear {

    /*
     *  yearly PF assumption
     *  for CE and Intern , PF = 0 ; << no other logic required >>
     *  for RCE >> previous PF + current + multiplier * remaining month * pf per month
     *  for RPE >> critical pf calculation procedure 1;
     *      s1: .......doc..........start...........................end...................
     *      s2: ....................start.............doc...........end...................
     *      s3: ....................start...........................end.........doc.......
     *
     *      6.isBefore(7) == true
     *      6.isAfter(7)==false
     *
     * */

    public static double getPF(EmployeeSalary salary, TaxQueryConfig taxQueryConfig, List<EmployeeSalary> previousSalary) {
        Employee employee = salary.getEmployee();

        if (
            employee.getEmployeeCategory() == EmployeeCategory.INTERN ||
            employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE
        ) {
            // regular to contractual
            return previousSalary
                .stream()
                .filter(x -> x.getPfContribution() != null && x.getPfContribution() > 0d)
                .mapToDouble(EmployeeSalary::getPfContribution)
                .sum();
        } else if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
            return getFullPf(employee, previousSalary, salary);
        } else if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
            LocalDate salGenMonthStart = LocalDate.of(salary.getYear(), salary.getMonth().ordinal() + 1, 01);
            LocalDate salGenMonthEnd = LocalDate.of(salary.getYear(), salGenMonthStart.getMonth(), salGenMonthStart.lengthOfMonth());

            LocalDate doj = employee.getDateOfJoining();
            LocalDate doc = doj.plusMonths(6);
            if (employee.getDateOfConfirmation() != null) {
                doc = employee.getDateOfConfirmation();
            }

            boolean isDocBetweenThisIncomeYear = DateUtil.isBetween(
                doc,
                taxQueryConfig.getIncomeYearStartDate(),
                taxQueryConfig.getIncomeYearEndDate()
            );

            if (isDocBetweenThisIncomeYear) {
                /*
                 * C1: start...........doc......salGenMonthStart...............end
                 * C2: start...........salGenMonthStart........doc.............end
                 * */
                if (doc.isBefore(salGenMonthEnd)) {
                    return getFullPf(employee, previousSalary, salary);
                } else {
                    int remainingMonthInIncomeYear = CalculateMultiplier.getMultiplier(doc.getMonth().getValue());
                    double pfExpected =
                        MathRoundUtil.round(employee.getMainGrossSalary() * SalaryConstants.PF_PERCENT_FROM_GROSS) *
                        remainingMonthInIncomeYear;
                    return pfExpected;
                }
            } else if (doc.isBefore(taxQueryConfig.getIncomeYearStartDate())) {
                return getFullPf(employee, previousSalary, salary);
            } else if (doc.isAfter(taxQueryConfig.getIncomeYearEndDate())) {
                return 0;
            } else {
                return 0;
            }
        } else {
            return 0d;
        }
    }

    public static double getPfForResigningEmployeesLastFullMonth(List<EmployeeSalary> previousSalary, EmployeeSalary salary) {
        double previousPF = previousSalary.stream().mapToDouble(EmployeeSalary::getPfDeduction).sum();
        double futurePF = salary.getPfDeduction();
        return MathRoundUtil.round(previousPF + futurePF);
    }

    private static double getFullPf(Employee employee, List<EmployeeSalary> previousSalary, EmployeeSalary salary) {
        double previousPF = previousSalary.stream().mapToDouble(EmployeeSalary::getPfDeduction).sum();
        int remainingMonths = CalculateMultiplier.getMultiplier(Month.fromEnum(salary.getMonth()));
        double futurePF =
            salary.getPfDeduction() +
            MathRoundUtil.round(employee.getMainGrossSalary() * SalaryConstants.PF_PERCENT_FROM_GROSS) *
            (double) (remainingMonths - 1);
        return MathRoundUtil.round(previousPF + futurePF);
    }

    public static double getPFForFinalSettlement(List<EmployeeSalary> givenSalary) {
        double givenPF = givenSalary.stream().mapToDouble(EmployeeSalary::getPfDeduction).sum();
        return MathRoundUtil.round(givenPF);
    }
}
