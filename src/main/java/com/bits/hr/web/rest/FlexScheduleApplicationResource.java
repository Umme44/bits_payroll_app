package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FlexScheduleApplicationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.FlexScheduleApplication}.
 */
@RestController
@RequestMapping("/api/attendance-mgt")
public class FlexScheduleApplicationResource {

    private final Logger log = LoggerFactory.getLogger(FlexScheduleApplicationResource.class);

    private static final String ENTITY_NAME = "flexScheduleApplication";
    private static final String RESOURCE_NAME = "FlexScheduleApplicationResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlexScheduleApplicationService flexScheduleApplicationService;
    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public FlexScheduleApplicationResource(
        FlexScheduleApplicationService flexScheduleApplicationService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.flexScheduleApplicationService = flexScheduleApplicationService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /flex-schedule-applications} : Create a new flexScheduleApplication.
     *
     * @param flexScheduleApplicationDTO the flexScheduleApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flexScheduleApplicationDTO, or with status {@code 400 (Bad Request)} if the flexScheduleApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flex-schedule-applications")
    public ResponseEntity<FlexScheduleApplicationDTO> createFlexScheduleApplication(
        @Valid @RequestBody FlexScheduleApplicationDTO flexScheduleApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FlexScheduleApplication : {}", flexScheduleApplicationDTO);
        if (flexScheduleApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new flexScheduleApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }

        User currentUser = currentEmployeeService.getCurrentUser().get();

        //auto approve when HR On Behalf Apply
        flexScheduleApplicationDTO.setStatus(Status.APPROVED);
        flexScheduleApplicationDTO.setCreatedAt(Instant.now());
        flexScheduleApplicationDTO.setAppliedAt(LocalDate.now());
        flexScheduleApplicationDTO.setCreatedById(currentUser.getId());
        flexScheduleApplicationDTO.setSanctionedAt(Instant.now());
        flexScheduleApplicationDTO.setSanctionedById(currentUser.getId());
        flexScheduleApplicationDTO.setAppliedById(currentUser.getId());

        FlexScheduleApplicationDTO result = flexScheduleApplicationService.save(flexScheduleApplicationDTO);
        eventLoggingPublisher.publishEvent(currentUser, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity
            .created(new URI("/api/attendance-mgt/flex-schedule-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flex-schedule-applications/:id} : Updates an existing flexScheduleApplication.
     *
     * @param id the id of the flexScheduleApplicationDTO to update.
     * @param flexScheduleApplicationDTO the flexScheduleApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexScheduleApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the flexScheduleApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flexScheduleApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flex-schedule-applications/{id}")
    public ResponseEntity<FlexScheduleApplicationDTO> updateFlexScheduleApplication(
        @PathVariable Long id,
        @Valid @RequestBody FlexScheduleApplicationDTO flexScheduleApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FlexScheduleApplication : {}", flexScheduleApplicationDTO);

        if (flexScheduleApplicationDTO.getId() == null || !flexScheduleApplicationDTO.getId().equals(id)) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        User currentUser = currentEmployeeService.getCurrentUser().get();
        flexScheduleApplicationDTO.setUpdatedAt(Instant.now());
        flexScheduleApplicationDTO.setUpdatedById(currentUser.getId());
        flexScheduleApplicationDTO.setSanctionedAt(Instant.now());
        flexScheduleApplicationDTO.setSanctionedById(currentUser.getId());

        FlexScheduleApplicationDTO result = flexScheduleApplicationService.save(flexScheduleApplicationDTO);
        eventLoggingPublisher.publishEvent(currentUser, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flexScheduleApplicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /flex-schedule-applications} : get all the flexScheduleApplications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flexScheduleApplications in body.
     */
    @GetMapping("/flex-schedule-applications")
    public ResponseEntity<List<FlexScheduleApplicationDTO>> getAllFlexScheduleApplications(
        @RequestParam(required = false) Long employeeId,
        @RequestParam(required = false) List<Long> timeSlotIdList,
        @RequestParam(value = "startDate", required = false) LocalDate startDate,
        @RequestParam(value = "endDate", required = false) LocalDate endDate,
        @RequestParam(required = false) Status status,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of FlexScheduleApplications");
        Page<FlexScheduleApplicationDTO> page = flexScheduleApplicationService.findAll(
            employeeId,
            timeSlotIdList,
            startDate,
            endDate,
            status,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User currentUser = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(currentUser, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flex-schedule-applications/:id} : get the "id" flexScheduleApplication.
     *
     * @param id the id of the flexScheduleApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flexScheduleApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flex-schedule-applications/{id}")
    public ResponseEntity<FlexScheduleApplicationDTO> getFlexScheduleApplication(@PathVariable Long id) {
        log.debug("REST request to get FlexScheduleApplication : {}", id);
        Optional<FlexScheduleApplicationDTO> flexScheduleApplicationDTO = flexScheduleApplicationService.findOne(id);
        User currentUser = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(currentUser, flexScheduleApplicationDTO, RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(flexScheduleApplicationDTO);
    }

    /**
     * {@code DELETE  /flex-schedule-applications/:id} : delete the "id" flexScheduleApplication.
     *
     * @param id the id of the flexScheduleApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flex-schedule-applications/{id}")
    public ResponseEntity<Void> deleteFlexScheduleApplication(@PathVariable Long id) {
        log.debug("REST request to delete FlexScheduleApplication : {}", id);
        Optional<FlexScheduleApplicationDTO> flexScheduleApplicationDTO = flexScheduleApplicationService.findOne(id);
        flexScheduleApplicationService.delete(id);
        User currentUser = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(currentUser, flexScheduleApplicationDTO, RequestMethod.DELETE, RESOURCE_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
