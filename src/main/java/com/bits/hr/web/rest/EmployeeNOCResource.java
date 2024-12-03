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
import com.bits.hr.service.dto.CertificateApprovalDto;
import com.bits.hr.service.dto.EmployeeNOCDTO;
import com.bits.hr.service.event.EmployeeNOCApplicationEvent;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.mapper.EmployeeNOCMapper;
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

/**
 * REST controller for managing {@link com.bits.hr.domain.EmployeeNOC}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class EmployeeNOCResource {

    private static final String RESOURCE_NAME = "EmployeeNOCResource";

    private final Logger log = LoggerFactory.getLogger(EmployeeNOCResource.class);

    private static final String ENTITY_NAME = "employeeNOC";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeNOCService employeeNOCService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private EmployeeNOCMapper employeeNOCMapper;

    public EmployeeNOCResource(EmployeeNOCService employeeNOCService) {
        this.employeeNOCService = employeeNOCService;
    }

    @PostMapping("/employee-no-objection-certificates")
    public ResponseEntity<EmployeeNOCDTO> createEmployeeNOC(@Valid @RequestBody EmployeeNOCDTO employeeNOCDTO) throws URISyntaxException {
        log.debug("REST request to save EmployeeNOC : {}", employeeNOCDTO);
        if (employeeNOCDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeNOC cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeNOCDTO.getId());
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        User user = currentEmployeeService.getCurrentUser().get();

        EmployeeNOC result = employeeNOCService.createEmployeeNOC(employeeNOCDTO, employeeOptional.get(), user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity.ok().body(employeeNOCMapper.toDto(result));
    }

    @PutMapping("/employee-no-objection-certificates")
    public ResponseEntity<EmployeeNOCDTO> updateEmployeeNOC(@Valid @RequestBody EmployeeNOCDTO employeeNOCDTO) throws URISyntaxException {
        log.debug("REST request to update EmployeeNOC : {}", employeeNOCDTO);
        if (employeeNOCDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Employee> employee = employeeRepository.findById(employeeNOCDTO.getEmployeeId());
        User user = currentEmployeeService.getCurrentUser().get();

        EmployeeNOCDTO result = employeeNOCService.updateEmployeeNOC(employeeNOCDTO, employee.get(), user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeNOCDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/employee-no-objection-certificates")
    public ResponseEntity<List<EmployeeNOCDTO>> getAllEmployeeNOCS(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) CertificateStatus status,
        @RequestParam(required = false) Integer selectedYear,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmployeeNOCS");
        Page<EmployeeNOCDTO> page = employeeNOCService.findAll(searchText, status, selectedYear, pageable);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, page.getContent(), RequestMethod.GET, RESOURCE_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
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
        employeeNOCService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/employee-no-objection-certificates/approve/{id}")
    public ResponseEntity<EmployeeNOCDTO> approveEmployeeNOC(@PathVariable Long id, @RequestBody CertificateApprovalDto approvalDto) {
        log.debug("REST request to approve EmployeeNOC : {}", id);
        User user = currentEmployeeService.getCurrentUser().get();
        EmployeeNOC employeeNOC = employeeNOCService.approveEmployeeNoc(id, approvalDto, user);

        eventLoggingPublisher.publishEvent(user, employeeNOC, RequestMethod.POST, RESOURCE_NAME);
        publishEvent(employeeNOC, EventType.APPROVED);

        return ResponseEntity.ok().body(employeeNOCMapper.toDto(employeeNOC));
    }

    //    @PostMapping("/employee-no-objection-certificates/reject/{id}")
    //    public ResponseEntity<EmployeeNOCDTO> rejectEmployeeNOC(@PathVariable Long id, @RequestBody CertificateApprovalDto approvalDto) {
    //        log.debug("REST request to reject EmployeeNOC : {}", id);
    //        User user = currentEmployeeService.getCurrentUser().get();
    //        EmployeeNOCDTO employeeNOCDTO = employeeNOCService.rejectEmployeeNoc(id, approvalDto, user);
    //
    //        eventLoggingPublisher.publishEvent(user, employeeNOCDTO, RequestMethod.POST, RESOURCE_NAME);
    //
    //        return ResponseEntity.ok().body(employeeNOCDTO);
    //    }

    @GetMapping("/employee-no-objection-certificates/print-format/{id}")
    public ResponseEntity<EmployeeNOCDTO> employeeNOCPrintFormat(@PathVariable Long id) {
        log.debug("REST request to get print format of EmployeeNOC : {}", id);
        User user = currentEmployeeService.getCurrentUser().get();
        Optional<EmployeeNOCDTO> employeeNOCDTO = employeeNOCService.getEmployeeNocPrintFormat(id);

        eventLoggingPublisher.publishEvent(user, employeeNOCDTO, RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(employeeNOCDTO);
    }

    @GetMapping("/employee-no-objection-certificates/is-unique")
    public ResponseEntity<Boolean> isEmployeeNOCReferenceNumberUnique(@RequestParam(required = true) String referenceNumber) {
        log.debug("REST request to check if reference number is unique : {}", referenceNumber);
        User user = currentEmployeeService.getCurrentUser().get();
        boolean result = employeeNOCService.isReferenceNumberUnique(referenceNumber);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    private void publishEvent(EmployeeNOC employeeNOC, EventType eventType) {
        log.debug("publishing employeeNoc application event with event: " + eventType);
        EmployeeNOCApplicationEvent event = new EmployeeNOCApplicationEvent(this, employeeNOC, eventType);
        applicationEventPublisher.publishEvent(event);
    }
}
