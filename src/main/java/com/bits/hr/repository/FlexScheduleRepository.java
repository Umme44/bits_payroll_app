package com.bits.hr.repository;

import com.bits.hr.domain.FlexSchedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FlexSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlexScheduleRepository extends JpaRepository<FlexSchedule, Long> {
    @Query("select flexSchedule from FlexSchedule flexSchedule where flexSchedule.createdBy.login = ?#{principal.username}")
    List<FlexSchedule> findByCreatedByIsCurrentUser();

    @Query(value = "select flexSchedule from FlexSchedule flexSchedule " + "where flexSchedule.employee.id = :employeeId ")
    List<FlexSchedule> findAllByEmployeeId(long employeeId);

    @Query(
        value = "select flexSchedule from FlexSchedule flexSchedule " +
        "where flexSchedule.employee.id = :employeeId and flexSchedule.effectiveDate = :effectiveDate"
    )
    List<FlexSchedule> findByEmployeeAndEffectiveDate(long employeeId, LocalDate effectiveDate);

    @Query(
        value = "select flexSchedule from FlexSchedule flexSchedule " +
        "where flexSchedule.employee.id = :employeeId " +
        "AND flexSchedule.effectiveDate <= :date ORDER BY flexSchedule.effectiveDate desc "
    )
    Page<FlexSchedule> findEffectiveFlexSchedule(long employeeId, LocalDate date, Pageable pageable);

    @Query(
        value = "select  model from FlexSchedule model where (model.employee.id=:employeeId or :employeeId is null )" +
        "and ((model.effectiveDate>=:startEffectiveDate and model.effectiveDate<=:endEffectiveDate) " +
        "or ( cast(:startEffectiveDate as date) is null OR cast(:endEffectiveDate as date) is null))"
    )
    List<FlexSchedule> getFlexScheduleByEffectiveDates(Long employeeId, LocalDate startEffectiveDate, LocalDate endEffectiveDate);

    @Query(value = "select model from FlexSchedule model order by model.employee.pin")
    List<FlexSchedule> findAllByEmployeePin();
}
