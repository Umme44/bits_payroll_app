package com.bits.hr.repository;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.RecruitmentRequisitionForm;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecruitmentRequisitionForm entity.
 */
@Repository
public interface RecruitmentRequisitionFormRepository extends JpaRepository<RecruitmentRequisitionForm, Long> {

    @Query(value = "select model from RecruitmentRequisitionForm model " +
        "where model.isDeleted <> true " +
        "and ( :requesterId is null or model.requester.id = :requesterId) " +
        "and ( :departmentId is null or model.department.id = :departmentId) " +
        "and ( " +
        "       (cast(:startDate as date) is null and cast(:endDate as date) is null) " +
        "       or (model.dateOfRequisition >= :startDate and model.dateOfRequisition <= :endDate) " +
        "    ) " +
        "and (:requisitionStatus is null or model.requisitionStatus = :requisitionStatus )" +
        " order by model.rrfNumber desc ")
    Page<RecruitmentRequisitionForm> findAllRrfForAdmin(
        Long requesterId,
        Long departmentId,
        LocalDate startDate,
        LocalDate endDate,
        RequisitionStatus requisitionStatus,
        Pageable pageable
    );

    @Query(value = "select model from RecruitmentRequisitionForm model " +
        "where model.requester.id=:employeeId " +
        "and model.isDeleted <> true " +
        "and ((cast(:startDate as date) is null and cast(:endDate as date) is null) or (model.dateOfRequisition >= :startDate and model.dateOfRequisition <= :endDate) )"+
        "and (:requisitionStatus is null or model.requisitionStatus = :requisitionStatus )" +
        "order by model.rrfNumber desc ")
    Page<RecruitmentRequisitionForm> getAllCreatedByEmployee(long employeeId, RequisitionStatus requisitionStatus, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value = "select coalesce(count(model.id),0) from RecruitmentRequisitionForm model where model.dateOfRequisition >=:yearStart and model.dateOfRequisition <=:yearEnd ")
    int countByYearRange( LocalDate yearStart , LocalDate yearEnd);

    @Query(value = "select model from RecruitmentRequisitionForm model where model.rrfNumber=:rrfNumber")
    List<RecruitmentRequisitionForm> findByRrfNumber(String rrfNumber);

    //    PENDING,
    //    LM_APPROVED,
    //    HOD_APPROVED,
    //    CTO_APPROVED,
    //    HOHR_VETTED,
    @Query(value = "select model from RecruitmentRequisitionForm model " +
        " where " +
        "( model.requisitionStatus='PENDING' or " +
        " model.requisitionStatus='LM_APPROVED' or " +
        " model.requisitionStatus='HOD_APPROVED' or " +
        " model.requisitionStatus='CTO_APPROVED' or " +
        " model.requisitionStatus='HOHR_VETTED' " +
        " and (" +
        "       model.recommendedBy01.id=:employeeId " +
        "       or model.recommendedBy02.id=:employeeId " +
        "       or model.recommendedBy03.id=:employeeId " +
        "       or model.recommendedBy04.id=:employeeId " +
        "       or model.recommendedBy05.id=:employeeId " +
        " ) ) and model.isDeleted <> true " +
        "order by model.rrfNumber desc " )
    List<RecruitmentRequisitionForm> getApprovalList(long employeeId);


    @Query(value = "select model from RecruitmentRequisitionForm model where model.id = :id and model.requester.id = :requesterId")
    Optional<RecruitmentRequisitionForm> findByIdAndEmployeeId(Long id, Long requesterId);

    @Query("select model from RecruitmentRequisitionForm model " +
        "where model.isDeleted <> true " +
        "and ( :employeeId is null or model.requester.id = :employeeId) " +
        "and ( :departmentId is null or model.department.id = :departmentId) " +
        "and ( " +
        "       (cast(:startDate as date) is null and cast(:endDate as date) is null) " +
        "    or (model.dateOfRequisition >= :startDate and model.dateOfRequisition <= :endDate) " +
        "    ) " +
        "and (:requisitionStatus is null or model.requisitionStatus = :requisitionStatus ) " +
        "and model.createdBy = :user " +
        "and model.requester <> :requester " +
        "order by model.rrfNumber desc ")
    Page<RecruitmentRequisitionForm> findAllRaisedByUser(
        Long employeeId,
        Long departmentId,
        LocalDate startDate,
        LocalDate endDate,
        RequisitionStatus requisitionStatus,
        Pageable pageable,
        User user,
        Employee requester);

    @Query("select model from RecruitmentRequisitionForm model " +
        "       where ( model.createdBy = :user " +
        "           and model.requester <> :requester ) AND model.isDeleted <> true ")
    Page<RecruitmentRequisitionForm> findAllRaisedOnBehalf(User user, Employee requester, Pageable pageable);

    @Query("select count(model) from RecruitmentRequisitionForm model where model.requisitionStatus = 'PENDING' and model.requisitionStatus = 'ON_PROGRESS'")
    int getPendingCount();

    @Query(value = "select model from RecruitmentRequisitionForm model " +
        "where model.isDeleted <> true " +
        "and ( :requesterId is null or model.requester.id = :requesterId) " +
        "and ( :departmentId is null or model.department.id = :departmentId) " +
        "and ( " +
        "       (cast(:startDate as date) is null and cast(:endDate as date) is null) " +
        "       or (model.dateOfRequisition >= :startDate and model.dateOfRequisition <= :endDate) " +
        "    ) " +
        "and (:requisitionStatus is null or model.requisitionStatus = :requisitionStatus )" +
        " order by model.rrfNumber desc ")
    List<RecruitmentRequisitionForm> findAllBySearchParams(Long requesterId,
                                                           Long departmentId,
                                                           RequisitionStatus requisitionStatus,
                                                           LocalDate startDate,
                                                           LocalDate endDate);
}
