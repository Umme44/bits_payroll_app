package com.bits.hr.service.salaryGeneration;

import com.bits.hr.domain.enumeration.Gender;
import org.springframework.stereotype.Service;

@Service
public interface IncomeTaxService {
    double calculateIncomeTax(
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
    );

    @Deprecated
    double calculateIncomeTaxOfTaxableIncome(
        double taxableAmount,
        String Place,
        boolean isDisabledChildParent,
        Gender gender,
        int age,
        boolean isDisabled,
        boolean isFreedomFighter,
        boolean isFirstTimeTaxGiver
    );

    @Deprecated
    double calculateIncomeTax(
        double mainGrossPerMonth,
        double perYearFestivalBonus,
        double pfContribution,
        String Place,
        boolean isDisabledChildParent,
        Gender gender,
        int age,
        boolean isDisabled,
        boolean isFreedomFighter,
        boolean isFirstTimeTaxGiver
    );
}
