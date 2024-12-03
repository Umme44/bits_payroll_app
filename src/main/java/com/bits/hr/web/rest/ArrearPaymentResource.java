package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.ArrearPaymentService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.ArrearPaymentDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.ArrearPayment}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class ArrearPaymentResource {

    private final Logger log = LoggerFactory.getLogger(ArrearPaymentResource.class);

    private static final String ENTITY_NAME = "arrearPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArrearPaymentService arrearPaymentService;

    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public ArrearPaymentResource(
        ArrearPaymentService arrearPaymentService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.arrearPaymentService = arrearPaymentService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /arrear-payments} : Create a new arrearPayment.
     *
     * @param arrearPaymentDTO the arrearPaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arrearPaymentDTO, or with status {@code 400 (Bad Request)} if the arrearPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arrear-payments")
    public ResponseEntity<ArrearPaymentDTO> createArrearPayment(@Valid @RequestBody ArrearPaymentDTO arrearPaymentDTO)
        throws URISyntaxException {
        log.debug("REST request to save ArrearPayment : {}", arrearPaymentDTO);
        if (arrearPaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new arrearPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArrearPaymentDTO result = arrearPaymentService.save(arrearPaymentDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, " ArrearPayment");

        return ResponseEntity
            .created(new URI("/api/arrear-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arrear-payments} : Updates an existing arrearPayment.
     *
     * @param arrearPaymentDTO the arrearPaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arrearPaymentDTO,
     * or with status {@code 400 (Bad Request)} if the arrearPaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arrearPaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arrear-payments")
    public ResponseEntity<ArrearPaymentDTO> updateArrearPayment(@Valid @RequestBody ArrearPaymentDTO arrearPaymentDTO)
        throws URISyntaxException {
        log.debug("REST request to update ArrearPayment : {}", arrearPaymentDTO);
        if (arrearPaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArrearPaymentDTO result = arrearPaymentService.save(arrearPaymentDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "ArrearPayment");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arrearPaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /arrear-payments} : get all the arrearPayments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arrearPayments in body.
     */
    @GetMapping("/arrear-payments")
    public ResponseEntity<List<ArrearPaymentDTO>> getAllArrearPayments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ArrearPayments");
        Page<ArrearPaymentDTO> page = arrearPaymentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "AitConfig");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/arrear-payments/get-by-arrear-salary-item/{arrearSalaryItemId}")
    public ResponseEntity<List<ArrearPaymentDTO>> getAllArrearPaymentsByArrearIteam(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable long arrearSalaryItemId
    ) {
        log.debug("REST request to get a page of ArrearPayments by ArrearItemId" + arrearSalaryItemId);
        Page<ArrearPaymentDTO> page = arrearPaymentService.findAllByArrearItem(pageable, arrearSalaryItemId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arrear-payments/:id} : get the "id" arrearPayment.
     *
     * @param id the id of the arrearPaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arrearPaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arrear-payments/{id}")
    public ResponseEntity<ArrearPaymentDTO> getArrearPayment(@PathVariable Long id) {
        log.debug("REST request to get ArrearPayment : {}", id);
        Optional<ArrearPaymentDTO> arrearPaymentDTO = arrearPaymentService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (arrearPaymentDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, arrearPaymentDTO.get(), RequestMethod.GET, "AitConfig");
        }

        return ResponseUtil.wrapOrNotFound(arrearPaymentDTO);
    }

    /**
     * {@code DELETE  /arrear-payments/:id} : delete the "id" arrearPayment.
     *
     * @param id the id of the arrearPaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arrear-payments/{id}")
    public ResponseEntity<Void> deleteArrearPayment(@PathVariable Long id) {
        log.debug("REST request to delete ArrearPayment : {}", id);

        Optional<ArrearPaymentDTO> arrearPaymentDTOOptional = arrearPaymentService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (arrearPaymentDTOOptional.isPresent()) {
            eventLoggingPublisher.publishEvent(user, arrearPaymentDTOOptional.get(), RequestMethod.DELETE, "AitConfig");
        }
        arrearPaymentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
