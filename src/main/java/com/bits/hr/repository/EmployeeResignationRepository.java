package com.bits.hr.repository;

import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.Status;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmployeeResignation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeResignationRepository extends JpaRepository<EmployeeResignation, Long> {
    @Query(
        "SELECT model FROM EmployeeResignation model " + " WHERE model.employee.id = :employeeId and model.approvalStatus <> 'NOT_APPROVED'"
    )
    List<EmployeeResignation> findEmployeeResignationByEmployeeId(Long employeeId);

    @Query("SELECT model FROM EmployeeResignation model " + " WHERE model.employee.id = :employeeId and model.approvalStatus = 'APPROVED'")
    List<EmployeeResignation> findApprovedEmployeeResignationByEmployeeId(Long employeeId);

    // caution: jpa does not take empty list
    @Query(value = "select model from EmployeeResignation model " + " where model.id IN :ids AND model.approvalStatus = 'PENDING' ")
    List<EmployeeResignation> getPendingResignationsByIds(List<Long> ids);

    @Query(value = "select model from EmployeeResignation model " + " where model.approvalStatus = 'PENDING' ")
    List<EmployeeResignation> getAllPendingResignations();

    @Query(" SELECT model FROM EmployeeResignation model " + " WHERE model.employee.id = :employeeId and model.approvalStatus =:status ")
    List<EmployeeResignation> findResignationByEmpIdAndStatus(Long employeeId, Status status);

    @Query(
        "select model from EmployeeResignation model " +
        "where " +
        "(" +
        " (model.employee.fullName = '' OR lower(model.employee.fullName) like lower(concat('%', :searchText, '%'))) OR " +
        "(model.employee.pin = '' OR lower(model.employee.pin) like lower(concat('%', :searchText, '%'))) " +
        ") AND " +
        "(( cast(:startDate as date) IS null OR cast(:endDate as date) is null) OR (model.resignationDate BETWEEN :startDate and :endDate)) " +
        "order by model.resignationDate"
    )
    Page<EmployeeResignation> findAll(String searchText, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(
        "select model from EmployeeResignation model " +
        "       where model.approvalStatus = 'APPROVED' " +
        "           and model.lastWorkingDay < :date"
    )
    List<EmployeeResignation> findAllAfterLWDEnd(LocalDate date);

    @Query(
        "select model from EmployeeResignation model " +
        "       where model.approvalStatus = 'APPROVED' " +
        "           and model.lastWorkingDay=:date"
    )
    List<EmployeeResignation> findAllBeforeLWDEnd(LocalDate date);

    @Query(
        "select model from EmployeeResignation model " +
        "where model.employee.pin = :pin " +
        "and model.approvalStatus = 'APPROVED' " +
        "and model.lastWorkingDay < :date"
    )
    List<EmployeeResignation> findResignedEmployeeByPin(String pin, LocalDate date);
}
