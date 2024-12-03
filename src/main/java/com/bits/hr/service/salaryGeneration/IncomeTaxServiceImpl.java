package com.bits.hr.service.salaryGeneration;

import com.bits.hr.domain.enumeration.Gender;

@Deprecated
public class IncomeTaxServiceImpl implements IncomeTaxService {

    @Override
    public double calculateIncomeTax(
        double mainGrossPerMonth, //list of item + type
        double perYearFestivalBonus,
        double pfContribution,
        String Place, // dcc ?? => Enum **
        boolean isDisabledChildParent, // hasDisabledChild
        Gender gender,
        int age, // date of birth
        boolean isDisabled,
        boolean isFreedomFighter,
        boolean isFirstTimeTaxGiver
        // income tax already paid **   //  entity for advance iTax
    ) {
        double taxableIncome = 0.0;

        double mainGrossPerYear = mainGrossPerMonth * 12;
        double perYearBasic = mainGrossPerYear * .60;

        taxableIncome += perYearBasic;

        double perYearHouseRent = mainGrossPerYear * .30;

        double houseRentRebate = Math.min(3_00_000d, (perYearBasic * .50));

        taxableIncome += (perYearHouseRent - houseRentRebate);

        double perYearMedical = mainGrossPerYear * .06;

        double medicalRebate = Math.min(1_20_000d, (perYearBasic * .10));
        if (isDisabled) {
            medicalRebate = Math.min(10_00_000, perYearBasic * .10);
        }
        taxableIncome += (perYearHouseRent - medicalRebate);

        double perYearConveyance = mainGrossPerYear - (perYearBasic + perYearHouseRent + perYearMedical);

        double conveyanceRebate = Math.min(perYearConveyance, 30_000d);

        taxableIncome += (perYearHouseRent - conveyanceRebate);

        taxableIncome += perYearFestivalBonus;

        taxableIncome += pfContribution; // 100% taxable

        double investmentRebate = Math.min((taxableIncome * .25), (1_50_00_000d));

        taxableIncome -= investmentRebate;

        double incomeTax = calculateIncomeTaxOfTaxableIncome(
            taxableIncome,
            null,
            isDisabledChildParent,
            gender,
            age,
            isDisabled,
            isFreedomFighter,
            isFirstTimeTaxGiver
        );
        return incomeTax;
    }

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
        return 0;
    }

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
        double L1 = 3_00_000d;
        double percent1 = 0.00d;
        double L2 = 1_00_000d;
        double percent2 = 0.05d;
        double L3 = 3_00_000d;
        double percent3 = 0.10d;
        double L4 = 4_00_000d;
        double percent4 = 0.15d;
        double L5 = 5_00_000d;
        double percent5 = 0.20d;
        double percent6 = 0.25d;

        // rest of the amount itax=25%

        // first band modification
        if (gender == Gender.FEMALE || age >= 65) {
            L1 = 3_50_000D;
        }
        if (isDisabled) {
            L1 = 4_50_000D;
        }
        if (isFreedomFighter) {
            L1 = 4_75_000D;
        }
        if (isDisabledChildParent) {
            L1 += 50_000;
        }

        double balance = taxableAmount;
        double incomeTax = 0D;

        // first ......................
        if (balance >= L1) {
            balance = balance - L1;
            incomeTax += 0D;
        } else {
            balance = 0;
            incomeTax = 0D;
        }

        // 2 ......................

        if (balance >= L2) {
            incomeTax += L2 * percent2;
            balance = balance - L2;
        } else {
            incomeTax += balance * percent2;
            balance = 0d;
        }
        // 3 ......................
        if (balance >= L3) {
            incomeTax += L3 * percent3;
            balance = balance - L3;
        } else {
            incomeTax += balance * percent3;
            balance = 0d;
        }

        // 4 ......................
        if (balance >= L4) {
            incomeTax += L4 * percent4;
            balance = balance - L4;
        } else {
            incomeTax += balance * percent4;
            balance = 0d;
        }

        // 5 ......................
        if (balance >= L5) {
            incomeTax += L5 * percent5;
            balance = balance - L5;
        } else {
            incomeTax += balance * percent5;
            balance = 0d;
        }
        // 6 ......................
        if (balance > 0) {
            incomeTax += balance * percent6;
            balance = 0d;
        }
        return incomeTax;
    }
}
