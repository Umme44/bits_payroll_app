package com.bits.hr.web.rest.vm;

import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.SalaryGenerationPreValidationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.SalaryGenerationPreValidationDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payroll-mgt")
@Log4j2
public class SalaryGenerationPreValidationResource {

    @Autowired
    private SalaryGenerationPreValidationService salaryGenerationPreValidationService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    private final String resourceName = this.getClass().getSimpleName();

    @GetMapping("/salary-generator-pre-validation/{year}/{month}")
    public ResponseEntity<SalaryGenerationPreValidationDTO> getSalaryGenerationConstraint(@PathVariable int year, @PathVariable int month) {
        log.debug("REST request to check SalaryGenerator pre validation before generation, year: {}, month: {}", year, month);
        SalaryGenerationPreValidationDTO result = salaryGenerationPreValidationService.getSalaryGenerationPreValidations(year, month);

        eventLoggingPublisher.publishEvent(currentEmployeeService.getCurrentUser().get(), result, RequestMethod.GET, resourceName);
        return ResponseEntity.ok().body(result);
    }
}
