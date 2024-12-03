package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfLoan;
import com.bits.hr.domain.PfLoanApplication;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.repository.PfLoanApplicationRepository;
import com.bits.hr.repository.PfLoanRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.PfAccountService;
import com.bits.hr.service.PfLoanApplicationService;
import com.bits.hr.service.PfLoanService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import com.bits.hr.service.dto.PfLoanApplicationEligibleDTO;
import com.bits.hr.service.dto.PfLoanApplicationFormDTO;
import com.bits.hr.service.dto.PfLoanDTO;
import com.bits.hr.service.finalSettlement.dto.salary.TimeDuration;
import com.bits.hr.service.finalSettlement.util.ServiceTenure;
import com.bits.hr.service.mapper.PfAccountMapper;
import com.bits.hr.service.mapper.PfLoanApplicationMapper;
import com.bits.hr.service.mapper.PfLoanMapper;
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
public class PfLoanApplicationServiceImpl implements PfLoanApplicationService {

    private final Logger log = LoggerFactory.getLogger(PfLoanApplicationServiceImpl.class);

    private final PfLoanApplicationRepository pfLoanApplicationRepository;
    private final PfLoanRepository pfLoanRepository;
    private final PfCollectionRepository pfCollectionRepository;
    private final PfAccountService pfAccountService;
    private final PfLoanService pfLoanService;
    private final EmployeeService employeeService;
    private final CurrentEmployeeService currentEmployeeService;

    private final PfLoanApplicationMapper pfLoanApplicationMapper;
    private final PfLoanMapper pfLoanMapper;
    private final PfAccountMapper pfAccountMapper;

    public PfLoanApplicationServiceImpl(
        PfLoanApplicationRepository pfLoanApplicationRepository,
        PfLoanRepository pfLoanRepository,
        PfCollectionRepository pfCollectionRepository,
        PfAccountService pfAccountService,
        PfLoanService pfLoanService,
        EmployeeService employeeService,
        CurrentEmployeeService currentEmployeeService,
        PfLoanMapper pfLoanMapper,
        PfLoanApplicationMapper pfLoanApplicationMapper,
        PfAccountMapper pfAccountMapper
    ) {
        this.pfLoanApplicationRepository = pfLoanApplicationRepository;
        this.pfLoanRepository = pfLoanRepository;
        this.pfCollectionRepository = pfCollectionRepository;
        this.pfAccountService = pfAccountService;
        this.pfLoanService = pfLoanService;
        this.employeeService = employeeService;
        this.currentEmployeeService = currentEmployeeService;

        this.pfLoanMapper = pfLoanMapper;
        this.pfLoanApplicationMapper = pfLoanApplicationMapper;
        this.pfAccountMapper = pfAccountMapper;
    }

