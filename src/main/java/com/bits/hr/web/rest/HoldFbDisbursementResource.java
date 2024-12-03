package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.HoldFbDisbursementService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.HoldFbDisbursementDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.bits.hr.util.annotation.ValidateNaturalText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.HoldFbDisbursement}.
 */
@Validated
@RestController
@RequestMapping("/api/payroll-mgt")
public class HoldFbDisbursementResource {

    private final Logger log = LoggerFactory.getLogger(HoldFbDisbursementResource.class);

    private static final String ENTITY_NAME = "holdFbDisbursement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HoldFbDisbursementService holdFbDisbursementService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public HoldFbDisbursementResource(
        HoldFbDisbursementService holdFbDisbursementService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.holdFbDisbursementService = holdFbDisbursementService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /hold-fb-disbursements} : Create a new holdFbDisbursement.
     *
     * @param holdFbDisbursementDTO the holdFbDisbursementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new holdFbDisbursementDTO, or with status {@code 400 (Bad Request)} if the holdFbDisbursement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hold-fb-disbursements")
    public ResponseEntity<HoldFbDisbursementDTO> createHoldFbDisbursement(@Valid @RequestBody HoldFbDisbursementDTO holdFbDisbursementDTO)
        throws URISyntaxException {
        log.debug("REST request to save HoldFbDisbursement : {}", holdFbDisbursementDTO);
        if (holdFbDisbursementDTO.getId() != null) {
            throw new BadRequestAlertException("A new holdFbDisbursement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        holdFbDisbursementDTO.setDisbursedById(currentEmployeeService.getCurrentUserId().get());
        HoldFbDisbursementDTO result = holdFbDisbursementService.save(holdFbDisbursementDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "HoldFbDisbursement");
        return ResponseEntity
            .created(new URI("/api/hold-fb-disbursements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hold-fb-disbursements} : Updates an existing holdFbDisbursement.
     *
     * @param holdFbDisbursementDTO the holdFbDisbursementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holdFbDisbursementDTO,
     * or with status {@code 400 (Bad Request)} if the holdFbDisbursementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the holdFbDisbursementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hold-fb-disbursements")
    public ResponseEntity<HoldFbDisbursementDTO> updateHoldFbDisbursement(@Valid @RequestBody HoldFbDisbursementDTO holdFbDisbursementDTO)
        throws URISyntaxException {
        log.debug("REST request to update HoldFbDisbursement : {}", holdFbDisbursementDTO);
        if (holdFbDisbursementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HoldFbDisbursementDTO result = holdFbDisbursementService.save(holdFbDisbursementDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "HoldFbDisbursement");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, holdFbDisbursementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hold-fb-disbursements} : get all the holdFbDisbursements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of holdFbDisbursements in body.
     */
    @GetMapping("/hold-fb-disbursements")
    public ResponseEntity<List<HoldFbDisbursementDTO>> getAllHoldFbDisbursements(
        @RequestParam(required = false) @ValidateNaturalText String searchText,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of HoldFbDisbursements");
        Page<HoldFbDisbursementDTO> page = holdFbDisbursementService.findAll(searchText, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "HoldFbDisbursement");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hold-fb-disbursements/:id} : get the "id" holdFbDisbursement.
     *
     * @param id the id of the holdFbDisbursementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the holdFbDisbursementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hold-fb-disbursements/{id}")
    public ResponseEntity<HoldFbDisbursementDTO> getHoldFbDisbursement(@PathVariable Long id) {
        log.debug("REST request to get HoldFbDisbursement : {}", id);
        Optional<HoldFbDisbursementDTO> holdFbDisbursementDTO = holdFbDisbursementService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (holdFbDisbursementDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, holdFbDisbursementDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(holdFbDisbursementDTO);
    }

    /**
     * {@code DELETE  /hold-fb-disbursements/:id} : delete the "id" holdFbDisbursement.
     *
     * @param id the id of the holdFbDisbursementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hold-fb-disbursements/{id}")
    public ResponseEntity<Void> deleteHoldFbDisbursement(@PathVariable Long id) {
        log.debug("REST request to delete HoldFbDisbursement : {}", id);
        Optional<HoldFbDisbursementDTO> holdFbDisbursementDTO = holdFbDisbursementService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (holdFbDisbursementDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, holdFbDisbursementDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        holdFbDisbursementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
