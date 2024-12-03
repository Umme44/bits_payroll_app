package com.bits.hr.service.salaryGeneration;

import com.bits.hr.config.YamlConfig;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.service.salaryGeneration.config.ConfigParser;
import com.bits.hr.service.salaryGeneration.config.IncomeTaxConfig;
import com.bits.hr.service.salaryGeneration.config.TaxSlab;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

@Import(YamlConfig.class)
@Service
@RequiredArgsConstructor
public class IncomeTaxServiceImplWithConfig implements IncomeTaxService {

    private static final String VAR_PER_YEAR_BASIC = "perYearBasic";
    private static final String VAR_PER_YEAR_CONVEYANCE = "perYearConveyance";
    private static final String VAR_PER_YEAR_HOUSE_RENT = "perYearHouseRent";
    private static final String VAR_PER_YEAR_FESTIVAL_BONUS = "perYearFestivalBonus";
    private static final String VAR_IS_DISABLED = "isDisabled";
    private static final String VAR_TAXABLE_INCOME = "taxableIncome";
    private static final String VAR_IS_FREEDOM_FIGHTER = "isFreedomFighter";
    private static final String VAR_HAS_DISABLED_CHILD = "hasDisabledChild";
    private static final String VAR_GENDER = "gender";
    private static final String VAR_IS_FIRST_TIME_TAX_GIVER = "isFirstTimeTaxGiver";
    private static final String VAR_AGE = "age";

    private final ConfigParser configParser;
    private final SpelExpressionParser spelExpressionParser;
    private IncomeTaxConfig taxConfig;

    @PostConstruct
    private void init() {
        this.taxConfig = configParser.parseFromConfigFile();
    }

    /*TODO:Will Remove*/
    @Deprecated
    @Override
    public double calculateIncomeTax(
        double mainGrossPerMonth,
        double perYearFestivalBonus,
        double pfContribution,
        String Place,
        boolean hasDisabledChild,
        Gender gender,
        int age,
        boolean isDisabled,
        boolean isFreedomFighter,
        boolean isFirstTimeTaxGiver
    ) {
        double mainGrossPerYear = mainGrossPerMonth * 12;
        double perYearBasic = mainGrossPerYear * .60;
        double perYearHouseRent = mainGrossPerYear * .30;
        double perYearMedical = mainGrossPerYear * .06;
        return calculateIncomeTax(
            perYearBasic,
            perYearHouseRent,
            mainGrossPerYear - (perYearBasic + perYearHouseRent + perYearMedical),
            perYearFestivalBonus,
            pfContribution,
            hasDisabledChild,
            gender,
            age,
            isDisabled,
            isFreedomFighter,
            isFirstTimeTaxGiver
        );
    }

    /*
     * this method will be used mainly for calculation of income tax
     *
     * */
    @Override
    public double calculateIncomeTax(
        double perYearBasic,
        double perYearHouseRent,
        double perYearConveyance,
        double perYearFestivalBonus,
        double pfContribution,
        boolean hasDisabledChild,
        Gender gender,
        int age,
        boolean isDisabled,
        boolean isFreedomFighter,
        boolean isFirstTimeTaxGiver
    ) {
        double taxableIncome = 0.0;
        taxableIncome += perYearBasic;

        final StandardEvaluationContext context = initContext(
            taxableIncome,
            isDisabled,
            perYearBasic,
            perYearConveyance,
            perYearHouseRent,
            perYearFestivalBonus,
            hasDisabledChild,
            isFreedomFighter,
            isFirstTimeTaxGiver,
            gender,
            age,
            taxConfig.getTaxRules().getTaxSlabs()
        );

        double houseRentRebate = getValueAsDouble(context, taxConfig.getTaxRules().getHouseRentExemption());
        taxableIncome += (perYearHouseRent - houseRentRebate);

        double medicalRebate = getValueAsDouble(context, taxConfig.getTaxRules().getMedicalExemption());
        taxableIncome += (perYearHouseRent - medicalRebate);

        double conveyanceRebate = getValueAsDouble(context, taxConfig.getTaxRules().getConveyanceExemption());

        taxableIncome += (perYearHouseRent - conveyanceRebate);
        taxableIncome += perYearFestivalBonus;
        taxableIncome += pfContribution; // 100% taxable

        double investmentRebate = getValueAsDouble(context, taxConfig.getTaxRules().getRebate());

        taxableIncome -= investmentRebate;

        return calculateIncomeTaxOfTaxableIncome(taxableIncome, context);
    }

    private double getValueAsDouble(StandardEvaluationContext context, String expression) {
        return getValueAsDouble(context, expression, Double.class);
    }

    @SuppressWarnings({ "unchecked", "SameParameterValue" })
    private <T> T getValueAsDouble(StandardEvaluationContext context, String expression, Class<T> clazz) {
        return (T) spelExpressionParser.parseExpression(expression).getValue(context);
    }

    private StandardEvaluationContext initContext(
        double taxableIncome,
        boolean isDisabled,
        double perYearBasic,
        double perYearConveyance,
        double perYearHouseRent,
        double perYearFestivalBonus,
        boolean hasDisabledChild,
        boolean isFreedomFighter,
        boolean isFirstTimeTaxGiver,
        Gender gender,
        int age,
        TaxSlab[] taxSlabs
    ) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable(VAR_TAXABLE_INCOME, taxableIncome);
        context.setVariable(VAR_IS_DISABLED, isDisabled);
        context.setVariable(VAR_PER_YEAR_BASIC, perYearBasic);
        context.setVariable(VAR_PER_YEAR_CONVEYANCE, perYearConveyance);
        context.setVariable(VAR_PER_YEAR_HOUSE_RENT, perYearHouseRent);
        context.setVariable(VAR_PER_YEAR_FESTIVAL_BONUS, perYearFestivalBonus);
        context.setVariable(VAR_HAS_DISABLED_CHILD, hasDisabledChild);
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

    /*TODO: WILL REMOVE*/
    @Deprecated
    @Override
    public double calculateIncomeTaxOfTaxableIncome(
        double taxableAmount,
        String Place,
        boolean isDisabledChildParent,
        Gender gender,
        int age,
        boolean isDisabled,
        boolean isFreedomFighter,
        boolean isFirstTimeTaxGiver
    ) {
        return calculateIncomeTaxOfTaxableIncome(
            taxableAmount,
            initContext(
                taxableAmount,
                isDisabled,
                0,
                0,
                0,
                0,
                isDisabledChildParent,
                isFreedomFighter,
                isFirstTimeTaxGiver,
                gender,
                age,
                taxConfig.getTaxRules().getTaxSlabs()
            )
        );
    }

    /*
     * this method is responsible to calculate annual income tax
     * from taxable income
     * */
    public double calculateIncomeTaxOfTaxableIncome(double taxableAmount, StandardEvaluationContext context) {
        double taxSlab1 = taxConfig.getTaxRules().getTaxSlabs()[0].getLimit();

        for (String expression : taxConfig.getTaxRules().getFirstTaxSlabSpecialCases().getExpressions()) {
            taxSlab1 = getValueAsDouble(context, expression);
            context.setVariable("taxSlab_1", taxSlab1);
        }
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

        return incomeTax;
    }

    /*
     * Utility method to serve context
     */
    public static class CalculationFunctions {

        public double min(double val1, double val2) {
            return Math.min(val1, val2);
        }

        public double max(double val1, double val2) {
            return Math.max(val1, val2);
        }
    }
}
