package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Nominee;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfNominee;
import com.bits.hr.domain.enumeration.*;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.NomineeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfNomineeRepository;
import com.bits.hr.service.ConfigService;
import com.bits.hr.service.NomineeService;
import com.bits.hr.service.communication.NID.NIDVerificationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.config.DefinedKeys;
import com.bits.hr.service.dto.*;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.NomineeFileService;
import com.bits.hr.service.mapper.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Nominee}.
 */
@Service
@Transactional
public class NomineeServiceImpl implements NomineeService {

    private static final String ENTITY_NAME = "nominee";
    private static final String NID_VERIFICATION_FAILED = "nidVerificationFailed";
    private static final String GUARDIAN_NID_VERIFICATION_FAILED = "guardianNidVerificationFailed";
    private final Logger log = LoggerFactory.getLogger(NomineeServiceImpl.class);
    private final NomineeRepository nomineeRepository;

    private final EmployeeRepository employeeRepository;

    private final NomineeMapper nomineeMapper;

    private final EmployeeMinimalMapper employeeMinimalMapper;

    private final EmployeeNomineeInfoMapper employeeNomineeInfoMapper;

    private final EmployeeDetailsMapperForPFNomineePrintPage employeeDetailsMapperForPFNomineePrintPage;
    private final ConfigService configService;

    private final double MAX_PERCENTAGE = 100;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private NomineeFileService nomineeFileService;

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private NIDVerificationService nidVerificationService;

    @Autowired
    private PfNomineeRepository pfNomineeRepository;

    @Autowired
    private PfNomineeMapper pfNomineeMapper;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    public NomineeServiceImpl(
        NomineeRepository nomineeRepository,
        EmployeeRepository employeeRepository,
        NomineeMapper nomineeMapper,
        EmployeeMinimalMapper employeeMinimalMapper,
        EmployeeNomineeInfoMapper employeeNomineeInfoMapper,
        EmployeeDetailsMapperForPFNomineePrintPage employeeDetailsMapperForPFNomineePrintPage,
        ConfigService configService
    ) {
        this.nomineeRepository = nomineeRepository;
        this.employeeRepository = employeeRepository;
        this.nomineeMapper = nomineeMapper;
        this.employeeDetailsMapperForPFNomineePrintPage = employeeDetailsMapperForPFNomineePrintPage;
        this.employeeMinimalMapper = employeeMinimalMapper;
        this.employeeNomineeInfoMapper = employeeNomineeInfoMapper;
        this.configService = configService;
    }

    @Override
    public NomineeDTO save(NomineeDTO nomineeDTO) {
        log.debug("Request to save Nominee : {}", nomineeDTO);
        Nominee nominee = nomineeMapper.toEntity(nomineeDTO);
        nominee = nomineeRepository.save(nominee);
        nominee.setIsLocked(false);

        double remainingPercentage = getRemainingPercentage(nomineeDTO);
        if (nomineeDTO.getSharePercentage() > remainingPercentage) {
            nomineeDTO.setSharePercentage(remainingPercentage);
        }

        return nomineeMapper.toDto(nominee);
    }

