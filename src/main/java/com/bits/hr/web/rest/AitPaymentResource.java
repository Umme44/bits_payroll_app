package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.AitPaymentService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.AitPaymentDTO;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.AitPayment}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class AitPaymentResource {

    private final Logger log = LoggerFactory.getLogger(AitPaymentResource.class);

    private static final String ENTITY_NAME = "aitPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AitPaymentService aitPaymentService;

    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public AitPaymentResource(
        AitPaymentService aitPaymentService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.aitPaymentService = aitPaymentService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /ait-payments} : Create a new aitPayment.
     *
     * @param aitPaymentDTO the aitPaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aitPaymentDTO, or with status {@code 400 (Bad Request)} if the aitPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ait-payments")
    public ResponseEntity<AitPaymentDTO> createAitPayment(@RequestBody AitPaymentDTO aitPaymentDTO) throws URISyntaxException {
        log.debug("REST request to save AitPayment : {}", aitPaymentDTO);
        if (aitPaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new aitPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AitPaymentDTO result = aitPaymentService.save(aitPaymentDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "AitPayment");

        return ResponseEntity
            .created(new URI("/api/ait-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ait-payments} : Updates an existing aitPayment.
     *
     * @param aitPaymentDTO the aitPaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aitPaymentDTO,
     * or with status {@code 400 (Bad Request)} if the aitPaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aitPaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ait-payments")
    public ResponseEntity<AitPaymentDTO> updateAitPayment(@RequestBody AitPaymentDTO aitPaymentDTO) throws URISyntaxException {
        log.debug("REST request to update AitPayment : {}", aitPaymentDTO);
        if (aitPaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AitPaymentDTO result = aitPaymentService.save(aitPaymentDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "AitPayment");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aitPaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ait-payments} : get all the aitPayments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aitPayments in body.
     */
    @GetMapping("/ait-payments")
    public ResponseEntity<List<AitPaymentDTO>> getAllAitPayments(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of AitPayments");
        Page<AitPaymentDTO> page = aitPaymentService.findAll(searchText, startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "AitPayment");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ait-payments/:id} : get the "id" aitPayment.
     *
     * @param id the id of the aitPaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aitPaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ait-payments/{id}")
    public ResponseEntity<AitPaymentDTO> getAitPayment(@PathVariable Long id) {
        log.debug("REST request to get AitPayment : {}", id);
        Optional<AitPaymentDTO> aitPaymentDTO = aitPaymentService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (aitPaymentDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, aitPaymentDTO.get(), RequestMethod.GET, "AitPayment");
        }

        return ResponseUtil.wrapOrNotFound(aitPaymentDTO);
    }

    /**
     * {@code DELETE  /ait-payments/:id} : delete the "id" aitPayment.
     *
     * @param id the id of the aitPaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ait-payments/{id}")
    public ResponseEntity<Void> deleteAitPayment(@PathVariable Long id) {
        log.debug("REST request to delete AitPayment : {}", id);
        Optional<AitPaymentDTO> aitPaymentDTOOptional = aitPaymentService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (aitPaymentDTOOptional.isPresent()) {
            eventLoggingPublisher.publishEvent(user, aitPaymentDTOOptional.get(), RequestMethod.DELETE, "AitConfig");
        }
        aitPaymentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
