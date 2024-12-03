package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.PfLoanRepaymentService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.PfLoanRepaymentDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.PfLoanRepayment}.
 */
@RestController
@RequestMapping("/api/payroll-mgt/pf-loan-repayments")
public class PfLoanRepaymentResource {

    private final Logger log = LoggerFactory.getLogger(PfLoanRepaymentResource.class);

    private static final String ENTITY_NAME = "pfLoanRepayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PfLoanRepaymentService pfLoanRepaymentService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public PfLoanRepaymentResource(
        PfLoanRepaymentService pfLoanRepaymentService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.pfLoanRepaymentService = pfLoanRepaymentService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /pf-loan-repayments} : Create a new pfLoanRepayment.
     *
     * @param pfLoanRepaymentDTO the pfLoanRepaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfLoanRepaymentDTO, or with status {@code 400 (Bad Request)} if the pfLoanRepayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PfLoanRepaymentDTO> createPfLoanRepayment(@RequestBody PfLoanRepaymentDTO pfLoanRepaymentDTO)
        throws URISyntaxException {
        log.debug("REST request to save PfLoanRepayment : {}", pfLoanRepaymentDTO);
        if (pfLoanRepaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new pfLoanRepayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PfLoanRepaymentDTO result = pfLoanRepaymentService.save(pfLoanRepaymentDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "PfLoanRepayment");
        return ResponseEntity
            .created(new URI("/api/payroll-mgt/pf-loan-repayments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pf-loan-repayments} : Updates an existing pfLoanRepayment.
     *
     * @param pfLoanRepaymentDTO the pfLoanRepaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pfLoanRepaymentDTO,
     * or with status {@code 400 (Bad Request)} if the pfLoanRepaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pfLoanRepaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<PfLoanRepaymentDTO> updatePfLoanRepayment(@RequestBody PfLoanRepaymentDTO pfLoanRepaymentDTO)
        throws URISyntaxException {
        log.debug("REST request to update PfLoanRepayment : {}", pfLoanRepaymentDTO);
        if (pfLoanRepaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PfLoanRepaymentDTO result = pfLoanRepaymentService.save(pfLoanRepaymentDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "PfLoanRepayment");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfLoanRepaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pf-loan-repayments} : get all the pfLoanRepayments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pfLoanRepayments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PfLoanRepaymentDTO>> getAllPfLoanRepayments(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PfLoanRepayments");
        Page<PfLoanRepaymentDTO> page = pfLoanRepaymentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfLoanRepayment");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pf-loan-repayments/:id} : get the "id" pfLoanRepayment.
     *
     * @param id the id of the pfLoanRepaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pfLoanRepaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PfLoanRepaymentDTO> getPfLoanRepayment(@PathVariable Long id) {
        log.debug("REST request to get PfLoanRepayment : {}", id);
        Optional<PfLoanRepaymentDTO> pfLoanRepaymentDTO = pfLoanRepaymentService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (pfLoanRepaymentDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfLoanRepaymentDTO.get(), RequestMethod.GET, "PfLoanRepayment");
        }
        return ResponseUtil.wrapOrNotFound(pfLoanRepaymentDTO);
    }

    /**
     * {@code DELETE  /pf-loan-repayments/:id} : delete the "id" pfLoanRepayment.
     *
     * @param id the id of the pfLoanRepaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfLoanRepayment(@PathVariable Long id) {
        log.debug("REST request to delete PfLoanRepayment : {}", id);
        Optional<PfLoanRepaymentDTO> pfLoanRepaymentDTO = pfLoanRepaymentService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (pfLoanRepaymentDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfLoanRepaymentDTO.get(), RequestMethod.GET, "PfLoanRepayment");
        }
        pfLoanRepaymentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/{year}/{month}")
    public List<PfLoanRepaymentDTO> getAllMobileBillsByYearAndMonth(@PathVariable int year, @PathVariable int month) {
        log.debug("REST request to get all pf loan repayments By year and month");
        return pfLoanRepaymentService.findAllByYearAndMonth(year, month);
    }
}
