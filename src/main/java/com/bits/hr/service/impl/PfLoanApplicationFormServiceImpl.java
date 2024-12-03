package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfLoan;
import com.bits.hr.domain.PfLoanApplication;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.repository.PfLoanApplicationFormRepository;
import com.bits.hr.repository.PfLoanRepository;
import com.bits.hr.service.PfLoanApplicationFormService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import com.bits.hr.service.dto.PfLoanApplicationFormDTO;
import com.bits.hr.service.finalSettlement.dto.PfStatement;
import com.bits.hr.service.finalSettlement.helperMethods.PfStatementGenerationService;
import com.bits.hr.service.mapper.PfAccountMapper;
import com.bits.hr.service.mapper.PfLoanApplicationMapper;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PfLoanApplication}.
 */
@Service
@Transactional
public class PfLoanApplicationFormServiceImpl implements PfLoanApplicationFormService {

    private final Logger log = LoggerFactory.getLogger(PfLoanApplicationServiceImpl.class);

    private final PfLoanApplicationFormRepository pfLoanApplicationFormRepository;
    private final PfAccountRepository pfAccountRepository;
    private final PfCollectionRepository pfCollectionRepository;
    private final PfLoanRepository pfLoanRepository;
    private final PfStatementGenerationService pfStatementGenerationService;
    private final CurrentEmployeeService currentEmployeeService;

    private final PfLoanApplicationMapper pfLoanApplicationMapper;
    private final PfAccountMapper pfAccountMapper;

    public PfLoanApplicationFormServiceImpl(
        PfLoanApplicationFormRepository pfLoanApplicationFormRepository,
        PfAccountRepository pfAccountRepository,
        CurrentEmployeeService currentEmployeeService,
        PfCollectionRepository pfCollectionRepository,
        PfLoanRepository pfLoanRepository,
        PfStatementGenerationService pfStatementGenerationService,
        EmployeeServiceImpl employeeService,
        PfLoanApplicationMapper pfLoanApplicationMapper,
        PfAccountMapper pfAccountMapper
    ) {
        this.pfLoanApplicationFormRepository = pfLoanApplicationFormRepository;
        this.pfAccountRepository = pfAccountRepository;
        this.pfCollectionRepository = pfCollectionRepository;
        this.pfLoanRepository = pfLoanRepository;
        this.pfStatementGenerationService = pfStatementGenerationService;
        this.currentEmployeeService = currentEmployeeService;
        this.pfLoanApplicationMapper = pfLoanApplicationMapper;
        this.pfAccountMapper = pfAccountMapper;
    }

    @Override
    public PfLoanApplicationDTO save(PfLoanApplicationDTO pfLoanApplicationDTO) {
        log.debug("Request to save PfLoanApplication Form : {}", pfLoanApplicationDTO);
        PfLoanApplication pfLoanApplication = pfLoanApplicationMapper.toEntity(pfLoanApplicationDTO);
        pfLoanApplication = pfLoanApplicationFormRepository.save(pfLoanApplication);
        return pfLoanApplicationMapper.toDto(pfLoanApplication);
    }

    public double getPfEligibleAmount1() {
        double pfEligibleAmount = 0d;
        if (currentEmployeeService.getCurrentUserEmail().isPresent()) {
            Optional<PfStatement> pfStatement = pfStatementGenerationService.generate(currentEmployeeService.getCurrentEmployeeId().get());
            if (pfStatement.isPresent()) {
                double totalPfWithInterest = pfStatement.get().getTotalContributionWithInterests().getTotal();
                pfEligibleAmount = totalPfWithInterest * .8;
                return pfEligibleAmount;
            }
        } else {
            throw new NoEmployeeProfileException();
        }
        return pfEligibleAmount;
    }

    @Override
    public Page<PfLoanApplicationDTO> findByPin(Pageable pageable) {
        log.debug("Request to get all PfLoanApplications");
        String pin = getCurrentEmployeePin();
        return pfLoanApplicationFormRepository.findAllByPfAccountPin(pageable, pin).map(pfLoanApplicationMapper::toDto);
    }

    @Override
    public List<PfAccountDTO> findPfAccountsOfCurrentUser() throws Exception {
        log.debug("Request to get all PfAccounts By Pin");
        String pin = getCurrentEmployeePin();
        List<PfAccountDTO> pfAccounts = pfAccountMapper.toDto(pfAccountRepository.getPfAccountsByPin(pin));
        if (pfAccounts.size() < 1) {
            log.debug("no pf accounts for this employee");
            throw new Exception("");
        }
        return pfAccounts;
    }

