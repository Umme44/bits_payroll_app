package com.bits.hr.web.rest.salaryGeneration;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.salaryGeneration.SalaryVisibilityControlServiceImpl;
import com.bits.hr.web.rest.SalaryGeneratorMasterResource;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payroll-mgt")
public class SalaryVisibilityControlResource {

    private final Logger log = LoggerFactory.getLogger(SalaryGeneratorMasterResource.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryVisibilityControlServiceImpl salaryVisibilityControlServiceImpl;

    @GetMapping("/salary-generator-masters/make-salary-visible/{year}/{month}")
    public Boolean makeSalaryVisible(@PathVariable int year, @PathVariable int month) {
        log.debug("REST request to make Salary visible to employee");
        return salaryVisibilityControlServiceImpl.makeSalaryVisibleToEmployee(year, month);
    }

    @GetMapping("/salary-generator-masters/make-salary-hidden/{year}/{month}")
    public Boolean makeSalaryHide(@PathVariable int year, @PathVariable int month) {
        log.debug("REST request to make Salary hidden to employee");
        return salaryVisibilityControlServiceImpl.makeSalaryHideFromEmployee(year, month);
    }

    @GetMapping("/employee-salaries/make-employee-salary-visible/{id}/{year}/{month}")
    public Boolean makeEmployeeSalaryVisible(@PathVariable long id, @PathVariable int year, @PathVariable Month month) {
        log.debug("REST request to make Employee Salary visible to employee");

        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "Employee", "idnull");
        }

        return salaryVisibilityControlServiceImpl.makeEmployeeSalaryVisibleToEmployee(employeeOptional.get(), year, month);
    }

    @GetMapping("/employee-salaries/make-employee-salary-hidden/{id}/{year}/{month}")
    public Boolean makeEmployeeSalaryHide(@PathVariable long id, @PathVariable int year, @PathVariable Month month) {
        log.debug("REST request to make Employee Salary hidden to employee");

        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "Employee", "idnull");
        }

        return salaryVisibilityControlServiceImpl.makeEmployeeSalaryHideFromEmployee(employeeOptional.get(), year, month);
    }
}
