package com.bits.hr.repository;

import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the SalaryCertificate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaryCertificateRepository extends JpaRepository<SalaryCertificate, Long> {
    @Query(
        "select salaryCertificate from SalaryCertificate salaryCertificate where salaryCertificate.createdBy.login = ?#{principal.username} order by salaryCertificate.createdAt desc"
    )
    List<SalaryCertificate> findByCreatedByIsCurrentUser();

    @Query(
        "select salaryCertificate from SalaryCertificate salaryCertificate where salaryCertificate.updatedBy.login = ?#{principal.username}"
    )
    List<SalaryCertificate> findByUpdatedByIsCurrentUser();

    @Query(
        "select salaryCertificate from SalaryCertificate salaryCertificate where salaryCertificate.sanctionBy.login = ?#{principal.username}"
    )
    List<SalaryCertificate> findBySanctionByIsCurrentUser();

    @Query("select salaryCertificate from SalaryCertificate salaryCertificate where salaryCertificate.status = 'PENDING'")
    List<SalaryCertificate> getAllPending();

    @Query(
        "select salaryCertificate from SalaryCertificate salaryCertificate" +
        " where salaryCertificate.id IN :ids " +
        " AND salaryCertificate.status = 'PENDING'"
    )
    List<SalaryCertificate> getAllPendingByIds(List<Long> ids);

    //    @Query("select salaryCertificate from SalaryCertificate salaryCertificate " +
    //        "where salaryCertificate.createdBy.login = ?#{principal.username} " +
    //        "AND salaryCertificate.employeeSalary.id=:employeeSalaryId AND salaryCertificate.status <> 'NOT_APPROVED'")
    //    Optional<SalaryCertificate> findByEmployeeSalaryId(long employeeSalaryId);

    @Query(
        "select  model from SalaryCertificate model " +
        "where model.employee.id =:employeeId " +
        "and model.year = :year " +
        "and model.month =:month"
    )
    Optional<SalaryCertificate> findSalaryCertificateByEmployeeIdYearAndMonth(long employeeId, int year, Month month);

    @Query(
        value = "select model from SalaryCertificate model " +
        "where model.employee.id =:employeeId " +
        "and (model.status = 'APPROVED' or model.status = 'PENDING')"
    )
    List<SalaryCertificate> findApprovedSalaryCertificatesByEmployeeId(long employeeId);

    @Query(value = "select model from SalaryCertificate model " + "where model.employee.id =:employeeId order by model.createdAt desc ")
    List<SalaryCertificate> findAllByEmployeeId(long employeeId);

    @Query(
        value = "select model from SalaryCertificate model " +
        "where lower(model.employee.fullName) like concat('%',lower(:searchText),'%') " +
        "or model.employee.pin like concat('%',lower(:searchText),'%') order by model.createdAt desc "
    )
    Page<SalaryCertificate> findAllBySearchText(String searchText, Pageable pageable);

    @Query(
        value = "select model from SalaryCertificate model " +
        "where model.employee.id =:employeeId " +
        "and ( :status is null or model.status = :status) " +
        "order by model.createdAt desc "
    )
    Page<SalaryCertificate> findAllFilterByStatus(Long employeeId, Status status, Pageable pageable);

    @Query(
        value = "select model from SalaryCertificate model " +
        "where (:status is null or model.status = :status) " +
        "and (lower(model.employee.fullName) like concat('%',lower(:searchText),'%') or model.employee.pin = :searchText) " +
        "and (:selectedYear=0 or (EXTRACT(YEAR FROM model.createdAt) = :selectedYear)) " +
        "order by model.createdAt desc"
    )
    Page<SalaryCertificate> findAllUsingFilter(String searchText, Status status, Integer selectedYear, Pageable pageable);

    @Query(
        value = "select case when count(model) > 0 then false else true end from SalaryCertificate model " +
        "where model.status = 'APPROVED' and model.referenceNumber=:referenceNumber"
    )
    boolean isEmployeeNocReferenceNumberUnique(String referenceNumber);

    @Query(value = "select model from SalaryCertificate model " + "where model.status ='APPROVED' order by model.createdAt")
    List<SalaryCertificate> findAllApprovedRequests();

    @Query(
        value = "select model from SalaryCertificate model " +
        "where model.status ='APPROVED' " +
        "and (model.sanctionAt >= :fromDate and model.sanctionAt <= :toDate)" +
        "order by model.createdAt"
    )
    List<SalaryCertificate> findAllApprovedRequestsBetweenDateRange(LocalDate fromDate, LocalDate toDate);
}
