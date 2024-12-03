package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.ReferencesService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ReferencesDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.References}.
 */
@RestController
@RequestMapping("/api/employee-mgt/references")
public class ReferencesResource {

    private static final String ENTITY_NAME = "references";
    private final Logger log = LoggerFactory.getLogger(ReferencesResource.class);
    private final ReferencesService referencesService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ReferencesResource(
        ReferencesService referencesService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.referencesService = referencesService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /references} : Create a new references.
     *
     * @param referencesDTO the referencesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referencesDTO, or with status {@code 400 (Bad Request)} if the references has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReferencesDTO> createReferences(@Valid @RequestBody ReferencesDTO referencesDTO) throws URISyntaxException {
        log.debug("REST request to save References : {}", referencesDTO);
        if (referencesDTO.getId() != null) {
            throw new BadRequestAlertException("A new references cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReferencesDTO result = referencesService.save(referencesDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "References");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/references/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /references} : Updates an existing references.
     *
     * @param referencesDTO the referencesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referencesDTO,
     * or with status {@code 400 (Bad Request)} if the referencesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referencesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping({ "", "/" })
    public ResponseEntity<ReferencesDTO> updateReferences(@Valid @RequestBody ReferencesDTO referencesDTO) throws URISyntaxException {
        log.debug("REST request to update References : {}", referencesDTO);
        if (referencesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReferencesDTO result = referencesService.save(referencesDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "References");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referencesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /references} : get all the references.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of references in body.
     */
    @GetMapping("")
    public List<ReferencesDTO> getAllReferences() {
        log.debug("REST request to get all References");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "References");
        return referencesService.findAll();
    }

    @GetMapping("/get-by-employee/{employeeId}")
    public List<ReferencesDTO> getAllReferencesByEmployee(@PathVariable long employeeId) {
        log.debug("REST request to get all References by employee id");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "References");
        return referencesService.findAllByEmployee(employeeId);
    }

    /**
     * {@code GET  /references/:id} : get the "id" references.
     *
     * @param id the id of the referencesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referencesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferencesDTO> getReferences(@PathVariable Long id) {
        log.debug("REST request to get References : {}", id);
        Optional<ReferencesDTO> referencesDTO = referencesService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (referencesDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "References");
        }
        return ResponseUtil.wrapOrNotFound(referencesDTO);
    }

    /**
     * {@code DELETE  /references/:id} : delete the "id" references.
     *
     * @param id the id of the referencesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferences(@PathVariable Long id) {
        log.debug("REST request to delete References : {}", id);
        Optional<ReferencesDTO> referencesDTO = referencesService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (referencesDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, referencesDTO.get(), RequestMethod.GET, "References");
        }
        referencesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
