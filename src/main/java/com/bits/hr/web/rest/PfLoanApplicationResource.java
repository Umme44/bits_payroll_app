package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.PfLoanApplicationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.*;
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

import javax.validation.Valid;

/**
 * REST controller for managing {@link com.bits.hr.domain.PfLoanApplication}.
 */
@RestController
@RequestMapping("/api/pf-mgt/pf-loan-applications")
public class PfLoanApplicationResource {

    private final Logger log = LoggerFactory.getLogger(PfLoanApplicationResource.class);

    private static final String ENTITY_NAME = "pfLoanApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PfLoanApplicationService pfLoanApplicationService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public PfLoanApplicationResource(
        PfLoanApplicationService pfLoanApplicationService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.pfLoanApplicationService = pfLoanApplicationService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /pf-loan-applications} : Create a new pfLoanApplication.
     *
     * @param pfLoanApplicationDTO the pfLoanApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfLoanApplicationDTO, or with status {@code 400 (Bad Request)} if the pfLoanApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PfLoanApplicationDTO> createPfLoanApplication(@RequestBody @Valid PfLoanApplicationDTO pfLoanApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save PfLoanApplication : {}", pfLoanApplicationDTO);
        if (pfLoanApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new pfLoanApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PfLoanApplicationDTO result = pfLoanApplicationService.save(pfLoanApplicationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "PfLoanApplication");
        return ResponseEntity
            .created(new URI("/api/pf-mgt/pf-loan-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pf-loan-applications} : Updates an existing pfLoanApplication.
     *
     * @param pfLoanApplicationDTO the pfLoanApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pfLoanApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the pfLoanApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pfLoanApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<PfLoanApplicationDTO> updatePfLoanApplication(@RequestBody @Valid PfLoanApplicationDTO pfLoanApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to update PfLoanApplication : {}", pfLoanApplicationDTO);
        if (pfLoanApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PfLoanApplicationDTO result = pfLoanApplicationService.save(pfLoanApplicationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "PfLoanApplication");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfLoanApplicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pf-loan-applications} : get all the pfLoanApplications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pfLoanApplications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PfLoanApplicationDTO>> getAllPfLoanApplications(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PfLoanApplications");
        Page<PfLoanApplicationDTO> page = pfLoanApplicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfLoanApplication");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pf-loan-applications/:id} : get the "id" pfLoanApplication.
     *
     * @param id the id of the pfLoanApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pfLoanApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PfLoanApplicationDTO> getPfLoanApplication(@PathVariable Long id) {
        log.debug("REST request to get PfLoanApplication : {}", id);
        Optional<PfLoanApplicationDTO> pfLoanApplicationDTO = pfLoanApplicationService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (pfLoanApplicationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfLoanApplicationDTO.get(), RequestMethod.GET, "PfLoanApplication");
        }
        return ResponseUtil.wrapOrNotFound(pfLoanApplicationDTO);
    }

    /**
     * {@code DELETE  /pf-loan-applications/:id} : delete the "id" pfLoanApplication.
     *
     * @param id the id of the pfLoanApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfLoanApplication(@PathVariable Long id) {
        log.debug("REST request to delete PfLoanApplication : {}", id);
        Optional<PfLoanApplicationDTO> pfLoanApplicationDTO = pfLoanApplicationService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (pfLoanApplicationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfLoanApplicationDTO.get(), RequestMethod.DELETE, "PfLoanApplication");
        }
        pfLoanApplicationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST  /pf-loan-applications} : Approve a pfLoanApplication.
     *
     * @param pfLoanApplicationFormDTO the pfLoanDTO to create a new pfLoan.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfLoanApplicationDTO, or with status {@code 400 (Bad Request)} if the pfLoanApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hr/approval")
    public ResponseEntity<PfLoanDTO> approvePfLoanApplication(@RequestBody PfLoanApplicationFormDTO pfLoanApplicationFormDTO)
        throws URISyntaxException, Exception {
        log.debug("REST request to pf loan application approve");
        PfLoanDTO result = pfLoanApplicationService.approve(pfLoanApplicationFormDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "pfLoan", result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /pf-loan-applications} : Reject a pfLoanApplication.
     *
     * @param pfLoanApplicationFormDTO to .
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfLoanApplicationDTO, or with status {@code 400 (Bad Request)} if the pfLoanApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hr/reject")
    public ResponseEntity<PfLoanApplicationDTO> rejectPfLoanApplication(@RequestBody PfLoanApplicationFormDTO pfLoanApplicationFormDTO)
        throws URISyntaxException, Exception {
        log.debug("REST request to pf loan application approve");
        PfLoanApplicationDTO result = pfLoanApplicationService.reject(pfLoanApplicationFormDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "pfLoanApplicationFormDTO", result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /check-eligibility/{pfAccountId} } : check pf loan eligibility.
     *
     * @param pfAccountId
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the details of PfLoanApplicationFormDTO in body.
     */
    @GetMapping("/check-eligibility/{pfAccountId}")
    public ResponseEntity<PfLoanApplicationEligibleDTO> getPfLoanApplicationEligibility(@PathVariable long pfAccountId) {
        log.debug("REST request to get eligibility of pf loan application");
        PfLoanApplicationEligibleDTO result = pfLoanApplicationService.checkPfLoanApplicationEligibility(pfAccountId);
        return ResponseEntity.ok().body(result);
    }
}
