package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.InsuranceClaim;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.InsuranceClaimRepository;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.service.InsuranceConfigurationService;
import com.bits.hr.service.InsuranceRegistrationService;
import com.bits.hr.service.dto.*;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.service.mapper.InsuranceRegistrationMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InsuranceRegistration}.
 */
@Log4j2
@Service
@Transactional
public class InsuranceRegistrationServiceImpl implements InsuranceRegistrationService {

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Autowired
    private InsuranceRegistrationMapper insuranceRegistrationMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InsuranceConfigurationService insuranceConfigurationService;

    @Autowired
    private EmployeeMinimalMapper employeeMinimalMapper;

    @Autowired
    private InsuranceClaimRepository insuranceClaimRepository;

    @Override
    public InsuranceRegistrationDTO createInsuranceRegistration(Long userId, InsuranceRegistrationDTO insuranceRegistrationDTO) {
        try {
            boolean isEmployeeEligible = isEmployeeEligibleForInsurance(insuranceRegistrationDTO.getEmployeeId());

            if (isEmployeeEligible == false) {
                throw new RuntimeException();
            }

            insuranceRegistrationDTO.setCreatedAt(Instant.now());
            insuranceRegistrationDTO.setCreatedById(userId);
            insuranceRegistrationDTO.setInsuranceStatus(InsuranceStatus.PENDING);
            insuranceRegistrationDTO.setAvailableBalance(0D);

            InsuranceRegistration insuranceRegistration = insuranceRegistrationMapper.toEntity(insuranceRegistrationDTO);
            insuranceRegistration = insuranceRegistrationRepository.save(insuranceRegistration);
            return insuranceRegistrationMapper.toDto(insuranceRegistration);
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    @Override
    public InsuranceRegistrationDTO updateInsuranceRegistration(Long userId, InsuranceRegistrationDTO insuranceRegistrationDTO) {
        try {
            insuranceRegistrationDTO.setUpdatedAt(Instant.now());
            insuranceRegistrationDTO.setUpdatedById(userId);

            InsuranceRegistration insuranceRegistration = insuranceRegistrationMapper.toEntity(insuranceRegistrationDTO);
            insuranceRegistration = insuranceRegistrationRepository.save(insuranceRegistration);
            return insuranceRegistrationMapper.toDto(insuranceRegistration);
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceRegistrationDTO> findAll(Pageable pageable) {
        List<InsuranceRegistrationDTO> insuranceRegistrationDTOList = insuranceRegistrationRepository
            .findAll(pageable)
            .map(insuranceRegistrationMapper::toDto)
            .getContent();
        return insuranceRegistrationDTOList;
    }

    @Override
    public Page<InsuranceRegistrationAdminDTO> findAllInsuranceRegistrationUsingFilter(
        String searchText,
        Integer year,
        Integer month,
        InsuranceStatus status,
        Boolean isExcluded,
        Boolean isCancelled,
        Boolean isSeperated,
        Pageable pageable
    ) {
        try {
            Instant searchFrom = null;
            Instant searchTo = null;
            if (year != 0 && month != 0) {
                searchFrom = LocalDate.of(year, month, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
                searchTo =
                    LocalDate
                        .of(year, month, Month.of(month).length(searchFrom.atZone(ZoneId.systemDefault()).toLocalDate().isLeapYear()))
                        .atTime(23, 59, 59)
                        .atZone(ZoneId.systemDefault())
                        .toInstant();
            }
            if (year != 0 && month == 0) {
                searchFrom = LocalDate.of(year, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
                searchTo = LocalDate.of(year, 12, 31).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
            }

            Page<String> employeeIdListPageable = insuranceRegistrationRepository.getEmployeePINsUsingFilter(
                searchText,
                searchFrom,
                searchTo,
                status,
                isExcluded,
                isCancelled,
                isSeperated,
                pageable
            );
            List<String> employeeIdList = employeeIdListPageable.getContent();

            List<InsuranceRegistrationAdminDTO> insuranceRegistrationAdminDTOList = new ArrayList<>();

            for (int i = 0; i < employeeIdList.size(); i++) {
                List<InsuranceRegistration> insuranceRegistrations = insuranceRegistrationRepository.getInsuranceRegistrationsUsingFilter(
                    employeeIdList.get(i),
                    searchText,
                    searchFrom,
                    searchTo,
                    status,
                    isExcluded,
                    isCancelled,
                    isSeperated
                );

                if (insuranceRegistrations.size() > 0) {
                    Collections.sort(
                        insuranceRegistrations,
                        Comparator.comparing((InsuranceRegistration ir) -> ir.getInsuranceRelation().ordinal())
                    );
                    InsuranceRegistrationAdminDTO adminDTO = new InsuranceRegistrationAdminDTO();
                    adminDTO.setEmployeePin(employeeIdList.get(i));
                    List<InsuranceRegistrationDTO> insuranceRegistrationDTOList = insuranceRegistrationMapper.toDto(insuranceRegistrations);

                    for (InsuranceRegistrationDTO ir : insuranceRegistrationDTOList) {
                        LocalDate effectiveDate = null;
                        if (
                            ir.getInsuranceRelation().equals(InsuranceRelation.SELF) &&
                            ir.getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)
                        ) {
                            effectiveDate = ir.getDateOfConfirmation();
                        } else if (
                            ir.getInsuranceRelation().equals(InsuranceRelation.SELF) &&
                            ir.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)
                        ) {
                            effectiveDate = ir.getDateOfJoining();
                        } else if (!ir.getInsuranceRelation().equals(InsuranceRelation.SELF)) {
                            effectiveDate = ir.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
                        }

                        ir.setEffectiveDate(effectiveDate);
                    }

                    adminDTO.setInsuranceRegistrationDTOList(insuranceRegistrationDTOList);
                    insuranceRegistrationAdminDTOList.add(adminDTO);
                }
            }

            Page<InsuranceRegistrationAdminDTO> page = new PageImpl<>(
                insuranceRegistrationAdminDTOList,
                employeeIdListPageable.getPageable(),
                employeeIdListPageable.getTotalElements()
            );

            return page;
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    @Override
    public List<InsuranceRegistrationDTO> getInsuranceRegistrationListByEmployeeId(Long employeeId) {
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findPreviousRelationsByEmployeeId(
            employeeId
        );

        Collections.sort(
            insuranceRegistrationList,
            Comparator.comparing((InsuranceRegistration ir) -> ir.getInsuranceRelation().ordinal())
        );

        List<InsuranceRegistrationDTO> insuranceRegistrationDTOList = insuranceRegistrationMapper.toDto(insuranceRegistrationList);
        return insuranceRegistrationDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsuranceRegistrationDTO> findOne(Long id) {
        Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(id);

        if (insuranceRegistration.isPresent()) {
            InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration.get());
            return Optional.of(insuranceRegistrationDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<InsuranceRegistrationDTO> findOneByInsuranceCardId(String cardId) {
        List<InsuranceRegistration> insuranceRegistrations = new ArrayList<>();
        Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findByInsuranceCardId(cardId.trim());
        if (insuranceRegistration.isPresent()) {
            insuranceRegistrations.add(insuranceRegistration.get());
        }
        List<InsuranceRegistrationDTO> insuranceRegistrationDTOs = insuranceRegistrationMapper.toDto(insuranceRegistrations);
        return insuranceRegistrationDTOs;
    }

    @Override
    public void delete(Long id) {
        Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(id);

        if (insuranceRegistration.get().getInsuranceStatus() == InsuranceStatus.PENDING) {
            insuranceRegistrationRepository.deleteById(id);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public InsuranceRegistrationDTO approveInsuranceRegistration(User user, InsuranceApprovalDTO insuranceApprovalDTO) {
        boolean isUnique = isCardIdUnique(insuranceApprovalDTO.getInsuranceCardId(), null);

        if (!isUnique) {
            throw new RuntimeException();
        }

        Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(
            insuranceApprovalDTO.getRegistrationId()
        );
        double maxLimit = insuranceConfigurationService.getAllInsuranceConfiguration().get(0).getMaxTotalClaimLimitPerYear();

        if (insuranceRegistration.isPresent() && insuranceRegistration.get().getInsuranceStatus() == InsuranceStatus.PENDING) {
            insuranceRegistration.get().setInsuranceId(String.valueOf(insuranceApprovalDTO.getInsuranceCardId()));
            insuranceRegistration.get().setApprovedAt(Instant.now());
            insuranceRegistration.get().setApprovedBy(user);
            insuranceRegistration.get().setAvailableBalance(maxLimit);
            insuranceRegistration.get().setInsuranceStatus(InsuranceStatus.APPROVED);

            InsuranceRegistration insuranceRegistrationApproved = insuranceRegistrationRepository.save(insuranceRegistration.get());

            InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistrationApproved);

            return insuranceRegistrationDTO;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean isCardIdUnique(String insuranceCardId, Long id) {
        Optional<InsuranceRegistration> insuranceRegistrationOptional = insuranceRegistrationRepository.findByInsuranceCardId(
            insuranceCardId
        );

        if (insuranceRegistrationOptional.isPresent()) {
            if (id != null) {
                if (insuranceRegistrationOptional.get().getId().equals(id)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public InsuranceRegistrationDTO rejectInsuranceRegistration(User user, InsuranceApprovalDTO insuranceApprovalDTO) {
        Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(
            insuranceApprovalDTO.getRegistrationId()
        );

        if (insuranceRegistration.isPresent() && insuranceRegistration.get().getInsuranceStatus() == InsuranceStatus.PENDING) {
            insuranceRegistration.get().setInsuranceStatus(InsuranceStatus.NOT_APPROVED);
            insuranceRegistration.get().setUnapprovalReason(insuranceApprovalDTO.getReason());
            insuranceRegistration.get().setUpdatedAt(Instant.now());
            insuranceRegistration.get().setUpdatedBy(user);

            InsuranceRegistration insuranceRegistrationApproved = insuranceRegistrationRepository.save(insuranceRegistration.get());

            InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistrationApproved);

            return insuranceRegistrationDTO;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public InsuranceRegistrationDTO cancelInsuranceRegistration(User user, InsuranceApprovalDTO insuranceApprovalDTO) {
        Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(
            insuranceApprovalDTO.getRegistrationId()
        );

        InsuranceStatus status = insuranceApprovalDTO.getStatus();

        if (insuranceRegistration.isPresent() && (status == InsuranceStatus.APPROVED || status == InsuranceStatus.PENDING)) {
            insuranceRegistration.get().setInsuranceStatus(InsuranceStatus.CANCELED);
            insuranceRegistration.get().setUnapprovalReason(insuranceApprovalDTO.getReason());
            insuranceRegistration.get().setUpdatedAt(Instant.now());
            insuranceRegistration.get().setUpdatedBy(user);

            InsuranceRegistration insuranceRegistrationApproved = insuranceRegistrationRepository.save(insuranceRegistration.get());

            InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistrationApproved);

            return insuranceRegistrationDTO;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public List<EmployeeMinimalDTO> getAllEligibleEmployees() {
        LocalDate today = LocalDate.now();
        List<Employee> employees = employeeRepository.getAllEligibleEmployeesForInsuranceRegistration(today);
        return employeeMinimalMapper.toDto(employees);
    }

    @Override
    public InsuranceRelationsDTO getRemainingRelations(Long employeeId) {
        List<InsuranceRelation> enumList = new ArrayList<InsuranceRelation>(EnumSet.allOf(InsuranceRelation.class));
        List<String> insuranceRelations = enumList.stream().map(InsuranceRelation::name).collect(Collectors.toList());

        List<String> registeredRelations = insuranceRegistrationRepository.getAllRegisteredRelationsByEmployeeId(employeeId);

        insuranceRelations.removeAll(registeredRelations);

        InsuranceRelationsDTO dto = new InsuranceRelationsDTO();
        dto.setRelations(insuranceRelations);

        return dto;
    }

    @Override
    public InsuranceRelationsDTO getAllRelations() {
        List<InsuranceRelation> enumList = new ArrayList<InsuranceRelation>(EnumSet.allOf(InsuranceRelation.class));
        List<String> insuranceRelations = enumList.stream().map(InsuranceRelation::name).collect(Collectors.toList());
        InsuranceRelationsDTO dto = new InsuranceRelationsDTO();
        dto.setRelations(insuranceRelations);
        return dto;
    }

    @Override
    public double getTotalSettledAmountWithinYearByInsuranceId(long id, LocalDate yearStartDate, LocalDate yearEndDate) {
        try {
            List<InsuranceClaim> insuranceClaims = insuranceClaimRepository.getInsuranceClaimsByInsuranceRegistrationId(
                id,
                yearStartDate,
                yearEndDate
            );

            double totalSettledAmount = 0;
            for (int i = 0; i < insuranceClaims.size(); i++) {
                totalSettledAmount += insuranceClaims.get(i).getSettledAmount();
            }
            return totalSettledAmount;
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    @Override
    public void updateAvailableBalanceInInsuranceRegistration(long insuranceId) {
        try {
            Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(insuranceId);
            if (!insuranceRegistration.isPresent()) {
                throw new BadRequestAlertException("Insurance Registration not found", "Insurance Registration", "idnull");
            }

            LocalDate currentYear = LocalDate.now();
            LocalDate yearStartDate = LocalDate.of(currentYear.getYear(), 1, 1);
            LocalDate yearEndDate = LocalDate.of(currentYear.getYear(), 12, 31);

            List<InsuranceConfigurationDTO> insuranceConfigurations = insuranceConfigurationService.getAllInsuranceConfiguration();

            if (insuranceConfigurations.size() < 1) {
                return;
            }

            double maximumLimit = insuranceConfigurations.get(0).getMaxTotalClaimLimitPerYear();
            double totalSettledAmount = getTotalSettledAmountWithinYearByInsuranceId(insuranceId, yearStartDate, yearEndDate);
            double remainingBalance = maximumLimit - totalSettledAmount;
            insuranceRegistration.get().setAvailableBalance(remainingBalance);
            insuranceRegistrationRepository.save(insuranceRegistration.get());
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    public boolean isEmployeeEligibleForInsurance(long employeeId) {
        try {
            Optional<Employee> currentEmployee = employeeRepository.findById(employeeId);

            if (currentEmployee.isPresent()) {
                Employee employee = currentEmployee.get();

                boolean isEligible =
                    employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE ||
                    employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE;

                if (isEligible && employee.getEmploymentStatus() != EmploymentStatus.RESIGNED) {
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }
}
