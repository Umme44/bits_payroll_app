package com.bits.hr.repository;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FlexScheduleApplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlexScheduleApplicationRepository extends JpaRepository<FlexScheduleApplication, Long> {
    @Query(
        "select flexScheduleApplication from FlexScheduleApplication flexScheduleApplication where flexScheduleApplication.sanctionedBy.login = ?#{principal.username}"
    )
    List<FlexScheduleApplication> findBySanctionedByIsCurrentUser();

    @Query(
        "select flexScheduleApplication from FlexScheduleApplication flexScheduleApplication where flexScheduleApplication.appliedBy.login = ?#{principal.username}"
    )
    List<FlexScheduleApplication> findByAppliedByIsCurrentUser();

    @Query(
        "select flexScheduleApplication from FlexScheduleApplication flexScheduleApplication where flexScheduleApplication.createdBy.login = ?#{principal.username}"
    )
    List<FlexScheduleApplication> findByCreatedByIsCurrentUser();

    @Query(
        "select flexScheduleApplication from FlexScheduleApplication flexScheduleApplication where flexScheduleApplication.updatedBy.login = ?#{principal.username}"
    )
    List<FlexScheduleApplication> findByUpdatedByIsCurrentUser();

    @Query(
        value = "select model from FlexScheduleApplication model " +
        "where model.requester.id = :requesterId " +
        "  and model.status <> 'NOT_APPROVED' " +
        "  and((:effectiveFrom between model.effectiveFrom and model.effectiveTo) " +
        "     or (:effectiveTo between model.effectiveFrom and model.effectiveTo))"
    )
    List<FlexScheduleApplication> findFlexScheduleApplicationBetweenEffectiveDate(
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        long requesterId
    );

    @Query(
        value = "select model from FlexScheduleApplication model " +
        "where model.requester.id = :requesterId" +
        "  and model.status = 'APPROVED' " +
        "  and((cast(:effectiveFrom as date) between model.effectiveFrom and model.effectiveTo) " +
        "     or (cast(:effectiveTo as date) between model.effectiveFrom and model.effectiveTo))"
    )
    List<FlexScheduleApplication> findApprovedApplicationsBetweenEffectiveDate(
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        long requesterId
    );

    // find which application will be ended after next 07 days
    // except already applied for new application
    @Query(
        value = "select model from FlexScheduleApplication model " +
        "where model.status = 'APPROVED' " +
        " and model.effectiveTo = :effectiveTo " +
        " and model.requester.id not in " +
        " ( select model.requester.id from FlexScheduleApplication model " +
        "where (model.status = 'APPROVED' or model.status = 'PENDING') " +
        " and model.effectiveFrom > :effectiveTo )"
    )
    List<FlexScheduleApplication> findAllForSendingEmailReminder(LocalDate effectiveTo);

    @Query(
        value = "select model from FlexScheduleApplication model " +
        "where model.status = 'APPROVED' " +
        "  and (:date between model.effectiveFrom and model.effectiveTo)" +
        "  and model.requester.id = :requesterId"
    )
    Optional<FlexScheduleApplication> findAllApprovedByEmployeeIdAndDate(long requesterId, LocalDate date);

    @Query("select model from FlexScheduleApplication model where model.status = :status")
    List<FlexScheduleApplication> findAllByStatus(Status status);

    @Query(
        "select model from FlexScheduleApplication model " +
        "where model.status = 'PENDING' " +
        "   and model.requester.reportingTo.id=:reportingToId"
    )
    List<FlexScheduleApplication> findAllPendingOfLM(long reportingToId);

    @Query(
        "select model from FlexScheduleApplication model " +
        "where (:requesterId is null or model.requester.id = :requesterId) " +
        "  and model.status = 'APPROVED' " +
        "  and model.sanctionedBy = :sanctionedBy"
    )
    Page<FlexScheduleApplication> findAllApprovedByUser(Pageable pageable, Long requesterId, User sanctionedBy);

    @Query(
        "select distinct model.requester from FlexScheduleApplication model " +
        "where model.status = 'APPROVED' " +
        "  and model.sanctionedBy.id = :sanctionedBy"
    )
    List<Employee> findAllEmployeeOfFlexScheduleApprovedByUser(long sanctionedBy);

    @Query("select model from FlexScheduleApplication model where model.requester.id = :requesterId")
    Page<FlexScheduleApplication> findAllByRequesterId(Pageable pageable, long requesterId);

    @Query(
        value = "select model from FlexScheduleApplication model " +
        "where model.status <> 'NOT_APPROVED' " +
        "  and model.requester.id = :requesterId "
    )
    List<FlexScheduleApplication> findAllActiveByRequesterId(long requesterId);

    @Query("select model from FlexScheduleApplication model where model.requester.id = :requesterId")
    List<FlexScheduleApplication> findAllByRequesterId(long requesterId);

    @Query("select count(model) from FlexScheduleApplication model " + "       where model.status = 'PENDING'")
    int getCountingOfAllPending();

    @Query(
        "select count(model) from FlexScheduleApplication model " +
        "       where model.status = 'PENDING' " +
        "           and model.requester.reportingTo.id=:reportingToId"
    )
    int getCountingOfPendingByReportingTo(long reportingToId);

    @Query(
        value = "Select model from FlexScheduleApplication model" +
        " where (:employeeId is null or model.requester.id=:employeeId) " +
        " and (:status is null or model.status=:status) " +
        " and (model.timeSlot.id in :timeSlotIdList) " +
        " and ( " +
        "  (cast(:startDate as date) is null and cast(:endDate as date) is null) or " +
        "  ( model.effectiveFrom >= :startDate and model.effectiveTo <=:endDate ) " +
        ") order by model.requester.pin"
    )
    Page<FlexScheduleApplication> findFlexApplicationBetweenDates(
        Long employeeId,
        List<Long> timeSlotIdList,
        LocalDate startDate,
        LocalDate endDate,
        Status status,
        Pageable pageable
    );

    @Query(
        value = "Select model from FlexScheduleApplication model" +
        " where (:employeeId is null or model.requester.id=:employeeId) " +
        " and (:status is null or model.status=:status) " +
        " and (model.timeSlot.id in :timeSlotIdList) " +
        " and ( " +
        "  (cast(:startDate as date) is null and cast(:endDate as date) is null) or " +
        "  ( model.effectiveFrom >= :startDate and model.effectiveTo <=:endDate ) " +
        ") order by model.requester.pin"
    )
    List<FlexScheduleApplication> findFlexApplicationBetweenDatesReport(
        Long employeeId,
        List<Long> timeSlotIdList,
        LocalDate startDate,
        LocalDate endDate,
        Status status
    );
}
