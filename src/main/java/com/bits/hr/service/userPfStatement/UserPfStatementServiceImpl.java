package com.bits.hr.service.userPfStatement;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.IndividualArrearSalary;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.IndividualArrearSalaryRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.userPfStatement.dto.UserPfStatementDTO;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@Transactional
public class UserPfStatementServiceImpl implements UserPfStatementService {

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    @Autowired
    private IndividualArrearSalaryRepository individualArrearSalaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    public UserPfStatementDTO getPfStatement(PfAccount pfAccount, LocalDate date) {
        UserPfStatementDTO userPfStatementDTO = new UserPfStatementDTO();

        Optional<Employee> employee = employeeRepository.findByPin(pfAccount.getPin());

        // 1. Employee Info
        userPfStatementDTO.setFullName(pfAccount.getAccHolderName());
        userPfStatementDTO.setPin(pfAccount.getPin());
        userPfStatementDTO.setDepartmentName(pfAccount.getDepartmentName());
        userPfStatementDTO.setDesignationName(pfAccount.getDesignationName());

        List<PfAccount> pfAccountList = pfAccountRepository.getPfAccountsByPin(pfAccount.getPin());

        if (pfAccountList.isEmpty()) {
            log.warn("No PF Account found for PIN: {}", employee.get().getPin());

            if (employee.isPresent()) {
                PfAccount pfAccount1 = this.createAndSaveAPfAccount(employeeMapper.toDto(employee.get()));
                log.info("PF Account has created for PIN: {}, PF Account ID: {}", employee.get().getPin(), pfAccount1.getId());
                pfAccountList.add(pfAccount1);
            }
        }

        // 2(a). opening and closing balance date of previous year
        userPfStatementDTO.setSelectedYear(date.getYear());
        userPfStatementDTO.setPreviousYear(date.getYear() - 1);
        userPfStatementDTO.setOpeningBalanceDate(LocalDate.of(date.getYear() - 1, java.time.Month.JANUARY, 1));
        userPfStatementDTO.setPreviousClosingBalanceDate(LocalDate.of(date.getYear() - 1, java.time.Month.DECEMBER, 31));

        // 2(b). Opening Balance Calculation (with Transfer BBL)
        Optional<PfCollection> pfOpeningBalanceOptional = pfCollectionRepository.getOpeningPfBalance(
            pfAccount.getPfCode(),
            pfAccount.getPin()
        );

        double openingTransferred = 0;

        if (pfOpeningBalanceOptional.isPresent()) {
            PfCollection openingBalance = pfOpeningBalanceOptional.get();
            openingTransferred =
                openingBalance.getEmployeeContribution() +
                openingBalance.getEmployeeInterest() +
                openingBalance.getEmployerContribution() +
                openingBalance.getEmployerInterest();
        }

        // individual arrear consideration........doj...last year-1day...........................................................
        List<IndividualArrearSalary> openingIndividualArrear = individualArrearSalaryRepository
            .getAllByEmployeeIdAndDateRange(
                employee.get().getId(),
                employee.get().getDateOfJoining(),
                LocalDate.of(date.getYear() - 2, 12, 31)
            )
            .stream()
            .filter(x -> x.getPfContribution() != null && x.getPfContribution() > 0)
            .collect(Collectors.toList());

        double openingIndvArrCC = MathRoundUtil.round(
            openingIndividualArrear.stream().mapToDouble(IndividualArrearSalary::getPfContribution).sum()
        );

        double openingIndvArrEC = MathRoundUtil.round(
            openingIndividualArrear.stream().mapToDouble(IndividualArrearSalary::getArrearPfDeduction).sum()
        );
        // individual arrear consideration......................................................................

        // 2(c). Opening Balance Calculation (BRAC IT Employee) [Collections of Selected Year - 2]

        double openingEC = pfCollectionRepository.getConsolidatedOfMonthlyEmployeePfContributionByYearAndMonth(
            pfAccount.getId(),
            Month.fromEnum(Month.DECEMBER),
            date.getYear() - 2
        );
        double openingCC = pfCollectionRepository.getConsolidatedOfMonthlyEmployerPfContributionByYearAndMonth(
            pfAccount.getId(),
            Month.fromEnum(Month.DECEMBER),
            date.getYear() - 2
        );

        final double openingBalance = openingEC + openingCC + openingTransferred + openingIndvArrCC + openingIndvArrEC;

        userPfStatementDTO.setOpeningBalance(openingBalance);

        // 3. [ last year ] (selectedYear - 1) pf contribution

        // individual arrear consideration......................................................................
        LocalDate prevSelectedYearStart = LocalDate.of(date.getYear() - 1, 1, 1);
        LocalDate prevSelectedYearEnd = LocalDate.of(date.getYear() - 1, 12, 31);
        List<IndividualArrearSalary> prevSelectedYearIndividualArrear = individualArrearSalaryRepository
            .getAllByEmployeeIdAndDateRange(employee.get().getId(), prevSelectedYearStart, prevSelectedYearEnd)
            .stream()
            .filter(x -> x.getPfContribution() != null && x.getPfContribution() > 0)
            .collect(Collectors.toList());

        double prevYearIndvArrCC = MathRoundUtil.round(
            prevSelectedYearIndividualArrear.stream().mapToDouble(IndividualArrearSalary::getPfContribution).sum()
        );

        double prevYearIndvArrEC = MathRoundUtil.round(
            prevSelectedYearIndividualArrear.stream().mapToDouble(IndividualArrearSalary::getArrearPfDeduction).sum()
        );

        // individual arrear consideration......................................................................

        double prevYearCC = pfCollectionRepository.getOneYearMonthlyEmployerPfContribution(pfAccount.getId(), date.getYear() - 1);
        double prevYearEC = pfCollectionRepository.getOneYearMonthlyEmployeePfContribution(pfAccount.getId(), date.getYear() - 1);

        final double prevYearTotalEC = prevYearEC + prevYearIndvArrEC;
        final double prevYearTotalCC = prevYearCC + prevYearIndvArrCC;

        userPfStatementDTO.setPreviousYearMemberPfContribution(prevYearTotalEC);
        userPfStatementDTO.setPreviousYearCompanyPfContribution(prevYearTotalCC);

        userPfStatementDTO.setOpeningAndPreviousYearContributionInTotal(openingBalance + prevYearTotalEC + prevYearTotalCC);

        // 4. (selectedYear) pf contribution

        // individual arrear consideration......................................................................
        LocalDate selectedYearStart = LocalDate.of(date.getYear(), 1, 1);
        LocalDate selectedDate = date;
        List<IndividualArrearSalary> selectedYearIndvArr = individualArrearSalaryRepository
            .getAllByEmployeeIdAndDateRange(employee.get().getId(), selectedYearStart, selectedDate)
            .stream()
            .filter(x -> x.getPfContribution() != null && x.getPfContribution() > 0)
            .collect(Collectors.toList());

        double selectedYearIndvArrCC = MathRoundUtil.round(
            selectedYearIndvArr.stream().mapToDouble(IndividualArrearSalary::getPfContribution).sum()
        );

        double selectedYearIndvArrEC = MathRoundUtil.round(
            selectedYearIndvArr.stream().mapToDouble(IndividualArrearSalary::getArrearPfDeduction).sum()
        );
        // individual arrear consideration......................................................................

        final double selectedYearEC = pfCollectionRepository.getOneYearMonthlyEmployeePfContributionTillMonth(
            pfAccount.getId(),
            date.getMonthValue(),
            date.getYear()
        );

        final double selectedYearCC = pfCollectionRepository.getOneYearMonthlyEmployerPfContributionTillMonth(
            pfAccount.getId(),
            date.getMonthValue(),
            date.getYear()
        );

        double totalSelectedYearEC = selectedYearEC + selectedYearIndvArrEC;
        double totalSelectedYearCC = selectedYearCC + selectedYearIndvArrCC;

        userPfStatementDTO.setSelectedYearMemberPfContribution(totalSelectedYearEC);
        userPfStatementDTO.setSelectedYearCompanyPfContribution(totalSelectedYearCC);

        userPfStatementDTO.setSelectedYearTotalPfContribution(totalSelectedYearEC + totalSelectedYearCC);

        // 4. pf interest

        final double uptoSelectedMonthYearPfMemberInterest = pfCollectionRepository.getTotalEmployeeInterestsTillMonthAndYear(
            pfAccount.getId(),
            date.getMonthValue(),
            date.getYear()
        );

        final double uptoSelectedMonthYearPfCompanyInterest = pfCollectionRepository.getTotalEmployerInterestsTillMonthAndYear(
            pfAccount.getId(),
            date.getMonthValue(),
            date.getYear()
        );

        userPfStatementDTO.setTillSelectedMonthYearPfMemberInterest(uptoSelectedMonthYearPfMemberInterest);
        userPfStatementDTO.setTillSelectedMonthYearPfCompanyInterest(uptoSelectedMonthYearPfCompanyInterest);
        userPfStatementDTO.setTotalTillSelectedMonthYearPfCompanyInterest(
            uptoSelectedMonthYearPfMemberInterest + uptoSelectedMonthYearPfCompanyInterest
        );

        final double closingBalanceTillSelectedDate =
            userPfStatementDTO.getOpeningAndPreviousYearContributionInTotal() +
            userPfStatementDTO.getSelectedYearTotalPfContribution() +
            userPfStatementDTO.getTotalTillSelectedMonthYearPfCompanyInterest();

        userPfStatementDTO.setTotalClosingBalance(closingBalanceTillSelectedDate);

        return userPfStatementDTO;
    }

