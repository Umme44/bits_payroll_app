package com.bits.hr.service.incomeTaxManagement;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.AitPaymentRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.repository.ProRataFestivalBonusRepository;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.incomeTaxManagement.helperMethods.CalculateMultiplier;
import com.bits.hr.service.incomeTaxManagement.model.IncomeTaxData;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.TaxCalculationDTO;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncomeTaxCalculatePerMonth {

    @Autowired
    private IncomeTaxCalculatePerYear incomeTaxCalculatePerYear;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    AitPaymentRepository aitPaymentRepository;

    @Autowired
    ProRataFestivalBonusRepository proRataFestivalBonusRepository;

    @Autowired
    GetConfigValueByKeyService getConfigValueByKeyService;

    @Autowired
    IncomeTaxDataPreparationService incomeTaxDataPreparationService;

    public double getTax(EmployeeSalary employeeSalary, TaxCalculationDTO taxCalculationDTO) {
        IncomeTaxData incomeTaxData = incomeTaxDataPreparationService.getDataForTaxCalculation(
            employeeSalary.getEmployee(),
            employeeSalary.getMonth().ordinal() + 1,
            employeeSalary.getYear(),
            Optional.of(employeeSalary)
        );
        taxCalculationDTO.setIncomeTaxData(incomeTaxData);

        // yearly tax calculation goes here
        double perYearIncomeTax = MathRoundUtil.round(incomeTaxCalculatePerYear.calculateYearlyIncomeTax(incomeTaxData, taxCalculationDTO));

        double previousIncomeTax =
            incomeTaxData.getPreviousSalaryList().stream().mapToDouble(EmployeeSalary::getTaxDeduction).sum() +
            incomeTaxData.getTaxCutFromIndividualArrears();

        Optional<EmployeeResignation> employeeResignationOptional = incomeTaxData.getEmployeeResignationOptional();
        Optional<LocalDate> lwd;
        if (employeeResignationOptional.isPresent()) {
            lwd = Optional.of(employeeResignationOptional.get().getLastWorkingDay());
        } else {
            lwd = Optional.empty();
        }
        // this+upto income year-end
        int remainingMonths = CalculateMultiplier.getDistributionMultiplier(
            employeeSalary.getEmployee(),
            Month.fromEnum(employeeSalary.getMonth()),
            employeeSalary.getYear(),
            incomeTaxData.getTaxQueryConfig(),
            lwd
        );
        double remainingIncomeTaxToBeGiven = perYearIncomeTax - previousIncomeTax;

        // if income tax cut for 300K(male) of taxable income and suddenly income tax 0 because of any leave without pay
        // then  remaining tax would be negative
        // but once tax is cut and given to govt. there are no happy path to have that money back,
        // but it's possible to balance with next year AIT
        double perMonthIncomeTax = 0;
        if (remainingIncomeTaxToBeGiven > 0 && remainingMonths > 0) {
            perMonthIncomeTax = remainingIncomeTaxToBeGiven / remainingMonths;
        } else if (remainingIncomeTaxToBeGiven < 0) {
            perMonthIncomeTax = remainingIncomeTaxToBeGiven;
        }

        //        if (remainingIncomeTaxToBeGiven <= 0) return 0.0d;

        // this logic will work for only last months of the tax year.
        // new block -- negative tax cut
        // negative income tax consideration
        // if income tax is negative, only give amount would be redemed

        boolean isTaxYearLastMonth = employeeSalary.getMonth().equals(Month.JUNE);

        double effectivePerMonthIncomeTax = perMonthIncomeTax;

        if (isTaxYearLastMonth) {
            if (perMonthIncomeTax < 0) {
                if (previousIncomeTax == 0) {
                    effectivePerMonthIncomeTax = 0;
                } else if (previousIncomeTax > Math.abs(perMonthIncomeTax)) {
                    // if per month tax is negative and positive value for per month tax and already paid tax is
                    effectivePerMonthIncomeTax = perMonthIncomeTax;
                } else if (previousIncomeTax < Math.abs(perMonthIncomeTax)) {
                    effectivePerMonthIncomeTax = -previousIncomeTax;
                }
            }
        } else {
            if (perMonthIncomeTax < 0) {
                effectivePerMonthIncomeTax = 0;
            }
        }

        // ------ tax report -----

        double remainingMonthIncomeTax = remainingIncomeTaxToBeGiven - effectivePerMonthIncomeTax;
        if (isTaxYearLastMonth || remainingIncomeTaxToBeGiven < 0) {
            remainingMonthIncomeTax = 0;
        }
        taxCalculationDTO.setMonthlyAnalysis(
            incomeTaxData.getPreviousSalaryList(),
            effectivePerMonthIncomeTax,
            remainingMonths - 1,
            remainingMonthIncomeTax
        );

        // ------ tax report -----

        return effectivePerMonthIncomeTax;
    }

    public Optional<EmployeeResignation> getEmployeeResignation(Employee employee) {
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employee.getId()
        );
        if (employeeResignationList.size() > 0) {
            return Optional.of(employeeResignationList.get(0));
        } else {
            return Optional.empty();
        }
    }

    private boolean isNoTaxDeductionForBrokenResignationMonth(EmployeeSalary employeeSalary) {
        // 0 tax for resigning employee
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository
            .findEmployeeResignationByEmployeeId(employeeSalary.getEmployee().getId())
            .stream()
            .filter(x -> x.getLastWorkingDay() != null && x.getApprovalStatus() == Status.APPROVED)
            .collect(Collectors.toList());

        if (!employeeResignationList.isEmpty()) {
            EmployeeResignation employeeResignation = employeeResignationList.get(0);

            boolean isResignationOnBrokenMonth =
                (employeeResignation.getLastWorkingDay().lengthOfMonth() != employeeResignation.getLastWorkingDay().getDayOfMonth());
            int resignationMonth = employeeResignation.getLastWorkingDay().getMonth().getValue();
            int salaryMonth = employeeSalary.getMonth().ordinal() + 1;

            int resignationYear = employeeResignation.getLastWorkingDay().getYear();
            int salYear = employeeSalary.getYear();

            // if this is the broken month return true else return false
            if (resignationMonth == salaryMonth && resignationYear == salYear && isResignationOnBrokenMonth) {
                return true;
            } else {
                return false;
            }
            //            if (resignationMonth == salaryMonth && resignationYear == salYear) {
            //                // CR :: tax will be calculated for last full month
            //                return false;
            //            }
            //            //
            //            if (isResignationOnBrokenMonth) {
            //                int rm2 = employeeResignation.getLastWorkingDay().minusMonths(1).getMonth().getValue();
            //                int ry2 = employeeResignation.getLastWorkingDay().minusMonths(1).getYear();
            //                if (rm2 == salaryMonth && ry2 == salYear) {
            //                    return true;
            //                }
            //            }

        }
        return false;
    }
}
