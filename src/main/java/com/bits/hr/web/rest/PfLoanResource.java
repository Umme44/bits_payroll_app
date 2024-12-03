package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.PfLoanService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.PfLoanDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.PfLoan}.
 */
@RestController
@RequestMapping("/api/pf-mgt/pf-loans")
public class PfLoanResource {

    private final Logger log = LoggerFactory.getLogger(PfLoanResource.class);

    private static final String ENTITY_NAME = "pfLoan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PfLoanService pfLoanService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public PfLoanResource(
        PfLoanService pfLoanService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.pfLoanService = pfLoanService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /pf-loans} : Create a new pfLoan.
     *
     * @param pfLoanDTO the pfLoanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfLoanDTO, or with status {@code 400 (Bad Request)} if the pfLoan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PfLoanDTO> createPfLoan(@RequestBody PfLoanDTO pfLoanDTO) throws URISyntaxException {
        log.debug("REST request to save PfLoan : {}", pfLoanDTO);
        if (pfLoanDTO.getId() != null) {
            throw new BadRequestAlertException("A new pfLoan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PfLoanDTO result = pfLoanService.save(pfLoanDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "PfLoan");
        return ResponseEntity
            .created(new URI("/api/pf-mgt/pf-loans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pf-loans} : Updates an existing pfLoan.
     *
     * @param pfLoanDTO the pfLoanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pfLoanDTO,
     * or with status {@code 400 (Bad Request)} if the pfLoanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pfLoanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<PfLoanDTO> updatePfLoan(@RequestBody PfLoanDTO pfLoanDTO) throws URISyntaxException {
        log.debug("REST request to update PfLoan : {}", pfLoanDTO);
        if (pfLoanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PfLoanDTO result = pfLoanService.save(pfLoanDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "PfLoan");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfLoanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pf-loans} : get all the pfLoans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pfLoans in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PfLoanDTO>> getAllPfLoans(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PfLoans");
        Page<PfLoanDTO> page = pfLoanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfLoan");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pf-loans/:id} : get the "id" pfLoan.
     *
     * @param id the id of the pfLoanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pfLoanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PfLoanDTO> getPfLoan(@PathVariable Long id) {
        log.debug("REST request to get PfLoan : {}", id);
        Optional<PfLoanDTO> pfLoanDTO = pfLoanService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (pfLoanDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfLoanDTO.get(), RequestMethod.GET, "PfLoan");
        }
        return ResponseUtil.wrapOrNotFound(pfLoanDTO);
    }

    /**
     * {@code DELETE  /pf-loans/:id} : delete the "id" pfLoan.
     *
     * @param id the id of the pfLoanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfLoan(@PathVariable Long id) {
        log.debug("REST request to delete PfLoan : {}", id);
        Optional<PfLoanDTO> pfLoanDTO = pfLoanService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (pfLoanDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfLoanDTO.get(), RequestMethod.DELETE, "PfLoan");
        }
        pfLoanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
