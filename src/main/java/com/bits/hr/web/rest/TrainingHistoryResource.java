package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.TrainingHistoryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TrainingHistoryDTO;
import java.net.URI;
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
 * REST controller for managing {@link com.bits.hr.domain.TrainingHistory}.
 */
@RestController
@RequestMapping("/api/employee-mgt/training-histories")
public class TrainingHistoryResource {

    private static final String ENTITY_NAME = "trainingHistory";
    private final Logger log = LoggerFactory.getLogger(TrainingHistoryResource.class);
    private final TrainingHistoryService trainingHistoryService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public TrainingHistoryResource(
        TrainingHistoryService trainingHistoryService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.trainingHistoryService = trainingHistoryService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /training-histories} : Create a new trainingHistory.
     *
     * @param trainingHistoryDTO the trainingHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingHistoryDTO, or with status {@code 400 (Bad Request)} if the trainingHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrainingHistoryDTO> createTrainingHistory(@Valid @RequestBody TrainingHistoryDTO trainingHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save TrainingHistory : {}", trainingHistoryDTO);
        if (trainingHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new trainingHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrainingHistoryDTO result = trainingHistoryService.save(trainingHistoryDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "TrainingHistory");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/training-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /training-histories} : Updates an existing trainingHistory.
     *
     * @param trainingHistoryDTO the trainingHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the trainingHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<TrainingHistoryDTO> updateTrainingHistory(@Valid @RequestBody TrainingHistoryDTO trainingHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to update TrainingHistory : {}", trainingHistoryDTO);
        if (trainingHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TrainingHistoryDTO result = trainingHistoryService.save(trainingHistoryDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "TrainingHistory");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trainingHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /training-histories} : get all the trainingHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainingHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrainingHistoryDTO>> getAllTrainingHistories(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TrainingHistories");
        Page<TrainingHistoryDTO> page = trainingHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "TrainingHistory");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /training-histories/get-by-employee/employeeId} : get all the trainingHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainingHistories in body.
     */
    @GetMapping("/get-by-employee/{employeeId}")
    public ResponseEntity<List<TrainingHistoryDTO>> getAllTrainingHistoriesByEmployee(@PathVariable long employeeId) {
        log.debug("REST request to get a page of TrainingHistories");
        List<TrainingHistoryDTO> result = trainingHistoryService.findAllByEmployee(employeeId);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /training-histories/:id} : get the "id" trainingHistory.
     *
     * @param id the id of the trainingHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainingHistoryDTO> getTrainingHistory(@PathVariable Long id) {
        log.debug("REST request to get TrainingHistory : {}", id);
        Optional<TrainingHistoryDTO> trainingHistoryDTO = trainingHistoryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (trainingHistoryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, trainingHistoryDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(trainingHistoryDTO);
    }

    /**
     * {@code DELETE  /training-histories/:id} : delete the "id" trainingHistory.
     *
     * @param id the id of the trainingHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingHistory(@PathVariable Long id) {
        log.debug("REST request to delete TrainingHistory : {}", id);
        Optional<TrainingHistoryDTO> trainingHistoryDTO = trainingHistoryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (trainingHistoryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, trainingHistoryDTO.get(), RequestMethod.GET, "AitConfig");
        }
        trainingHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
