package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.WorkFromHomeApplicationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import com.bits.hr.service.scheduler.schedulingService.WorkFromHomeSchedulerService;
import com.bits.hr.service.workFromHomeApplication.WorkFromHomeApplicationLMService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link com.bits.hr.domain.WorkFromHomeApplication}.
 */
@RestController
@RequestMapping("/api/common")
public class WorkFromHomeApplicationCommonResource {

    private final Logger log = LoggerFactory.getLogger(WorkFromHomeApplicationCommonResource.class);

    private static final String ENTITY_NAME = "workFromHomeApplication";

    private static final String RESOURCE_NAME = "WorkFromHomeApplicationCommonResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkFromHomeApplicationService workFromHomeApplicationService;

    private final CurrentEmployeeService currentEmployeeService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final EventLoggingPublisher eventLoggingPublisher;
    private final WorkFromHomeSchedulerService workFromHomeSchedulerService;
    private final WorkFromHomeApplicationLMService workFromHomeApplicationLMService;

    public WorkFromHomeApplicationCommonResource(
        WorkFromHomeApplicationService workFromHomeApplicationService,
        CurrentEmployeeService currentEmployeeService,
        ApplicationEventPublisher applicationEventPublisher,
        EventLoggingPublisher eventLoggingPublisher,
        WorkFromHomeSchedulerService workFromHomeSchedulerService,
        WorkFromHomeApplicationLMService workFromHomeApplicationLMService
    ) {
        this.workFromHomeApplicationService = workFromHomeApplicationService;
        this.currentEmployeeService = currentEmployeeService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.workFromHomeSchedulerService = workFromHomeSchedulerService;
        this.workFromHomeApplicationLMService = workFromHomeApplicationLMService;
    }

