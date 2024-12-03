package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import com.bits.hr.service.procurementRequisition.ProcReqMasterService;
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
 * REST controller for managing {@link com.bits.hr.domain.ProcReqMaster}.
 */
@RestController
@RequestMapping("/api/procurement-mgt")
public class ProcReqMasterResource {

    private final Logger log = LoggerFactory.getLogger(ProcReqMasterResource.class);

    private static final String ENTITY_NAME = "procReqMaster";
    private static final String RESOURCE_NAME = "procReqMasterResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcReqMasterService procReqMasterService;
    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public ProcReqMasterResource(
        ProcReqMasterService procReqMasterService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.procReqMasterService = procReqMasterService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /proc-req-masters} : Create a new procReqMaster.
     *
     * @param procReqMasterDTO the procReqMasterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new procReqMasterDTO, or with status {@code 400 (Bad Request)} if the procReqMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proc-req-masters")
    public ResponseEntity<ProcReqMasterDTO> createProcReqMaster(@Valid @RequestBody ProcReqMasterDTO procReqMasterDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProcReqMaster : {}", procReqMasterDTO);
        if (procReqMasterDTO.getId() != null) {
            throw new BadRequestAlertException("A new procReqMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcReqMasterDTO result = procReqMasterService.save(procReqMasterDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity
            .created(new URI("/api/proc-req-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proc-req-masters} : Updates an existing procReqMaster.
     *
     * @param procReqMasterDTO the procReqMasterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procReqMasterDTO,
     * or with status {@code 400 (Bad Request)} if the procReqMasterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the procReqMasterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proc-req-masters")
    public ResponseEntity<ProcReqMasterDTO> updateProcReqMaster(@Valid @RequestBody ProcReqMasterDTO procReqMasterDTO)
        throws URISyntaxException {
        log.debug("REST request to update ProcReqMaster : {}", procReqMasterDTO);
        if (procReqMasterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProcReqMasterDTO result = procReqMasterService.save(procReqMasterDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, procReqMasterDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/proc-req-masters/close/{id}")
    public ResponseEntity<Boolean> closeProcReqMaster(@PathVariable Long id) {
        procReqMasterService.close(id, currentEmployeeService.getCurrentEmployeeId().get());

        Optional<ProcReqMasterDTO> procReqMasterDTO = procReqMasterService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, procReqMasterDTO, RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity.ok().body(true);
    }

    /**
     * {@code GET  /proc-req-masters} : get all the procReqMasters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of procReqMasters in body.
     */
    @GetMapping("/proc-req-masters")
    public ResponseEntity<List<ProcReqMasterDTO>> getAllProcReqMasters(
        @RequestParam(required = false) Long employeeId,
        @RequestParam(required = false) Long departmentId,
        @RequestParam(required = false) RequisitionStatus requisitionStatus,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProcReqMasters");
        Page<ProcReqMasterDTO> page = procReqMasterService.findAll(employeeId, departmentId, requisitionStatus, year, month, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proc-req-masters/:id} : get the "id" procReqMaster.
     *
     * @param id the id of the procReqMasterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the procReqMasterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proc-req-masters/{id}")
    public ResponseEntity<ProcReqMasterDTO> getProcReqMaster(@PathVariable Long id) {
        log.debug("REST request to get ProcReqMaster : {}", id);
        Optional<ProcReqMasterDTO> procReqMasterDTO = procReqMasterService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, procReqMasterDTO, RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(procReqMasterDTO);
    }

    /**
     * {@code DELETE  /proc-req-masters/:id} : delete the "id" procReqMaster.
     *
     * @param id the id of the procReqMasterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proc-req-masters/{id}")
    public ResponseEntity<Void> deleteProcReqMaster(@PathVariable Long id) {
        log.debug("REST request to delete ProcReqMaster : {}", id);
        Optional<ProcReqMasterDTO> procReqMasterDTO = procReqMasterService.findOne(id);
        procReqMasterService.delete(id, false);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, procReqMasterDTO, RequestMethod.DELETE, "ProcReqOfficerResource");
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
