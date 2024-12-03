package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.AitPayment;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.ProRataFestivalBonus;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.repository.AitPaymentRepository;
import com.bits.hr.repository.ProRataFestivalBonusRepository;
import com.bits.hr.service.dto.FinalSettlementDTO;
import com.bits.hr.service.incomeTaxManagement.CalculatePreviousIncomeService;
import com.bits.hr.service.incomeTaxManagement.IncomeTaxCalculateWithConfigService;
import com.bits.hr.service.incomeTaxManagement.helperMethods.*;
import com.bits.hr.service.incomeTaxManagement.model.SalarySum;
import com.bits.hr.service.incomeTaxManagement.model.TaxQueryConfig;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.TaxCalculationDTO;
import com.bits.hr.service.salaryGeneration.config.ConfigParser;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

@Service
public class FinalSettlementIncomeTaxService {

    @Autowired
    private AitPaymentRepository aitPaymentRepository;

    @Autowired
    private ProRataFestivalBonusRepository proRataFestivalBonusRepository;

    @Autowired
    private CalculatePreviousIncomeService calculatePreviousIncomeService;

    @Autowired
    private ConfigParser configParser;

    // validation + data preparation phase
    public double getIncomeTax(Employee employee, FinalSettlementDTO finalSettlementDTO) {
        // if final settlement pass income year
        // ==> calculate only final settlement amount income tax , no need to consider anything except final settlement amount
        final TaxQueryConfig taxQueryConfig = CalculateTaxQueryConfig.getConfigForFinalSettlement(
            finalSettlementDTO.getFinalSettlementDate()
        );

        // ait payments
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAllBetween(
            employee.getId(),
            taxQueryConfig.getIncomeYearStartDate(),
            taxQueryConfig.getIncomeYearEndDate()
        );
        double aitPaymentSum = aitPaymentList.stream().mapToDouble(AitPayment::getAmount).sum();

        // festival bonus adjustment
        double proRataFestivalBonus = 0.0d;
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAllBetween(
            employee.getId(),
            taxQueryConfig.getIncomeYearStartDate(),
            taxQueryConfig.getIncomeYearEndDate()
        );

        proRataFestivalBonus = proRataFestivalBonusList.stream().mapToDouble(ProRataFestivalBonus::getAmount).sum();

        // taking consideration of old and present to future salaries
        ArrayList<SalarySum> salarySumList = new ArrayList<>();

        // given amount calculation
        List<EmployeeSalary> givenEmployeeSalaryList = calculatePreviousIncomeService.getAllPreviousSalariesWhichIsNotInHold(
            employee.getId(),
            taxQueryConfig
        );
        SalarySum givenSalaries = CalculateSalarySum.getSummationOfEmployeeSalaries(givenEmployeeSalaryList);
        salarySumList.add(givenSalaries);
        // to be given amount process
        SalarySum toBeGiven = processFinalSettlementToSalarySum(finalSettlementDTO);
        salarySumList.add(toBeGiven);

        // take consideration
        SalarySum yearlySalaryForTax = CalculateSalarySum.getSummationOfSalarySum(salarySumList);

        double yearlyPF = MathRoundUtil.round(ProvidentFundPerIncomeYear.getPFForFinalSettlement(givenEmployeeSalaryList));

        double yearlyFb = MathRoundUtil.round(YearlyFestivalBonus.getFb(employee.getMainGrossSalary(), employee.getEmployeeCategory()));

        yearlySalaryForTax.setEffectiveFestivalBonus(yearlyFb);

        // case :: income tax considering arrear salary
        // this month arrear

        double previousArrear = givenEmployeeSalaryList.stream().mapToDouble(EmployeeSalary::getArrearSalary).sum();
        SalarySum yearlySalary = Arrear.balanceArrear(yearlySalaryForTax, previousArrear);

        double calculatedYearlyIncomeTax = calculateIncomeTax(employee, yearlySalary, taxQueryConfig, proRataFestivalBonus, yearlyPF);

        double totalIncomeTax = calculatedYearlyIncomeTax;
        if (calculatedYearlyIncomeTax == 0) {
            totalIncomeTax = 0;
        } else if ((calculatedYearlyIncomeTax - aitPaymentSum) <= 0) {
            totalIncomeTax = 0;
        } else {
            totalIncomeTax = calculatedYearlyIncomeTax - aitPaymentSum;
        }

        List<EmployeeSalary> previousSalaries = calculatePreviousIncomeService.getAllPreviousSalaries(employee.getId(), taxQueryConfig);

        double givenIncomeTax = previousSalaries.stream().mapToDouble(EmployeeSalary::getTaxDeduction).sum();

        if (totalIncomeTax <= givenIncomeTax) {
            return 0;
        } else {
            return Math.ceil(totalIncomeTax - givenIncomeTax);
        }
    }

