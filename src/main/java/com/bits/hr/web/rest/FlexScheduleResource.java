package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FlexScheduleService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.FlexScheduleDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.FlexSchedule}.
 */
@RestController
@RequestMapping("/api/attendance-mgt")
public class FlexScheduleResource {

    private final Logger log = LoggerFactory.getLogger(FlexScheduleResource.class);

    private static final String ENTITY_NAME = "flexSchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlexScheduleService flexScheduleService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public FlexScheduleResource(
        FlexScheduleService flexScheduleService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.flexScheduleService = flexScheduleService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /flex-schedules} : Create a new flexSchedule.
     *
     * @param flexScheduleDTO the flexScheduleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flexScheduleDTO, or with status {@code 400 (Bad Request)} if the flexSchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flex-schedules")
    public ResponseEntity<FlexScheduleDTO> createFlexSchedule(@Valid @RequestBody FlexScheduleDTO flexScheduleDTO)
        throws URISyntaxException {
        log.debug("REST request to save FlexSchedule : {}", flexScheduleDTO);
        if (flexScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new flexSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlexScheduleDTO result = flexScheduleService.save(flexScheduleDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "FlexSchedule");
        return ResponseEntity
            .created(new URI("/api/flex-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flex-schedules} : Updates an existing flexSchedule.
     *
     * @param flexScheduleDTO the flexScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the flexScheduleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flexScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flex-schedules")
    public ResponseEntity<FlexScheduleDTO> updateFlexSchedule(@Valid @RequestBody FlexScheduleDTO flexScheduleDTO)
        throws URISyntaxException {
        log.debug("REST request to update FlexSchedule : {}", flexScheduleDTO);
        if (flexScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FlexScheduleDTO result = flexScheduleService.save(flexScheduleDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "FlexSchedule");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flexScheduleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /flex-schedules} : get all the flexSchedules.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flexSchedules in body.
     */
    @GetMapping("/flex-schedules")
    public ResponseEntity<List<FlexScheduleDTO>> getAllFlexSchedules(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FlexSchedules");
        Page<FlexScheduleDTO> page = flexScheduleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "FlexSchedule");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flex-schedules/:id} : get the "id" flexSchedule.
     *
     * @param id the id of the flexScheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flexScheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flex-schedules/{id}")
    public ResponseEntity<FlexScheduleDTO> getFlexSchedule(@PathVariable Long id) {
        log.debug("REST request to get FlexSchedule : {}", id);
        Optional<FlexScheduleDTO> flexScheduleDTO = flexScheduleService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (flexScheduleDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, flexScheduleDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(flexScheduleDTO);
    }

    /**
     * {@code DELETE  /flex-schedules/:id} : delete the "id" flexSchedule.
     *
     * @param id the id of the flexScheduleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flex-schedules/{id}")
    public ResponseEntity<Void> deleteFlexSchedule(@PathVariable Long id) {
        log.debug("REST request to delete FlexSchedule : {}", id);
        Optional<FlexScheduleDTO> flexScheduleDTO = flexScheduleService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (flexScheduleDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, flexScheduleDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        flexScheduleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/flex-schedules/search")
    public List<FlexScheduleDTO> getFlexScheduleByEffectiveDates(
        @RequestBody FlexScheduleDTO flexScheduleDTO,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        return flexScheduleService.getFlexScheduleByEffectiveDates(
            flexScheduleDTO.getEmployeeId(),
            flexScheduleDTO.getStartDate(),
            flexScheduleDTO.getEndDate()
        );
    }
}