    @Override
    public double getPfLoanEligibleAmount() throws Exception {
        log.debug("Request to get employee pf loan eligible amount");
        long pfAccountId = this.findPfAccountsOfCurrentUser().get(0).getId();

        //pf eligible amount = employee total pf contribution * 80%

        /* get only employee total pf contribution amount */
        double pfContribution = pfCollectionRepository.getPfCollection(pfAccountId);

        double pfLoanEligibleAmount = MathRoundUtil.round(pfContribution * .80);
        return pfLoanEligibleAmount;
    }

    public PfLoanApplicationFormDTO checkPfLoanApplicationEligibility() throws Exception {
        log.debug("Request to get info about employee pf loan application eligibility");

        //get current employee and details
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();

        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("you are not logged user", "", "");
        }

        Employee employee = employeeOptional.get();
        PfLoanApplicationFormDTO pfLoanApplicationFormDTO = new PfLoanApplicationFormDTO();

        //get employee category (only RCE employee is allowed)
        if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
            pfLoanApplicationFormDTO.setRegularConfirmedEmployee(true);
        } else {
            pfLoanApplicationFormDTO.setRegularConfirmedEmployee(false);
        }

        //get band of employee ( 1-13 band is allowed)
        String bandName = employee.getBand().getBandName();
        pfLoanApplicationFormDTO.setBandName(bandName);
        if (
            bandName.equals("1") ||
            bandName.equals("2") ||
            bandName.equals("3") ||
            bandName.equals("4") ||
            bandName.equals("5") ||
            bandName.equals("6") ||
            bandName.equals("7") ||
            bandName.equals("8") ||
            bandName.equals("9") ||
            bandName.equals("10") ||
            bandName.equals("11") ||
            bandName.equals("12") ||
            bandName.equals("13")
        ) {
            pfLoanApplicationFormDTO.setEligibleBand(true);
        } else {
            pfLoanApplicationFormDTO.setEligibleBand(false);
        }

        PfAccountDTO pfAccountDTO = this.findPfAccountsOfCurrentUser().get(0);
        long pfAccountId = pfAccountDTO.getId();

        //get pf account maturity(2 years) or 365 * 2 = 730 day (membership start date to Today date)
        LocalDate membershipStartDate = pfAccountDTO.getMembershipStartDate();
        long daysBetween = ChronoUnit.DAYS.between(membershipStartDate, LocalDate.now());
        pfLoanApplicationFormDTO.setMemberShipTotalDays((int) daysBetween);
        if (daysBetween >= 730) {
            pfLoanApplicationFormDTO.setPfAccountMatured(true);
        } else {
            pfLoanApplicationFormDTO.setPfAccountMatured(false);
        }

        //TODO if membership end date exists = account close (account status must be ACTIVE)

        //one Repaying loan is allowed and employee can not application for another pf loan
        List<PfLoan> pfLoanList = pfLoanRepository.findAllPfLoanByPinAndStatus(employee.getPin(), PfLoanStatus.OPEN_REPAYING);

        //if full loan PAID_OFF, employee can apply for another pf loan
        if (pfLoanList.size() == 0) {
            pfLoanApplicationFormDTO.setAnyOpenRepayingPfLoan(false);
        } else {
            pfLoanApplicationFormDTO.setAnyOpenRepayingPfLoan(true);
        }

        //pf eligible amount = employee total pf contribution * 80%
        /* get only employee total pf contribution amount */
        double pfContribution = pfCollectionRepository.getPfCollection(pfAccountId);
        double pfLoanEligibleAmount = MathRoundUtil.round(pfContribution * .80);
        pfLoanApplicationFormDTO.setPfLoanEligibleAmount(pfLoanEligibleAmount);

        return pfLoanApplicationFormDTO;
    }

    @Override
    public boolean checkAnyPfAccountForThisUser() {
        Optional<String> pinOptional = currentEmployeeService.getCurrentEmployeePin();
        if (!pinOptional.isPresent()) {
            return false;
        }
        String pin = pinOptional.get();
        log.debug("Request to get all PfAccounts By Pin");
        List<PfAccountDTO> pfAccounts = pfAccountMapper.toDto(pfAccountRepository.getPfAccountsByPin(pin));
        if (pfAccounts.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Optional<PfLoanApplicationDTO> findOne(Long id) {
        log.debug("Request to get PfLoanApplication : {}", id);
        return pfLoanApplicationFormRepository.findById(id).map(pfLoanApplicationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PfLoanApplication : {}", id);
        pfLoanApplicationFormRepository.deleteById(id);
    }

    private String getCurrentEmployeePin() {
        Optional<String> pin = currentEmployeeService.getCurrentEmployeePin();
        if (!pin.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        return pin.get();
    }
}
