package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.PfLoanApplicationFormService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.PfLoanApplication}.
 */
@RestController
@RequestMapping("/api/common/pf/pf-loan-application-form")
public class PfLoanApplicationFormResource {

    private final Logger log = LoggerFactory.getLogger(PfLoanApplicationFormResource.class);
    private static final String ENTITY_NAME = "pfLoanApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PfLoanApplicationFormService pfLoanApplicationFormService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public PfLoanApplicationFormResource(
        PfLoanApplicationFormService pfLoanApplicationFormService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.pfLoanApplicationFormService = pfLoanApplicationFormService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /pf-loan-application-form} : Create a new pfLoanApplication Form.
     *
     * @param pfLoanApplicationDTO the pfLoanApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfLoanApplicationDTO, or with status {@code 400 (Bad Request)} if the pfLoanApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("")
    public ResponseEntity<PfLoanApplicationDTO> createPfLoanApplication(@RequestBody PfLoanApplicationDTO pfLoanApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save PfLoanApplication: {}", pfLoanApplicationDTO);
        if (pfLoanApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new pfLoanApplication cannot already have an ID", ENTITY_NAME, "idexits");
        }

        PfLoanApplicationDTO result = pfLoanApplicationFormService.save(pfLoanApplicationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "PfLoanApplicationForm");
        return ResponseEntity
            .created((new URI("api/common/pf/pf-loan-application-form" + result.getId())))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pf-loan-application-form} : Updates an existing pfLoanApplication.
     *
     * @param pfLoanApplicationDTO the pfLoanApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pfLoanApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the pfLoanApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pfLoanApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<PfLoanApplicationDTO> updatePfLoanApplication(@RequestBody PfLoanApplicationDTO pfLoanApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to update PfLoanApplication : {}", pfLoanApplicationDTO);
        if (pfLoanApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PfLoanApplicationDTO result = pfLoanApplicationFormService.save(pfLoanApplicationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "PfLoanApplicationForm");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfLoanApplicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pf-loan-application-form} : get all the pfLoanApplications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pfLoanApplications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PfLoanApplicationDTO>> getLoanApplication(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PfLoanApplications");
        Page<PfLoanApplicationDTO> page = pfLoanApplicationFormService.findByPin(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfLoanApplicationForm");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pf-loan-application-form/:id} : get the "id" pfLoanApplication.
     *
     * @param id the id of the pfLoanApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pfLoanApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PfLoanApplicationDTO> getPfLoanApplication(@PathVariable Long id) {
        log.debug("REST request to get PfLoanApplication : {}", id);
        Optional<PfLoanApplicationDTO> pfLoanApplicationDTO = pfLoanApplicationFormService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (pfLoanApplicationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfLoanApplicationDTO.get(), RequestMethod.GET, "PfLoanApplicationForm");
        }
        return ResponseUtil.wrapOrNotFound(pfLoanApplicationDTO);
    }

    /**
     * {@code DELETE  /pf-loan-application-form/:id} : delete the "id" pfLoanApplication.
     *
     * @param id the id of the pfLoanApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfLoanApplication(@PathVariable Long id) {
        log.debug("REST request to delete PfLoanApplication : {}", id);
        Optional<PfLoanApplicationDTO> pfLoanApplicationDTO = pfLoanApplicationFormService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (pfLoanApplicationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfLoanApplicationDTO.get(), RequestMethod.GET, "PfLoanApplicationForm");
        }
        pfLoanApplicationFormService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /pf-loan-application-form/pf-accounts} : get the pfAccountsByPin.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pfAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pf-accounts")
    public ResponseEntity<List<PfAccountDTO>> getPfAccountsOfCurrentUser() throws Exception {
        log.debug("REST request to get current employee pf accounts:");
        List<PfAccountDTO> pfAccountList = pfLoanApplicationFormService.findPfAccountsOfCurrentUser();
        return ResponseEntity.ok().body(pfAccountList);
    }

    /**
     * {@code GET  /pf-loan-eligible-amount} : get the pf loan eligible amount.
     *
     * @return double type amount
     */
    @GetMapping("/pf-loan-eligible-amount")
    public double getEmployeePfLoanEligibleAmount() throws Exception {
        log.debug("REST request to get user pf loan eligible amount ");
        double result = pfLoanApplicationFormService.getPfLoanEligibleAmount();
        return result;
    }

    /**
     * {@code GET  /pf-loan-eligible-amount} : get the user has any pf account.
     *
     * @return boolean type
     */
    @GetMapping("/check-any-pf-account")
    public boolean hasAnyPfAccountForThisUser() throws Exception {
        log.debug("REST request to check any pf account for this user");
        boolean result = pfLoanApplicationFormService.checkAnyPfAccountForThisUser();
        return result;
    }
}
