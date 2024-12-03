package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FinalSettlementService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.FinalSettlementDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.FinalSettlement}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class FinalSettlementResource {

    private final Logger log = LoggerFactory.getLogger(FinalSettlementResource.class);

    private static final String ENTITY_NAME = "finalSettlement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinalSettlementService finalSettlementService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public FinalSettlementResource(
        FinalSettlementService finalSettlementService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.finalSettlementService = finalSettlementService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /final-settlements} : Create a new finalSettlement.
     *
     * @param finalSettlementDTO the finalSettlementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new finalSettlementDTO, or with status {@code 400 (Bad Request)} if the finalSettlement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/final-settlements")
    public ResponseEntity<FinalSettlementDTO> createFinalSettlement(@Valid @RequestBody FinalSettlementDTO finalSettlementDTO)
        throws URISyntaxException {
        log.debug("REST request to save FinalSettlement : {}", finalSettlementDTO);
        if (finalSettlementDTO.getId() != null) {
            throw new BadRequestAlertException("A new finalSettlement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinalSettlementDTO result = finalSettlementService.save(finalSettlementDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "FinalSettlement");

        return ResponseEntity
            .created(new URI("/api/final-settlements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /final-settlements} : Updates an existing finalSettlement.
     *
     * @param finalSettlementDTO the finalSettlementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finalSettlementDTO,
     * or with status {@code 400 (Bad Request)} if the finalSettlementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the finalSettlementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/final-settlements")
    public ResponseEntity<FinalSettlementDTO> updateFinalSettlement(@Valid @RequestBody FinalSettlementDTO finalSettlementDTO)
        throws URISyntaxException {
        log.debug("REST request to update FinalSettlement : {}", finalSettlementDTO);
        if (finalSettlementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FinalSettlementDTO result = finalSettlementService.update(finalSettlementDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "FinalSettlement");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finalSettlementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /final-settlements} : get all the finalSettlements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of finalSettlements in body.
     */
    @GetMapping("/final-settlements")
    public ResponseEntity<List<FinalSettlementDTO>> getAllFinalSettlements(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of FinalSettlements");
        Page<FinalSettlementDTO> page = finalSettlementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "FinalSettlement");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /final-settlements/:id} : get the "id" finalSettlement.
     *
     * @param id the id of the finalSettlementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the finalSettlementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/final-settlements/{id}")
    public ResponseEntity<FinalSettlementDTO> getFinalSettlement(@PathVariable Long id) {
        log.debug("REST request to get FinalSettlement : {}", id);
        Optional<FinalSettlementDTO> finalSettlementDTO = finalSettlementService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (finalSettlementDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, finalSettlementDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(finalSettlementDTO);
    }

    /**
     * {@code DELETE  /final-settlements/:id} : delete the "id" finalSettlement.
     *
     * @param id the id of the finalSettlementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/final-settlements/{id}")
    public ResponseEntity<Void> deleteFinalSettlement(@PathVariable Long id) {
        log.debug("REST request to delete FinalSettlement : {}", id);
        Optional<FinalSettlementDTO> finalSettlementDTO = finalSettlementService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (finalSettlementDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, finalSettlementDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        finalSettlementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
