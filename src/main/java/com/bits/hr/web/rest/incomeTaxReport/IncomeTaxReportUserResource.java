package com.bits.hr.web.rest.incomeTaxReport;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.incomeTaxManagement.TaxReportGeneration.*;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.IncomeTaxDropDownMenuDto;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.IncomeTaxReportDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")
public class IncomeTaxReportUserResource {

    @Autowired
    private IncomeTaxReportService incomeTaxReportService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @GetMapping("/income-tax-report/{id}")
    public IncomeTaxReportDTO salaryGeneration(@PathVariable Long id) throws Exception {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException(
                "No employee profile is associated with you. Please contact HR",
                "IncomeTaxReportDto",
                "noEmployee"
            );
        }
        if (employeeOptional.get().getEmployeeCategory().equals(EmployeeCategory.INTERN)) {
            throw new RuntimeException("Income tax report does not generate for Interns.");
        }

        return incomeTaxReportService.generateTaxReport(currentEmployeeService.getCurrentEmployee().get(), id);
    }

    @GetMapping("/get-income-tax-year-list")
    public List<IncomeTaxDropDownMenuDto> getDropDownMenuYears() {
        return incomeTaxReportService.getAitConfigYears();
    }

    @GetMapping("/income-tax-statement/is-employee-eligible")
    public boolean isEmployeeEligibleForTaxStatement() {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();

        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("No employee profile is associated with you. Please contact HR", "Employee", "noEmployee");
        }
        if (employeeOptional.get().getEmployeeCategory().equals(EmployeeCategory.INTERN)) {
            return false;
        } else {
            return true;
        }
    }

    @GetMapping("/get-assessment-year-by-aitConfigId/{aitConfigId}")
    public IncomeTaxDropDownMenuDto getAssessmentYearByAitConfigId(@PathVariable Long aitConfigId) {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        return incomeTaxReportService.getAssessmentYearByAitConfigId(aitConfigId);
    }
}
