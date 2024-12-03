package com.bits.hr.service.incomeTaxManagement;

import com.bits.hr.config.YamlConfig;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.SalaryIncome;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.TaxCalculationDTO;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.TaxLiability;
import com.bits.hr.service.salaryGeneration.config.ConfigParser;
import com.bits.hr.service.salaryGeneration.config.IncomeTaxConfig;
import com.bits.hr.service.salaryGeneration.config.TaxSlab;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Import;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

// constructor param **

@Log4j2
@Import(YamlConfig.class)
@RequiredArgsConstructor
public class IncomeTaxCalculateWithConfigService {

    private static final String VAR_PER_YEAR_TOTAL_INCOME = "perYearTotalIncome";
    private static final String VAR_PER_YEAR_BASIC = "perYearBasic";
    private static final String VAR_PER_YEAR_HOUSE_RENT = "perYearHouseRent";
    private static final String VAR_PER_YEAR_MEDICAL = "perYearMedical";
    private static final String VAR_PER_YEAR_CONVEYANCE = "perYearConveyance";

    private static final String VAR_PER_YEAR_FESTIVAL_BONUS = "perYearFestivalBonus";
    private static final String VAR_PER_YEAR_PF_EMPLOYER_CONTRIBUTION = "perYearPfEmployerContribution";

    private static final String VAR_IS_DISABLED = "isDisabled";
    private static final String VAR_TAXABLE_INCOME = "taxableIncome";
    private static final String VAR_IS_FREEDOM_FIGHTER = "isFreedomFighter";
    private static final String VAR_HAS_DISABLED_CHILD = "hasDisabledChild";
    private static final String VAR_GENDER = "gender";
    private static final String VAR_IS_FIRST_TIME_TAX_GIVER = "isFirstTimeTaxGiver";
    private static final String VAR_AGE = "age";

    private final ConfigParser configParser;
    private final SpelExpressionParser spelExpressionParser;
    private final IncomeTaxConfig taxConfig;
    private TaxCalculationDTO taxCalculationDTO;

    public IncomeTaxCalculateWithConfigService(
        ConfigParser configParser,
        SpelExpressionParser spelExpressionParser,
        int incomeYearStartYear,
        TaxCalculationDTO taxCalculationDTO
    ) {
        this.configParser = configParser;
        this.spelExpressionParser = spelExpressionParser;
        this.taxConfig = configParser.parseFromDatabase(incomeYearStartYear);
        this.taxCalculationDTO = taxCalculationDTO;
    }

