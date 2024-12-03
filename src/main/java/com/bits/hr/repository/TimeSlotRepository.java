package com.bits.hr.repository;

import com.bits.hr.domain.CustomTimeSlotDTO;
import com.bits.hr.domain.TimeSlot;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TimeSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    @Query("select model from TimeSlot model order by model.inTime")
    List<TimeSlot> findAll();

    @Query(
        "select case when count(model) > 0 then true else false end from TimeSlot model where concat('%',lower(model.title) ,'%') like concat('%',lower(:title) ,'%')"
    )
    Boolean checkTitleIsExists(String title);

    @Query("select timeSlot from TimeSlot timeSlot where timeSlot.isApplicableByEmployee = true order by timeSlot.inTime asc")
    List<TimeSlot> getTimeSlotsByIsApplicableByEmployeeAndOrderByInTimeAsc();

    @Query("select timeSlot from TimeSlot timeSlot order by timeSlot.inTime asc")
    List<TimeSlot> getAllTimeSlotOrderByInTimeAsc();

    @Query("select timeSlot from TimeSlot timeSlot where timeSlot.inTime=:inTime and timeSlot.outTime=:outTime")
    List<TimeSlot> getTimeSlotByInTimeAndOutTime(Instant inTime, Instant outTime);

    @Query(
        value = "select  time_slot.id as timeSLotId, " +
        "flex_schedule.id as flexScheduleId, " +
        "time_slot.title as timeSlotTitle, " +
        "flex_schedule.effective_date as effectiveDate, " +
        "flex_schedule.in_time as inTime," +
        "flex_schedule.out_time as outTime, " +
        "flex_schedule.employee_id as employeeId " +
        "from time_slot,flex_schedule " +
        "where to_char(flex_schedule.in_time, 'HH:MI:SS') = to_char(time_slot.in_time, 'HH:MI:SS') " +
        "and to_char(flex_schedule.out_time, 'HH:MI:SS') = to_char(time_slot.out_time, 'HH:MI:SS')",
        nativeQuery = true
    )
    List<CustomTimeSlotDTO> getFlexScheduleAndTimeSlot();

    @Query("select model from TimeSlot model where model.isDefaultShift = true")
    Optional<TimeSlot> findDefaultTimeSlot();

    @Query("select model from TimeSlot model where lower(model.code) like lower(:code) order by model.id desc ")
    Optional<TimeSlot> findByCode(String code);

    Optional<TimeSlot> findByTitle(String title);
}