    @Override
    public NomineeDTO saveWithFile(NomineeDTO nomineeDTO, MultipartFile file) {
        /* delete previous saved image */
        if (nomineeDTO.getId() != null) {
            Optional<Nominee> savedNominee = nomineeRepository.findById(nomineeDTO.getId());
            if (savedNominee.isPresent() && savedNominee.get().getImagePath() != null) {
                fileOperationService.delete(savedNominee.get().getImagePath());
            }
        }

        File savedFile = nomineeFileService.save(file);
        nomineeDTO.setImagePath(savedFile.getAbsolutePath());

        nomineeDTO.setIsLocked(false);
        double remainingPercentage = getRemainingPercentage(nomineeDTO);
        if (nomineeDTO.getSharePercentage() > remainingPercentage) {
            nomineeDTO.setSharePercentage(remainingPercentage);
        }

        if (nomineeDTO.getNominationDate() == null) {
            LocalDate now = LocalDate.now();
            nomineeDTO.setNominationDate(now);
        }

        NomineeDTO savedNominee = save(nomineeDTO);
        return savedNominee;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NomineeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Nominees");
        return nomineeRepository.findAll(pageable).map(nomineeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NomineeDTO> findOne(Long id) {
        log.debug("Request to get Nominee : {}", id);
        return nomineeRepository.findById(id).map(nomineeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Nominee : {}", id);
        nomineeRepository.deleteById(id);
    }

    @Override
    public double getRemainingPercentage(NomineeDTO nomineeDTO) {
        if (nomineeDTO.getEmployeeId() == null) {
            throw new BadRequestAlertException("Employee Id is null", "nominee", "idnull");
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(nomineeDTO.getEmployeeId());
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        if (nomineeDTO.getNomineeType() == null) {
            throw new BadRequestAlertException("No nominee type has selected", "nominee", "nomineeTypeNull");
        }

        if (nomineeDTO.getId() != null) {
            /* check is it for saved Entry */
            Optional<Nominee> savedNominee = nomineeRepository.findById(nomineeDTO.getId());
            if (savedNominee.isPresent()) {
                double totalUsed = nomineeRepository.getTotalUsedPercentage(nomineeDTO.getNomineeType(), nomineeDTO.getEmployeeId());
                /* subtract used share percentage */
                totalUsed = totalUsed - savedNominee.get().getSharePercentage();
                return MAX_PERCENTAGE - totalUsed;
            } else {
                return 0;
            }
        } else {
            double totalUsed = nomineeRepository.getTotalUsedPercentage(nomineeDTO.getNomineeType(), nomineeDTO.getEmployeeId());
            return MAX_PERCENTAGE - totalUsed;
        }
    }

    @Override
    public List<NomineeDTO> getNomineeListByEmployeeId(long employeeId) {
        List<Nominee> nomineeList = nomineeRepository.getNomineeByEmployeeId(employeeId);
        return nomineeMapper.toDto(nomineeList);
    }

    @Override
    public List<NomineeDTO> getNomineeListByEmployeeIdAndNomineeType(long employeeId, NomineeType nomineeType) {
        return nomineeMapper.toDto(nomineeRepository.getNomineesByEmployeeIdAndNomineeType(employeeId, nomineeType));
    }

    @Override
    public EmployeeDetailsNomineeReportDTO getGfNomineeDetailsByEmployeeId(long employeeId) {
        EmployeeDetailsNomineeReportDTO employeeDetailDto = employeeRepository
            .findById(employeeId)
            .map(employeeDetailsMapperForPFNomineePrintPage::toDTO)
            .get();
        return employeeDetailDto;
    }

    @Override
    public NomineeMasterDTO getNomineesByPin(String pin) {
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByPin(pin);
        if (!optionalEmployee.isPresent()) {
            return new NomineeMasterDTO();
        }
        Employee employee = optionalEmployee.get();
        long employeeId = employee.getId();

        // find all general & gf nominee by employeeId
        List<Nominee> nomineeList = nomineeRepository.getNomineeByEmployeeId(employeeId);

        // find pf Nominee list by employee pin
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAllByPfAccountPin(pin);

        List<NomineeDTO> nomineeDTOList = nomineeMapper.toDto(nomineeList);
        List<PfNomineeDTO> pfNomineeDTOList = pfNomineeMapper.toDto(pfNomineeList);

        // fetch pfAccountList for same employee to count total pfShare
        List<PfAccount> pfAccountList = pfAccountRepository.getPfAccountsByPin(pin);

        double totalGfShare = nomineeRepository.getTotalUsedPercentage(NomineeType.GRATUITY_FUND, employeeId);

        double totalGeneralShare = nomineeRepository.getTotalUsedPercentage(NomineeType.GENERAL, employeeId);

        double totalPfShare = 0;

        if (pfNomineeDTOList.size() > 0) {
            totalPfShare = pfNomineeRepository.getTotalSharePercentageByPfAccount(pfAccountList.get(0));
        }

        NomineeMasterDTO nomineeMasterDTO = new NomineeMasterDTO();

        nomineeMasterDTO.setId(employeeId);
        nomineeMasterDTO.setPin(pin);
        nomineeMasterDTO.setFullName(employee.getFullName());
        nomineeMasterDTO.setDesignationName(employee.getDesignation().getDesignationName());
        nomineeMasterDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
        nomineeMasterDTO.setUnitName(employee.getUnit().getUnitName());
        nomineeMasterDTO.setBandName(employee.getBand().getBandName());
        nomineeMasterDTO.setOfficialContactNo(employee.getOfficialContactNo());
        nomineeMasterDTO.setEmployeeCategory(employee.getEmployeeCategory());
        nomineeMasterDTO.setEmploymentStatus(employee.getEmploymentStatus());
        nomineeMasterDTO.setDateOfConfirmation(employee.getDateOfConfirmation());
        nomineeMasterDTO.setDateOfJoining(employee.getDateOfJoining());
        nomineeMasterDTO.setGfSharePercentage(totalGfShare);
        nomineeMasterDTO.setGeneralSharePercentage(totalGeneralShare);
        nomineeMasterDTO.setPfSharePercentage(totalPfShare);
        nomineeMasterDTO.setPfNomineeDTOList(pfNomineeDTOList);
        nomineeMasterDTO.setNomineeList(nomineeDTOList);
        nomineeMasterDTO.setIsAllGeneralNomineeApproved(
            checkNomineeApproveStatusByEmployeeIdAndNomineeType(employeeId, NomineeType.GENERAL)
        );
        nomineeMasterDTO.setIsAllGFNomineeApproved(
            checkNomineeApproveStatusByEmployeeIdAndNomineeType(employeeId, NomineeType.GRATUITY_FUND)
        );
        nomineeMasterDTO.setIsAllPfNomineeApproved(checkAllPFNomineeApprovedStatus(pin));

        return nomineeMasterDTO;
    }

    @Override
    public List<NomineeDTO> getNomineesByEmployeeIdAndNomineeType(Long employeeId, NomineeType nomineeType) {
        List<Nominee> nomineeList = nomineeRepository.getAllNomineesByIdAndNomineeType(employeeId, nomineeType);
        return nomineeMapper.toDto(nomineeList);
    }

    @Override
    public NomineeValidationDTO validateRemainingSharePercentage(final NomineeDTO nomineeDTO) {
        double remainingPercentage = this.getRemainingPercentage(nomineeDTO);
        final NomineeValidationDTO nomineeValidationDTO = new NomineeValidationDTO();
        nomineeValidationDTO.setRemainingSharePercentage(remainingPercentage);
        if (nomineeDTO.getSharePercentage() != null && remainingPercentage < nomineeDTO.getSharePercentage()) {
            nomineeValidationDTO.setDoesSharePercentageExceed(true);
        } else {
            nomineeValidationDTO.setDoesSharePercentageExceed(false);
        }
        return nomineeValidationDTO;
    }

    @Override
    public NomineeValidationDTO validateRemainingSharePercentageForAdmin(final NomineeDTO nomineeDTO) {
        double remainingPercentage = this.getRemainingPercentage(nomineeDTO);
        final NomineeValidationDTO nomineeValidationDTO = new NomineeValidationDTO();
        nomineeValidationDTO.setRemainingSharePercentage(remainingPercentage);
        if (nomineeDTO.getSharePercentage() != null && remainingPercentage < nomineeDTO.getSharePercentage()) {
            nomineeValidationDTO.setDoesSharePercentageExceed(true);
        } else {
            nomineeValidationDTO.setDoesSharePercentageExceed(false);
        }
        return nomineeValidationDTO;
    }

    @Override
    public NomineeDTO validateNomineeAndGuardianNID(final NomineeDTO nomineeDTO) {
        final NomineeValidationDTO nomineeValidationDTO = new NomineeValidationDTO();

        // check isNidVerificationIsRequired
        if (
            nomineeDTO.getIdentityType() != null &&
            nomineeDTO.getIdentityType().equals(IdentityType.NID) &&
            nomineeDTO.getIdNumber() != null
        ) {
            if(nomineeDTO.getNomineeType() == NomineeType.GRATUITY_FUND){
                nomineeValidationDTO.setNidVerificationRequired(configService.isNIDVerificationEnabled(DefinedKeys.is_gf_nominee_nid_verification_enabled_for_user_end));
            }
            else nomineeValidationDTO.setNidVerificationRequired(configService.isNIDVerificationEnabled(DefinedKeys.is_nominee_nid_verification_enabled_for_user_end));
        } else {
            nomineeValidationDTO.setNidVerificationRequired(false);
        }

        // check isNidVerificationIsRequired
        if (
            nomineeDTO.getGuardianName() != null &&
            !nomineeDTO.getGuardianName().isEmpty() &&
            nomineeDTO.getGuardianIdentityType().equals(IdentityType.NID) &&
            nomineeDTO.getGuardianIdNumber() != null
        ) {
            if(nomineeDTO.getNomineeType() == NomineeType.GRATUITY_FUND){
                nomineeValidationDTO.setNidVerificationRequired(configService.isNIDVerificationEnabled(DefinedKeys.is_gf_nominee_nid_verification_enabled_for_user_end));
            }
            else nomineeValidationDTO.setNidVerificationRequired(configService.isNIDVerificationEnabled(DefinedKeys.is_nominee_nid_verification_enabled_for_user_end));
        } else {
            nomineeValidationDTO.setGuardianNidVerificationRequired(false);
        }

        // verify nominee nid
        // disabling nid verification process for now
        if (nomineeValidationDTO.getIsNidVerificationRequired()) {
            Nominee nominee = nomineeMapper.toEntity(nomineeDTO);
            boolean isNidVerified = nidVerificationService.isNomineeNidVerified(nominee);
            nomineeValidationDTO.setNidVerified(isNidVerified);
        }

        // verify guardian nid
        // disabling nid verification process for now
        if (nomineeValidationDTO.getIsGuardianNidVerificationRequired()) {
            Nominee nominee = nomineeMapper.toEntity(nomineeDTO);
            boolean isGuardianNidVerified = nidVerificationService.isNomineeGuardianNidVerified(nominee);
            nomineeValidationDTO.setGuardianNidVerified(isGuardianNidVerified);
        }

        if (nomineeValidationDTO.getIsNidVerificationRequired()) {
            if (nomineeValidationDTO.getIsNidVerified()) {
                nomineeDTO.setIsNidVerified(true);
            } else {
                throw new BadRequestAlertException("NID verification has failed!", ENTITY_NAME, NID_VERIFICATION_FAILED);
            }
        } else if (nomineeValidationDTO.getIsGuardianNidVerificationRequired()) {
            if (nomineeValidationDTO.getIsGuardianNidVerified()) {
                nomineeDTO.setIsGuardianNidVerified(true);
            } else {
                throw new BadRequestAlertException("Guardian NID verification has failed!", ENTITY_NAME, GUARDIAN_NID_VERIFICATION_FAILED);
            }
        }
        return nomineeDTO;
    }

    @Override
    public boolean isEmployeeEligibleForGF(Employee employee) {
        if (
            employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE &&
            employee.getEmploymentStatus() != EmploymentStatus.RESIGNED
        ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isEmployeeEligibleForGeneralNominee(Employee employee) {
        if (employee.getEmployeeCategory() == EmployeeCategory.INTERN) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Page<NomineeDTO> getAllApprovedOrPendingNominees(String searchText, NomineeType nomineeType, Status status, Pageable pageable) {
        Page<Nominee> oneNomineeDTOList = null;
        List<NomineeDTO> finalNomineeDTOList = new ArrayList<>();
        if (!Objects.equals(searchText, "")) {
            List<Employee> employees = employeeRepository.searchAllByFullNameAndPin(searchText);
            if (employees.size() > 0) {
                for (Employee employee : employees) {
                    oneNomineeDTOList = nomineeRepository.getAllApprovedOrPendingNominees(employee.getId(), nomineeType, status, pageable);
                    List<NomineeDTO> nomineeDTOS = nomineeMapper.toDto(oneNomineeDTOList.getContent());
                    finalNomineeDTOList.addAll(nomineeDTOS);
                }
            } else {
                return new PageImpl<>(finalNomineeDTOList, pageable, 0);
            }
        } else {
            oneNomineeDTOList = nomineeRepository.getAllNomineesByNomineeTypeAndStatus(nomineeType, status, pageable);
            List<NomineeDTO> nomineeDTOS = nomineeMapper.toDto(oneNomineeDTOList.getContent());
            finalNomineeDTOList.addAll(nomineeDTOS);
        }

        return new PageImpl<>(finalNomineeDTOList, pageable, oneNomineeDTOList.getTotalElements());
    }

    @Override
    public EmployeeDetailsNomineeReportDTO getEmployeeDetailsById(long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employeeDetailsMapperForPFNomineePrintPage.toDTO(employee.get());
    }

    @Override
    public Page<EmployeeNomineeInfo> getAllNomineeByDateRange(
        Long employeeId,
        LocalDate startDate,
        LocalDate endDate,
        String nomineeType,
        Pageable pageable
    ) {
        Page<Employee> employees;
        // Search by Nominee Type (GF, Pf)
        if (nomineeType != null && (nomineeType.equals("Gf") || nomineeType.equals("Pf"))) {
            employees = employeeRepository.getAllGFAndPFNomineeByDateRange(employeeId, startDate, endDate, pageable);
        } else {
            // fetch all Nominees or General Nominees
            employees = employeeRepository.getAllGeneralNomineeByDateRange(employeeId, startDate, endDate, pageable);
        }
        List<EmployeeNomineeInfo> employeeNomineeInfoList = getEmployeeNomineeInfoList(employees.toList());
        return new PageImpl<>(employeeNomineeInfoList, pageable, employees.getTotalElements());
    }

    @Override
    public List<EmployeeNomineeInfo> getEmployeeNomineeInfoList(List<Employee> employeeList) {
        List<EmployeeNomineeInfo> employeeNomineeInfoList = new ArrayList<>();
        for (Employee employee : employeeList) {
            EmployeeNomineeInfo employeeNomineeInfo = employeeNomineeInfoMapper.toDto(employee);

            // Check Regular Confirm Employee Nominee Approve Status
            if (employeeNomineeInfo.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                // checking PF Nominee Approve Status
                employeeNomineeInfo.setIsAllPfNomineeApproved(checkAllPFNomineeApprovedStatus(employeeNomineeInfo.getPin()));
                // checking General Nominee Approve Status

                employeeNomineeInfo.setIsAllGeneralNomineeApproved(
                    checkNomineeApproveStatusByEmployeeIdAndNomineeType(employeeNomineeInfo.getId(), NomineeType.GENERAL)
                );

                // checking GF Nominee Approve Status
                employeeNomineeInfo.setIsAllGFNomineeApproved(
                    checkNomineeApproveStatusByEmployeeIdAndNomineeType(employeeNomineeInfo.getId(), NomineeType.GRATUITY_FUND)
                );
                // Check Probation Employee Nominee Approve Status
            } else if (
                employeeNomineeInfo.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE ||
                employeeNomineeInfo.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE
            ) {
                employeeNomineeInfo.setIsAllGeneralNomineeApproved(
                    checkNomineeApproveStatusByEmployeeIdAndNomineeType(employeeNomineeInfo.getId(), NomineeType.GENERAL)
                );
                employeeNomineeInfo.setIsAllGFNomineeApproved(NomineeStatus.NOT_ELIGIBLE);
                employeeNomineeInfo.setIsAllPfNomineeApproved(NomineeStatus.NOT_ELIGIBLE);
            } else {
                employeeNomineeInfo.setIsAllGFNomineeApproved(NomineeStatus.NOT_ELIGIBLE);
                employeeNomineeInfo.setIsAllPfNomineeApproved(NomineeStatus.NOT_ELIGIBLE);
                employeeNomineeInfo.setIsAllGeneralNomineeApproved(NomineeStatus.NOT_ELIGIBLE);
            }
            employeeNomineeInfoList.add(employeeNomineeInfo);
        }

        return employeeNomineeInfoList;
    }

    public NomineeStatus checkNomineeApproveStatusByEmployeeIdAndNomineeType(long employeeId, NomineeType nomineeType) {
        int totalEmployeeNominees = nomineeRepository.totalNomineesByIdAndNomineeType(employeeId, nomineeType);
        int totalPending = nomineeRepository.findAllByEmployeeIdAndNomineeTypeAndStatus(employeeId, nomineeType, Status.PENDING);
        int totalApproved = nomineeRepository.findAllByEmployeeIdAndNomineeTypeAndStatus(employeeId, nomineeType, Status.APPROVED);

        if (totalEmployeeNominees > 0) {
            if (totalApproved == totalEmployeeNominees) {
                return NomineeStatus.ALL_APPROVED;
            } else if (totalPending == totalEmployeeNominees) {
                return NomineeStatus.PENDING;
            } else {
                return NomineeStatus.PARTIAL_APPROVED;
            }
        } else {
            return NomineeStatus.INCOMPLETE;
        }
    }

    public NomineeStatus checkAllPFNomineeApprovedStatus(String employeePin) {
        int totalEmployeeNominees = pfNomineeRepository.totalNomineesByPin(employeePin);
        int totalPending = pfNomineeRepository.findAllByEmployeePin(employeePin, false);
        int totalApproved = pfNomineeRepository.findAllByEmployeePin(employeePin, true);

        if (totalEmployeeNominees > 0) {
            if (totalApproved == totalEmployeeNominees) {
                return NomineeStatus.ALL_APPROVED;
            } else if (totalPending == totalEmployeeNominees) {
                return NomineeStatus.PENDING;
            } else {
                return NomineeStatus.PARTIAL_APPROVED;
            }
        } else {
            return NomineeStatus.INCOMPLETE;
        }
    }
}
