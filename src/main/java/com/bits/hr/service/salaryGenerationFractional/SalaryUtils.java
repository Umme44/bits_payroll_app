package com.bits.hr.service.salaryGenerationFractional;

import static com.bits.hr.service.salaryGenerationFractional.SalaryConstants.PF_ELIGIBLE_EMPLOYEE_CATEGORY;
import static com.bits.hr.service.salaryGenerationFractional.SalaryConstants.PF_PERCENT_FROM_GROSS;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.util.MathRoundUtil;
import java.time.YearMonth;
import java.util.List;

public class SalaryUtils {

    // for fractional salary
    public static Double getPayableFractionalGross(List<Fraction> fractionList) {
        double payableGross = 0d;
        for (Fraction fraction : fractionList) {
            payableGross += fraction.getPerDayMainGross() * fraction.getDaysBetween();
        }
        return payableGross;
    }

    // employee contribution = company contribution
    // provident fund = per month payable salary * fraction days(31 in full month of 31 days)
    // if month = joining month => pay/cut full
    // if month = resigning month => pay/cut 0
    public static double providentFundContribution(EmployeeCategory employeeCategory, double gross) {
        if (employeeCategory == PF_ELIGIBLE_EMPLOYEE_CATEGORY) {
            return MathRoundUtil.round(gross * PF_PERCENT_FROM_GROSS);
        } else return 0d;
    }

    // 13 day money cash per year from AL
    public static double getLeaveEncashment(Employee employee) {
        double gross = employee.getMainGrossSalary();
        if (
            employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE ||
            employee.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE
        ) {
            // 1.08333334 OR (13.0d / 12.0d)
            return MathRoundUtil.round((gross / 30.0d) * 1.08333334d);
        } else {
            return 0d;
        }
    }

    // for general use
    public Double getPayableGross(Integer year, Integer month, Double mainGrossSalary, Integer fractionDays) {
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        return mainGrossSalary / daysInMonth * fractionDays;
    }
}
