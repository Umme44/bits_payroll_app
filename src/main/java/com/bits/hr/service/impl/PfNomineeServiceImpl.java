package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfNominee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.IdentityType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfNomineeRepository;
import com.bits.hr.service.ConfigService;
import com.bits.hr.service.PfNomineeService;
import com.bits.hr.service.communication.NID.NIDVerificationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.config.DefinedKeys;
import com.bits.hr.service.dto.NomineeValidationDTO;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.mapper.PfAccountMapper;
import com.bits.hr.service.mapper.PfNomineeMapper;
import com.bits.hr.service.search.FilterDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PfNominee}.
 */
@Service
@Transactional
public class PfNomineeServiceImpl implements PfNomineeService {

    private double MAX_SHARE_PERCENTAGE = 100.0;

    private static final String NID_VERIFICATION_FAILED = "nidVerificationFailed";
    private static final String GUARDIAN_NID_VERIFICATION_FAILED = "guardianNidVerificationFailed";
    private static final String ENTITY_NAME = "pfNomineeForm";

    private final Logger log = LoggerFactory.getLogger(PfNomineeServiceImpl.class);

    private final PfNomineeRepository pfNomineeRepository;

    private final PfNomineeMapper pfNomineeMapper;

    private final PfAccountRepository pfAccountRepository;

    private final PfAccountMapper pfAccountMapper;

    private final CurrentEmployeeService currentEmployeeService;

    private final FileOperationService fileOperationService;
    private final ConfigService configService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NIDVerificationService nidVerificationService;

    public PfNomineeServiceImpl(
        PfNomineeRepository pfNomineeRepository,
        PfNomineeMapper pfNomineeMapper,
        CurrentEmployeeService currentEmployeeService,
        PfAccountMapper pfAccountMapper,
        PfAccountRepository pfAccountRepository,
        FileOperationService fileOperationService,
        ConfigService configService
    ) {
        this.pfNomineeRepository = pfNomineeRepository;
        this.pfNomineeMapper = pfNomineeMapper;
        this.pfAccountMapper = pfAccountMapper;
        this.currentEmployeeService = currentEmployeeService;
        this.pfAccountRepository = pfAccountRepository;
        this.fileOperationService = fileOperationService;
        this.configService = configService;
    }

    @Override
    public PfNomineeDTO save(PfNomineeDTO pfNomineeDTO) {
        log.debug("Request to save PfNominee : {}", pfNomineeDTO);
        PfNominee pfNominee = pfNomineeMapper.toEntity(pfNomineeDTO);
        pfNominee = pfNomineeRepository.save(pfNominee);
        return pfNomineeMapper.toDto(pfNominee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PfNomineeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PfNominees");
        return pfNomineeRepository.findAll(pageable).map(pfNomineeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PfNomineeDTO> findOne(Long id) {
        log.debug("Request to get PfNominee : {}", id);
        return pfNomineeRepository.findById(id).map(pfNomineeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PfNominee : {}", id);
        User user = currentEmployeeService.getCurrentUser().get();
        try {
            PfNominee pfNominee = pfNomineeRepository.findById(id).get();
            if (pfNominee.getPhoto() != null && fileOperationService.isExist(pfNominee.getPhoto())) {
                fileOperationService.delete(pfNominee.getPhoto());
            }
            pfNomineeRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }

    @Override
    public void deleteForCommonUserNominee(Long id) {
        log.debug("Request to delete PfNominee : {}", id);
        User user = currentEmployeeService.getCurrentUser().get();
        try {
            PfNominee pfNominee = pfNomineeRepository.findById(id).get();
            if (pfNominee.getIsApproved() != null && pfNominee.getIsApproved()) {
                return;
            }
            if (pfNominee.getPhoto() != null && fileOperationService.isExist(pfNominee.getPhoto())) {
                fileOperationService.delete(pfNominee.getPhoto());
            }
            pfNomineeRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }

    public double getRemainingSharePercentageByPFNominee(PfNomineeDTO pfNomineeDTO) {
        if (pfNomineeDTO.getPfAccountId() != null) {
            final double consumedSharePercentage = pfNomineeRepository.sumConsumedSharePercentage(pfNomineeDTO.getPfAccountId());
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
        return 0;
    }

    @Override
    public double getRemainingSharePercentageByPFAccountId(long pfAccountId) {
        final double consumedSharePercentage = pfNomineeRepository.sumConsumedSharePercentage(pfAccountId);
        final double remainingSharePercentage = MAX_SHARE_PERCENTAGE - consumedSharePercentage;
        return remainingSharePercentage;
    }

    @Override
    public List<PfNomineeDTO> findAllByPfAccountId(Long id) {
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAllByPfAccountId(id);
        return pfNomineeMapper.toDto(pfNomineeList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PfAccountDTO> getPfAccountWithNominees(FilterDto filterDto) {
        log.debug("Request to get all PfAccounts by Pf Nominee");
        if (filterDto.getSearchText() == null) {
            filterDto.setSearchText("");
        }
        String searchText = "%" + filterDto.getSearchText().trim().toLowerCase() + "%";
        return pfAccountMapper.toDto(pfNomineeRepository.getPfAccountWithNominees(searchText));
    }

    @Override
    public boolean isEmployeeEligibleForPF() {
        Optional<Employee> currentEmployee = currentEmployeeService.getCurrentEmployee();

        if (currentEmployee.isPresent()) {
            Employee employee = currentEmployee.get();

            if (
                employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE &&
                employee.getEmploymentStatus() != EmploymentStatus.RESIGNED
            ) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new RuntimeException();
        }
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

    @Override
    public NomineeValidationDTO validateNominee(PfNomineeDTO pfNomineeDTO) {
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
    public double getTotalConsumedSharedPercentageOfCurrentUser(long pfAccountId) {
        return pfNomineeRepository.sumConsumedSharePercentage(pfAccountId);
    }

    @Override
    public Page<PfNomineeDTO> getAllPFNomineeByDateRange(LocalDate startDate, LocalDate endDate, Pageable page) {
        return pfNomineeRepository.getAllPFNomineeByDateRange(startDate, endDate, page).map(pfNomineeMapper::toDto);
    }

    @Override
    public List<PfNomineeDTO> findAllPfAccountByPin(String pin) {
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAllByPfAccountPin(pin);
        List<PfNomineeDTO> pfNomineeDTOList = pfNomineeMapper.toDto(pfNomineeList);
        return pfNomineeDTOList;
    }
}