    @Override
    public PfLoanApplicationDTO save(PfLoanApplicationDTO pfLoanApplicationDTO) {
        log.debug("Request to save PfLoanApplication : {}", pfLoanApplicationDTO);
        PfLoanApplication pfLoanApplication = pfLoanApplicationMapper.toEntity(pfLoanApplicationDTO);
        pfLoanApplication = pfLoanApplicationRepository.save(pfLoanApplication);
        return pfLoanApplicationMapper.toDto(pfLoanApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PfLoanApplicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PfLoanApplications");
        return pfLoanApplicationRepository.findAll(pageable).map(pfLoanApplicationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PfLoanApplicationDTO> findOne(Long id) {
        log.debug("Request to get PfLoanApplication : {}", id);
        return pfLoanApplicationRepository.findById(id).map(pfLoanApplicationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PfLoanApplication : {}", id);
        pfLoanApplicationRepository.deleteById(id);
    }

    @Override
    public PfLoanApplicationDTO reject(PfLoanApplicationFormDTO pfLoanApplicationFormDTO) {
        Optional<PfLoanApplicationDTO> pfLoanApplicationDTOOptional = this.findOne(pfLoanApplicationFormDTO.getPfLoanApplicationId());
        if (pfLoanApplicationDTOOptional.isPresent()) {
            PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationDTOOptional.get();

            pfLoanApplicationDTO.setStatus(Status.NOT_APPROVED);
            pfLoanApplicationDTO.setIsRejected(Boolean.TRUE);
            pfLoanApplicationDTO.setIsApproved(Boolean.FALSE);
            if (currentEmployeeService.getCurrentEmployeeId().isPresent()) {
                pfLoanApplicationDTO.setRejectedById(currentEmployeeService.getCurrentEmployeeId().get());
            }

            pfLoanApplicationDTO.setRejectionReason(pfLoanApplicationFormDTO.getRejectionReason());
            pfLoanApplicationDTO.setRemarks(pfLoanApplicationFormDTO.getRemarks());
            if (pfLoanApplicationFormDTO.getRejectionDate() == null) {
                pfLoanApplicationDTO.setRejectionDate(LocalDate.now());
            } else {
                pfLoanApplicationDTO.setRejectionDate(pfLoanApplicationFormDTO.getRejectionDate());
            }
            this.save(pfLoanApplicationDTO);
            return pfLoanApplicationDTO;
        }
        return pfLoanApplicationDTOOptional.get();
    }

    @Override
    public PfLoanDTO approve(PfLoanApplicationFormDTO pfLoanApplicationFormDTO) throws Exception {
        if (pfLoanApplicationFormDTO.getPfAccountId() == null) {
            throw new Exception("pf account not found");
        }

        //find pf account by pf account id
        Optional<PfAccountDTO> pfAccountDTO = pfAccountService.findOne(pfLoanApplicationFormDTO.getPfAccountId());

        if (!pfAccountDTO.isPresent()) {
            throw new Exception("PF Account Not Found");
        }
        PfAccount pfAccount = pfAccountMapper.toEntity(pfAccountDTO.get());
        String pin = pfAccount.getPin();
        String pfCode = pfAccount.getPfCode();

        Optional<PfLoan> pfLoanExisting = pfLoanRepository.findDuplicatePfLoan(
            pfLoanApplicationFormDTO.getDisbursementAmount(),
            pfLoanApplicationFormDTO.getDisbursementDate(),
            pin,
            pfCode
        );

        PfLoanDTO pfLoanDTO;

        if (pfLoanExisting.isPresent()) {
            //map pfLoanApplicationFormDTO to pfLoanDTO
            pfLoanDTO = mapToPfLoanDTO(pfLoanApplicationFormDTO, pfLoanMapper.toDto(pfLoanExisting.get()));
        } else {
            pfLoanDTO = mapToPfLoanDTO(pfLoanApplicationFormDTO, new PfLoanDTO());
        }
        PfLoanDTO savedPfLoanDTO = pfLoanService.save(pfLoanDTO);

        //update pfLoanApplication information
        Optional<PfLoanApplicationDTO> pfLoanApplicationDTOOptional = this.findOne(pfLoanApplicationFormDTO.getPfLoanApplicationId());

        if (pfLoanApplicationDTOOptional.isPresent()) {
            //map pfLoanApplicationFormDTO to pfLoanApplicationDTO
            PfLoanApplicationDTO pfLoanApplicationDTO = mapToApprovePfLoanApplicationDTO(
                pfLoanApplicationFormDTO,
                pfLoanApplicationDTOOptional.get()
            );
            this.save(pfLoanApplicationDTO);
        }

        return savedPfLoanDTO;
    }

    private PfLoanDTO mapToPfLoanDTO(PfLoanApplicationFormDTO pfLoanApplicationFormDTO, PfLoanDTO pfLoanDTO) {
        pfLoanDTO.setInstalmentStartFrom(pfLoanApplicationFormDTO.getInstalmentStartFrom());
        pfLoanDTO.setInstallmentAmount(pfLoanApplicationFormDTO.getInstallmentAmount());
        pfLoanDTO.setInstalmentNumber(pfLoanApplicationFormDTO.getNoOfInstallment().toString());
        pfLoanDTO.setDisbursementAmount(pfLoanApplicationFormDTO.getDisbursementAmount());
        pfLoanDTO.setDisbursementDate(pfLoanApplicationFormDTO.getDisbursementDate());
        pfLoanDTO.setStatus(pfLoanApplicationFormDTO.getPfLoanStatus());
        pfLoanDTO.setBankName(pfLoanApplicationFormDTO.getBankName());
        pfLoanDTO.setBankAccountNumber(pfLoanApplicationFormDTO.getBankAccountNumber());
        pfLoanDTO.setBankBranch(pfLoanApplicationFormDTO.getBankBranch());
        if (pfLoanApplicationFormDTO.getChequeNumber() != null) {
            pfLoanDTO.setChequeNumber(pfLoanApplicationFormDTO.getChequeNumber());
        }
        pfLoanDTO.setPfAccountId(pfLoanApplicationFormDTO.getPfAccountId());
        pfLoanDTO.setStatus(PfLoanStatus.OPEN_REPAYING);
        pfLoanDTO.setPfLoanApplicationId(pfLoanApplicationFormDTO.getPfLoanApplicationId());

        return pfLoanDTO;
    }

    private PfLoanApplicationDTO mapToApprovePfLoanApplicationDTO(
        PfLoanApplicationFormDTO pfLoanApplicationFormDTO,
        PfLoanApplicationDTO pfLoanApplicationDTO
    ) {
        pfLoanApplicationDTO.setStatus(Status.APPROVED);
        if (currentEmployeeService.getCurrentEmployeeId().isPresent()) {
            pfLoanApplicationDTO.setApprovedById(currentEmployeeService.getCurrentEmployeeId().get());
        }
        if (pfLoanApplicationFormDTO.getApprovalDate() == null) {
            pfLoanApplicationDTO.setApprovalDate(LocalDate.now());
        } else {
            pfLoanApplicationDTO.setApprovalDate(pfLoanApplicationFormDTO.getApprovalDate());
        }

        pfLoanApplicationDTO.setInstallmentAmount(pfLoanApplicationFormDTO.getInstallmentAmount());
        pfLoanApplicationDTO.setNoOfInstallment(pfLoanApplicationFormDTO.getNoOfInstallment());

        pfLoanApplicationDTO.setDisbursementAmount(pfLoanApplicationFormDTO.getDisbursementAmount());
        pfLoanApplicationDTO.setDisbursementDate(pfLoanApplicationDTO.getDisbursementDate());

        pfLoanApplicationDTO.setRemarks(pfLoanApplicationFormDTO.getRemarks());

        pfLoanApplicationDTO.setIsRecommended(pfLoanApplicationFormDTO.getIsRecommended());
        pfLoanApplicationDTO.setRecommendationDate(pfLoanApplicationFormDTO.getRecommendationDate());
        pfLoanApplicationDTO.setRecommendedById(pfLoanApplicationFormDTO.getRecommendedById());

        pfLoanApplicationDTO.setIsApproved(pfLoanApplicationFormDTO.getIsApproved());
        pfLoanApplicationDTO.setApprovalDate(pfLoanApplicationFormDTO.getApprovalDate());
        pfLoanApplicationDTO.setIsApproved(pfLoanApplicationFormDTO.getIsApproved());
        if (currentEmployeeService.getCurrentEmployeeId().isPresent()) {
            pfLoanApplicationDTO.setApprovedById(currentEmployeeService.getCurrentEmployeeId().get());
        }

        pfLoanApplicationDTO.setIsApproved(Boolean.TRUE);
        pfLoanApplicationDTO.setIsRejected(Boolean.FALSE);
        return pfLoanApplicationDTO;
    }

    public PfLoanApplicationEligibleDTO checkPfLoanApplicationEligibility(long pfAccountId) {
        log.debug("Request to get info about employee pf loan application eligibility");
        PfAccountDTO pfAccountDTO = pfAccountService.findOne(pfAccountId).get();

        //get employee and details by pin
        Optional<Employee> employeeOptional = employeeService.findEmployeeByPin(pfAccountDTO.getPin());

        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found with This PIN:" + pfAccountDTO.getPin(), "", "");
        }

        Employee employee = employeeOptional.get();
        PfLoanApplicationEligibleDTO pfLoanApplicationEligibleDTO = new PfLoanApplicationEligibleDTO();

        //get employee category (only RCE employee is allowed)
        pfLoanApplicationEligibleDTO.setEmployeeCategory(employee.getEmployeeCategory());
        if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
            pfLoanApplicationEligibleDTO.setRegularConfirmedEmployee(true);
        } else {
            pfLoanApplicationEligibleDTO.setRegularConfirmedEmployee(false);
        }

        //get band of employee ( 1-13 band is allowed)
        String bandName = employee.getBand().getBandName();
        pfLoanApplicationEligibleDTO.setBandName(bandName);
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
            pfLoanApplicationEligibleDTO.setEligibleBand(true);
        } else {
            pfLoanApplicationEligibleDTO.setEligibleBand(false);
        }

        //get pf account maturity(2 years) or 365 * 2 = 730 day (membership start date to Today date)
        //service tenure

        LocalDate membershipStartDate = pfAccountDTO.getMembershipStartDate();
        long daysBetween = ChronoUnit.DAYS.between(membershipStartDate, LocalDate.now());
        TimeDuration serviceTenure = ServiceTenure.calculateTenure(membershipStartDate, LocalDate.now());
        pfLoanApplicationEligibleDTO.setServiceTenure(
            serviceTenure.getYear() + " Years, " + serviceTenure.getMonth() + " Months, " + serviceTenure.getDay() + " Days"
        );
        if (daysBetween >= 730) {
            pfLoanApplicationEligibleDTO.setPfAccountMatured(true);
        } else {
            pfLoanApplicationEligibleDTO.setPfAccountMatured(false);
        }

        //TODO if membership end date exists = account close (account status must be ACTIVE)

        //one Repaying loan is allowed and employee can not application for another pf loan
        List<PfLoan> pfLoanList = pfLoanRepository.findAllPfLoanByPinAndStatus(employee.getPin(), PfLoanStatus.OPEN_REPAYING);

        //if full loan PAID_OFF, employee can apply for another pf loan
        if (pfLoanList.size() == 0) {
            pfLoanApplicationEligibleDTO.setAnyOpenRepayingPfLoan(false);
        } else {
            pfLoanApplicationEligibleDTO.setAnyOpenRepayingPfLoan(true);
        }

        //pf eligible amount = employee total pf contribution * 80%
        /* get only employee total pf contribution amount */
        double pfContribution = 0;
        if (pfCollectionRepository.getPfCollection(pfAccountId) != null) {
            pfContribution = pfCollectionRepository.getPfCollection(pfAccountId);
        }
        double pfLoanEligibleAmount = MathRoundUtil.round(pfContribution * .80);
        pfLoanApplicationEligibleDTO.setPfLoanEligibleAmount(pfLoanEligibleAmount);

        return pfLoanApplicationEligibleDTO;
    }
}
