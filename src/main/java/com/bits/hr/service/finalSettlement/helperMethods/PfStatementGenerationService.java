package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.finalSettlement.dto.PfStatement;
import com.bits.hr.service.finalSettlement.dto.pf.TotalPf;
import com.bits.hr.service.finalSettlement.dto.pf.YearlyPfCollection;
import com.bits.hr.service.finalSettlement.util.ServiceTenure;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PfStatementGenerationService {

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

    public Optional<PfStatement> generate(long employeeId) {
        boolean isMultipleAccountSupported = getConfigValueByKeyService.isMultiplePfAccountSupported();
        Employee employee = employeeRepository.findById(employeeId).get();
        if (employee.getEmployeeCategory() != EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
            log.debug(" PF statement are not available for any other employee except regular confirmed employee. ");
            return Optional.ofNullable(null);
        }

        PfStatement pfStatement = new PfStatement();

        pfStatement.setName(employee.getFullName());
        pfStatement.setDateOfJoining(employee.getDateOfJoining());
        pfStatement.setDateOfConfirmation(employee.getDateOfConfirmation());

        // get pf Account based on pin
        List<PfAccount> pfAccountList = pfAccountRepository.getPfAccountsByPin(employee.getPin());

        if (pfAccountList.isEmpty()) {
            //create and save a pf account
            PfAccount newPfAccount = new PfAccount();
            newPfAccount.setPfCode(employee.getPin());
            newPfAccount.setPin(employee.getPin().trim());
            newPfAccount.setAccHolderName(employee.getFullName());
            newPfAccount.setMembershipStartDate(employee.getDateOfConfirmation());
            newPfAccount.setDepartmentName(employee.getDepartment().getDepartmentName());
            newPfAccount.setUnitName(employee.getUnit().getUnitName());
            newPfAccount.setDesignationName(employee.getDesignation().getDesignationName());
            newPfAccount.setDateOfJoining(employee.getDateOfJoining());
            newPfAccount.setDateOfConfirmation(employee.getDateOfConfirmation());
            PfAccount savedPfAccount = pfAccountRepository.save(newPfAccount);
            pfAccountList.add(savedPfAccount);
            //log.debug("no pf account found for Pin :: " + employee.getPin() + " name :: " + employee.getFullName());
            //return Optional.ofNullable(null);
        }
        if (pfAccountList.size() > 1 && isMultipleAccountSupported == false) {
            log.debug(
                "Multiple pf account found for employee but feature is globally disabled Pin :: " +
                employee.getPin() +
                " name :: " +
                employee.getFullName()
            );
            return Optional.ofNullable(null);
        }

        // considering 1 pf account to be handled
        PfAccount pfAccount = pfAccountList.get(0);

        // map salary to pf collection
        salaryToPfCollectionMapperService.map(pfAccount);

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
        List<YearlyPfCollection> perYearPfCollection = yearlyPfCollectionService.get(pfAccount);

        pfStatement.setYearlyPfCollection(perYearPfCollection);

        // totalContribution -> without interests
        // PFInterests
        // Total PF Contribution with Interest
        //Adjustment for Arrear PF

        TotalPf totalContribution = getTotalContribution(perYearPfCollection);
        TotalPf totalInterests = getTotalInterests(pfAccount.getId());
        TotalPf totalContributionAndInterests = getContributionAndInterests(totalContribution, totalInterests);

        pfStatement.setTotalContribution(totalContribution);
        pfStatement.setTotalPfInterest(totalInterests);
        pfStatement.setTotalContributionWithInterests(totalContributionAndInterests);

        double arrearAdjustmentForPf = pfArrearsCollectionService.get(pfAccount);
        pfStatement.setAdjustmentForArrearsPf(arrearAdjustmentForPf);

        // net PF will be dependent upon service tenure
        // if service tenure is greater than one year ,
        // employee will get both employee contributions and employer contributions
        // else => employee will get only his interests and his part of interests
        // *** this logic handled in final settlement process

        double netPay =
            pfStatement.getAdjustmentForArrearsPf() +
            pfStatement.getOpeningBalance() +
            pfStatement.getTotalContributionWithInterests().getTotalEmployeePortion() +
            pfStatement.getTotalContributionWithInterests().getTotalEmployerPortion();

        pfStatement.setNetPfPayable(netPay);

        return Optional.of(pfStatement);
    }

    private TotalPf getTotalContribution(List<YearlyPfCollection> yearlyPfCollectionList) {
        TotalPf totalContribution = new TotalPf();

        double employeeContribution = 0d;
        double employerContribution = 0d;

        for (YearlyPfCollection yearlyPfCollection : yearlyPfCollectionList) {
            if (yearlyPfCollection.getYearlyTotalContribution() != null) {
                employeeContribution += yearlyPfCollection.getYearlyTotalContribution().getTotalEmployeeContributionInYear();
                employerContribution += yearlyPfCollection.getYearlyTotalContribution().getTotalEmployerContributionInYear();
            }
        }
        totalContribution.setTotalEmployeePortion(employeeContribution);
        totalContribution.setTotalEmployerPortion(employerContribution);

        return totalContribution;
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
