package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.BankBranchService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.BankBranchDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.BankBranch}.
 */
@RestController
@RequestMapping("/api/employee-mgt/bank-branches")
public class BankBranchResource {

    private static final String ENTITY_NAME = "bankBranch";
    private final Logger log = LoggerFactory.getLogger(BankBranchResource.class);
    private final BankBranchService bankBranchService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public BankBranchResource(
        BankBranchService bankBranchService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.bankBranchService = bankBranchService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /bank-branches} : Create a new bankBranch.
     *
     * @param bankBranchDTO the bankBranchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankBranchDTO, or with status {@code 400 (Bad Request)} if the bankBranch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BankBranchDTO> createBankBranch(@Valid @RequestBody BankBranchDTO bankBranchDTO) throws URISyntaxException {
        log.debug("REST request to save BankBranch : {}", bankBranchDTO);
        if (bankBranchDTO.getId() != null) {
            throw new BadRequestAlertException("A new bankBranch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BankBranchDTO result = bankBranchService.save(bankBranchDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "BankBranch");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/bank-branches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bank-branches} : Updates an existing bankBranch.
     *
     * @param bankBranchDTO the bankBranchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankBranchDTO,
     * or with status {@code 400 (Bad Request)} if the bankBranchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankBranchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<BankBranchDTO> updateBankBranch(@Valid @RequestBody BankBranchDTO bankBranchDTO) throws URISyntaxException {
        log.debug("REST request to update BankBranch : {}", bankBranchDTO);
        if (bankBranchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BankBranchDTO result = bankBranchService.save(bankBranchDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "BankBranch");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankBranchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bank-branches} : get all the bankBranches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankBranches in body.
     */
    @GetMapping("")
    public List<BankBranchDTO> getAllBankBranches() {
        log.debug("REST request to get all BankBranches");
        return bankBranchService.findAll();
    }


    @GetMapping("/page")
    public ResponseEntity<List<BankBranchDTO>> getNationalitiesPage(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of BankBranches");
        Page<BankBranchDTO> page = bankBranchService.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    /**
     * {@code GET  /bank-branches/:id} : get the "id" bankBranch.
     *
     * @param id the id of the bankBranchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankBranchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BankBranchDTO> getBankBranch(@PathVariable Long id) {
        log.debug("REST request to get BankBranch : {}", id);
        Optional<BankBranchDTO> bankBranchDTO = bankBranchService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (bankBranchDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, bankBranchDTO.get(), RequestMethod.GET, "BankBranch");
        }

        return ResponseUtil.wrapOrNotFound(bankBranchDTO);
    }

    /**
     * {@code DELETE  /bank-branches/:id} : delete the "id" bankBranch.
     *
     * @param id the id of the bankBranchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankBranch(@PathVariable Long id) {
        log.debug("REST request to delete BankBranch : {}", id);
        Optional<BankBranchDTO> bankBranchDTO = bankBranchService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (bankBranchDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, bankBranchDTO.get(), RequestMethod.DELETE, "BankBranch");
        }
        bankBranchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
