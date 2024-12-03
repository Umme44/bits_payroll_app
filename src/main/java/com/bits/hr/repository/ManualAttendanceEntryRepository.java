package com.bits.hr.repository;

import com.bits.hr.domain.ManualAttendanceEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ManualAttendanceEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManualAttendanceEntryRepository
    extends JpaRepository<ManualAttendanceEntry, Long>, JpaSpecificationExecutor<ManualAttendanceEntry> {
    @Query(value = "select model from ManualAttendanceEntry model where model.employee.id=:employeeId order by model.date desc")
    Page<ManualAttendanceEntry> findAllByEmployeeId(Pageable pageable, long employeeId);

    // caution: jpa does not take empty list
    @Query(
        value = "select model from ManualAttendanceEntry model " +
        " where model.id IN :ids " +
        " AND (model.isLineManagerApproved<>true or model.isHRApproved<>true)"
    )
    List<ManualAttendanceEntry> getUnapprovedAttendancesByIds(List<Long> ids);

    @Query(
        value = "select model from ManualAttendanceEntry model " +
        " where model.isLineManagerApproved = false and model.isHRApproved = false and model.isRejected = false "
    )
    List<ManualAttendanceEntry> getAllPendingAttendances();

    @Query(
        value = "select model from ManualAttendanceEntry model " +
        " where model.isLineManagerApproved = false " +
        "and model.isHRApproved = false and model.isRejected = false " +
        "and model.employee.id=:employeeId"
    )
    List<ManualAttendanceEntry> getAllPendingAttendancesByEmployeeId(long employeeId);

    @Query(
        value = "select model from ManualAttendanceEntry model " +
        " where model.isLineManagerApproved = false and model.isHRApproved = false and model.isRejected = false  " +
        " and model.employee.reportingTo.id = :employeeId "
    )
    List<ManualAttendanceEntry> getAllPendingAttendancesLM(long employeeId);

    @Query(value = "select model from ManualAttendanceEntry model " + "where model.employee.id=:employeeId " + "and model.date=:date")
    Optional<ManualAttendanceEntry> getExistingAttendanceEntry(long employeeId, LocalDate date);

    @Query(
        "select count(model) from ManualAttendanceEntry model " +
        "where model.isLineManagerApproved = false AND model.isHRApproved = false AND model.isRejected = false"
    )
    Integer getTotalPendingAttendancesHR();

    @Query(
        "select count(model) from ManualAttendanceEntry model " +
        "where model.isLineManagerApproved = false AND model.isHRApproved = false AND model.isRejected = false " +
        "AND model.employee.reportingTo.id=:employeeId"
    )
    Integer getTotalPendingAttendancesLM(long employeeId);

    @Query(
        value = "select model from ManualAttendanceEntry model " +
        "where model.employee.pin=:pin " +
        "and model.date between :startDate and :endDate " +
        "order by model.date desc "
    )
    List<ManualAttendanceEntry> findAllByPinAndDateRange(String pin, LocalDate startDate, LocalDate endDate);

    @Query(value = "select model from ManualAttendanceEntry model where model.employee.pin=:pin and model.date=:date ")
    List<ManualAttendanceEntry> findAlByPinAndDate(String pin, LocalDate date);

    @Query(
        value = "select model from ManualAttendanceEntry model " +
        "where model.isRejected = false " +
        "and model.employee.pin=:pin and model.date=:date "
    )
    List<ManualAttendanceEntry> findAllManualAttendanceEntriesByPinAndDate(String pin, LocalDate date);

    @Query(
        value = "select model from ManualAttendanceEntry model where model.employee.id=:employeeId " +
        "and model.date between :startDate and :endDate " +
        "and model.isLineManagerApproved = false AND model.isHRApproved = false AND model.isRejected = false "
    )
    List<ManualAttendanceEntry> findAllPendingByEmployeeIdAndDateRange(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "select model from ManualAttendanceEntry model " +
        "where model.employee.id = :employeeId and model.date = :date " +
        "and model.isHRApproved = false and model.isLineManagerApproved = false and model.isRejected = false "
    )
    List<ManualAttendanceEntry> findAllPendingByEmployeeIdAndDate(long employeeId, LocalDate date);

    @Query(value = "select model from ManualAttendanceEntry model where model.employee.id = :employeeId order by model.date desc ")
    Page<ManualAttendanceEntry> findAllByEmployeeId(long employeeId, Pageable pageable);

    @Query(
        value = "select case when count(model) > 0 then true else false end from ManualAttendanceEntry model where model.employee.id=:employeeId " +
        "and model.date =:date"
    )
    Boolean findDuplicateEntryForDate(long employeeId, LocalDate date);

    @Query(value = "select model from ManualAttendanceEntry model where model.employee.id=:employeeId " + "and model.date =:date")
    Optional<ManualAttendanceEntry> findDuplicateEntryForDateV2(long employeeId, LocalDate date);

    Page<ManualAttendanceEntry> findAll(Pageable pageable);
}
