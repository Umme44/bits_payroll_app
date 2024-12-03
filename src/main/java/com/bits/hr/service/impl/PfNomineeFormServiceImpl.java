package com.bits.hr.service.impl;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfNominee;
import com.bits.hr.domain.enumeration.IdentityType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfNomineeRepository;
import com.bits.hr.service.ConfigService;
import com.bits.hr.service.PfAccountService;
import com.bits.hr.service.PfNomineeFormService;
import com.bits.hr.service.PfNomineeService;
import com.bits.hr.service.communication.NID.NIDVerificationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.config.DefinedKeys;
import com.bits.hr.service.dto.EmployeeDetailsNomineeReportDTO;
import com.bits.hr.service.dto.NomineeValidationDTO;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.NomineeFileService;
import com.bits.hr.service.mapper.EmployeeDetailsMapperForPFNomineePrintPage;
import com.bits.hr.service.mapper.PfAccountMapper;
import com.bits.hr.service.mapper.PfNomineeMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link PfNominee}.
 */
@Service
@Transactional
@Log4j2
public class PfNomineeFormServiceImpl implements PfNomineeFormService {

    private double MAX_SHARE_PERCENTAGE = 100.0;

    private static final String NID_VERIFICATION_FAILED = "nidVerificationFailed";
    private static final String GUARDIAN_NID_VERIFICATION_FAILED = "guardianNidVerificationFailed";
    private static final String ENTITY_NAME = "pfNomineeForm";

    private final PfNomineeRepository pfNomineeRepository;
    private final PfAccountRepository pfAccountRepository;
    private final PfNomineeService pfNomineeService;
    private final CurrentEmployeeService currentEmployeeService;

    private final PfNomineeMapper pfNomineeMapper;
    private final PfAccountMapper pfAccountMapper;

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private NomineeFileService nomineeFileService;

    @Autowired
    private NIDVerificationService nidVerificationService;

    @Autowired
    private PfAccountService pfAccountService;

    private final EmployeeRepository employeeRepository;
    private final ConfigService configService;
    private final EmployeeDetailsMapperForPFNomineePrintPage employeeDetailsMapperForPFNomineePrintPage;

