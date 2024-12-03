package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.repository.FinalSettlementRepository;
import com.bits.hr.service.EmployeeResignationService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeResignationDTO;
import com.bits.hr.service.scheduler.schedulingService.SeparationReminderMailSchedulerService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;

/**
 * REST controller for managing {@link com.bits.hr.domain.EmployeeResignation}.
 */
@RestController
@RequestMapping("/api/employee-mgt/resignations")
public class EmployeeResignationResource {

    private static final String ENTITY_NAME = "employeeResignation";
    private static final String RESOURCE_NAME = "EmployeeResignationResource";
    private final Logger log = LoggerFactory.getLogger(EmployeeResignationResource.class);
    private final EmployeeResignationService employeeResignationService;
    private final EmployeeRepository employeeRepository;
    private final EmployeeResignationRepository employeeResignationRepository;
    private final FinalSettlementRepository finalSettlementRepository;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    private final SeparationReminderMailSchedulerService separationReminderMailSchedulerService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public EmployeeResignationResource(
        EmployeeResignationService employeeResignationService,
        EmployeeRepository employeeRepository,
        EmployeeResignationRepository employeeResignationRepository,
        FinalSettlementRepository finalSettlementRepository,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher,
        SeparationReminderMailSchedulerService separationReminderMailSchedulerService
    ) {
        this.employeeResignationService = employeeResignationService;
        this.employeeRepository = employeeRepository;
        this.employeeResignationRepository = employeeResignationRepository;
        this.finalSettlementRepository = finalSettlementRepository;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.separationReminderMailSchedulerService = separationReminderMailSchedulerService;
    }

    /**
     * {@code POST  /employee-resignations} : Create a new employeeResignation.
     *
     * @param employeeResignationDTO the employeeResignationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeResignationDTO, or with status {@code 400 (Bad Request)} if the employeeResignation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeResignationDTO> createEmployeeResignation(@RequestBody @Valid EmployeeResignationDTO employeeResignationDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmployeeResignation : {}", employeeResignationDTO);
        if (employeeResignationDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeResignation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        //        if (
        //            employeeResignationDTO.getResignationDate()
        //                .isAfter(employeeResignationDTO.getLastWorkingDay()) ||
        //                employeeResignationDTO.getResignationDate()
        //                    .isEqual(employeeResignationDTO.getLastWorkingDay())
        //        ) {
        //            throw new BadRequestAlertException(" Resignation date should not exceed last working day ", ENTITY_NAME, "invalidData");
        //        }
        EmployeeResignationDTO result = employeeResignationService.processAndSaveNewResignation(employeeResignationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/employee-mgt/resignations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-resignations} : Updates an existing employeeResignation.
     *
     * @param employeeResignationDTO the employeeResignationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeResignationDTO,
     * or with status {@code 400 (Bad Request)} if the employeeResignationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeResignationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<EmployeeResignationDTO> updateEmployeeResignation(@RequestBody @Valid EmployeeResignationDTO employeeResignationDTO)
        throws URISyntaxException {
        log.debug("REST request to update EmployeeResignation : {}", employeeResignationDTO);
        if (employeeResignationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeResignationDTO result = employeeResignationService.save(employeeResignationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeResignationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employee-resignations} : get all the employeeResignations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeResignations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeResignationDTO>> getAllEmployeeResignations(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmployeeResignations");
        Page<EmployeeResignationDTO> page = employeeResignationService.findAll(searchText, startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-resignations/:id} : get the "id" employeeResignation.
     *
     * @param id the id of the employeeResignationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeResignationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResignationDTO> getEmployeeResignation(@PathVariable Long id) {
        log.debug("REST request to get EmployeeResignation : {}", id);
        Optional<EmployeeResignationDTO> employeeResignationDTO = employeeResignationService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (employeeResignationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, employeeResignationDTO.get(), RequestMethod.GET, RESOURCE_NAME);
        }
        return ResponseUtil.wrapOrNotFound(employeeResignationDTO);
    }

    /**
     * {@code DELETE  /employee-resignations/:id} : delete the "id" employeeResignation.
     *
     * @param id the id of the employeeResignationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeResignation(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeResignation : {}", id);
        Optional<EmployeeResignationDTO> employeeResignationDTO = employeeResignationService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (employeeResignationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, employeeResignationDTO.get(), RequestMethod.DELETE, RESOURCE_NAME);
        }
        employeeResignationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/approve")
    public ResponseEntity<EmployeeResignationDTO> approveEmployeeResignation(@RequestBody EmployeeResignationDTO employeeResignationDTO)
        throws URISyntaxException {
        log.debug("REST request to update EmployeeResignation : {}", employeeResignationDTO);
        if (employeeResignationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeResignationDTO result = employeeResignationService.approveResignation(employeeResignationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeResignationDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/reject")
    public ResponseEntity<Void> denyEmployeeResignation(@RequestBody EmployeeResignationDTO employeeResignationDTO)
        throws URISyntaxException {
        // data validations
        log.debug("REST request to update EmployeeResignation : {}", employeeResignationDTO);
        if (employeeResignationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        // if final settlement done , through exceptions
        if (!finalSettlementRepository.findFinalSettlementByEmployeeId(employeeResignationDTO.getEmployeeId()).isEmpty()) {
            throw new BadRequestAlertException(
                "Final settlement already done for this employee",
                ENTITY_NAME,
                "finalSettlementAlreadyDone"
            );
        }
        // todo: << will be based on management approval >> if salary generated after resignation process ,
        // that single salary should be generated by deleting resignation entry
        boolean result = employeeResignationService.rejectResignation(employeeResignationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, employeeResignationDTO.getId().toString()))
            .build();
    }

    @GetMapping("/notice-period/{employeeId}")
    public ResponseEntity<Integer> getEmployeeNoticePeriod(@PathVariable long employeeId) {
        log.debug("REST request to get notice period in day using employee category");
        int days = employeeResignationService.getEmployeeNoticePeriod(employeeId);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, days, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(days);
    }

    @GetMapping("/send-mail-replacement-for-resigned-employee/{lastWorkingDate}")
    public ResponseEntity<HttpStatus> sendMailForReplacementOfResignedEmployee(@PathVariable LocalDate lastWorkingDate) {
        log.debug("REST request to get scheduler mail to HR for resigned employee's replacement");
        separationReminderMailSchedulerService.sendMailForReplacementOfResignedEmployee(lastWorkingDate);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
