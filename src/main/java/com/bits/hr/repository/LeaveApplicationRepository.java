package com.bits.hr.repository;

import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.LeaveType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LeaveApplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    @Query(
        value = "Select model " +
        " from LeaveApplication model " +
        " where model.employee.id=:enployeeId " +
        "   and model.leaveType=:leaveType " +
        "   and ( " +
        "       ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "       ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "       ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "       ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "       ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "       )   "
    )
    List<LeaveApplication> getRequested(long enployeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " Select model " +
        " from LeaveApplication model " +
        " where model.employee.id=:enployeeId " +
        "   and model.leaveType=:leaveType " +
        "   and model.isRejected = true " +
        "   and ( " +
        "           ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "           ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "           ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "           ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "           ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "       )   "
    )
    List<LeaveApplication> getRejected(long enployeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " Select model " +
        " from LeaveApplication model " +
        " where model.employee.id=:employeeId " +
        "   and model.leaveType=:leaveType " +
        "   and ( model.isLineManagerApproved=true OR model.isHRApproved=true )" +
        "   and ( " +
        "           ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "           ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "           ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "           ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "           ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "       )   "
    )
    List<LeaveApplication> getDaysApprovedBetweenDates(long employeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "Select model " +
        " from LeaveApplication model " +
        " where model.employee.pin=:pin " +
        "   and model.leaveType=:leaveType " +
        "   and model.isRejected=true " +
        "   and model.applicationDate >=:startOfYear " +
        "   and model.applicationDate<=:endOfYear " +
        "   and ( " +
        "           ( :startOfYear  BETWEEN model.startDate and model.endDate ) or " +
        "           ( :endOfYear    BETWEEN model.startDate and model.endDate ) or " +
        "           ( model.startDate BETWEEN :startOfYear and :endOfYear ) or " +
        "           ( model.endDate   BETWEEN :startOfYear and :endOfYear ) or " +
        "           ( model.startDate  <= :startOfYear and model.endDate >=:endOfYear ) " +
        "       )   "
    )
    List<LeaveApplication> getDaysCancelledByPinAndTypeAndYear(String pin, LeaveType leaveType, LocalDate startOfYear, LocalDate endOfYear);

    @Query(
        value = "Select model " +
        " from LeaveApplication model " +
        " where model.employee.pin=:pin " +
        "   and ( " +
        "           ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "           ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "           ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "           ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "           ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "       ) "
    )
    List<LeaveApplication> getLeaveApplicationsByEmployeePinBetweenTwoDates(String pin, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " Select model " +
        " from LeaveApplication model " +
        " where model.employee.id=:employeeId " +
        "   and ( " +
        "           ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "           ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "           ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "           ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "           ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "       )   "
    )
    List<LeaveApplication> getLeaveApplicationByEmployeeIdBetweenTwoDates(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " Select model " +
        " from LeaveApplication model " +
        " where model.employee.id  = :employeeId " +
        "   and model.startDate = :startDate " +
        "   and model.endDate = :endDate "
    )
    List<LeaveApplication> checkDuplicates(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "Select model " +
        " from LeaveApplication model " +
        " where model.employee.pin=:pin " +
        " and ( model.isHRApproved=true or model.isLineManagerApproved=true )" +
        " and ( " +
        "       ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "       ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "       ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "       ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "       ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "   )   "
    )
    List<LeaveApplication> getApprovedLeaveApplicationsByEmployeePinBetweenTwoDates(String pin, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "Select model " +
        " from LeaveApplication model " +
        " where model.employee.id=:employeeId " +
        "   and ( model.isHRApproved=true or model.isLineManagerApproved=true )" +
        "   and ( " +
        "           ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "           ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "           ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "           ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "           ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "       )   "
    )
    List<LeaveApplication> getApprovedLeaveApplicationsByEmployeeIdBetweenTwoDates(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " select model " +
        " FROM LeaveApplication model " +
        " WHERE" +
        " ( " +
        " ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        " ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        " ( model.startDate BETWEEN :startDate and :endDate ) or " +
        " ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        " ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        ")"
    )
    List<LeaveApplication> findLeaveApplicationBetweenDates(LocalDate startDate, LocalDate endDate);

    // todo: AMS query fix
    @Query(
        value = " Select model " +
        " from LeaveApplication model " +
        " where model.employee.pin=:pin " +
        " and ( " +
        "  ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "  ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "  ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "  ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "  ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        " ) "
    )
    List<LeaveApplication> findEmployeeLeaveApplicationBetweenDates(String pin, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " Select model " +
        " from LeaveApplication model " +
        " where model.employee.pin=:pin and model.isRejected = false" +
        " and ( " +
        "  ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "  ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "  ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "  ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "  ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        " ) "
    )
    List<LeaveApplication> findEmployeeLeaveApplicationBetweenDatesExceptRejected(String pin, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "select model from LeaveApplication model where model.employee.pin=:pin and model.isRejected = false " +
        "and (model.startDate between :startDate and :endDate or model.endDate between :startDate and :endDate)"
    )
    List<LeaveApplication> findApprovedAndPendingLeaveApplicationsByEmployeePinAndDateRange(
        String pin,
        LocalDate startDate,
        LocalDate endDate
    );

    @Query(
        value = "select model from LeaveApplication model " +
        "where model.employee.pin=:pin and model.isRejected = false and model.leaveType = :leaveType " +
        "and (model.startDate between :startDate and :endDate or model.endDate between :startDate and :endDate)"
    )
    List<LeaveApplication> findApprovedAndPendingLeaveByEmployeePinAndLeaveTypeDateRange(
        String pin,
        LeaveType leaveType,
        LocalDate startDate,
        LocalDate endDate
    );

    @Query(
        value = "select model from LeaveApplication model " +
        "where model.employee.id=:employeeId and model.isRejected = false and model.leaveType = :leaveType " +
        "and (model.startDate between :startDate and :endDate or model.endDate between :startDate and :endDate)"
    )
    List<LeaveApplication> findApprovedAndPendingLeaveByEmployeeIdAndLeaveTypeBetweenDate(
        long employeeId,
        LeaveType leaveType,
        LocalDate startDate,
        LocalDate endDate
    );

    @Query(
        value = "select model from LeaveApplication model " +
        "where (:pin = '' OR model.employee.pin=:pin) AND " +
        "(:leaveType is null OR model.leaveType=:leaveType) AND " +
        "(" +
        "( cast(:startDate as date) is null OR cast(:endDate as date) is null) OR " +
        "(model.startDate BETWEEN :startDate and :endDate) OR " +
        "(model.endDate BETWEEN :startDate and :endDate) OR " +
        "(model.startDate >= :startDate and model.endDate <=:endDate)" +
        ")"
    )
    Page<LeaveApplication> findLeaveApplicationsBetweenDatesAndLeaveType(
        String pin,
        LocalDate startDate,
        LocalDate endDate,
        LeaveType leaveType,
        Pageable pageable
    );

    @Query(value = "select model from LeaveApplication model " + "where model.employee.pin=:pin AND " + "model.leaveType=:leaveType")
    List<LeaveApplication> findLeaveApplicationByEmployeeAndLeaveType(String pin, LeaveType leaveType);

    @Query(value = " Select model " + " from LeaveApplication model " + " where model.employee.pin=:pin ")
    List<LeaveApplication> findEmployeeLeaveApplicationByEmployeePin(String pin);

    @Query(value = " Select model " + " from LeaveApplication model " + " where model.employee.id=:employeeId")
    List<LeaveApplication> findEmployeeLeaveApplicationByEmployeeId(long employeeId);

    @Query(
        value = " Select model " +
        " from LeaveApplication model " +
        " where model.employee.id = :employeeId " +
        "   and  model.leaveType =:leaveType   " +
        "   and  ( model.isLineManagerApproved = true or model.isHRApproved = true) "
    )
    List<LeaveApplication> findApprovedLeaveApplicationByEmployeeIdAndLeaveType(long employeeId, LeaveType leaveType);

    // caution: jpa does not take empty list
    @Query(
        value = " select model " +
        " from LeaveApplication model " +
        " where model.id in :ids " +
        "   and (model.isLineManagerApproved<>true or model.isHRApproved<>true)" +
        "   and model.isRejected <> true "
    )
    List<LeaveApplication> getPendingLeaveApplicationsByIds(List<Long> ids);

    @Query(
        value = " select model " +
        " from LeaveApplication model " +
        " where not( model.isHRApproved = true or model.isLineManagerApproved = true )" +
        " and model.isRejected <> true "
    )
    List<LeaveApplication> getAllPendingLeaveApplications();

    @Query(
        value = " select model " +
        " from LeaveApplication model " +
        " where not( model.isHRApproved = true or model.isLineManagerApproved = true )" +
        "   and model.isRejected <> true " +
        "   and model.employee.reportingTo.id=:employeeId "
    )
    List<LeaveApplication> getAllPendingLeaveApplicationsLM(long employeeId);

    // caution: jpa does not take empty list
    @Query(
        value = " select model " +
        " from LeaveApplication model " +
        " where model.id in :ids " +
        "   and (model.isLineManagerApproved<>true or model.isHRApproved<>true)" +
        "   and model.isRejected <> true " +
        "   and model.employee.reportingTo.id=:employeeId "
    )
    List<LeaveApplication> getPendingLeaveApplicationsByIdsLM(List<Long> ids, long employeeId);

    @Query(
        "select count(model) from LeaveApplication model " +
        "where not(model.isHRApproved = true or model.isLineManagerApproved = true ) " +
        "AND model.isRejected <> true"
    )
    int getTotalPendingLeaveApplications();

    @Query(
        "select count(model) from LeaveApplication model " +
        "where not(model.isHRApproved = true or model.isLineManagerApproved = true ) " +
        "AND model.isRejected <> true " +
        "AND model.employee.reportingTo.id=:employeeId"
    )
    int getTotalPendingLeaveApplicationsLM(long employeeId);

    @Query(
        "select model from LeaveApplication model where model.id=:id " +
        "and model.isHRApproved = false and model.isLineManagerApproved = false  and model.isRejected = false " +
        "and model.employee.id=:employeeId"
    )
    Optional<LeaveApplication> findPendingLeaveApplicationByIdAndEmployeeId(Long id, Long employeeId);

    @Query(
        "select model from LeaveApplication model " +
        "where not (model.isHRApproved = true or model.isLineManagerApproved = true) " +
        "  and model.isRejected <> true " +
        "  and model.employee.id = :employeeId"
    )
    List<LeaveApplication> findPendingLeaveApplicationsByEmployeeId(long employeeId);

    @Query(
        "select model from LeaveApplication model  " +
        "where model.isHRApproved = false and model.isLineManagerApproved = false  and model.isRejected = false " +
        "and model.employee.id=:employeeId"
    )
    List<LeaveApplication> findAllPendingLeaveApplicationsByEmployeeId(Long employeeId);

    @Query(
        "select model from LeaveApplication model  " +
        "where model.isHRApproved = false and model.isLineManagerApproved = false and model.isRejected = false " +
        "and model.employee.id=:employeeId and" +
        "(" +
        "( cast(:startDate as date) is null OR :startDate BETWEEN model.startDate and model.endDate) OR " +
        "( cast(:endDate as date) is null OR :endDate BETWEEN model.startDate and model.endDate) OR " +
        "(model.startDate BETWEEN :startDate and :endDate) OR " +
        "(model.endDate BETWEEN :startDate and :endDate) OR " +
        "(model.startDate  <= :startDate and model.endDate >=:endDate)" +
        ")"
    )
    List<LeaveApplication> findPendingLeaveApplicationByEmployeeIdAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "select model from LeaveApplication model where model.sanctionedBy.id=:userId " +
        "and model.isLineManagerApproved = true and model.isRejected <> true"
    )
    Page<LeaveApplication> findLeaveApprovedByUserId(Pageable pageable, long userId);

    @Query(
        value = "select model from LeaveApplication model where model.sanctionedBy.id=:userId " +
        "and model.isLineManagerApproved = true and model.isRejected <> true " +
        "and (" +
        "lower(model.employee.fullName) like concat('%', :searchText, '%') or model.employee.pin like concat('%', :searchText, '%') " +
        ")"
    )
    Page<LeaveApplication> findLeaveApprovedByUserIdWithFilter(Pageable pageable, long userId, String searchText);

    @Query(
        value = "select case when count(model) > 0 then true else false end from LeaveApplication model " +
        "where model.startDate = :startDate and model.endDate = :endDate " +
        "and ( model.isHRApproved = true or model.isLineManagerApproved = true )" +
        "and model.employee.id = :employeeId"
    )
    boolean hasAnyApprovedLeaveApplicationFromStartDateToEndDate(LocalDate startDate, LocalDate endDate, long employeeId);
}