    public double calculateIncomeTax(
        double perYearBasic,
        double perYearHouseRent,
        double perYearMedical,
        double perYearConveyance,
        double perYearFestivalBonus,
        double perYearPfEmployerContribution,
        Gender gender,
        int age,
        boolean hasDisabledChild,
        boolean isDisabled,
        boolean isFreedomFighter,
        boolean isFirstTimeTaxGiver,
        TaxCalculationDTO taxCalculationDTO
    ) {
        // data Captured for report section
        taxCalculationDTO.setTaxReportConfigurations(taxConfig.getTaxReportConfigurations());
        taxCalculationDTO.setHasConsolidatedExemption(taxConfig.getTaxRules().getHasConsolidatedExemption());

        double taxableIncome = 0.0;
        taxableIncome += perYearBasic;

        final StandardEvaluationContext context = initContext(
            taxableIncome,
            perYearBasic,
            perYearHouseRent,
            perYearMedical,
            perYearConveyance,
            perYearFestivalBonus,
            perYearPfEmployerContribution,
            isDisabled,
            hasDisabledChild,
            isFreedomFighter,
            isFirstTimeTaxGiver,
            gender,
            age,
            taxConfig.getTaxRules().getTaxSlabs()
        );

        // initial data
        double totalExemption = 0d;
        double totalIncome = 0d;
        double houseRentExemption = 0d;
        double taxableHouseRent = 0d;
        double medicalExemption = 0d;
        double taxableMedical = 0d;
        double conveyanceExemption = 0d;
        double taxableConveyance = 0d;
        totalIncome =
            perYearBasic + perYearHouseRent + perYearMedical + perYearConveyance + perYearFestivalBonus + perYearPfEmployerContribution;

        if (taxConfig.getTaxRules().getHasConsolidatedExemption()) {
            // consolidated exemption new exemption rule 2023
            /**
             * new Rules,
             * exemption = min(4.5 lakh , (totalYearlyIncome/3))
             * rebate = min((taxableIncome*.03), 10lakh)
             * **/
            context.setVariable(VAR_PER_YEAR_TOTAL_INCOME, totalIncome);
            totalExemption = Math.round(getValueAsDouble(context, taxConfig.getTaxRules().getConsolidatedExemption()));

            // MathRoundUtil.round(Math.min(450000, (perYearTotalIncome / 3)));
            // context ::        min(450000, (#perYearTotalIncome / 3))
            taxableIncome = (totalIncome - totalExemption) < 0 ? 0 : (totalIncome - totalExemption);
        } else {
            // --------------- old exemption rule ----------------
            // HR exemption ----------------------------------------------------------------------------------------
            houseRentExemption = getValueAsDouble(context, taxConfig.getTaxRules().getHouseRentExemption());
            taxableHouseRent = (perYearHouseRent - houseRentExemption) < 0 ? 0 : (perYearHouseRent - houseRentExemption);
            taxableIncome += taxableHouseRent;
            // MED exemption ----------------------------------------------------------------------------------------
            medicalExemption = getValueAsDouble(context, taxConfig.getTaxRules().getMedicalExemption());
            taxableMedical = (perYearMedical - medicalExemption) < 0 ? 0 : (perYearMedical - medicalExemption);
            taxableIncome += taxableMedical;

            // CONV exemption ----------------------------------------------------------------------------------------
            conveyanceExemption = getValueAsDouble(context, taxConfig.getTaxRules().getConveyanceExemption());
            taxableConveyance = (perYearConveyance - conveyanceExemption) < 0 ? 0 : (perYearConveyance - conveyanceExemption);
            taxableIncome += taxableConveyance;

            taxableIncome += perYearFestivalBonus;
            taxableIncome += perYearPfEmployerContribution; // 100% taxable

            totalExemption = houseRentExemption + medicalExemption + conveyanceExemption;
        }

        taxCertificateSalaryIncome(
            taxCalculationDTO,
            perYearBasic,
            perYearHouseRent,
            perYearMedical,
            perYearConveyance,
            perYearFestivalBonus,
            perYearPfEmployerContribution,
            taxableIncome,
            totalExemption,
            totalIncome,
            houseRentExemption,
            taxableHouseRent,
            medicalExemption,
            taxableMedical,
            conveyanceExemption,
            taxableConveyance
        );

        context.setVariable(VAR_TAXABLE_INCOME, taxableIncome);

        double incomeTax = calculateIncomeTaxOfTaxableIncome(taxableIncome, context);
        // if income tax is 0 , return 0 ; minimum 5000 per year income tax is only applicable who have to pay tax , not for 0 amount of tax payer
        // for 0 amount of taxable amount , no need to calculate further operations.

        double rebate = Math.round(getValueAsDouble(context, taxConfig.getTaxRules().getRebate()));

        double maxAllowedInvestment = Math.round(getValueAsDouble(context, taxConfig.getTaxRules().getMaxAllowedInvestment()));

        // double investmentRebate = taxableIncome <=1500000 ?  taxableIncome * .25 * .15  : taxableIncome * .25 * .10;

        taxCalculationDTO.setMaxAllowedInvestment(maxAllowedInvestment);
        taxCalculationDTO.setRebate(rebate);

        double incomeTaxWithoutInvestmentRebate = incomeTax;
        incomeTax -= rebate;

        if (incomeTaxWithoutInvestmentRebate == 0.0d) {
            return 0.0d;
        } else if (incomeTax < 5000) {
            return 5000;
        } else {
            return incomeTax;
        }
    }

