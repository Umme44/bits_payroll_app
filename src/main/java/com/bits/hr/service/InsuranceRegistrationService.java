package com.bits.hr.service;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.service.dto.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Interface for managing {@link com.bits.hr.domain.InsuranceRegistration}.
 */
@Service
public interface InsuranceRegistrationService {
    InsuranceRegistrationDTO createInsuranceRegistration(Long userId, InsuranceRegistrationDTO insuranceRegistrationDTO);
    InsuranceRegistrationDTO updateInsuranceRegistration(Long userId, InsuranceRegistrationDTO insuranceRegistrationDTO);
    List<InsuranceRegistrationDTO> findAll(Pageable pageable);
    Page<InsuranceRegistrationAdminDTO> findAllInsuranceRegistrationUsingFilter(
        String searchText,
        Integer year,
        Integer month,
        InsuranceStatus status,
        Boolean isExcluded,
        Boolean isCancelled,
        Boolean isSeperated,
        Pageable pageable
    );
    List<InsuranceRegistrationDTO> getInsuranceRegistrationListByEmployeeId(Long employeeId);
    Optional<InsuranceRegistrationDTO> findOne(Long id);
    List<InsuranceRegistrationDTO> findOneByInsuranceCardId(String cardId);
    void delete(Long id);
    InsuranceRegistrationDTO approveInsuranceRegistration(User user, InsuranceApprovalDTO insuranceApprovalDTO);
    boolean isCardIdUnique(String insuranceCardId, Long id);
    InsuranceRegistrationDTO rejectInsuranceRegistration(User user, InsuranceApprovalDTO insuranceApprovalDTO);
    InsuranceRegistrationDTO cancelInsuranceRegistration(User user, InsuranceApprovalDTO insuranceApprovalDTO);
    List<EmployeeMinimalDTO> getAllEligibleEmployees();
    InsuranceRelationsDTO getRemainingRelations(Long employeeId);
    InsuranceRelationsDTO getAllRelations();
    double getTotalSettledAmountWithinYearByInsuranceId(long id, LocalDate yearStartDate, LocalDate yearEndDate);
    void updateAvailableBalanceInInsuranceRegistration(long insuranceId);
}
