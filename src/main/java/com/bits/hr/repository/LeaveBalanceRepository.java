package com.bits.hr.repository;

import com.bits.hr.domain.LeaveBalance;
import com.bits.hr.domain.enumeration.LeaveType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LeaveBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    @Query(value = "Select model from LeaveBalance model where model.employee.pin=:pin AND model.year =:year")
    List<LeaveBalance> findAllByYearAndPin(Integer year, String pin);

    @Query(value = "Select model from LeaveBalance model where model.employee.id=:employeeId AND model.year =:year")
    List<LeaveBalance> findAllByYearAndEmployeeId(Integer year, long employeeId);

    @Query(
        value = "Select model from LeaveBalance model where model.employee.id=:employeeId AND model.year =:year AND model.leaveType=:leaveType"
    )
    Optional<LeaveBalance> findAllByYearAndEmployeeIdAndLeaveType(Integer year, long employeeId, LeaveType leaveType);

    boolean existsByEmployeeIdAndLeaveTypeAndYear(Long employeeId, LeaveType leaveType, Integer year);

    boolean existsByEmployeeIdAndLeaveType(Long employeeId, LeaveType leaveType);

    Optional<LeaveBalance> findByEmployeeIdAndLeaveTypeAndYear(Long employeeId, LeaveType leaveType, Integer year);

    @Query(
        value = " Select model " +
        " from LeaveBalance model " +
        " where model.employee.id=:employeeId " +
        "   AND model.leaveType =:leaveType "
    )
    List<LeaveBalance> findByEmployeeAndLeaveType(long employeeId, LeaveType leaveType);

    @Query(
        value = "select model from LeaveBalance model where (:leaveType is null OR model.leaveType=:leaveType) AND " +
        "(:year is null or model.year = :year) AND " +
        "(model.employee.pin like concat('%', :employeePin,'%'))"
    )
    Page<LeaveBalance> findAllByFiltering(Pageable pageable, String employeePin, LeaveType leaveType, Integer year);
}