    private double calculateIncomeTax(
        Employee employee,
        SalarySum salarySum,
        TaxQueryConfig taxQueryConfig,
        double proRataFestivalBonus,
        double yearlyPfContribution
    ) {
        double perYearBasic = salarySum.getBasic();
        double perYearHouseRent = salarySum.getHouseRent();
        double perYearMedical = salarySum.getMedical();
        double perYearConveyance = salarySum.getConveyance();
        double perYearFestivalBonus = salarySum.getEffectiveFestivalBonus();

        perYearFestivalBonus = perYearFestivalBonus + proRataFestivalBonus;

        double perYearPfEmployerContribution = yearlyPfContribution;

        Gender gender = Gender.MALE;
        int age = 0;

        // defult configuration
        boolean hasDisabledChild = false;
        boolean isDisabled = false;
        boolean isFreedomFighter = false;
        boolean isFirstTimeTaxGiver = false;

        if (employee.getGender() != null) gender = employee.getGender();
        if (employee.getDateOfBirth() != null) age = LocalDate.now().compareTo(employee.getDateOfBirth());
        if (employee.getHasDisabledChild() != null) hasDisabledChild = employee.getHasDisabledChild();
        if (employee.getIsPhysicallyDisabled() != null) isDisabled = employee.getIsPhysicallyDisabled();
        if (employee.getIsFreedomFighter() != null) isFreedomFighter = employee.getIsFreedomFighter();
        if (employee.getIsFirstTimeAitGiver() != null) isFirstTimeTaxGiver = employee.getIsFirstTimeAitGiver();

        int incomeYearStartYear = taxQueryConfig.getIncomeYearStart();

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        // todo :: populate proper data here in tax calculation DTO for proper report.
        TaxCalculationDTO taxCalculationDTO = new TaxCalculationDTO(employee);
        IncomeTaxCalculateWithConfigService incomeTaxCalculateWithConfigService = new IncomeTaxCalculateWithConfigService(
            configParser,
            spelExpressionParser,
            incomeYearStartYear,
            taxCalculationDTO
        );

        double netTaxPerYear = incomeTaxCalculateWithConfigService.calculateIncomeTax(
            perYearBasic,
            perYearHouseRent,
            perYearMedical,
            perYearConveyance,
            perYearFestivalBonus,
            perYearPfEmployerContribution,
            gender,
            age,
            hasDisabledChild,
            isDisabled,
            isFreedomFighter,
            isFirstTimeTaxGiver,
            taxCalculationDTO
        );

        return netTaxPerYear;
    }

    private SalarySum processFinalSettlementToSalarySum(FinalSettlementDTO finalSettlementDTO) {
        SalarySum salarySum = new SalarySum();
        salarySum.setBasic(finalSettlementDTO.getmBasic());
        salarySum.setHouseRent(finalSettlementDTO.getmHouseRent());
        salarySum.setMedical(finalSettlementDTO.getmMedical());
        salarySum.setConveyance(finalSettlementDTO.getmConveyance());
        salarySum.setGross(
            finalSettlementDTO.getmBasic() +
            finalSettlementDTO.getmHouseRent() +
            finalSettlementDTO.getmMedical() +
            finalSettlementDTO.getmConveyance()
        );
        return salarySum;
    }
}
