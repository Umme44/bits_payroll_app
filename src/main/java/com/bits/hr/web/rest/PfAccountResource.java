package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.PfAccountService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.PfAccountDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;
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
 * REST controller for managing {@link com.bits.hr.domain.PfAccount}.
 */
@RestController
@RequestMapping("/api/pf-mgt/pf-accounts")
public class PfAccountResource {

    private final Logger log = LoggerFactory.getLogger(PfAccountResource.class);

    private static final String ENTITY_NAME = "pfAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PfAccountService pfAccountService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public PfAccountResource(
        PfAccountService pfAccountService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.pfAccountService = pfAccountService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /pf-accounts} : Create a new pfAccount.
     *
     * @param pfAccountDTO the pfAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfAccountDTO, or with status {@code 400 (Bad Request)} if the pfAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PfAccountDTO> createPfAccount(@RequestBody @Valid PfAccountDTO pfAccountDTO) throws URISyntaxException {
        log.debug("REST request to save PfAccount : {}", pfAccountDTO);
        if (pfAccountDTO.getMembershipEndDate() != null) {
            long daysBetween = ChronoUnit.DAYS.between(pfAccountDTO.getMembershipStartDate(), pfAccountDTO.getMembershipEndDate());
            if (daysBetween < 0) {
                throw new BadRequestAlertException("End date cannot less than Start date", ENTITY_NAME, "endDateError");
            }
        }
        if (pfAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new pfAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PfAccountDTO result = pfAccountService.create(pfAccountDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "PfAccount");
        return ResponseEntity
            .created(new URI("/api/pf-mgt/pf-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pf-accounts} : Updates an existing pfAccount.
     *
     * @param pfAccountDTO the pfAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pfAccountDTO,
     * or with status {@code 400 (Bad Request)} if the pfAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pfAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<PfAccountDTO> updatePfAccount(@RequestBody @Valid PfAccountDTO pfAccountDTO) throws URISyntaxException {
        log.debug("REST request to update PfAccount : {}", pfAccountDTO);

        long daysBetween = 0l;
        if (pfAccountDTO.getMembershipEndDate() != null) {
            daysBetween = ChronoUnit.DAYS.between(pfAccountDTO.getMembershipStartDate(), pfAccountDTO.getMembershipEndDate());
        }
        if (daysBetween < 0) {
            throw new BadRequestAlertException("End date cannot less than Start date", ENTITY_NAME, "endDateError");
        }
        if (pfAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PfAccountDTO result = pfAccountService.update(pfAccountDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "PfAccount");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pf-accounts} : get all the pfAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pfAccounts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PfAccountDTO>> getAllPfAccounts(
        @RequestParam(required = false) String pin,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PfAccounts");
        Page<PfAccountDTO> page = pfAccountService.findAll(pin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfAccount");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pf-accounts} : get all the pfAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pfAccounts in body.
     */
    @GetMapping("/list")
    public ResponseEntity<List<PfAccountDTO>> getAllPfAccounts() {
        log.debug("REST request to get a page of PfAccounts");
        List<PfAccountDTO> result = pfAccountService.findAll();
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /pf-accounts/:id} : get the "id" pfAccount.
     *
     * @param id the id of the pfAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pfAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PfAccountDTO> getPfAccount(@PathVariable Long id) {
        log.debug("REST request to get PfAccount : {}", id);
        Optional<PfAccountDTO> pfAccountDTO = pfAccountService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (pfAccountDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfAccountDTO.get(), RequestMethod.GET, "PfAccount");
        }
        return ResponseUtil.wrapOrNotFound(pfAccountDTO);
    }

    /**
     * {@code DELETE  /pf-accounts/:id} : delete the "id" pfAccount.
     *
     * @param id the id of the pfAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfAccount(@PathVariable Long id) {
        log.debug("REST request to delete PfAccount : {}", id);
        Optional<PfAccountDTO> pfAccountDTO = pfAccountService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (pfAccountDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfAccountDTO.get(), RequestMethod.DELETE, "PfAccount");
        }
        pfAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
