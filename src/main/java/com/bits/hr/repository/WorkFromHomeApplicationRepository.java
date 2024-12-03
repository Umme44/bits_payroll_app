package com.bits.hr.repository;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.domain.enumeration.Status;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the WorkFromHomeApplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkFromHomeApplicationRepository extends JpaRepository<WorkFromHomeApplication, Long> {
    @Query(
        "select workFromHomeApplication from WorkFromHomeApplication workFromHomeApplication where workFromHomeApplication.appliedBy.login = ?#{principal.username}"
    )
    List<WorkFromHomeApplication> findByAppliedByIsCurrentUser();

    @Query(
        "select workFromHomeApplication from WorkFromHomeApplication workFromHomeApplication where workFromHomeApplication.createdBy.login = ?#{principal.username}"
    )
    List<WorkFromHomeApplication> findByCreatedByIsCurrentUser();

    @Query(
        "select workFromHomeApplication from WorkFromHomeApplication workFromHomeApplication where workFromHomeApplication.updatedBy.login = ?#{principal.username}"
    )
    List<WorkFromHomeApplication> findByUpdatedByIsCurrentUser();

    @Query(
        "select workFromHomeApplication from WorkFromHomeApplication workFromHomeApplication where workFromHomeApplication.sanctionedBy.login = ?#{principal.username}"
    )
    List<WorkFromHomeApplication> findBySanctionedByIsCurrentUser();

    @Query(
        "select case when count(model) > 0 then true else false end " +
        "from WorkFromHomeApplication model " +
        "where model.employee.id=:employeeId and  model.id<>:workApplicationId " +
        "and ( " +
        "(model.startDate<=:bookingStartDate and model.endDate>=:bookingStartDate) " +
        "or (model.startDate<=:bookingEndDate and model.endDate>=:bookingEndDate) " +
        "or (model.startDate BETWEEN :bookingStartDate and :bookingEndDate) " +
        "or ( model.endDate   BETWEEN :bookingStartDate and :bookingEndDate ) " +
        "or( model.startDate  <= :bookingStartDate and model.endDate >=:bookingEndDate )" +
        ") "
    )
    boolean checkIsAppliedForUpdate(Long employeeId, Long workApplicationId, LocalDate bookingStartDate, LocalDate bookingEndDate);

    @Query(
        value = "SELECT CASE WHEN COUNT(model.ID) > 0 THEN TRUE ELSE FALSE END\n" +
        "FROM WORK_FROM_HOME_APPLICATION AS model\n" +
        "WHERE model.EMPLOYEE_ID = :employeeId\n" +
        "  AND (model.STATUS = 'APPROVED' OR model.STATUS = 'PENDING')\n" +
        "  AND ((model.START_DATE <= :bookingStartDate\n" +
        "    AND model.END_DATE >= :bookingStartDate)\n" +
        "    OR (model.START_DATE <= :bookingEndDate\n" +
        "        AND model.END_DATE >= :bookingEndDate)\n" +
        "    OR (model.START_DATE\n" +
        "        BETWEEN :bookingStartDate AND :bookingEndDate)\n" +
        "    OR (model.END_DATE\n" +
        "        BETWEEN :bookingStartDate AND :bookingEndDate)\n" +
        "    OR (model.START_DATE <= :bookingStartDate\n" +
        "        AND model.END_DATE >= :bookingEndDate))\n",
        nativeQuery = true
    )
    boolean checkIsAppliedV2(Long employeeId, LocalDate bookingStartDate, LocalDate bookingEndDate);

    @Query(
        value = "select model from WorkFromHomeApplication model " +
        "where model.status = 'PENDING' " +
        "  and model.employee.reportingTo.id = :employeeId " +
        "  and (lower(model.employee.fullName) like lower(concat('%', :searchText, '%')) or " +
        "       lower(model.employee.pin) like lower(concat('%', :searchText, '%')))"
    )
    Page<WorkFromHomeApplication> findPendingApplicationsByLineManagerId(long employeeId, String searchText, Pageable pageable);

    @Query(
        "select model from WorkFromHomeApplication model where model.status='PENDING' and " +
        "(lower(model.employee.pin) like lower(concat('%', :searchText, '%')) or lower(model.employee.fullName) like lower(concat('%', :searchText, '%')))"
    )
    Page<WorkFromHomeApplication> findAllPendingApplications(String searchText, Pageable pageable);

    @Query("select model from WorkFromHomeApplication model where model.employee.id=:employeeId")
    Page<WorkFromHomeApplication> findAllAppliedApplicationsByEmployeeId(long employeeId, Pageable pageable);

    @Query(
        "select model from WorkFromHomeApplication model " +
        "where model.endDate=:endDate and model.status='APPROVED' " +
        "and model.employee.id not in " +
        "   (select model.employee from WorkFromHomeApplication model " +
        "where model.startDate>:endDate " +
        "and (model.status='APPROVED' or model.status='PENDING'))"
    )
    List<WorkFromHomeApplication> findApplicationsForReminder(LocalDate endDate);

    @Query("select model from WorkFromHomeApplication model where model.appliedAt<=:selectedDate and model.status='PENDING'")
    List<WorkFromHomeApplication> findApplicationPendingForCertainDays(LocalDate selectedDate);

    @Query(
        "select model.employee from WorkFromHomeApplication model " +
        "where model.startDate<=:today and model.endDate>=:today " +
        "and model.status='APPROVED'"
    )
    List<Employee> findActiveWHFEmployee(LocalDate today);

    @Query(
        "select model from WorkFromHomeApplication model " +
        " where model.employee.id=:employeeId " +
        " and model.startDate<=:effectiveDate and model.endDate>=:effectiveDate " +
        " and model.status='APPROVED' "
    )
    List<WorkFromHomeApplication> findApprovedWFH(LocalDate effectiveDate, Long employeeId);

    @Query(
        "select model from WorkFromHomeApplication model where model.employee.id=:employeeId and (model.startDate<=:today and model.endDate>=:today) and model.status='APPROVED'"
    )
    List<WorkFromHomeApplication> findEligibleWFHApplicationAfterApprovedOrRejectEvent(long employeeId, LocalDate today);

    @Query("select count(model) from WorkFromHomeApplication model where model.status= 'PENDING'")
    int getTotalPendingWFHApplicationsHR();

    @Query(
        "select count(model) from WorkFromHomeApplication model where model.employee.reportingTo.id=:employeeId and model.status= 'PENDING'"
    )
    int getTotalPendingWFHApplicationsLM(long employeeId);

    @Query(
        value = "select model from WorkFromHomeApplication model " +
        "where model.status = 'PENDING' " +
        "  and model.employee.reportingTo.id = :employeeId "
    )
    List<WorkFromHomeApplication> findPendingApplicationsByRelieverId(long employeeId);

    @Query(
        value = "select model from WorkFromHomeApplication model " +
        "  where model.employee.id = :employeeId " +
        "  and model.status = 'APPROVED' " +
        "  and model.startDate>= :currentDate order by model.startDate"
    )
    List<WorkFromHomeApplication> findFutureApprovedWFHApplication(long employeeId, LocalDate currentDate, Pageable pageable);

    @Query(
        value = "select model " +
        " from WorkFromHomeApplication model " +
        " where (:employeeId is null or model.employee.id=:employeeId) " +
        " and (:status is null or model.status=:status) " +
        " and ( " +
        "  (cast(:startDate as date) is null and cast(:endDate as date) is null) or " +
        "  ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "  ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "  ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "  ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "  ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        ") "
    )
    Page<WorkFromHomeApplication> findWorkFromApplicationBetweenDates(
        Long employeeId,
        LocalDate startDate,
        LocalDate endDate,
        Status status,
        Pageable pageable
    );
}
