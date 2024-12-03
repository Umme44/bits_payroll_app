package com.bits.hr.repository;

import com.bits.hr.domain.AttendanceEntry;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AttendanceEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttendanceEntryRepository extends JpaRepository<AttendanceEntry, Long> {
    @Query(
        value = " select model " +
        "from AttendanceEntry model " +
        "where model.employee.pin=:pin and " +
        "model.date>=:startDate and model.date<=:endDate"
    )
    List<AttendanceEntry> getAttendanceEntryByEmployeePinAndBetweenTwoDates(String pin, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " select model " +
        "from AttendanceEntry model " +
        "where model.employee.id=:employeeId and " +
        "model.date>=:startDate and model.date<=:endDate"
    )
    List<AttendanceEntry> getAttendanceEntryListByEmployeeIdAndBetweenTwoDates(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " select model.date " +
        "from AttendanceEntry model " +
        "where model.employee.id=:employeeId and " +
        "model.date>=:startDate and model.date<=:endDate "
    )
    HashSet<LocalDate> getAttendanceEntryByEmployeeIdAndBetweenTwoDates(long employeeId, LocalDate startDate, LocalDate endDate);

    Optional<AttendanceEntry> findByEmployeeIdAndDate(Long employee_id, LocalDate date);

    @Query(value = "select model from AttendanceEntry model " + "where model.employee.id=:id and model.date=:date")
    Optional<AttendanceEntry> findAttendanceEntryByDateAndEmployee(LocalDate date, long id);

    @Query(
        value = " select model from AttendanceEntry model " +
        " WHERE (:pin = '' OR model.employee.pin=:pin) " +
        " AND ( cast(:startDate as date) IS null OR model.date >=:startDate ) " +
        " AND ( cast(:endDate as date) IS null OR model.date <=:endDate ) "
    )
    Page<AttendanceEntry> findAll(String pin, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value = "select model from AttendanceEntry model " + "where model.date=:date and model.outTime is null ")
    List<AttendanceEntry> getAllNotCompiledAttendances(LocalDate date);
}
