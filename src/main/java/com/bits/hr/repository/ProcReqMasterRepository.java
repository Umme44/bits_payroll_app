package com.bits.hr.repository;

import com.bits.hr.domain.ProcReqMaster;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProcReqMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcReqMasterRepository extends JpaRepository<ProcReqMaster, Long> {
    @Query(
        "select model from ProcReqMaster model " +
        "where (:departmentId is null or model.department.id = :departmentId) " +
        "and (:employeeId is null or model.requestedBy.id = :employeeId) " +
        "order by model.requisitionNo desc "
    )
    Page<ProcReqMaster> query(Long departmentId, Long employeeId, Pageable pageable);

    @Query("select model from ProcReqMaster model " + "where model.id = :id " + "and model.requestedBy.id = :employeeId")
    Optional<ProcReqMaster> findByIdAndEmployeeId(Long id, long employeeId);

    @Query("select model from ProcReqMaster model where model.requisitionNo = :prfNumber")
    Optional<ProcReqMaster> findByPRFNumber(String prfNumber);

    @Query(
        "select model from ProcReqMaster model " +
        "where model.requestedBy.id = :requesterId " +
        "and (:departmentId is null or model.department.id = :departmentId) " +
        "order by model.requisitionNo desc "
    )
    Page<ProcReqMaster> findAllByEmployeeIdAndDepartmentId(long requesterId, Long departmentId, Pageable pageable);

    @Query(
        "select model from ProcReqMaster model " +
        "where (:employeeId is null or model.requestedBy.id = :employeeId) " +
        "and (:departmentId is null or model.department.id = :departmentId) " +
        "and (:requisitionStatus is null or model.requisitionStatus = :requisitionStatus) " +
        "and ( " +
        "       (cast(:startDate as date) is null and cast(:endDate as date) is null) or (model.requestedDate between :startDate and :endDate)" +
        "    ) " +
        "order by model.requisitionNo desc "
    )
    Page<ProcReqMaster> findAll(
        Long employeeId,
        Long departmentId,
        RequisitionStatus requisitionStatus,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    );

    @Query(
        "select model from ProcReqMaster model where model.nextApprovalFrom.id = :recommenderId " +
        "and (model.requisitionStatus = 'PENDING' or model.requisitionStatus = 'IN_PROGRESS') " +
        "order by model.requisitionNo desc "
    )
    List<ProcReqMaster> findPendingList(long recommenderId);
}
