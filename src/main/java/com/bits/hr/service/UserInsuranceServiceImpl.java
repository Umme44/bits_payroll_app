package com.bits.hr.service;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.*;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.*;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.*;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.service.mapper.InsuranceClaimMapper;
import com.bits.hr.service.mapper.InsuranceRegistrationMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserInsuranceServiceImpl implements UserInsuranceService {

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    InsuranceConfigurationService insuranceConfigurationService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private InsuranceClaimRepository insuranceClaimRepository;

    @Autowired
    private InsuranceConfigurationRepository insuranceConfigurationRepository;

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Autowired
    private InsuranceRegistrationMapper insuranceRegistrationMapper;

    @Autowired
    private InsuranceClaimMapper insuranceClaimMapper;

    @Autowired
    private EmployeeMinimalMapper employeeMinimalMapper;

    @Autowired
    private EmployeeCommonService employeeCommonService;

    @Override
    public List<InsuranceRegistrationDTO> getAllInsuranceRegistration(Employee employee) {
        try {
            List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAllInsuranceRegistrationByEmployeeId(
                employee.getId()
            );

            Collections.sort(
                insuranceRegistrationList,
                Comparator.comparing((InsuranceRegistration ir) -> ir.getInsuranceRelation().ordinal())
            );

            List<InsuranceRegistrationDTO> insuranceRegistrationDTOList = insuranceRegistrationMapper.toDto(insuranceRegistrationList);
            return insuranceRegistrationDTOList;
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    @Override
    public InsuranceRegistrationDTO getInsuranceRegistrationById(Long registrationId) {
        try {
            long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
            Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(registrationId);

            boolean isValidEmployee = insuranceRegistration.get().getEmployee().getId() == employeeId;

            if (insuranceRegistration.isPresent() && isValidEmployee) {
                InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration.get());
                return insuranceRegistrationDTO;
            } else {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    @Override
    public List<InsuranceClaimDTO> getAllInsuranceClaims(String employeePin) {
        try {
            List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAllInsuranceClaimByEmployeePin(employeePin);
            List<InsuranceClaimDTO> insuranceClaimDTOList = insuranceClaimMapper.toDto(insuranceClaimList);

            return insuranceClaimDTOList;
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    @Override
    public InsuranceClaimDTO getInsuranceClaimById(Long claimId, String employeePin) {
        try {
            Optional<InsuranceClaim> insuranceClaim = insuranceClaimRepository.findById(claimId);
            boolean isValidEmployee = insuranceClaim.get().getInsuranceRegistration().getEmployee().getPin().equals(employeePin);

            if (insuranceClaim.isPresent() && isValidEmployee) {
                InsuranceClaimDTO insuranceClaimDTO = insuranceClaimMapper.toDto(insuranceClaim.get());
                return insuranceClaimDTO;
            } else {
                throw new BadRequestAlertException("Insurance claim not found", "InsuranceClaim", "notFound");
            }
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    @Override
    public InsuranceRegistrationDTO createInsuranceRegistration(
        InsuranceRegistrationDTO insuranceRegistrationDTO,
        Employee employee,
        User user
    ) {
        try {
            long employeeId = employee.getId();
            long currentUserId = user.getId();

            boolean isEmployeeEligible = isEmployeeEligibleForInsurance();

            if (isEmployeeEligible == false) {
                throw new RuntimeException();
            }

            insuranceRegistrationDTO.setEmployeeId(employeeId);
            insuranceRegistrationDTO.setCreatedAt(Instant.now());
            insuranceRegistrationDTO.setCreatedById(currentUserId);
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
    public InsuranceRegistrationDTO updateInsuranceRegistration(
        InsuranceRegistrationDTO insuranceRegistrationDTO,
        Employee employee,
        User user
    ) {
        try {
            Long employeeId = employee.getId();
            long currentUserId = user.getId();

            boolean isValidEmployee = insuranceRegistrationDTO.getEmployeeId().equals(employeeId);

            if (isValidEmployee && insuranceRegistrationDTO.getInsuranceStatus() == InsuranceStatus.PENDING) {
                insuranceRegistrationDTO.setUpdatedAt(Instant.now());
                insuranceRegistrationDTO.setUpdatedById(currentUserId);

                InsuranceRegistration insuranceRegistration = insuranceRegistrationMapper.toEntity(insuranceRegistrationDTO);
                insuranceRegistration = insuranceRegistrationRepository.save(insuranceRegistration);

                return insuranceRegistrationMapper.toDto(insuranceRegistration);
            } else {
                log.error("Only pending requests are allowed to update.");
                throw new BadRequestAlertException(
                    "Only pending requests are allowed to update..",
                    "InsuranceRegistration",
                    "statusIsNotPending"
                );
            }
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteInsuranceRegistration(Long registrationId) {
        log.debug("Request to delete InsuranceRegistration : {}", registrationId);

        try {
            long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
            Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(registrationId);
            if (insuranceRegistration.isPresent() && insuranceRegistration.get().getInsuranceStatus() == InsuranceStatus.PENDING) {
                insuranceRegistrationRepository.deleteById(registrationId);
            } else {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    @Override
    public InsuranceRelationsDTO getRemainingInsuranceRelations(Employee employee) {
        List<com.bits.hr.domain.enumeration.InsuranceRelation> enumList = new ArrayList<com.bits.hr.domain.enumeration.InsuranceRelation>(
            EnumSet.allOf(com.bits.hr.domain.enumeration.InsuranceRelation.class)
        );
        List<String> insuranceRelations = enumList
            .stream()
            .map(com.bits.hr.domain.enumeration.InsuranceRelation::name)
            .collect(Collectors.toList());

        List<String> registeredRelations = insuranceRegistrationRepository.getAllRegisteredRelationsByEmployeeId(employee.getId());

        insuranceRelations.removeAll(registeredRelations);

        InsuranceRelationsDTO dto = new InsuranceRelationsDTO();
        dto.setRelations(insuranceRelations);

        return dto;
    }

    @Override
    public EmployeeDetailsDTOForInsuranceRegistration getEmployeeDetailsForInsuranceRegistrationByEmployeeId(Long id) {
        Optional<EmployeeDTO> currentEmployee = employeeService.findOne(id);

        if (currentEmployee.isPresent()) {
            EmployeeDetailsDTOForInsuranceRegistration employeeDetails = new EmployeeDetailsDTOForInsuranceRegistration();
            employeeDetails.setFullName(currentEmployee.get().getFullName());
            employeeDetails.setPin(currentEmployee.get().getPin().trim());
            employeeDetails.setDateOfBirth(currentEmployee.get().getDateOfBirth());
            employeeDetails.setNationalIdNo(currentEmployee.get().getNationalIdNo());
            employeeDetails.setEmployeeCategory(currentEmployee.get().getEmployeeCategory());
            employeeDetails.setDepartment(currentEmployee.get().getDepartmentName());
            employeeDetails.setDesignation(currentEmployee.get().getDesignationName());
            employeeDetails.setUnit(currentEmployee.get().getUnitName());
            employeeDetails.setGender(currentEmployee.get().getGender());
            employeeDetails.setPicture(currentEmployee.get().getPicture());
            employeeDetails.setMaritalStatus(currentEmployee.get().getMaritalStatus());
            employeeDetails.setSpouseName(currentEmployee.get().getSpouseName());
            return employeeDetails;
        } else {
            throw new RuntimeException();
        }
    }

    public boolean isEmployeeEligibleForInsurance() {
        try {
            Optional<Employee> currentEmployee = currentEmployeeService.getCurrentEmployee();

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
