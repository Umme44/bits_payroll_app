package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeSalaryService;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.EmployeeSalaryPayslipDto;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.service.selecteable.SelectableDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/payroll-mgt")
public class SalaryPayslipResource {

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMinimalMapper employeeMinimalMapper;

    // Get payslip
    @GetMapping("/payslip/{id}/{year}/{month}")
    public ResponseEntity<EmployeeSalaryPayslipDto> getPayslipForMonthYear(
        @PathVariable("id") long id,
        @PathVariable("year") int year,
        @PathVariable("month") int month
    ) {
        log.debug("REST Request to get salary Payslip ");
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        EmployeeSalaryPayslipDto result = employeeSalaryService.getSalaryPayslipByEmployeeAndYearAndMonth(
            employeeOptional.get(),
            year,
            month
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/my-salary-year-list/{id}")
    public ResponseEntity<HashSet<Integer>> getMyDisbursedSalaryYearList(@PathVariable long id) {
        log.debug("REST Request to get generated salary years ");
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        return ResponseEntity.ok(employeeSalaryService.getSelectableYearsByEmployee(employeeOptional.get()));
    }

    @GetMapping("/my-salary-month-list/{id}/{year}")
    public ResponseEntity<List<SelectableDTO>> getMyDisbursedSalaryYearWiseMonthList(@PathVariable long id, @PathVariable int year) {
        log.debug("REST Request to get generated months of Year: " + year);
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        return ResponseEntity.ok(employeeSalaryService.getAllSelectableListByEmployeeAndYear(employeeOptional.get(), year));
    }

    @GetMapping("/payslip/eligible-employee")
    public ResponseEntity<List<EmployeeMinimalDTO>> getAllEligibleEmployeeForSalaryPayslip() {
        log.debug("REST Request to get list of eligible employee for salary payslip");

        List<EmployeeMinimalDTO> employeeMinimalDTOS = employeeSalaryService.getAllEmployeesExceptInterns();
        return ResponseEntity.ok(employeeMinimalDTOS);
    }
}
