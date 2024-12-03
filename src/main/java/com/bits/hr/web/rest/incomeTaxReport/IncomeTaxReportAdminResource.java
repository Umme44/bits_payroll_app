package com.bits.hr.web.rest.incomeTaxReport;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.incomeTaxManagement.TaxReportGeneration.IncomeTaxReportService;
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
@RequestMapping("/api/payroll-mgt")
public class IncomeTaxReportAdminResource {

    @Autowired
    private IncomeTaxReportService incomeTaxReportService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/income-tax-report/{employeeId}/{aitConfigId}")
    public IncomeTaxReportDTO salaryGeneration(@PathVariable Long employeeId, @PathVariable Long aitConfigId) throws Exception {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "IncomeTaxReportDto", "idnull");
        }

        if (employeeOptional.get().getEmployeeCategory().equals(EmployeeCategory.INTERN)) {
            throw new RuntimeException("Income tax report does not generate for Interns.");
        }

        return incomeTaxReportService.generateTaxReport(employeeOptional.get(), aitConfigId);
    }

    @GetMapping("/income-tax-report/get-all-eligible-employees-within-year/{id}")
    public List<EmployeeMinimalDTO> getAllActiveEmployeeWithinYear(@PathVariable Long id) {
        return incomeTaxReportService.getAllActiveEmployeeListWithinTaxYear(id);
    }

    @GetMapping("/get-income-tax-year-list-admin")
    public List<IncomeTaxDropDownMenuDto> getDropDownMenuYears() {
        return incomeTaxReportService.getAitConfigYears();
    }
}