    private void taxCertificateSalaryIncome(
        TaxCalculationDTO taxCalculationDTO,
        double perYearBasic,
        double perYearHouseRent,
        double perYearMedical,
        double perYearConveyance,
        double perYearFestivalBonus,
        double perYearPfEmployerContribution,
        double taxableIncome,
        double totalExemption,
        double totalIncome,
        double houseRentExemption,
        double taxableHouseRent,
        double medicalExemption,
        double taxableMedical,
        double conveyanceExemption,
        double taxableConveyance
    ) {
        try {
            List<SalaryIncome> salaryIncomeList = new ArrayList<>();

            SalaryIncome basicSalaryIncome = new SalaryIncome();
            basicSalaryIncome.setKey(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeBasic().getKey());
            basicSalaryIncome.setVisibleInTaxReport(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeBasic().getIsVisibleInTaxReport()
            );
            basicSalaryIncome.setSalary(perYearBasic);
            basicSalaryIncome.setExemption(0.0d);
            basicSalaryIncome.setTaxableIncome(perYearBasic);
            basicSalaryIncome.setHead(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeBasic().getHead());
            basicSalaryIncome.setSubHead(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeBasic().getSubHead()
            );
            salaryIncomeList.add(basicSalaryIncome);

            SalaryIncome houseRentSalaryIncome = new SalaryIncome();
            houseRentSalaryIncome.setKey(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeHr().getKey());
            houseRentSalaryIncome.setVisibleInTaxReport(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeHr().getIsVisibleInTaxReport()
            );
            houseRentSalaryIncome.setSalary(perYearHouseRent);
            houseRentSalaryIncome.setExemption(houseRentExemption);
            houseRentSalaryIncome.setTaxableIncome(taxableHouseRent);
            houseRentSalaryIncome.setHead(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeHr().getHead());
            houseRentSalaryIncome.setSubHead(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeHr().getSubHead()
            );
            salaryIncomeList.add(houseRentSalaryIncome);

            SalaryIncome medicalSalaryIncome = new SalaryIncome();
            medicalSalaryIncome.setKey(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeMedical().getKey());
            medicalSalaryIncome.setVisibleInTaxReport(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeMedical().getIsVisibleInTaxReport()
            );
            medicalSalaryIncome.setSalary(perYearMedical);
            medicalSalaryIncome.setExemption(medicalExemption);
            medicalSalaryIncome.setTaxableIncome(taxableMedical);
            medicalSalaryIncome.setHead(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeMedical().getHead());
            medicalSalaryIncome.setSubHead(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeMedical().getSubHead()
            );
            salaryIncomeList.add(medicalSalaryIncome);

            SalaryIncome conveyanceSalaryIncome = new SalaryIncome();
            conveyanceSalaryIncome.setKey(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeConveyanceAllowance().getKey()
            );
            conveyanceSalaryIncome.setVisibleInTaxReport(
                taxConfig
                    .getTaxReportConfigurations()
                    .getSalaryIncomeLabels()
                    .getSalaryIncomeConveyanceAllowance()
                    .getIsVisibleInTaxReport()
            );
            conveyanceSalaryIncome.setSalary(perYearConveyance);
            conveyanceSalaryIncome.setExemption(conveyanceExemption);
            conveyanceSalaryIncome.setTaxableIncome(taxableConveyance);
            conveyanceSalaryIncome.setHead(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeConveyanceAllowance().getHead()
            );
            conveyanceSalaryIncome.setSubHead(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeConveyanceAllowance().getSubHead()
            );
            salaryIncomeList.add(conveyanceSalaryIncome);

            SalaryIncome festivalBonusSalaryIncome = new SalaryIncome();
            festivalBonusSalaryIncome.setKey(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeFestivalBonus().getKey()
            );
            festivalBonusSalaryIncome.setVisibleInTaxReport(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeFestivalBonus().getIsVisibleInTaxReport()
            );
            festivalBonusSalaryIncome.setSalary(perYearFestivalBonus);
            festivalBonusSalaryIncome.setExemption(0d);
            festivalBonusSalaryIncome.setTaxableIncome(perYearFestivalBonus);
            festivalBonusSalaryIncome.setHead(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeFestivalBonus().getHead()
            );
            festivalBonusSalaryIncome.setSubHead(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeHr().getSubHead()
            );
            salaryIncomeList.add(festivalBonusSalaryIncome);

            SalaryIncome pfSalaryIncome = new SalaryIncome();
            pfSalaryIncome.setKey(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomePf().getKey());
            pfSalaryIncome.setVisibleInTaxReport(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomePf().getIsVisibleInTaxReport()
            );
            pfSalaryIncome.setSalary(perYearPfEmployerContribution);
            pfSalaryIncome.setExemption(0d);
            pfSalaryIncome.setTaxableIncome(perYearPfEmployerContribution);
            pfSalaryIncome.setHead(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomePf().getHead());
            pfSalaryIncome.setSubHead(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomePf().getSubHead());
            salaryIncomeList.add(pfSalaryIncome);

            // total
            // sum of list won't work here as there are multiple rules for calculations
            SalaryIncome totalSalaryIncome = new SalaryIncome();
            totalSalaryIncome.setKey(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeTotal().getKey());
            totalSalaryIncome.setVisibleInTaxReport(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeTotal().getIsVisibleInTaxReport()
            );
            totalSalaryIncome.setSalary(totalIncome);
            totalSalaryIncome.setExemption(totalExemption);
            totalSalaryIncome.setTaxableIncome(taxableIncome);
            totalSalaryIncome.setHead(taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeTotal().getHead());
            totalSalaryIncome.setSubHead(
                taxConfig.getTaxReportConfigurations().getSalaryIncomeLabels().getSalaryIncomeTotal().getSubHead()
            );

            taxCalculationDTO.setSalaryIncomeList(salaryIncomeList);
            taxCalculationDTO.setTotalSalaryIncome(totalSalaryIncome);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private double getValueAsDouble(StandardEvaluationContext context, String expression) {
        return Double.parseDouble(spelExpressionParser.parseExpression(expression).getValue(context).toString());
    }

    private StandardEvaluationContext initContext(
        double taxableIncome,
        double perYearBasic,
        double perYearHouseRent,
        double perYearMedical,
        double perYearConveyance,
        double perYearFestivalBonus,
        double perYearPfEmployerContribution,
        boolean isDisabled,
        boolean hasDisabledChild,
        boolean isFreedomFighter,
        boolean isFirstTimeTaxGiver,
        Gender gender,
        int age,
        TaxSlab[] taxSlabs
    ) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable(VAR_TAXABLE_INCOME, taxableIncome);

        context.setVariable(VAR_PER_YEAR_BASIC, perYearBasic);
        context.setVariable(VAR_PER_YEAR_HOUSE_RENT, perYearHouseRent);
        context.setVariable(VAR_PER_YEAR_MEDICAL, perYearMedical);
        context.setVariable(VAR_PER_YEAR_CONVEYANCE, perYearConveyance);

        context.setVariable(VAR_PER_YEAR_FESTIVAL_BONUS, perYearFestivalBonus);
        context.setVariable(VAR_PER_YEAR_PF_EMPLOYER_CONTRIBUTION, perYearPfEmployerContribution);

        context.setVariable(VAR_HAS_DISABLED_CHILD, hasDisabledChild);
        context.setVariable(VAR_IS_DISABLED, isDisabled);
        context.setVariable(VAR_IS_FREEDOM_FIGHTER, isFreedomFighter);
        context.setVariable(VAR_GENDER, gender.ordinal());
        context.setVariable(VAR_IS_FIRST_TIME_TAX_GIVER, isFirstTimeTaxGiver);
        context.setVariable(VAR_AGE, age);
        for (int i = 0; i < taxSlabs.length; i++) {
            context.setVariable("taxSlab_" + (i + 1), taxSlabs[i].getLimit());
        }
        context.setRootObject(new CalculationFunctions());
        return context;
    }

    /*
     * this method is responsible to calculate annual income tax
     * considering defined income slabs
     * from taxable income
     * */
    public double calculateIncomeTaxOfTaxableIncome(double taxableAmount, StandardEvaluationContext context) {
        double taxSlab1 = calculateSlab_1(context);

        taxConfig.getTaxRules().getTaxSlabs()[0].setLimit(taxSlab1);
        double balance = taxableAmount;
        double incomeTax = 0D;

        TaxSlab[] taxSlabs = taxConfig.getTaxRules().getTaxSlabs();
        for (int i = 0; i < taxSlabs.length - 1; i++) {
            TaxSlab taxSlab = taxSlabs[i];
            if (balance >= taxSlab.getLimit()) {
                balance -= taxSlab.getLimit();
                incomeTax += (taxSlab.getLimit() * taxSlab.getRate());
            } else {
                incomeTax += (balance * taxSlab.getRate());
                balance = 0;
                break;
            }
        }

        final TaxSlab taxSlab = taxConfig.getTaxRules().getTaxSlabs()[taxSlabs.length - 1];
        if (balance >= taxSlab.getLimit()) {
            incomeTax += (balance * taxSlab.getRate());
        }

        List<TaxLiability> taxLiabilityList = getTaxLiability(taxableAmount, taxSlabs);
        this.taxCalculationDTO.setTaxLiability(taxLiabilityList);
        return incomeTax;
    }

    private double calculateSlab_1(StandardEvaluationContext context) {
        double taxSlab1 = taxConfig.getTaxRules().getTaxSlabs()[0].getLimit();

        for (String expression : taxConfig.getTaxRules().getFirstTaxSlabSpecialCases().getExpressions()) {
            taxSlab1 = getValueAsDouble(context, expression);
            context.setVariable("taxSlab_1", taxSlab1);
        }
        return taxSlab1;
    }

    /*
     * this method will take context as a parameter and return
     * income tax slab 1
     * */

    /*
     * Utility method to serve context expression
     */
    public static class CalculationFunctions {

        public double min(double val1, double val2) {
            return Math.min(val1, val2);
        }

        public double max(double val1, double val2) {
            return Math.max(val1, val2);
        }
    }

    // reporting purpose use only
    private List<TaxLiability> getTaxLiability(double taxableAmount, TaxSlab[] taxSlabs) {
        double balance = taxableAmount;

        List<TaxLiability> taxLiabilityList = new ArrayList<>();

        List<Double> slabWiseTaxableIncome = new ArrayList<>();

        for (int i = 0; i < taxSlabs.length; i++) {
            TaxSlab taxSlab = taxSlabs[i];
            double tax = 0d;

            if (balance >= taxSlab.getLimit()) {
                if (taxSlabs.length - 1 == i) {
                    tax = balance * taxSlab.getRate();
                    slabWiseTaxableIncome.add(balance);
                    balance = 0d;
                } else {
                    balance -= taxSlab.getLimit();
                    tax = taxSlab.getLimit() * taxSlab.getRate();
                    slabWiseTaxableIncome.add(taxSlab.getLimit());
                }
            } else if (balance > 0) {
                tax = balance * taxSlab.getRate();
                slabWiseTaxableIncome.add(balance);
                balance = 0;
            } else {
                tax = 0;
                slabWiseTaxableIncome.add(0d);
            }

            //            if (i == 0) {
            //                taxLiability.setHead("ON FIRST  " + NumberFormatUtil.processToDecimalFormat(String.valueOf(MathRoundUtil.round(taxSlab.getLimit()))));
            //            }
            //            if (i != 0 && i != taxSlabs.length - 1) {
            //                taxLiability.setHead("ON NEXT UP TO  " + NumberFormatUtil.processToDecimalFormat(String.valueOf(MathRoundUtil.round(taxSlab.getLimit()))));
            //            }
            //            if (i != 0 && i == taxSlabs.length - 1) {
            //                taxLiability.setHead("ON BALANCE AMOUNT");
            //            }

            TaxLiability taxLiability = new TaxLiability();
            taxLiability.setHead(taxSlab.getHead());
            taxLiability.setSubHead(taxSlab.getSubHead());
            taxLiability.setRate(taxSlab.getRate());
            taxLiability.setSlab(getSlabWiseTaxableIncome(slabWiseTaxableIncome, i));
            taxLiability.setTax(tax);
            taxLiabilityList.add(taxLiability);
        }

        return taxLiabilityList;
    }

    private double getSlabWiseTaxableIncome(List<Double> slabWseIncome, int index) {
        try {
            return slabWseIncome.get(index);
        } catch (Exception ex) {
            return 0d;
        }
    }
}
