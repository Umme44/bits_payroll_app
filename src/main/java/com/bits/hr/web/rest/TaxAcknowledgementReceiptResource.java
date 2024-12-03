package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.TaxAcknowledgementReceiptService;
import com.bits.hr.service.communication.sheduledEmail.TaxAcknowledgementReminderService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TaxAcknowledgementReceiptDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.TaxAcknowledgementReceipt}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class TaxAcknowledgementReceiptResource {

    private final Logger log = LoggerFactory.getLogger(TaxAcknowledgementReceiptResource.class);

    private static final String ENTITY_NAME = "taxAcknowledgementReceipt";
    private static final String RESOURCE_NAME = "TaxAcknowledgementReceiptResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxAcknowledgementReceiptService taxAcknowledgementReceiptService;
    private final TaxAcknowledgementReminderService taxAcknowledgementReminderService;
    private final EventLoggingPublisher eventLoggingPublisher;
    private final CurrentEmployeeService currentEmployeeService;

    public TaxAcknowledgementReceiptResource(
        TaxAcknowledgementReceiptService taxAcknowledgementReceiptService,
        TaxAcknowledgementReminderService taxAcknowledgementReminderService,
        EventLoggingPublisher eventLoggingPublisher,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.taxAcknowledgementReceiptService = taxAcknowledgementReceiptService;
        this.taxAcknowledgementReminderService = taxAcknowledgementReminderService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.currentEmployeeService = currentEmployeeService;
    }

    /**
     * {@code POST  /tax-acknowledgement-receipts} : Create a new taxAcknowledgementReceipt.
     *
     * @param taxAcknowledgementReceiptDTO the taxAcknowledgementReceiptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxAcknowledgementReceiptDTO, or with status {@code 400 (Bad Request)} if the taxAcknowledgementReceipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tax-acknowledgement-receipts")
    public ResponseEntity<TaxAcknowledgementReceiptDTO> createTaxAcknowledgementReceipt(
        @Valid @RequestBody TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TaxAcknowledgementReceipt : {}", taxAcknowledgementReceiptDTO);
        if (taxAcknowledgementReceiptDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxAcknowledgementReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxAcknowledgementReceiptDTO result = taxAcknowledgementReceiptService.save(taxAcknowledgementReceiptDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity
            .created(new URI("/api/tax-acknowledgement-receipts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tax-acknowledgement-receipts} : Updates an existing taxAcknowledgementReceipt.
     *
     * @param taxAcknowledgementReceiptDTO the taxAcknowledgementReceiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxAcknowledgementReceiptDTO,
     * or with status {@code 400 (Bad Request)} if the taxAcknowledgementReceiptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxAcknowledgementReceiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tax-acknowledgement-receipts")
    public ResponseEntity<TaxAcknowledgementReceiptDTO> updateTaxAcknowledgementReceipt(
        @Valid @RequestBody TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxAcknowledgementReceipt : {}", taxAcknowledgementReceiptDTO);
        if (taxAcknowledgementReceiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaxAcknowledgementReceiptDTO result = taxAcknowledgementReceiptService.save(taxAcknowledgementReceiptDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxAcknowledgementReceiptDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code GET  /tax-acknowledgement-receipts} : get all the taxAcknowledgementReceipts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxAcknowledgementReceipts in body.
     */
    @GetMapping("/tax-acknowledgement-receipts")
    public ResponseEntity<List<TaxAcknowledgementReceiptDTO>> getAllTaxAcknowledgementReceipts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TaxAcknowledgementReceipts");
        Page<TaxAcknowledgementReceiptDTO> page = taxAcknowledgementReceiptService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tax-acknowledgement-receipts/:id} : get the "id" taxAcknowledgementReceipt.
     *
     * @param id the id of the taxAcknowledgementReceiptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxAcknowledgementReceiptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tax-acknowledgement-receipts/{id}")
    public ResponseEntity<TaxAcknowledgementReceiptDTO> getTaxAcknowledgementReceipt(@PathVariable Long id) {
        log.debug("REST request to get TaxAcknowledgementReceipt : {}", id);
        Optional<TaxAcknowledgementReceiptDTO> taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptService.findOne(id);
        taxAcknowledgementReceiptDTO.ifPresent(acknowledgementReceiptDTO -> acknowledgementReceiptDTO.setFilePath(""));
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(taxAcknowledgementReceiptDTO);
    }

    /**
     * {@code DELETE  /tax-acknowledgement-receipts/:id} : delete the "id" taxAcknowledgementReceipt.
     *
     * @param id the id of the taxAcknowledgementReceiptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tax-acknowledgement-receipts/{id}")
    public ResponseEntity<Void> deleteTaxAcknowledgementReceipt(@PathVariable Long id) {
        log.debug("REST request to delete TaxAcknowledgementReceipt : {}", id);
        Optional<TaxAcknowledgementReceiptDTO> taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptService.findOne(id);
        taxAcknowledgementReceiptService.delete(id);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, taxAcknowledgementReceiptDTO.get(), RequestMethod.DELETE, RESOURCE_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/tax-acknowledgement-receipts/scheduler/{currentDate}")
    public ResponseEntity<HttpStatus> sendTestTaxAcknowledgementReminderMail(@PathVariable("currentDate") LocalDate currentDate) {
        log.debug("REST request to check  tax acknowledgement reminder mail", ENTITY_NAME);
        taxAcknowledgementReminderService.reminderWhoNotSubmitted(currentDate);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/tax-acknowledgement-receipts/scheduler/{pin}/{currentDate}")
    public ResponseEntity<HttpStatus> sendTestTaxAcknowledgementReminderMailByPIN(
        @PathVariable("pin") String pin,
        @PathVariable("currentDate") LocalDate currentDate
    ) {
        log.debug("REST request to check  tax acknowledgement reminder mail", ENTITY_NAME);
        taxAcknowledgementReminderService.reminderWhoNotSubmittedForSchedulerTest(pin, currentDate);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
