package com.bits.hr.service.impl;

import com.bits.hr.domain.InsuranceClaim;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.ClaimStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.InsuranceClaimRepository;
import com.bits.hr.service.InsuranceClaimService;
import com.bits.hr.service.InsuranceRegistrationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.InsuranceClaimDTO;
import com.bits.hr.service.mapper.InsuranceClaimMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InsuranceClaim}.
 */
@Service
@Transactional
public class InsuranceClaimServiceImpl implements InsuranceClaimService {

    private final Logger log = LoggerFactory.getLogger(InsuranceClaimServiceImpl.class);

    private final InsuranceClaimRepository insuranceClaimRepository;

    private final InsuranceClaimMapper insuranceClaimMapper;

    private final CurrentEmployeeService currentEmployeeService;

    private final InsuranceRegistrationService insuranceRegistrationService;

    public InsuranceClaimServiceImpl(
        InsuranceClaimRepository insuranceClaimRepository,
        InsuranceClaimMapper insuranceClaimMapper,
        CurrentEmployeeService currentEmployeeService,
        InsuranceRegistrationService insuranceRegistrationService
    ) {
        this.insuranceClaimRepository = insuranceClaimRepository;
        this.insuranceClaimMapper = insuranceClaimMapper;
        this.currentEmployeeService = currentEmployeeService;
        this.insuranceRegistrationService = insuranceRegistrationService;
    }

    @Override
    public InsuranceClaimDTO createInsuranceClaim(InsuranceClaimDTO insuranceClaimDTO, User user) {
        try {
            insuranceClaimDTO.setCreatedAt(Instant.now());
            insuranceClaimDTO.setCreatedById(user.getId());

            InsuranceClaim insuranceClaim = insuranceClaimMapper.toEntity(insuranceClaimDTO);
            insuranceClaim = insuranceClaimRepository.save(insuranceClaim);

            insuranceRegistrationService.updateAvailableBalanceInInsuranceRegistration(insuranceClaimDTO.getInsuranceRegistrationId());

            return insuranceClaimMapper.toDto(insuranceClaim);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public InsuranceClaimDTO updateInsuranceClaim(InsuranceClaimDTO insuranceClaimDTO, User user) {
        try {
            insuranceClaimDTO.setUpdatedAt(Instant.now());
            insuranceClaimDTO.setUpdatedById(user.getId());

            InsuranceClaim insuranceClaim = insuranceClaimMapper.toEntity(insuranceClaimDTO);
            insuranceClaim = insuranceClaimRepository.save(insuranceClaim);

            insuranceRegistrationService.updateAvailableBalanceInInsuranceRegistration(insuranceClaimDTO.getInsuranceRegistrationId());

            return insuranceClaimMapper.toDto(insuranceClaim);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceClaimDTO> findAll(String searchText, ClaimStatus status, Integer year, Integer month, Pageable pageable) {
        log.debug("Request to get all InsuranceClaims");

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

        return insuranceClaimRepository
            .findAllInsuranceClaimsUsingFilter(searchText, status, searchFrom, searchTo, pageable)
            .map(insuranceClaimMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsuranceClaimDTO> findOne(Long id) {
        log.debug("Request to get InsuranceClaim : {}", id);
        return insuranceClaimRepository.findById(id).map(insuranceClaimMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsuranceClaim : {}", id);
        Optional<InsuranceClaim> insuranceClaimOptional = insuranceClaimRepository.findById(id);

        if (!insuranceClaimOptional.isPresent()) {
            throw new BadRequestAlertException("Insurance Claim not found", "Insurance Claim", "idnull");
        }
        insuranceClaimRepository.deleteById(id);
    }
}
