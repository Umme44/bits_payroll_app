package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentCertificate;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EmploymentCertificateService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmploymentCertificateDTO;
import com.bits.hr.service.event.EmploymentCertificateApplicationEvent;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.mapper.EmploymentCertificateMapper;
import java.net.URI;
import java.net.URISyntaxException;
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
public class EmploymentCertificateCommonResource {

    private final Logger log = LoggerFactory.getLogger(EmploymentCertificateResource.class);

    private static final String ENTITY_NAME = "employmentCertificate";

    private static final String RESOURCE_NAME = "EmploymentCertificateCommonResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmploymentCertificateService employmentCertificateService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private EmploymentCertificateMapper employmentCertificateMapper;

    public EmploymentCertificateCommonResource(EmploymentCertificateService employmentCertificateService) {
        this.employmentCertificateService = employmentCertificateService;
    }

    @PostMapping("/employment-certificates")
    public ResponseEntity<EmploymentCertificateDTO> createEmploymentCertificate() throws URISyntaxException {
        log.debug("REST request to save EmploymentCertificate : {}");

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        User user = currentEmployeeService.getCurrentUser().get();

        EmploymentCertificate result = employmentCertificateService.createEmploymentCertificate(employeeOptional.get(), user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        publishEvent(result, EventType.CREATED);

        return ResponseEntity.ok().body(employmentCertificateMapper.toDto(result));
    }

    @PutMapping("/employment-certificates")
    public ResponseEntity<EmploymentCertificateDTO> updateEmploymentCertificate(
        @Valid @RequestBody EmploymentCertificateDTO employmentCertificateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmploymentCertificate : {}", employmentCertificateDTO);
        if (employmentCertificateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!employmentCertificateDTO.getCertificateStatus().equals(CertificateStatus.SENT_FOR_GENERATION)) {
            throw new BadRequestAlertException("Only pending requests are allowed to update.", "EmploymentCertificate", "accessForbidden");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        EmploymentCertificateDTO result = employmentCertificateService.updateEmploymentCertificate(employmentCertificateDTO, user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employment-certificates")
    public ResponseEntity<List<EmploymentCertificateDTO>> getAllEmploymentCertificates(
        @RequestParam(required = false) CertificateStatus status,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmploymentCertificates");

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        Page<EmploymentCertificateDTO> page = employmentCertificateService.findAllByEmployeeId(employeeOptional.get(), status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, page.getContent(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/employment-certificates/{id}")
    public ResponseEntity<EmploymentCertificateDTO> getEmploymentCertificate(@PathVariable Long id) {
        log.debug("REST request to get EmploymentCertificate : {}", id);
        Optional<EmploymentCertificateDTO> employmentCertificateDTO = employmentCertificateService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employmentCertificateDTO, RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(employmentCertificateDTO);
    }

    @DeleteMapping("/employment-certificates/{id}")
    public ResponseEntity<Void> deleteEmploymentCertificate(@PathVariable Long id) {
        log.debug("REST request to delete EmploymentCertificate : {}", id);
        employmentCertificateService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private void publishEvent(EmploymentCertificate employmentCertificate, EventType eventType) {
        log.debug("publishing employeeNoc application event with event: " + eventType);
        EmploymentCertificateApplicationEvent event = new EmploymentCertificateApplicationEvent(this, employmentCertificate, eventType);
        applicationEventPublisher.publishEvent(event);
    }
}
