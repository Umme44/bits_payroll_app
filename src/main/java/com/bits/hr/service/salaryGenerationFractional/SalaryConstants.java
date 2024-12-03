package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.enumeration.EmployeeCategory;

public class SalaryConstants {

    public static final double BASIC_PERCENT = .60D;
    //    public static final double HOUSE_RENT_PERCENT = .30D;
    public static final double HOUSE_RENT_PERCENT = .30D;

    public static final double MEDICAL_PERCENT = .06D;
    public static final double CONVEYANCE_PERCENT = .04D;

    // PF RELATED
    public static final EmployeeCategory PF_ELIGIBLE_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
    // PF = 10 % OF BASIC , BASIC = 60% OF GROSS
    public static final double PF_PERCENT_FROM_GROSS = BASIC_PERCENT * .10D;
    // 10% of basic
    public static final double PF_PERCENT_FROM_BASIC = 0.10D;
}
