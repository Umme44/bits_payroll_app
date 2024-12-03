package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.finalSettlement.dto.DetailedPfStatement;
import com.bits.hr.service.finalSettlement.dto.pf.TotalPf;
import com.bits.hr.service.finalSettlement.dto.pf.YearlyPfCollectionForDetailedPfStatement;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DetailedPfStatementService {

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private YearlyPfCollectionService yearlyPfCollectionService;

    @Autowired
    private SalaryToPfCollectionMapperService salaryToPfCollectionMapperService;

    @Autowired
    private PfArrearsCollectionService pfArrearsCollectionService;

    @Autowired
    private YearlyPfCollectionServiceForDetailedPfStatement yearlyPfCollectionServiceForDetailedPfStatement;

    public Optional<DetailedPfStatement> generate(
        PfAccount pfAccount,
        int startingYear,
        int startingMonth,
        int endingYear,
        int endingMonth
    ) {
        DetailedPfStatement pfStatement = new DetailedPfStatement();

        pfStatement.setName(pfAccount.getAccHolderName());
        pfStatement.setDateOfJoining(pfAccount.getDateOfJoining());
        pfStatement.setDateOfConfirmation(pfAccount.getDateOfConfirmation());

        // opening balance
        Optional<PfCollection> pfOpeningBalanceOptional = pfCollectionRepository.getOpeningPfBalance(
            pfAccount.getPfCode(),
            pfAccount.getPin()
        );

        if (pfOpeningBalanceOptional.isPresent()) {
            PfCollection openingBalance = pfOpeningBalanceOptional.get();
            double sumOpeningBalance =
                openingBalance.getEmployeeContribution() +
                openingBalance.getEmployeeInterest() +
                openingBalance.getEmployerContribution() +
                openingBalance.getEmployerInterest();

            pfStatement.setOpeningBalance(sumOpeningBalance);
        } else {
            pfStatement.setOpeningBalance(0d);
        }

        // yearly pf collections
        List<YearlyPfCollectionForDetailedPfStatement> perYearPfCollection = yearlyPfCollectionServiceForDetailedPfStatement.get(
            pfAccount,
            startingYear,
            startingMonth,
            endingYear,
            endingMonth
        );

        pfStatement.setYearlyPfCollection(perYearPfCollection);

        // totalContribution -> without interests
        // PFInterests
        // Total PF Contribution with Interest
        //Adjustment for Arrear PF

        TotalPf totalContribution = getTotalContribution(perYearPfCollection);
        TotalPf totalInterests = getTotalInterests(pfAccount.getId());
        TotalPf totalArreatPf = getTotalArrearPf(perYearPfCollection);
        TotalPf totalContributionAndInterests = getContributionAndInterests(totalContribution, totalInterests);

        pfStatement.setTotalContribution(totalContribution);
        pfStatement.setTotalPfInterest(totalInterests);
        pfStatement.setTotalContributionWithInterests(totalContributionAndInterests);

        //        double arrearAdjustmentForPf = pfArrearsCollectionService.get(pfAccount);
        pfStatement.setAdjustmentForArrearsPf(totalArreatPf);

        // net PF will be dependent upon service tenure
        // if service tenure is greater than one year ,
        // employee will get both employee contributions and employer contributions
        // else => employee will get only his interests and his part of interests
        // *** this logic handled in final settlement process

        double netPay =
            pfStatement.getAdjustmentForArrearsPf().getTotalEmployeePortion() +
            pfStatement.getAdjustmentForArrearsPf().getTotalEmployerPortion() +
            pfStatement.getOpeningBalance() +
            pfStatement.getTotalContributionWithInterests().getTotalEmployeePortion() +
            pfStatement.getTotalContributionWithInterests().getTotalEmployerPortion();

        pfStatement.setNetPfPayable(netPay);

        return Optional.of(pfStatement);
    }

    private TotalPf getTotalContribution(List<YearlyPfCollectionForDetailedPfStatement> yearlyPfCollectionList) {
        TotalPf totalContribution = new TotalPf();

        double employeeContribution = 0d;
        double employerContribution = 0d;

        for (YearlyPfCollectionForDetailedPfStatement yearlyPfCollection : yearlyPfCollectionList) {
            if (yearlyPfCollection.getYearlyTotalContribution() != null) {
                employeeContribution += yearlyPfCollection.getYearlyTotalContribution().getTotalEmployeeContributionInYear();
                employerContribution += yearlyPfCollection.getYearlyTotalContribution().getTotalEmployerContributionInYear();
            }
        }
        totalContribution.setTotalEmployeePortion(employeeContribution);
        totalContribution.setTotalEmployerPortion(employerContribution);

        return totalContribution;
    }

    private TotalPf getTotalArrearPf(List<YearlyPfCollectionForDetailedPfStatement> yearlyPfCollectionList) {
        TotalPf totalArrearPfContribution = new TotalPf();

        double employeeContribution = 0d;
        double employerContribution = 0d;

        for (YearlyPfCollectionForDetailedPfStatement yearlyPfCollection : yearlyPfCollectionList) {
            if (yearlyPfCollection.getYearlyTotalContribution() != null) {
                employeeContribution += yearlyPfCollection.getYearlyTotalArrearPfDeduction().getTotalEmployeeArrearPfDeduction();
                employerContribution += yearlyPfCollection.getYearlyTotalArrearPfDeduction().getTotalEmployerArrearPfDeduction();
            }
        }
        totalArrearPfContribution.setTotalEmployeePortion(employeeContribution);
        totalArrearPfContribution.setTotalEmployerPortion(employerContribution);
        totalArrearPfContribution.setTotal(employeeContribution + employerContribution);

        return totalArrearPfContribution;
    }

    private TotalPf getTotalInterests(long pfAccountId) {
        TotalPf totalPfContribution = new TotalPf();

        double employeeInterests = 0;
        if (pfCollectionRepository.getTotalEmployeeInterests(pfAccountId) != null) {
            employeeInterests = pfCollectionRepository.getTotalEmployeeInterests(pfAccountId);
        }

        double employerInterests = 0;
        if (pfCollectionRepository.getTotalEmployerInterests(pfAccountId) != null) {
            employerInterests = pfCollectionRepository.getTotalEmployerInterests(pfAccountId);
        }
        totalPfContribution.setTotalEmployeePortion(employeeInterests);
        totalPfContribution.setTotalEmployerPortion(employerInterests);
        return totalPfContribution;
    }

    private TotalPf getContributionAndInterests(TotalPf totalPfCollection, TotalPf totalPfInterests) {
        TotalPf totalPf = new TotalPf();

        totalPf.setTotalEmployeePortion(totalPfCollection.getTotalEmployeePortion() + totalPfInterests.getTotalEmployeePortion());

        totalPf.setTotalEmployerPortion(totalPfCollection.getTotalEmployerPortion() + totalPfInterests.getTotalEmployerPortion());

        return totalPf;
    }
}
