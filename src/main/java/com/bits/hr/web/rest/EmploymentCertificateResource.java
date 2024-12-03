package com.bits.hr.web.rest;

import com.bits.hr.domain.EmploymentCertificate;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EmploymentCertificateService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.CertificateApprovalDto;
import com.bits.hr.service.dto.EmploymentCertificateDTO;
import com.bits.hr.service.event.EmploymentCertificateApplicationEvent;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.mapper.EmploymentCertificateMapper;
import java.util.List;
import java.util.Optional;
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

/**
 * REST controller for managing {@link com.bits.hr.domain.EmploymentCertificate}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class EmploymentCertificateResource {

    private final Logger log = LoggerFactory.getLogger(EmploymentCertificateResource.class);

    private static final String ENTITY_NAME = "employmentCertificate";

    private static final String RESOURCE_NAME = "EmploymentCertificateResource";

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

    public EmploymentCertificateResource(EmploymentCertificateService employmentCertificateService) {
        this.employmentCertificateService = employmentCertificateService;
    }

    @GetMapping("/employment-certificates")
    public ResponseEntity<List<EmploymentCertificateDTO>> getAllEmploymentCertificates(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) CertificateStatus status,
        @RequestParam(required = false) Integer selectedYear,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmploymentCertificates");
        Page<EmploymentCertificateDTO> page = employmentCertificateService.findAll(searchText, status, selectedYear, pageable);
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

    @PostMapping("/employment-certificates/approve/{id}")
    public ResponseEntity<EmploymentCertificateDTO> approveEmploymentCertificate(
        @PathVariable Long id,
        @RequestBody CertificateApprovalDto approvalDto
    ) {
        log.debug("REST request to approve EmployeeNOC : {}", id);
        User user = currentEmployeeService.getCurrentUser().get();
        EmploymentCertificate employmentCertificate = employmentCertificateService.approveEmployeeNoc(id, approvalDto, user);

        eventLoggingPublisher.publishEvent(user, employmentCertificate, RequestMethod.POST, RESOURCE_NAME);
        publishEvent(employmentCertificate, EventType.APPROVED);

        return ResponseEntity.ok().body(employmentCertificateMapper.toDto(employmentCertificate));
    }

    //    @PostMapping("/employment-certificates/reject/{id}")
    //    public ResponseEntity<EmploymentCertificateDTO> rejectEmploymentCertificate(@PathVariable Long id, @RequestBody CertificateApprovalDto approvalDto) {
    //        log.debug("REST request to reject EmployeeNOC : {}", id);
    //        User user = currentEmployeeService.getCurrentUser().get();
    //        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateService.rejectEmployeeNoc(id, approvalDto, user);
    //
    //        eventLoggingPublisher.publishEvent(user, employmentCertificateDTO, RequestMethod.POST, RESOURCE_NAME);
    //
    //        return ResponseEntity.ok().body(employmentCertificateDTO);
    //    }

    @GetMapping("/employment-certificates/print-format/{id}")
    public ResponseEntity<EmploymentCertificateDTO> employmentCertificatePrintFormat(@PathVariable Long id) {
        log.debug("REST request to get print format of EmployeeNOC : {}", id);
        Optional<EmploymentCertificateDTO> employeeNOCDTO = employmentCertificateService.getEmploymentCertificatePrintFormat(id);
        User user = currentEmployeeService.getCurrentUser().get();

        eventLoggingPublisher.publishEvent(user, employeeNOCDTO, RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(employeeNOCDTO);
    }

    @GetMapping("/employment-certificates/is-unique")
    public ResponseEntity<Boolean> isEmploymentCertificateReferenceNumberUnique(@RequestParam(required = true) String referenceNumber) {
        log.debug("REST request to check if reference number is unique : {}", referenceNumber);
        boolean result = employmentCertificateService.isReferenceNumberUnique(referenceNumber);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    private void publishEvent(EmploymentCertificate employmentCertificate, EventType eventType) {
        EmploymentCertificateApplicationEvent event = new EmploymentCertificateApplicationEvent(this, employmentCertificate, eventType);
        applicationEventPublisher.publishEvent(event);
    }
}
