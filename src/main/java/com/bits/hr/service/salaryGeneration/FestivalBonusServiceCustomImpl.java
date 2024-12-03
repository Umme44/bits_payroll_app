package com.bits.hr.service.salaryGeneration;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import org.springframework.stereotype.Service;

@Service
public class FestivalBonusServiceCustomImpl implements FestivalBonusServiceCustom {

    @Override
    public double calculateMonthlyProvisionsForFestivalBonus(
        Double mainGross,
        EmployeeCategory employeeCategory,
        Integer numberOfServiceDays
    ) {
        double festivalBonus = 0d;
        switch (employeeCategory) {
            case REGULAR_CONFIRMED_EMPLOYEE:
                festivalBonus = ((mainGross * 0.60) * 2) / 12;
                break;
            case REGULAR_PROVISIONAL_EMPLOYEE:
                // main gross is as same as main gross basic for provisioner employee
                // festivalBonus = ((mainGross * 2) / 365d) * numberOfServiceDays;
                festivalBonus = ((mainGross * 0.60) * 2) / 12;
                break;
            case CONTRACTUAL_EMPLOYEE:
                festivalBonus = ((mainGross * 0.50) * 2) / 12;
                break;
            case INTERN:
                festivalBonus = 0d;
                break;
            default:
                return 0d;
        }

        return festivalBonus;
    }
}
