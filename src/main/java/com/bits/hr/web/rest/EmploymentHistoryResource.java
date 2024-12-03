package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EmploymentHistoryService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
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
 * REST controller for managing {@link com.bits.hr.domain.EmploymentHistory}.
 */
@RestController
@RequestMapping("/api/employee-mgt/employment-histories")
public class EmploymentHistoryResource {

    private static final String ENTITY_NAME = "employmentHistory";
    private final Logger log = LoggerFactory.getLogger(EmploymentHistoryResource.class);
    private final EmploymentHistoryService employmentHistoryService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public EmploymentHistoryResource(
        EmploymentHistoryService employmentHistoryService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.employmentHistoryService = employmentHistoryService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /employment-histories} : Create a new employmentHistory.
     *
     * @param employmentHistoryDTO the employmentHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employmentHistoryDTO, or with status {@code 400 (Bad Request)} if the employmentHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmploymentHistoryDTO> createEmploymentHistory(@RequestBody EmploymentHistoryDTO employmentHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmploymentHistory : {}", employmentHistoryDTO);
        if (employmentHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new employmentHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmploymentHistoryDTO result = employmentHistoryService.save(employmentHistoryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "EmploymentHistory");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/employment-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employment-histories} : Updates an existing employmentHistory.
     *
     * @param employmentHistoryDTO the employmentHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the employmentHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employmentHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<EmploymentHistoryDTO> updateEmploymentHistory(@RequestBody EmploymentHistoryDTO employmentHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to update EmploymentHistory : {}", employmentHistoryDTO);
        if (employmentHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmploymentHistoryDTO result = employmentHistoryService.save(employmentHistoryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "EmploymentHistory");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employment-histories} : get all the employmentHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employmentHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmploymentHistoryDTO>> getAllEmploymentHistories(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmploymentHistories");
        Page<EmploymentHistoryDTO> page = employmentHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmploymentHistory");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employment-histories/:id} : get the "id" employmentHistory.
     *
     * @param id the id of the employmentHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employmentHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmploymentHistoryDTO> getEmploymentHistory(@PathVariable Long id) {
        log.debug("REST request to get EmploymentHistory : {}", id);
        Optional<EmploymentHistoryDTO> employmentHistoryDTO = employmentHistoryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (employmentHistoryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, employmentHistoryDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(employmentHistoryDTO);
    }

    /**
     * {@code DELETE  /employment-histories/:id} : delete the "id" employmentHistory.
     *
     * @param id the id of the employmentHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploymentHistory(@PathVariable Long id) {
        log.debug("REST request to delete EmploymentHistory : {}", id);
        Optional<EmploymentHistoryDTO> employmentHistoryDTO = employmentHistoryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (employmentHistoryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, employmentHistoryDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        employmentHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
