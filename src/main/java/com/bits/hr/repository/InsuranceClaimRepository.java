package com.bits.hr.repository;

import com.bits.hr.domain.InsuranceClaim;
import com.bits.hr.domain.enumeration.ClaimStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InsuranceClaim entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Long> {
    @Query(
        value = "select model from InsuranceClaim model " +
        "where model.insuranceRegistration.employee.pin =:employeePin order by model.createdAt desc "
    )
    List<InsuranceClaim> findAllInsuranceClaimByEmployeePin(String employeePin);

    @Query(
        value = "select model from InsuranceClaim model " +
        "where model.insuranceRegistration.id =:registrationId " +
        "and (" +
        "       ( :status = 'SETTLED' and model.settlementDate =:settlementDate and model.settledAmount = :settledAmount) " +
        "       or ( :status <> 'SETTLED' and model.regretDate =:regretDate ) " +
        "    )" +
        ""
    )
    List<InsuranceClaim> findDuplicate(
        long registrationId,
        String status,
        LocalDate regretDate,
        LocalDate settlementDate,
        Double settledAmount
    );

    @Query(
        value = "select model from InsuranceClaim model " +
        "where model.insuranceRegistration.id = :id " +
        "and model.claimStatus = 'SETTLED'" +
        "and (model.settlementDate >= :yearStartDate and model.settlementDate <= :yearEndDate)" +
        ""
    )
    List<InsuranceClaim> getInsuranceClaimsByInsuranceRegistrationId(long id, LocalDate yearStartDate, LocalDate yearEndDate);

    @Query(
        "select model from InsuranceClaim model " +
        "where (" +
        "         (lower(model.insuranceRegistration.name) like concat('%',lower(:searchText) ,'%')) " +
        "         or (model.insuranceRegistration.employee.pin = :searchText)" +
        "      ) " +
        "and (:status is null or model.claimStatus = :status) " +
        "and ( " +
        "       (cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null)" +
        "       or (model.createdAt >= :searchFrom and model.createdAt <= :searchTo)" +
        "    ) " +
        "order by model.createdAt desc "
    )
    Page<InsuranceClaim> findAllInsuranceClaimsUsingFilter(
        String searchText,
        ClaimStatus status,
        Instant searchFrom,
        Instant searchTo,
        Pageable pageable
    );
}
