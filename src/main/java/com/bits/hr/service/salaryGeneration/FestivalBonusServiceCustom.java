package com.bits.hr.service.salaryGeneration;

import com.bits.hr.domain.enumeration.EmployeeCategory;

public interface FestivalBonusServiceCustom {
    double calculateMonthlyProvisionsForFestivalBonus(Double mainGross, EmployeeCategory employeeCategory, Integer numberOfServiceDays);
}
