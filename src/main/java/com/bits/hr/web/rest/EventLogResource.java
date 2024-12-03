package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.EventLogService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EventLogDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.EventLog}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class EventLogResource {

    private final Logger log = LoggerFactory.getLogger(EventLogResource.class);

    private static final String ENTITY_NAME = "eventLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventLogService eventLogService;

    public EventLogResource(EventLogService eventLogService) {
        this.eventLogService = eventLogService;
    }

    /**
     * {@code POST  /event-logs} : Create a new eventLog.
     *
     * @param eventLogDTO the eventLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventLogDTO, or with status {@code 400 (Bad Request)} if the eventLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-logs")
    public ResponseEntity<EventLogDTO> createEventLog(@Valid @RequestBody EventLogDTO eventLogDTO) throws URISyntaxException {
        log.debug("REST request to save EventLog : {}", eventLogDTO);
        if (eventLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventLogDTO result = eventLogService.save(eventLogDTO);
        return ResponseEntity
            .created(new URI("/api/event-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-logs} : Updates an existing eventLog.
     *
     * @param eventLogDTO the eventLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLogDTO,
     * or with status {@code 400 (Bad Request)} if the eventLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-logs")
    public ResponseEntity<EventLogDTO> updateEventLog(@Valid @RequestBody EventLogDTO eventLogDTO) throws URISyntaxException {
        log.debug("REST request to update EventLog : {}", eventLogDTO);
        if (eventLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EventLogDTO result = eventLogService.save(eventLogDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /event-logs} : get all the eventLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventLogs in body.
     */
    @GetMapping("/event-logs")
    public ResponseEntity<List<EventLogDTO>> getAllEventLogs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of EventLogs");
        Page<EventLogDTO> page = eventLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-logs/:id} : get the "id" eventLog.
     *
     * @param id the id of the eventLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-logs/{id}")
    public ResponseEntity<EventLogDTO> getEventLog(@PathVariable Long id) {
        log.debug("REST request to get EventLog : {}", id);
        Optional<EventLogDTO> eventLogDTO = eventLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventLogDTO);
    }

    /**
     * {@code DELETE  /event-logs/:id} : delete the "id" eventLog.
     *
     * @param id the id of the eventLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-logs/{id}")
    public ResponseEntity<Void> deleteEventLog(@PathVariable Long id) {
        log.debug("REST request to delete EventLog : {}", id);
        Optional<EventLogDTO> eventLogDTO = eventLogService.findOne(id);
        eventLogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
