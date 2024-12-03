package com.bits.hr.repository;

import com.bits.hr.domain.SpecialShiftTiming;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RamadanSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialShiftTimingRepository extends JpaRepository<SpecialShiftTiming, Long> {
    @Query("select ramadanSchedule from SpecialShiftTiming ramadanSchedule where ramadanSchedule.createdBy.login = ?#{principal.username}")
    List<SpecialShiftTiming> findByCreatedByIsCurrentUser();

    @Query("select ramadanSchedule from SpecialShiftTiming ramadanSchedule where ramadanSchedule.updatedBy.login = ?#{principal.username}")
    List<SpecialShiftTiming> findByUpdatedByIsCurrentUser();

    @Query("select model from SpecialShiftTiming model " + " where :date between model.startDate and model.endDate")
    List<SpecialShiftTiming> findByStartAndEndDateBetween(LocalDate date);
}
