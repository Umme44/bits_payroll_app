package com.bits.hr.repository;

import com.bits.hr.domain.Holidays;
import com.bits.hr.domain.enumeration.HolidayType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Holidays entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HolidaysRepository extends JpaRepository<Holidays, Long> {
    @Query(
        value = " select model " +
        " from Holidays model " +
        " where model.description=:description " +
        "   AND model.holidayType=:holidayType " +
        "   AND model.startDate=:startDate " +
        "   AND model.endDate=:endDate"
    )
    Optional<Holidays> findDuplicate(LocalDate startDate, LocalDate endDate, String description, HolidayType holidayType);

    @Query(
        value = "select model from Holidays model " +
        " where (" +
        "   ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "   ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "   ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "   ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "   ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        ")"
    )
    List<Holidays> findHolidaysStartDateBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query(
        value = " select model FROM Holidays model  " +
        "   where (" +
        "           ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "           ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "           ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "           ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "           ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "         )   " +
        " order by model.startDate "
    )
    List<Holidays> findAllHolidaysBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query(value = "select model from Holidays model where model.startDate=:date and model.isMoonDependent=false")
    List<Holidays> findNonMoonDependentHolidaysByStartDate(LocalDate date);
}
