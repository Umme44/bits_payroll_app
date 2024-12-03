package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.HoldSalaryDisbursementService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.HoldSalaryDisbursementDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.HoldSalaryDisbursement}.
 */
@RestController
@RequestMapping("/api/payroll-mgt/")
public class HoldSalaryDisbursementResource {

    private final Logger log = LoggerFactory.getLogger(HoldSalaryDisbursementResource.class);

    private static final String ENTITY_NAME = "holdSalaryDisbursement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HoldSalaryDisbursementService holdSalaryDisbursementService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public HoldSalaryDisbursementResource(
        HoldSalaryDisbursementService holdSalaryDisbursementService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.holdSalaryDisbursementService = holdSalaryDisbursementService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /hold-salary-disbursements} : Create a new holdSalaryDisbursement.
     *
     * @param holdSalaryDisbursementDTO the holdSalaryDisbursementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new holdSalaryDisbursementDTO, or with status {@code 400 (Bad Request)} if the holdSalaryDisbursement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hold-salary-disbursements")
    public ResponseEntity<HoldSalaryDisbursementDTO> createHoldSalaryDisbursement(
        @Valid @RequestBody HoldSalaryDisbursementDTO holdSalaryDisbursementDTO
    ) throws URISyntaxException {
        log.debug("REST request to save HoldSalaryDisbursement : {}", holdSalaryDisbursementDTO);
        if (holdSalaryDisbursementDTO.getId() != null) {
            throw new BadRequestAlertException("A new holdSalaryDisbursement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HoldSalaryDisbursementDTO result = holdSalaryDisbursementService.save(holdSalaryDisbursementDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "HoldSalaryDisbursement");
        return ResponseEntity
            .created(new URI("/api/hold-salary-disbursements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hold-salary-disbursements} : Updates an existing holdSalaryDisbursement.
     *
     * @param holdSalaryDisbursementDTO the holdSalaryDisbursementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holdSalaryDisbursementDTO,
     * or with status {@code 400 (Bad Request)} if the holdSalaryDisbursementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the holdSalaryDisbursementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hold-salary-disbursements")
    public ResponseEntity<HoldSalaryDisbursementDTO> updateHoldSalaryDisbursement(
        @Valid @RequestBody HoldSalaryDisbursementDTO holdSalaryDisbursementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HoldSalaryDisbursement : {}", holdSalaryDisbursementDTO);
        if (holdSalaryDisbursementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HoldSalaryDisbursementDTO result = holdSalaryDisbursementService.save(holdSalaryDisbursementDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "HoldSalaryDisbursement");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, holdSalaryDisbursementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hold-salary-disbursements} : get all the holdSalaryDisbursements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of holdSalaryDisbursements in body.
     */
    @GetMapping("/hold-salary-disbursements")
    public ResponseEntity<List<HoldSalaryDisbursementDTO>> getAllHoldSalaryDisbursements(
        @RequestParam(required = false) String searchText,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of HoldSalaryDisbursements");
        Page<HoldSalaryDisbursementDTO> page = holdSalaryDisbursementService.findAll(searchText, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "HoldSalaryDisbursement");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hold-salary-disbursements/:id} : get the "id" holdSalaryDisbursement.
     *
     * @param id the id of the holdSalaryDisbursementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the holdSalaryDisbursementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hold-salary-disbursements/{id}")
    public ResponseEntity<HoldSalaryDisbursementDTO> getHoldSalaryDisbursement(@PathVariable Long id) {
        log.debug("REST request to get HoldSalaryDisbursement : {}", id);
        Optional<HoldSalaryDisbursementDTO> holdSalaryDisbursementDTO = holdSalaryDisbursementService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (holdSalaryDisbursementDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, holdSalaryDisbursementDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(holdSalaryDisbursementDTO);
    }

    /**
     * {@code DELETE  /hold-salary-disbursements/:id} : delete the "id" holdSalaryDisbursement.
     *
     * @param id the id of the holdSalaryDisbursementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hold-salary-disbursements/{id}")
    public ResponseEntity<Void> deleteHoldSalaryDisbursement(@PathVariable Long id) {
        log.debug("REST request to delete HoldSalaryDisbursement : {}", id);
        Optional<HoldSalaryDisbursementDTO> holdSalaryDisbursementDTO = holdSalaryDisbursementService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (holdSalaryDisbursementDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, holdSalaryDisbursementDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        holdSalaryDisbursementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
