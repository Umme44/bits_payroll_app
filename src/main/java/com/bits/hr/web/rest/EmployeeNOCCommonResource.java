package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeNOC;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeNOCService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeNOCDTO;
import com.bits.hr.service.event.EmployeeNOCApplicationEvent;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.mapper.EmployeeNOCMapper;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/common")
public class EmployeeNOCCommonResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static final String RESOURCE_NAME = "EmployeeNOCCommonResource";
    private static final String ENTITY_NAME = "employeeNOC";
    private final Logger log = LoggerFactory.getLogger(EmployeeNOCResource.class);

    @Autowired
    private EmployeeNOCService employeeNOCService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private EmployeeNOCMapper employeeNOCMapper;

    @PostMapping("/employee-no-objection-certificates")
    public ResponseEntity<EmployeeNOCDTO> createEmployeeNOC(@Valid @RequestBody EmployeeNOCDTO employeeNOCDTO) throws URISyntaxException {
        log.debug("REST request to save EmployeeNOC : {}", employeeNOCDTO);
        if (employeeNOCDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeNOC cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        User user = currentEmployeeService.getCurrentUser().get();

        EmployeeNOC result = employeeNOCService.createEmployeeNOC(employeeNOCDTO, employeeOptional.get(), user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        publishEvent(result, EventType.CREATED);

        return ResponseEntity.ok().body(employeeNOCMapper.toDto(result));
    }

    @PutMapping("/employee-no-objection-certificates")
    public ResponseEntity<EmployeeNOCDTO> updateEmployeeNOC(@Valid @RequestBody EmployeeNOCDTO employeeNOCDTO) throws URISyntaxException {
        log.debug("REST request to update EmployeeNOC : {}", employeeNOCDTO);
        if (employeeNOCDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Employee employee = currentEmployeeService.getCurrentEmployee().get();
        User user = currentEmployeeService.getCurrentUser().get();

        EmployeeNOCDTO result = employeeNOCService.updateEmployeeNOC(employeeNOCDTO, employee, user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employee-no-objection-certificates")
    public ResponseEntity<List<EmployeeNOCDTO>> getAllEmployeeNOCS(
        @RequestParam(required = false) CertificateStatus status,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmployeeNOCS");

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        Page<EmployeeNOCDTO> page = employeeNOCService.findAllByEmployeeId(employeeOptional.get(), status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, page.getContent(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/employee-no-objection-certificates/{id}")
    public ResponseEntity<EmployeeNOCDTO> getEmployeeNOC(@PathVariable Long id) {
        log.debug("REST request to get EmployeeNOC : {}", id);
        Optional<EmployeeNOCDTO> employeeNOCDTO = employeeNOCService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeeNOCDTO, RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(employeeNOCDTO);
    }

    @DeleteMapping("/employee-no-objection-certificates/{id}")
    public ResponseEntity<Void> deleteEmployeeNOC(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeNOC : {}", id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        employeeNOCService.delete(id);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("employee-no-objection-certificates/has-any-approved-leave-application")
    public ResponseEntity<Boolean> hasAnyApprovedLeaveApplicationWithinDateRange(
        @RequestParam(required = true) LocalDate startDate,
        @RequestParam(required = true) LocalDate endDate
    ) {
        log.debug("REST request to check if theres any approved leave application EmployeeNOC : {}");
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        boolean result = employeeNOCService.hasAnyApprovedLeaveApplicationBetweenDateRange(startDate, endDate, employeeOptional.get());

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("employee-no-objection-certificates/calculate-leave-days")
    public ResponseEntity<Long> calculateLeaveDays(
        @RequestParam(required = true) LocalDate startDate,
        @RequestParam(required = true) LocalDate endDate
    ) {
        log.debug("REST request to calculate total leave days EmployeeNOC : {}");
        long result = employeeNOCService.calculateTotalLeaveDays(startDate, endDate);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    private void publishEvent(EmployeeNOC employeeNOC, EventType eventType) {
        log.debug("publishing employeeNoc application event with event: " + eventType);
        EmployeeNOCApplicationEvent event = new EmployeeNOCApplicationEvent(this, employeeNOC, eventType);
        applicationEventPublisher.publishEvent(event);
    }
}