    public PfNomineeFormServiceImpl(
        PfNomineeRepository pfNomineeRepository,
        PfAccountRepository pfAccountRepository,
        PfNomineeService pfNomineeService,
        CurrentEmployeeService currentEmployeeService,
        PfNomineeMapper pfNomineeMapper,
        PfAccountMapper pfAccountMapper,
        EmployeeRepository employeeRepository,
        EmployeeDetailsMapperForPFNomineePrintPage employeeDetailsMapperForPFNomineePrintPage,
        ConfigService configService
    ) {
        this.pfNomineeRepository = pfNomineeRepository;
        this.pfAccountRepository = pfAccountRepository;
        this.pfNomineeService = pfNomineeService;
        this.currentEmployeeService = currentEmployeeService;
        this.pfNomineeMapper = pfNomineeMapper;
        this.pfAccountMapper = pfAccountMapper;
        this.employeeRepository = employeeRepository;
        this.employeeDetailsMapperForPFNomineePrintPage = employeeDetailsMapperForPFNomineePrintPage;
        this.configService = configService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PfNomineeDTO> findOne(Long id) throws IOException {
        log.debug("Request to get PfNominee : {}", id);
        String pin = getCurrentEmployeePin();
        //validating -> if the employee has any pf account
        PfAccount pfAccount = getPfAccount(pin);
        Optional<PfNominee> pfNominee = pfNomineeRepository.findByIdAndPin(id, pfAccount.getPin());

        Optional<PfNomineeDTO> pfNomineeDTO = pfNominee.map(pfNomineeMapper::toDto);

        /*if(pfNomineeDTO.get().getPhoto() != null){
            pfNomineeDTO.get().setPfNomineeImage(fileOperationService.loadAsByte(pfNomineeDTO.get().getPhoto()));
        }*/
        return pfNomineeDTO;
    }

    @Override
    public PfNomineeDTO save(MultipartFile file, PfNomineeDTO pfNomineeDTO, boolean hasMultipartChanged) {
        log.debug("Request to save PfNominee for Current User : {}", pfNomineeDTO);

        String pin = currentEmployeeService.getCurrentEmployeePin().get();
        PfAccount pfAccount = pfAccountRepository.getPfAccountsByPin(pin).get(0);
        pfNomineeDTO.setPfAccountId(pfAccount.getId());

        final double remaining = getRemainingSharePercentageOfCurrentEmployee(pfNomineeDTO);

        if (remaining - pfNomineeDTO.getSharePercentage() < 0) {
            /* set remaining share percentage if user request share percentage exceeds 100 */
            pfNomineeDTO.setSharePercentage(remaining);
        }

        if (hasMultipartChanged) {
            File savedFile = nomineeFileService.save(file);
            pfNomineeDTO.setPhoto(savedFile.getAbsolutePath());
        }

        PfNominee pfNomineeEntity = pfNomineeMapper.toEntity(pfNomineeDTO);
        pfNomineeEntity = pfNomineeRepository.save(pfNomineeEntity);
        return pfNomineeMapper.toDto(pfNomineeEntity);
    }

    @Override
    public PfNomineeDTO save(PfNomineeDTO pfNomineeDTO) {
        log.debug("Request to save PfNominee for Current User : {}", pfNomineeDTO);

        String pin = currentEmployeeService.getCurrentEmployeePin().get();
        PfAccount pfAccount = pfAccountRepository.getPfAccountsByPin(pin).get(0);
        pfNomineeDTO.setPfAccountId(pfAccount.getId());

        final double remaining = getRemainingSharePercentageOfCurrentEmployee(pfNomineeDTO);

        if (remaining - pfNomineeDTO.getSharePercentage() < 0) {
            /* set remaining share percentage if user request share percentage exceeds 100 */
            pfNomineeDTO.setSharePercentage(remaining);
        }

        PfNominee pfNomineeEntity = pfNomineeMapper.toEntity(pfNomineeDTO);
        pfNomineeEntity = pfNomineeRepository.save(pfNomineeEntity);
        return pfNomineeMapper.toDto(pfNomineeEntity);
    }

    @Override
    public PfNomineeDTO validateIsApproved(PfNomineeDTO pfNomineeDTO) {
        if (pfNomineeDTO.getId() != null) {
            /* check pf nominee is approved already, then do not update  */
            Optional<PfNominee> pfNominee = pfNomineeRepository.findById(pfNomineeDTO.getId());
            if (pfNominee.isPresent() && pfNominee.get().getIsApproved() != null && pfNominee.get().getIsApproved()) {
                throw new BadRequestAlertException("", "", "");
            }
        }
        /* by default, set isApproved to false */
        pfNomineeDTO.setIsApproved(false);

        return pfNomineeDTO;
    }

    public List<PfAccountDTO> findPfAccountsByPin() {
        Optional<String> pin = currentEmployeeService.getCurrentEmployeePin();
        return pfAccountMapper.toDto(pfAccountRepository.getPfAccountsByPin(pin.get()));
    }

    public PfAccount getPfAccount(String pin) {
        //if user has no pf account //show sweet alert or bad request alert
        List<PfAccount> pfAccountList = pfAccountRepository.getPfAccountsByPin(pin);
        if (pfAccountList.isEmpty()) {
            throw new BadRequestAlertException("You have no PF Account!", "pfAccount", "noPfAccount");
        }
        //return 1st pf account
        return pfAccountList.get(0);
    }

    public String getCurrentEmployeePin() {
        //current employee pin
        Optional<String> pinOptional = currentEmployeeService.getCurrentEmployeePin();
        if (!pinOptional.isPresent()) {
            throw new BadRequestAlertException("You are not logged employee", "pfNominee", "noEmployee");
        }
        String pin = pinOptional.get();
        return pin;
    }

    public Page<PfNomineeDTO> findPfNomineesOfCurrentUser(Pageable pageable) throws Exception {
        Optional<String> pin = currentEmployeeService.getCurrentEmployeePin();
        if (!pin.isPresent()) {
            log.debug("PIN not found for this Employee");
            throw new Exception("PIN Not Found for this Employee");
        }
        return pfNomineeRepository.findAllByPfAccountPin(pageable, pin.get()).map(pfNomineeMapper::toDto);
    }

    @Override
    public EmployeeDetailsNomineeReportDTO getEmployeeDetailsByPin(String pin) {
        EmployeeDetailsNomineeReportDTO employeeDetailsDTO = employeeRepository
            .findByPin(pin)
            .map(employeeDetailsMapperForPFNomineePrintPage::toDTO)
            .get();
        return employeeDetailsDTO;
    }

    @Override
    public List<PfNomineeDTO> findAll() {
        log.debug("All Pf Nominee for current user");
        try {
            Optional<String> pinOptional = currentEmployeeService.getCurrentEmployeePin();
            List<PfNominee> pfNomineeList = pfNomineeRepository.findAllByPfAccountPin(pinOptional.get());
            return pfNomineeMapper.toDto(pfNomineeList);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public NomineeValidationDTO validateRemainingSharePercentage(PfNomineeDTO pfNomineeDTO) {
        double remainingPercentage = this.getRemainingSharePercentageOfCurrentEmployee(pfNomineeDTO);
        final NomineeValidationDTO nomineeValidationDTO = new NomineeValidationDTO();
        nomineeValidationDTO.setRemainingSharePercentage(remainingPercentage);
        if (pfNomineeDTO.getSharePercentage() != null && remainingPercentage < pfNomineeDTO.getSharePercentage()) {
            nomineeValidationDTO.setDoesSharePercentageExceed(true);
        } else {
            nomineeValidationDTO.setDoesSharePercentageExceed(false);
        }
        return nomineeValidationDTO;
    }

    private double getRemainingSharePercentageOfCurrentEmployee(PfNomineeDTO pfNomineeDTO) {
        long pfAccountIdOfCurrentUser = pfAccountService.getPfAccountIdOfCurrentUser();

        // main calculation
        final double consumedSharePercentage = pfNomineeRepository.sumConsumedSharePercentage(pfAccountIdOfCurrentUser);
        final double remainingSharePercentage = MAX_SHARE_PERCENTAGE - consumedSharePercentage;

        if (pfNomineeDTO.getId() == null) {
            return remainingSharePercentage;
        } else {
            // add saved share percentage with remaining
            double savedSharePercentage = 0d;
            Optional<PfNominee> savedPfNominee = pfNomineeRepository.findById(pfNomineeDTO.getId());
            if (savedPfNominee.isPresent()) {
                savedSharePercentage = savedPfNominee.get().getSharePercentage();
            }
            return remainingSharePercentage + savedSharePercentage;
        }
    }

    @Override
    public NomineeValidationDTO validateNominee(final PfNomineeDTO pfNomineeDTO) {
        final NomineeValidationDTO nomineeValidationDTO = new NomineeValidationDTO();

        if (pfNomineeDTO.getIdentityType().equals(IdentityType.NID) && pfNomineeDTO.getIdNumber() != null) {
            nomineeValidationDTO.setNidVerificationRequired(configService.isNIDVerificationEnabled(DefinedKeys.is_pf_nominee_nid_verification_enabled_for_user_end));
        } else {
            nomineeValidationDTO.setNidVerificationRequired(false);
        }

        if (nomineeValidationDTO.getIsNidVerificationRequired()) {
            PfNominee pfNominee = pfNomineeMapper.toEntity(pfNomineeDTO);
            boolean isNidVerified = nidVerificationService.isPFNomineeNIDVerified(pfNominee);
            nomineeValidationDTO.setNidVerified(isNidVerified);
            if (!isNidVerified) return nomineeValidationDTO;
        }

        if (
            pfNomineeDTO.getGuardianName() != null &&
            !pfNomineeDTO.getGuardianName().isEmpty() &&
            pfNomineeDTO.getGuardianIdentityType().equals(IdentityType.NID) &&
            pfNomineeDTO.getGuardianIdNumber() != null
        ) {
            nomineeValidationDTO.setNidVerificationRequired(configService.isNIDVerificationEnabled(DefinedKeys.is_pf_nominee_nid_verification_enabled_for_user_end));
        } else {
            nomineeValidationDTO.setGuardianNidVerificationRequired(false);
        }

        if (nomineeValidationDTO.getIsGuardianNidVerificationRequired()) {
            PfNominee pfNominee = pfNomineeMapper.toEntity(pfNomineeDTO);
            boolean isGuardianNidVerified = nidVerificationService.isPFNomineeGuardianNIDVerified(pfNominee);
            nomineeValidationDTO.setGuardianNidVerified(isGuardianNidVerified);
            if (!isGuardianNidVerified) return nomineeValidationDTO;
        }

        return nomineeValidationDTO;
    }

    @Override
    public PfNomineeDTO validateNomineeAndGuardianNID(PfNomineeDTO pfNomineeDTO) {
        NomineeValidationDTO nomineeValidation = validateNominee(pfNomineeDTO);
        if (nomineeValidation.getIsNidVerificationRequired()) {
            if (nomineeValidation.getIsNidVerified()) {
                pfNomineeDTO.setIsNidVerified(true);
            } else {
                throw new BadRequestAlertException("NID verification has failed!", ENTITY_NAME, NID_VERIFICATION_FAILED);
            }
        } else if (nomineeValidation.getIsGuardianNidVerificationRequired()) {
            if (nomineeValidation.getIsGuardianNidVerified()) {
                pfNomineeDTO.setIsGuardianNidVerified(true);
            } else {
                throw new BadRequestAlertException("Guardian NID verification has failed!", ENTITY_NAME, GUARDIAN_NID_VERIFICATION_FAILED);
            }
        }
        return pfNomineeDTO;
    }

    @Override
    public double getTotalConsumedSharedPercentageOfCurrentUser() {
        long pfAccountId = pfAccountService.getPfAccountIdOfCurrentUser();
        double consumed = pfNomineeRepository.sumConsumedSharePercentage(pfAccountId);
        return consumed;
    }
}
