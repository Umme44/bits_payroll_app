package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.WorkingExperienceService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.WorkingExperienceDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.WorkingExperience}.
 */
@RestController
@RequestMapping("/api/employee-mgt/working-experiences")
public class WorkingExperienceResource {

    private static final String ENTITY_NAME = "workingExperience";
    private final Logger log = LoggerFactory.getLogger(WorkingExperienceResource.class);
    private final WorkingExperienceService workingExperienceService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public WorkingExperienceResource(
        WorkingExperienceService workingExperienceService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.workingExperienceService = workingExperienceService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /working-experiences} : Create a new workingExperience.
     *
     * @param workingExperienceDTO the workingExperienceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workingExperienceDTO, or with status {@code 400 (Bad Request)} if the workingExperience has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WorkingExperienceDTO> createWorkingExperience(@Valid @RequestBody WorkingExperienceDTO workingExperienceDTO)
        throws URISyntaxException {
        log.debug("REST request to save WorkingExperience : {}", workingExperienceDTO);
        if (workingExperienceDTO.getId() != null) {
            throw new BadRequestAlertException("A new workingExperience cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkingExperienceDTO result = workingExperienceService.save(workingExperienceDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "WorkingExperience");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/working-experiences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /working-experiences} : Updates an existing workingExperience.
     *
     * @param workingExperienceDTO the workingExperienceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workingExperienceDTO,
     * or with status {@code 400 (Bad Request)} if the workingExperienceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workingExperienceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<WorkingExperienceDTO> updateWorkingExperience(@Valid @RequestBody WorkingExperienceDTO workingExperienceDTO)
        throws URISyntaxException {
        log.debug("REST request to update WorkingExperience : {}", workingExperienceDTO);
        if (workingExperienceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkingExperienceDTO result = workingExperienceService.save(workingExperienceDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "WorkingExperience");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workingExperienceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /working-experiences} : get all the workingExperiences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workingExperiences in body.
     */
    @GetMapping("")
    public List<WorkingExperienceDTO> getAllWorkingExperiences() {
        log.debug("REST request to get all WorkingExperiences");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "WorkingExperience");
        return workingExperienceService.findAll();
    }

    /**
     * {@code GET  /working-experiences} : get all the workingExperiences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workingExperiences in body.
     */
    @GetMapping("/get-by-employee/{employeeId}")
    public List<WorkingExperienceDTO> getAllWorkingExperiencesByEmployee(@PathVariable long employeeId) {
        log.debug("REST request to get all WorkingExperiences by employee id");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "WorkingExperience");
        return workingExperienceService.findByEmployee(employeeId);
    }

    /**
     * {@code GET  /working-experiences/:id} : get the "id" workingExperience.
     *
     * @param id the id of the workingExperienceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workingExperienceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkingExperienceDTO> getWorkingExperience(@PathVariable Long id) {
        log.debug("REST request to get WorkingExperience : {}", id);
        Optional<WorkingExperienceDTO> workingExperienceDTO = workingExperienceService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (workingExperienceDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "WorkingExperience");
        }
        return ResponseUtil.wrapOrNotFound(workingExperienceDTO);
    }

    /**
     * {@code DELETE  /working-experiences/:id} : delete the "id" workingExperience.
     *
     * @param id the id of the workingExperienceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkingExperience(@PathVariable Long id) {
        log.debug("REST request to delete WorkingExperience : {}", id);
        Optional<WorkingExperienceDTO> workingExperienceDTO = workingExperienceService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (workingExperienceDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, workingExperienceDTO.get(), RequestMethod.DELETE, "WorkingExperience");
        }
        workingExperienceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
