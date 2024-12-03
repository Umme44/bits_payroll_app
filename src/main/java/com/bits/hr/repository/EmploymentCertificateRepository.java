package com.bits.hr.repository;

import com.bits.hr.domain.EmploymentCertificate;
import com.bits.hr.domain.enumeration.CertificateStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmploymentCertificate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmploymentCertificateRepository extends JpaRepository<EmploymentCertificate, Long> {
    @Query(
        "select employmentCertificate from EmploymentCertificate employmentCertificate where employmentCertificate.createdBy.login = ?#{principal.username}"
    )
    List<EmploymentCertificate> findByCreatedByIsCurrentUser();

    @Query(
        "select employmentCertificate from EmploymentCertificate employmentCertificate where employmentCertificate.updatedBy.login = ?#{principal.username}"
    )
    List<EmploymentCertificate> findByUpdatedByIsCurrentUser();

    @Query(
        "select employmentCertificate from EmploymentCertificate employmentCertificate where employmentCertificate.generatedBy.login = ?#{principal.username}"
    )
    List<EmploymentCertificate> findByGeneratedByIsCurrentUser();

    //    @Query(value = "select model from EmploymentCertificate model " +
    //        "where (lower(model.employee.fullName) like concat('%',lower(:searchText) ,'%') or model.employee.pin = :searchText) " +
    //        "and ( :status is null or model.certificateStatus = :status) " +
    //        "and ( cast(:startDate as date ) is null or ( model.createdAt >= :startDate and model.createdAt <= :endDate ) )" +
    //        "")
    //    Page<EmploymentCertificate> findAllUsingFilter(String searchText, CertificateStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(
        value = "select model from EmploymentCertificate model " +
        "where (:status is null or model.certificateStatus = :status) " +
        "and (lower(model.employee.fullName) like concat('%',lower(:searchText),'%') or model.employee.pin = :searchText) " +
        "and (cast(:startDate as date) is null or (model.createdAt >= :startDate and model.createdAt <= :endDate)) " +
        "order by model.createdAt desc"
    )
    Page<EmploymentCertificate> findAllUsingFilter(
        String searchText,
        CertificateStatus status,
        Instant startDate,
        Instant endDate,
        Pageable pageable
    );

    @Query(
        value = "select model from EmploymentCertificate model " +
        "where model.employee.id =:employeeId " +
        "and ( :status is null or model.certificateStatus = :status) " +
        "order by model.createdAt desc "
    )
    Page<EmploymentCertificate> findAllByEmployeeIdUsingFilter(long employeeId, CertificateStatus status, Pageable pageable);

    @Query(value = "select model from EmploymentCertificate model " + "where model.certificateStatus ='GENERATED' and model.id =:id")
    Optional<EmploymentCertificate> findApprovedEmploymentCertificateById(long id);

    @Query(
        value = "select case when count(model) > 0 then false else true end from EmploymentCertificate model " +
        "where model.certificateStatus = 'GENERATED' and model.referenceNumber=:referenceNumber"
    )
    boolean isEmploymentCertificateReferenceNumberUnique(String referenceNumber);

    @Query(value = "select model from EmploymentCertificate model " + "where model.certificateStatus ='GENERATED' order by model.createdAt")
    List<EmploymentCertificate> findAllApprovedRequests();

    @Query(
        value = "select model from EmploymentCertificate model " +
        "where model.certificateStatus ='GENERATED' " +
        "and (model.generatedAt >= :startDate and model.generatedAt <= :endDate)" +
        "order by model.createdAt"
    )
    List<EmploymentCertificate> findAllApprovedRequestsBetweenDateRange(Instant startDate, Instant endDate);
}