    private PfAccount createAndSaveAPfAccount(EmployeeDTO employeeDTO) {
        PfAccount pfAccount = new PfAccount();
        pfAccount.setPfCode(employeeDTO.getPin());
        pfAccount.setAccHolderName(employeeDTO.getFullName());
        pfAccount.setDepartmentName(employeeDTO.getDepartmentName());
        pfAccount.setDesignationName(employeeDTO.getDesignationName());
        pfAccount.setPin(employeeDTO.getPin());
        pfAccount.setUnitName(employeeDTO.getUnitName());
        pfAccount.setMembershipStartDate(employeeDTO.getDateOfConfirmation());
        pfAccount.setDateOfJoining(employeeDTO.getDateOfJoining());
        pfAccount.setDateOfConfirmation(employeeDTO.getDateOfConfirmation());
        pfAccount.setStatus(PfAccountStatus.ACTIVE);
        return pfAccountRepository.save(pfAccount);
    }

    public Boolean checkValidityOfUserPfStatement() {
        Optional<EmployeeDTO> employeeDTOOptional = currentEmployeeService.getCurrentEmployeeDTO();
        //if user has no profile associated
        if (!employeeDTOOptional.isPresent()) {
            return false;
        } else {
            EmployeeDTO employeeDTO = employeeDTOOptional.get();
            if (employeeDTO.getEmployeeCategory() != EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                log.debug(" PF statement are not available for any other employee except regular confirmed employee.");
                return false;
            } else {
                return true;
            }
        }
    }
}
