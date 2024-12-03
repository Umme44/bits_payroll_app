package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EmployeePinConfigurationService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeePinConfigurationDTO;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.EmployeePinConfiguration}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class EmployeePinConfigurationResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(EmployeePinConfigurationResource.class);
    private static final String ENTITY_NAME = "employeePinConfiguration";
    private final EmployeePinConfigurationService employeePinConfigurationService;
    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public EmployeePinConfigurationResource(
        EmployeePinConfigurationService employeePinConfigurationService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.employeePinConfigurationService = employeePinConfigurationService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @PostMapping("/employee-pin-configurations")
    public ResponseEntity<EmployeePinConfigurationDTO> createEmployeePinConfiguration(
        @Valid @RequestBody EmployeePinConfigurationDTO employeePinConfigurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save EmployeePinConfiguration : {}", employeePinConfigurationDTO);
        if (employeePinConfigurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeePinConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        EmployeePinConfigurationDTO result = employeePinConfigurationService.create(employeePinConfigurationDTO, user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, ENTITY_NAME);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/employee-pin-configurations")
    public ResponseEntity<EmployeePinConfigurationDTO> updateEmployeePinConfiguration(
        @Valid @RequestBody EmployeePinConfigurationDTO employeePinConfigurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeePinConfiguration : {}", employeePinConfigurationDTO);
        if (employeePinConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        User user = currentEmployeeService.getCurrentUser().get();

        EmployeePinConfigurationDTO result = employeePinConfigurationService.update(employeePinConfigurationDTO, user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, ENTITY_NAME);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employee-pin-configurations")
    public ResponseEntity<List<EmployeePinConfigurationDTO>> getAllEmployeePinConfigurations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmployeePinConfigurations");
        Page<EmployeePinConfigurationDTO> page = employeePinConfigurationService.findAll(pageable);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, page, RequestMethod.PUT, ENTITY_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/employee-pin-configurations/{id}")
    public ResponseEntity<EmployeePinConfigurationDTO> getEmployeePinConfiguration(@PathVariable Long id) {
        log.debug("REST request to get EmployeePinConfiguration : {}", id);
        Optional<EmployeePinConfigurationDTO> employeePinConfigurationDTO = employeePinConfigurationService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeePinConfigurationDTO, RequestMethod.PUT, ENTITY_NAME);

        return ResponseUtil.wrapOrNotFound(employeePinConfigurationDTO);
    }

    @GetMapping("/employee-pin-configurations/get-by-employee-category")
    public ResponseEntity<List<EmployeePinConfigurationDTO>> getEmployeePinConfigurationByEmployeeCategory(
        @RequestParam EmployeeCategory employeeCategory
    ) {
        log.debug("REST request to get EmployeePinConfiguration By Employee category : {}");
        List<EmployeePinConfigurationDTO> employeePinConfigurationDTOS = employeePinConfigurationService.findByEmployeeCategory(
            employeeCategory
        );

        return ResponseEntity.ok().body(employeePinConfigurationDTOS);
    }

    @DeleteMapping("/employee-pin-configurations/{id}")
    public ResponseEntity<Void> deleteEmployeePinConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete EmployeePinConfiguration : {}", id);
        employeePinConfigurationService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, ENTITY_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/employee-pin-configurations/is-pin-sequence-unique")
    public ResponseEntity<Boolean> isPinSequenceUnique(
        @RequestParam String startingPin,
        @RequestParam String endingPin,
        @RequestParam(required = false) Long pinConfigurationId
    ) {
        log.debug("REST request to check if pin sequence is unique : {}");
        boolean result = employeePinConfigurationService.isPinSequenceUnique(startingPin, endingPin, pinConfigurationId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employee-pin-configurations/is-pin-sequence-full-filled")
    public ResponseEntity<Boolean> isPinSequenceFullFilled(@RequestParam EmployeeCategory category) {
        log.debug("REST request to check if pin sequence is unique : {}");
        boolean result = employeePinConfigurationService.isPinSequenceFullFilled(category);
        return ResponseEntity.ok().body(result);
    }
}
