package com.bits.hr.service.incomeTaxManagement;

import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.repository.*;
import com.bits.hr.service.incomeTaxManagement.model.IncomeTaxData;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.TaxCalculationDTO;
import com.bits.hr.service.salaryGeneration.config.ConfigParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

/*
 * this class will be responsible for
 * calculating yearly income tax considering
 * post AIT and pre ait
 * */
@Service
public class IncomeTaxCalculatePerYear {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AitPaymentRepository aitPaymentRepository;

    @Autowired
    AitConfigRepository aitConfigRepository;

    @Autowired
    EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    ProRataFestivalBonusRepository proRataFestivalBonusRepository;

    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    CalculatePreviousIncomeService calculatePreviousIncomeService;

    @Autowired
    ConfigParser configParser;

    /*
     * this method will mainly be responsible for calculating employee income tax
     * salarySum will mainly have all salary attributes of salary
     * If salary changing then salary sum needs to be calculated.
     * */
    public double calculateYearlyIncomeTax(IncomeTaxData incomeTaxData, TaxCalculationDTO taxCalculationDTO) {
        double perYearBasic = incomeTaxData.getYearlyBasic();
        double perYearHouseRent = incomeTaxData.getYearlyHouseRent();
        double perYearMedical = incomeTaxData.getYearlyMedical();
        double perYearConveyance = incomeTaxData.getYearlyConveyance();
        double perYearFestivalBonus = incomeTaxData.getEffectiveFestivalBonus() + incomeTaxData.getIndividualArrearFestivalBonus();
        double perYearPfEmployerContribution =
            incomeTaxData.getYearlyPf() + incomeTaxData.getArrearPf() + incomeTaxData.getIndividualArrearPf();

        taxCalculationDTO.setPerYearFestivalBonus(perYearFestivalBonus);
        taxCalculationDTO.setPerYearPfEmployerContribution(perYearPfEmployerContribution);

        Gender gender = incomeTaxData.getGender();
        int age = incomeTaxData.getAge();
        boolean hasDisabledChild = incomeTaxData.isHasDisabledChild();
        boolean isDisabled = incomeTaxData.isDisabled();
        boolean isFreedomFighter = incomeTaxData.isFreedomFighter();
        boolean isFirstTimeTaxGiver = incomeTaxData.isFirstTimeTaxGiver();

        int incomeYearStartYear = incomeTaxData.getTaxQueryConfig().getIncomeYearStart();

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

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

        taxCalculationDTO.setNetTaxLiability(netTaxPerYear);

        double ait = incomeTaxData.getAit();

        taxCalculationDTO.setLastYearAdjustment(ait);
        if (netTaxPerYear == 0) {
            return 0;
        }
        // negative income tax would be approved [ said by palash da ]
        // else if ((netTaxPerYear - ait) <= 0 && netTaxPerYear<0) {
        //    return netTaxPerYear;
        // }
        else {
            return netTaxPerYear - ait;
        }
    }
}
