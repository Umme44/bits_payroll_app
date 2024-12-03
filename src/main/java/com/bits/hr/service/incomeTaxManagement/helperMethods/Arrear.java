package com.bits.hr.service.incomeTaxManagement.helperMethods;

import com.bits.hr.service.incomeTaxManagement.model.SalarySum;
import com.bits.hr.service.salaryGenerationFractional.SalaryConstants;
import com.bits.hr.util.MathRoundUtil;

public class Arrear {

    public static SalarySum balanceArrear(SalarySum yearlySalarySum, double yearlyArrear) {
        double basicArrear = MathRoundUtil.round(yearlyArrear * SalaryConstants.BASIC_PERCENT);
        double houseRentArrear = MathRoundUtil.round(yearlyArrear * SalaryConstants.HOUSE_RENT_PERCENT);
        double medicalArrear = MathRoundUtil.round(yearlyArrear * SalaryConstants.MEDICAL_PERCENT);
        double conveyenceArrear = MathRoundUtil.round(yearlyArrear - (basicArrear + houseRentArrear + medicalArrear));

        yearlySalarySum.setBasic(yearlySalarySum.getBasic() + basicArrear);
        yearlySalarySum.setHouseRent(yearlySalarySum.getHouseRent() + houseRentArrear);
        yearlySalarySum.setMedical(yearlySalarySum.getMedical() + medicalArrear);
        yearlySalarySum.setConveyance(yearlySalarySum.getConveyance() + conveyenceArrear);

        return yearlySalarySum;
    }
}