    /**
     * {@code POST  /work-from-home-applications} : Create a new workFromHomeApplication.
     *
     * @param workFromHomeApplicationDTO the workFromHomeApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workFromHomeApplicationDTO, or with status {@code 400 (Bad Request)} if the workFromHomeApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-from-home-applications")
    public ResponseEntity<WorkFromHomeApplicationDTO> createWorkFromHomeApplication(
        @Valid @RequestBody WorkFromHomeApplicationDTO workFromHomeApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WorkFromHomeApplication : {}", workFromHomeApplicationDTO);
        if (workFromHomeApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new workFromHomeApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        Optional<User> user = currentEmployeeService.getCurrentUser();
        if (employee.isPresent() && user.isPresent()) {
            workFromHomeApplicationDTO.setEmployeeId(employee.get().getId());
            workFromHomeApplicationDTO.setCreatedById(user.get().getId());
            workFromHomeApplicationDTO.setAppliedById(user.get().getId());
            workFromHomeApplicationDTO.setAppliedAt(LocalDate.now());
            workFromHomeApplicationDTO.setCreatedAt(Instant.now());
        } else {
            throw new NoEmployeeProfileException();
        }
        WorkFromHomeApplicationDTO result = workFromHomeApplicationService.save(workFromHomeApplicationDTO);
        eventLoggingPublisher.publishEvent(user.get(), result, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/common/work-from-home-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-from-home-applications} : Updates an existing workFromHomeApplication.
     *
     * @param workFromHomeApplicationDTO the workFromHomeApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workFromHomeApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the workFromHomeApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workFromHomeApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-from-home-applications")
    public ResponseEntity<WorkFromHomeApplicationDTO> updateWorkFromHomeApplication(
        @Valid @RequestBody WorkFromHomeApplicationDTO workFromHomeApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkFromHomeApplication : {}", workFromHomeApplicationDTO);
        if (workFromHomeApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        Optional<User> user = currentEmployeeService.getCurrentUser();
        if (employee.isPresent()) {
            workFromHomeApplicationDTO.setUpdatedById(user.get().getId());
            workFromHomeApplicationDTO.setUpdatedAt(Instant.now());
        } else {
            throw new NoEmployeeProfileException();
        }
        WorkFromHomeApplicationDTO result = workFromHomeApplicationService.save(workFromHomeApplicationDTO);
        eventLoggingPublisher.publishEvent(user.get(), result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workFromHomeApplicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /work-from-home-applications} : get all the workFromHomeApplications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workFromHomeApplications in body.
     */
    @GetMapping("/work-from-home-applications")
    public ResponseEntity<List<WorkFromHomeApplicationDTO>> getAllWorkFromHomeApplications(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of WorkFromHomeApplications");
        Page<WorkFromHomeApplicationDTO> page = workFromHomeApplicationService.findAll(pageable);
        Optional<User> user = currentEmployeeService.getCurrentUser();
        eventLoggingPublisher.publishEvent(user.get(), Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/work-from-home-applications/employeeId")
    public ResponseEntity<List<WorkFromHomeApplicationDTO>> getAllWorkFromHomeApplicationsByEmployeeId(Pageable pageable) {
        log.debug("REST request to get a page of WorkFromHomeApplications");
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        long employeeId;
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        Page<WorkFromHomeApplicationDTO> page = workFromHomeApplicationService.getAllAppliedApplicationsByEmployeeId(employeeId, pageable);
        Optional<User> user = currentEmployeeService.getCurrentUser();
        eventLoggingPublisher.publishEvent(user.get(), Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-from-home-applications/:id} : get the "id" workFromHomeApplication.
     *
     * @param id the id of the workFromHomeApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workFromHomeApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-from-home-applications/{id}")
    public ResponseEntity<WorkFromHomeApplicationDTO> getWorkFromHomeApplication(@PathVariable Long id) {
        log.debug("REST request to get WorkFromHomeApplication : {}", id);
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        long employeeId;
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        Optional<WorkFromHomeApplicationDTO> workFromHomeApplicationDTO = workFromHomeApplicationService.findOne(id);
        Optional<User> user = currentEmployeeService.getCurrentUser();
        eventLoggingPublisher.publishEvent(user.get(), workFromHomeApplicationDTO, RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(workFromHomeApplicationDTO);
    }

    /**
     * {@code DELETE  /work-from-home-applications/:id} : delete the "id" workFromHomeApplication.
     *
     * @param id the id of the workFromHomeApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-from-home-applications/{id}")
    public ResponseEntity<Void> deleteWorkFromHomeApplication(@PathVariable Long id) {
        log.debug("REST request to delete WorkFromHomeApplication : {}", id);
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        Optional<WorkFromHomeApplicationDTO> workFromHomeApplicationDTO = workFromHomeApplicationService.findOne(id);
        long employeeId;
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        workFromHomeApplicationService.delete(id);
        Optional<User> user = currentEmployeeService.getCurrentUser();
        eventLoggingPublisher.publishEvent(user.get(), workFromHomeApplicationDTO, RequestMethod.DELETE, RESOURCE_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/work-from-home-applications/isApplied")
    public ResponseEntity<Boolean> checkIsApplied(@RequestBody WorkFromHomeApplicationDTO workFromHomeApplicationDTO) {
        boolean isApplied = false;
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        if (employee.isPresent()) {
            workFromHomeApplicationDTO.setEmployeeId(employee.get().getId());
        } else {
            throw new NoEmployeeProfileException();
        }

        LocalDate startDate = workFromHomeApplicationDTO.getStartDate();
        LocalDate endDate = workFromHomeApplicationDTO.getEndDate();
        Long requesterId = workFromHomeApplicationDTO.getEmployeeId();
        if (startDate != null && endDate != null) {
            if (workFromHomeApplicationDTO.getId() != null) {
                isApplied =
                    workFromHomeApplicationService.checkIsAppliedForUpdate(
                        requesterId,
                        workFromHomeApplicationDTO.getId(),
                        startDate,
                        endDate
                    );
            } else {
                isApplied = workFromHomeApplicationService.checkIsApplied(requesterId, startDate, endDate);
            }
        } else {
            throw new RuntimeException("startDate or endDate can not be null");
        }
        Optional<User> user = currentEmployeeService.getCurrentUser();
        eventLoggingPublisher.publishEvent(user.get(), isApplied, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity.ok(isApplied);
    }
}
