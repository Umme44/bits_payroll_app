package com.bits.hr.repository;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.enumeration.EventType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmploymentHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmploymentHistoryRepository extends JpaRepository<EmploymentHistory, Long> {
    List<EmploymentHistory> findAllByEmployeeAndEffectiveDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);

    Optional<EmploymentHistory> findOneByEffectiveDateAndEmployeeAndEventType(
        LocalDate effectiveDate,
        Employee employee,
        EventType eventType
    );

    @Query(value = "Select model from EmploymentHistory model where model.eventType= :eventType")
    Page<EmploymentHistory> findAll(EventType eventType, Pageable pageable);

    @Query(
        value = "select model from EmploymentHistory model " +
        "               where model.eventType= :eventType " +
        "                   and (:employeeId is null or model.employee.id = :employeeId) " +
        "                   and (" +
        "                           ( cast(:startDate as date) is null or cast(:endDate as date) is null)" +
        "                             or (model.effectiveDate between :startDate and :endDate)" +
        "                           )" +
        ""
    )
    Page<EmploymentHistory> findAll(EventType eventType, Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(
        value = "Select model " +
        "from EmploymentHistory model " +
        "where model.employee.pin=:pin " +
        "and model.effectiveDate >=:startDate " +
        "and model.effectiveDate<=:endDate " +
        "and model.eventType= :eventType"
    )
    List<EmploymentHistory> getEmploymentHistoryByEmployeePinBetweenTwoDates(
        String pin,
        LocalDate startDate,
        LocalDate endDate,
        EventType eventType
    );

    @Query(value = "Select model " + "from EmploymentHistory model " + "where model.employee.pin=:pin " + "and model.eventType= :eventType")
    List<EmploymentHistory> getEmploymentHistoryByEmployeePin(String pin, EventType eventType);

    @Query(
        value = "Select model " +
        "from EmploymentHistory model " +
        "where model.effectiveDate >=:startDate " +
        "and model.effectiveDate<=:endDate " +
        "and model.eventType= :eventType"
    )
    List<EmploymentHistory> getEmploymentHistoryBetweenTwoDates(LocalDate startDate, LocalDate endDate, EventType eventType);

    @Query(
        value = " Select model " +
        "   from EmploymentHistory model " +
        "   where model.employee.id=:employeeId " +
        "   order by model.effectiveDate desc "
    )
    List<EmploymentHistory> getEmploymentHistoriesByEmployeeId(Long employeeId);

    @Query(
        value = " Select model " +
        "   from EmploymentHistory model " +
        "   where model.employee.id=:employeeId " +
        "   and ( model.eventType='PROMOTION' OR model.eventType='INCREMENT') " +
        "   and model.effectiveDate = " +
        "       (   select MIN(model.effectiveDate) from EmploymentHistory model " +
        "           where model.employee.id=:employeeId " +
        "           and ( model.eventType='PROMOTION' OR model.eventType='INCREMENT')) "
    )
    List<EmploymentHistory> getFirstIncrementOrPromotion(Long employeeId);

    @Query(
        value = "Select model " +
        " from EmploymentHistory model " +
        " where model.employee = :employee " +
        " and model.effectiveDate = :effectiveDate " +
        " and model.eventType= :eventType"
    )
    List<EmploymentHistory> findDuplicates(Employee employee, LocalDate effectiveDate, EventType eventType);

    @Query(
        value = " Select model " +
        "   from EmploymentHistory model " +
        "   where model.employee.id=:employeeId " +
        "   and ( model.eventType='PROMOTION' OR model.eventType='INCREMENT') " +
        "   and model.effectiveDate <= :date order by model.effectiveDate desc "
    )
    Page<EmploymentHistory> getLastEffectiveSalaryChange(Long employeeId, LocalDate date, Pageable pageable);

    @Query(
        value = " Select model " +
        "   from EmploymentHistory model " +
        "   where model.employee.id=:employeeId " +
        "   and ( model.eventType='PROMOTION' OR model.eventType='INCREMENT') " +
        "   order by model.effectiveDate asc "
    )
    Page<EmploymentHistory> getFirstEffectiveSalaryChange(Long employeeId, Pageable pageable);

    @Query(
        value = " select count(model.id) " +
        " from EmploymentHistory model " +
        " where model.employee.id=:employeeId " +
        " and ( model.eventType='PROMOTION' OR model.eventType='INCREMENT') "
    )
    long getNumberOfPromotionOrIncrementByEmployeeId(long employeeId);

    Optional<EmploymentHistory> findByIdAndEventType(Long id, EventType eventType);

    @Query(
        value = " select model " +
        " from EmploymentHistory model " +
        " where model.employee.id=:employeeId " +
        " and model.eventType = :eventType "
    )
    List<EmploymentHistory> getAllByEmployeeIdAndEventType(long employeeId, EventType eventType);
}
