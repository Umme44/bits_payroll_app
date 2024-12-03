package com.bits.hr.service.finalSettlement.util;

import com.bits.hr.service.finalSettlement.dto.PfStatement;
import com.bits.hr.service.finalSettlement.dto.pf.PfContribution;
import com.bits.hr.service.finalSettlement.dto.pf.PfDetails;
import com.bits.hr.service.finalSettlement.dto.pf.PfInterest;
import com.bits.hr.service.finalSettlement.dto.pf.YearlyPfCollection;
import com.bits.hr.service.finalSettlement.dto.salary.TimeDuration;
import com.bits.hr.util.MathRoundUtil;
import java.util.ArrayList;

public class PfStatementToPfDetails {

    public static PfDetails generatePfDetailsFromPfStatement(PfStatement pfStatement, TimeDuration pFTenure) {
        PfDetails pfDetails = new PfDetails();

        // opening balance calculation
        double openingBalance = pfStatement.getOpeningBalance();

        pfDetails.setOpeningBalance(openingBalance);

        ArrayList<PfContribution> pfContributionList = new ArrayList<>();
        ArrayList<PfInterest> pfInterestList = new ArrayList<>();

        // pf contribution
        for (YearlyPfCollection yearlyPfCollection : pfStatement.getYearlyPfCollection()) {
            PfContribution pfContribution = new PfContribution();

            pfContribution.setYear(yearlyPfCollection.getYear());

            double yearlyEmployeeContribution = pfStatement.getTotalContribution().getTotalEmployeePortion();
            double yearlyEmployerContribution = pfStatement.getTotalContribution().getTotalEmployerPortion();

            pfContribution.setOwnContribution(yearlyEmployeeContribution);
            pfContribution.setCompanyContribution(yearlyEmployerContribution);

            pfContributionList.add(pfContribution);
        }
        pfDetails.setPfContributionList(pfContributionList);

        PfInterest pfInterest = new PfInterest();
        pfInterest.setOwnInterest(pfStatement.getTotalPfInterest().getTotalEmployeePortion());
        pfInterest.setCompanyInterest(pfStatement.getTotalPfInterest().getTotalEmployerPortion());
        pfDetails.setPfInterest(pfInterest);
        pfDetails.setPfArrear(pfStatement.getAdjustmentForArrearsPf());
        // total payable PF

        if (pFTenure.getYear() < 1) {
            pfDetails.setRemarks("Provident Fund service tenure is lesser then one year");
            pfDetails.setTotalPfPayable(
                MathRoundUtil.round((pfStatement.getNetPfPayable() - pfStatement.getOpeningBalance()) / 2.0d) +
                pfStatement.getOpeningBalance()
            );
        } else {
            pfDetails.setTotalPfPayable(pfStatement.getNetPfPayable());
        }

        return pfDetails;
    }
}
