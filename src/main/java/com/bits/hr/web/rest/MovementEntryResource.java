package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.MovementEntryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.MovementEntryDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
 * REST controller for managing {@link com.bits.hr.domain.MovementEntry}.
 */
@RestController
@RequestMapping("/api/attendance-mgt")
public class MovementEntryResource {

    private final Logger log = LoggerFactory.getLogger(MovementEntryResource.class);

    private static final String ENTITY_NAME = "movementEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MovementEntryService movementEntryService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public MovementEntryResource(
        MovementEntryService movementEntryService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.movementEntryService = movementEntryService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /movement-entries} : Create a new movementEntry.
     *
     * @param movementEntryDTO the movementEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movementEntryDTO, or with status {@code 400 (Bad Request)} if the movementEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/movement-entries")
    public ResponseEntity<MovementEntryDTO> createMovementEntry(@Valid @RequestBody MovementEntryDTO movementEntryDTO)
        throws URISyntaxException {
        log.debug("REST request to save MovementEntry : {}", movementEntryDTO);
        if (movementEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new movementEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovementEntryDTO result = movementEntryService.save(movementEntryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "MovementEntry");
        return ResponseEntity
            .created(new URI("/api/movement-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/movement-entries/apply-approve-by-hr")
    public ResponseEntity<MovementEntryDTO> applyAndApproveMovementEntryByHR(@Valid @RequestBody MovementEntryDTO movementEntryDTO)
        throws URISyntaxException {
        log.debug("REST request to save MovementEntry : {}", movementEntryDTO);
        if (movementEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new movementEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }

        MovementEntryDTO result = movementEntryService.applyAndApproveMovementEntryByHR(movementEntryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "MovementEntry");
        return ResponseEntity
            .created(new URI("/api/movement-entries/apply-approve-by-hr" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /movement-entries} : Updates an existing movementEntry.
     *
     * @param movementEntryDTO the movementEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movementEntryDTO,
     * or with status {@code 400 (Bad Request)} if the movementEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movementEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/movement-entries")
    public ResponseEntity<MovementEntryDTO> updateMovementEntry(@Valid @RequestBody MovementEntryDTO movementEntryDTO)
        throws URISyntaxException {
        log.debug("REST request to update MovementEntry : {}", movementEntryDTO);
        if (movementEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        movementEntryDTO.setUpdatedAt(LocalDate.now());
        MovementEntryDTO result = movementEntryService.save(movementEntryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "MovementEntry");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movementEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /movement-entries} : get all the movementEntries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movementEntries in body.
     */
    @GetMapping("/movement-entries")
    public ResponseEntity<List<MovementEntryDTO>> getAllMovementEntries(
        @RequestParam(required = false) Long employeeId,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of MovementEntries");
        Page<MovementEntryDTO> page = movementEntryService.findAll(employeeId, startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "MovementEntry");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /movement-entries/:id} : get the "id" movementEntry.
     *
     * @param id the id of the movementEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movementEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/movement-entries/{id}")
    public ResponseEntity<MovementEntryDTO> getMovementEntry(@PathVariable Long id) {
        log.debug("REST request to get MovementEntry : {}", id);
        Optional<MovementEntryDTO> movementEntryDTO = movementEntryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (movementEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "MovementEntry");
        }
        return ResponseUtil.wrapOrNotFound(movementEntryDTO);
    }

    /**
     * {@code DELETE  /movement-entries/:id} : delete the "id" movementEntry.
     *
     * @param id the id of the movementEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/movement-entries/{id}")
    public ResponseEntity<Void> deleteMovementEntry(@PathVariable Long id) {
        log.debug("REST request to delete MovementEntry : {}", id);
        Optional<MovementEntryDTO> movementEntryDTO = movementEntryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (movementEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, movementEntryDTO.get(), RequestMethod.DELETE, "MovementEntry");
        }
        movementEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
