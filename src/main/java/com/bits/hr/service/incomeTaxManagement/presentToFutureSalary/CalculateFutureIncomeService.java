package com.bits.hr.service.incomeTaxManagement.presentToFutureSalary;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.service.incomeTaxManagement.helperMethods.CalculateSalarySum;
import com.bits.hr.service.incomeTaxManagement.model.IncomeTaxData;
import com.bits.hr.service.incomeTaxManagement.model.SalarySum;
import com.bits.hr.service.incomeTaxManagement.model.TaxQueryConfig;
import com.bits.hr.util.DateUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculateFutureIncomeService {

    @Autowired
    MockSalaryGenerationService mockSalaryGenerationService;

    public SalarySum getFutureSalarySum(
        int trackingMonth,
        int trackingYear,
        Employee employee,
        EmployeeSalary trackingSalary,
        TaxQueryConfig taxQueryConfig,
        Optional<EmployeeResignation> employeeResignationOptional,
        IncomeTaxData incomeTaxData
    ) {
        List<EmployeeSalary> presentToFutureSalaries = getFutureSalaries(
            trackingMonth,
            trackingYear,
            employee,
            trackingSalary,
            taxQueryConfig,
            employeeResignationOptional
        );

        incomeTaxData.setPresentToFutureSalaryList(presentToFutureSalaries);

        return CalculateSalarySum.getSummationOfEmployeeSalaries(presentToFutureSalaries);
    }

    public List<EmployeeSalary> getFutureSalaries(
        int trackingMonth,
        int trackingYear,
        Employee employee,
        EmployeeSalary trackingSalary,
        TaxQueryConfig taxQueryConfig,
        Optional<EmployeeResignation> employeeResignation
    ) {
        List<EmployeeSalary> employeeSalaryList = new ArrayList<>();

        LocalDate trackingMonthStart = LocalDate.of(trackingYear, trackingMonth, 1);
        LocalDate consideredEndDate = taxQueryConfig.getIncomeYearEndDate();
        LocalDate lwdInThisIncomeYear = consideredEndDate;

        if (employee.getIsFixedTermContract() != null && employee.getIsFixedTermContract()) {
            LocalDate contactEndDate = employee.getContractPeriodEndDate();
            if (employee.getContractPeriodExtendedTo() != null) {
                contactEndDate = employee.getContractPeriodExtendedTo();
            }
            if (DateUtil.isBetweenOrEqual(contactEndDate, trackingMonthStart, consideredEndDate)) {
                consideredEndDate = contactEndDate;
            }
        }

        if (employeeResignation.isPresent()) {
            LocalDate lwd = employeeResignation.get().getLastWorkingDay();
            if (DateUtil.isBetweenOrEqual(lwd, trackingMonthStart, consideredEndDate)) {
                consideredEndDate = lwd;
            }
        }

        LocalDate lwdMonthStart = LocalDate.of(lwdInThisIncomeYear.getYear(), lwdInThisIncomeYear.getMonth(), 1);

        employeeSalaryList.add(trackingSalary);
        trackingMonthStart = trackingMonthStart.plusMonths(1);

        for (LocalDate date = trackingMonthStart; date.isBefore(consideredEndDate); date = date.plusMonths(1)) {
            int month = date.getMonthValue();
            int year = date.getYear();

            // last month could be fractional, in that case, fraction days need to be considered
            int fractionDays = date.lengthOfMonth();

            if (!isLastDayOfMonth(lwdInThisIncomeYear) && lwdMonthStart.isEqual(date)) {
                fractionDays = (int) ChronoUnit.DAYS.between(lwdMonthStart, lwdInThisIncomeYear) + 1;
            }

            Optional<EmployeeSalary> employeeSalaryOptional = mockSalaryGenerationService.generateMonthlySalary(
                month,
                year,
                employee,
                fractionDays
            );

            if (employeeSalaryOptional.isPresent()) {
                employeeSalaryList.add(employeeSalaryOptional.get());
            }
        }

        return employeeSalaryList;
    }

    private boolean isLastDayOfMonth(LocalDate localDate) {
        if (localDate.lengthOfMonth() == localDate.getDayOfMonth()) {
            return true;
        } else {
            return false;
        }
    }
}
