package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Employee}.
 */
public interface EmployeeService {
    EmployeeDTO create(EmployeeDTO employeeDTO);

    EmployeeDTO update(EmployeeDTO employeeDTO);

    Page<EmployeeDTO> findAll(Pageable pageable);

    Optional<EmployeeDTO> findOne(Long id);

    EmployeeMinimalDTO findEmployeeMinimalById(Long id);

    void delete(Long id);

    Optional<Employee> findEmployeeByPin(String pin);

    Optional<Employee> findEmployeeById(Long id);

    public Employee convertToEntity(EmployeeDTO employeeDTO);

    /**
     *  "direct reporting" refers only to the immediate, one-to-one relationship
     *  between a manager and their direct reports. Direct reports are the
     *  employees who report directly to a particular manager or supervisor,
     *  without any intermediaries.
     * */
    List<Employee> getDirectReportingTo(Employee employee);

    /**
     * "All reporting" encompasses all levels of the organizational hierarchy,
     * including both direct and indirect reporting relationships.
     * Indirect reports are employees who report to a manager who is not their
     * immediate supervisor.
     * */
    List<Employee> getAllReportingTo(Employee selectedEmployee);

    long getTotalWorkingDays(Employee employee);

    DateRangeDTO getServiceTenureDateRange(Employee employee);

    boolean isActiveInSelectedDate(Employee employee, LocalDate date);

    EmployeeDTO findDeptHeadByDeptId(Long departmentId);

    void createJoiningEntry(EmployeeDTO employeeDTO);

    EmployeeDashboardAnalyticsDTO getEmployeeDashboardAnalytics(LocalDate selectedDate);

    boolean fixMultipleJoiningEntry();
}
