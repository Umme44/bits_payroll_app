package com.bits.hr.repository;

import com.bits.hr.domain.EmployeeNOC;
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
 * Spring Data  repository for the EmployeeNOC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeNOCRepository extends JpaRepository<EmployeeNOC, Long> {
    @Query("select employeeNOC from EmployeeNOC employeeNOC where employeeNOC.createdBy.login = ?#{principal.username}")
    List<EmployeeNOC> findByCreatedByIsCurrentUser();

    @Query("select employeeNOC from EmployeeNOC employeeNOC where employeeNOC.updatedBy.login = ?#{principal.username}")
    List<EmployeeNOC> findByUpdatedByIsCurrentUser();

    @Query("select employeeNOC from EmployeeNOC employeeNOC where employeeNOC.generatedBy.login = ?#{principal.username}")
    List<EmployeeNOC> findByGeneratedByIsCurrentUser();

    //    @Query(value = "select model from EmployeeNOC model " +
    //        "where (lower(model.employee.fullName) like concat('%',lower(:searchText) ,'%') or model.employee.pin = :searchText) " +
    //        "and ( :status is null or model.certificateStatus = :status) " +
    //        "and ( cast(:startDate as date ) is null or ( model.createdAt >= :startDate and model.createdAt <= :endDate ) )" +
    //        "")
    //    Page<EmployeeNOC> findAllUsingFilter(String searchText, CertificateStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(
        value = "select model from EmployeeNOC model " +
        "where (:status is null or model.certificateStatus = :status) " +
        "and (lower(model.employee.fullName) like concat('%',lower(:searchText),'%') or model.employee.pin = :searchText) " +
        "and (cast(:startDate as date) is null or (model.createdAt >= :startDate and model.createdAt <= :endDate)) " +
        "order by model.createdAt desc"
    )
    Page<EmployeeNOC> findAllUsingFilter(
        String searchText,
        CertificateStatus status,
        Instant startDate,
        Instant endDate,
        Pageable pageable
    );

    @Query(
        value = "select model from EmployeeNOC model " +
        "where model.employee.id =:employeeId " +
        "and ( :status is null or model.certificateStatus = :status) " +
        "order by model.createdAt desc "
    )
    Page<EmployeeNOC> findAllByEmployeeIdUsingFilter(long employeeId, CertificateStatus status, Pageable pageable);

    @Query(value = "select model from EmployeeNOC model " + "where model.certificateStatus ='GENERATED' and model.id =:id")
    Optional<EmployeeNOC> findApprovedEmployeeNocById(long id);

    @Query(
        value = "select case when count(model) > 0 then false else true end from EmployeeNOC model " +
        "where model.certificateStatus = 'GENERATED' and model.referenceNumber=:referenceNumber"
    )
    boolean isEmployeeNocReferenceNumberUnique(String referenceNumber);

    @Query(value = "select model from EmployeeNOC model " + "where model.certificateStatus ='GENERATED' order by model.createdAt")
    List<EmployeeNOC> findAllApprovedRequests();

    @Query(
        value = "select model from EmployeeNOC model " +
        "where model.certificateStatus ='GENERATED' " +
        "and (model.generatedAt >= :startDate and model.generatedAt <= :endDate)" +
        "order by model.createdAt"
    )
    List<EmployeeNOC> findAllApprovedRequestsBetweenDateRange(Instant startDate, Instant endDate);
}
