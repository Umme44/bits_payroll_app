package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.EmployeeSalaryPayslipDto;
import com.bits.hr.service.salaryGenerationFractional.Fraction;
import com.bits.hr.service.selecteable.SelectableDTO;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EmployeeSalary}.
 */
public interface EmployeeSalaryService {
    EmployeeSalaryDTO save(EmployeeSalaryDTO employeeSalaryDTO);
    EmployeeSalary save(EmployeeSalary employeeSalary);

    EmployeeSalaryDTO update(EmployeeSalaryDTO employeeSalaryDTO);
    EmployeeSalary update(EmployeeSalary employeeSalary);

    Page<EmployeeSalaryDTO> findAllByYearAndMonth(int year, int month, String searchText, Pageable pageable);

    @Transactional(readOnly = true)
    List<EmployeeSalaryDTO> findByEmployeeIdAndYearAndMonth(long employeeId, int year, int month);

    @Transactional(readOnly = true)
    List<EmployeeSalary> findByEmployeeAndYearAndMonth(long employeeId, int year, int month);

    @Transactional(readOnly = true)
    List<EmployeeSalary> findAllByYearAndMonth(int year, int month);

    Optional<EmployeeSalaryDTO> findOne(Long id);

    void delete(Long id);
    void delete(List<Long> idList);
    void deleteByYearAndMonth(int year, int month);

    EmployeeSalaryPayslipDto getSalaryPayslipByYearAndMonthForUser(Employee employee, int year, int month);
    EmployeeSalaryPayslipDto getSalaryPayslipByEmployeeAndYearAndMonth(Employee employee, int year, int month);

    HashSet<Integer> getSelectableYearsByEmployee(Employee employee);

    List<SelectableDTO> getAllSelectableListByEmployeeAndYear(Employee employee, int year);

    List<SelectableDTO> getVisibleSelectableListByEmployeeAndYear(Employee employee, int year);

    List<Fraction> getFractions(Long id);

    boolean changeHoldStatusTo(String holdStatus, EmployeeSalary employeeSalary);

    List<EmployeeMinimalDTO> getAllEmployeesExceptInterns();

    ExportXLPropertiesDTO prepareDataForSalaryExport(int startYear, int startMonth, int endYear, int endMonth);
}
