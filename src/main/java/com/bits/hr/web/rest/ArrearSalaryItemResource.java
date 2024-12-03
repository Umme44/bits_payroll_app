package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.ArrearSalaryItemService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ArrearSalaryItemDTO;
import com.bits.hr.service.dto.ArrearSalaryItemDisburseDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.ArrearSalaryItem}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class ArrearSalaryItemResource {

    private final Logger log = LoggerFactory.getLogger(ArrearSalaryItemResource.class);

    private static final String ENTITY_NAME = "arrearSalaryItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArrearSalaryItemService arrearSalaryItemService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public ArrearSalaryItemResource(
        ArrearSalaryItemService arrearSalaryItemService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.arrearSalaryItemService = arrearSalaryItemService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /arrear-salary-items} : Create a new arrearSalaryItem.
     *
     * @param arrearSalaryItemDTO the arrearSalaryItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arrearSalaryItemDTO, or with status {@code 400 (Bad Request)} if the arrearSalaryItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arrear-salary-items")
    public ResponseEntity<ArrearSalaryItemDTO> createArrearSalaryItem(@Valid @RequestBody ArrearSalaryItemDTO arrearSalaryItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save ArrearSalaryItem : {}", arrearSalaryItemDTO);
        if (arrearSalaryItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new arrearSalaryItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArrearSalaryItemDTO result = arrearSalaryItemService.save(arrearSalaryItemDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "ArrearSalaryItem");

        return ResponseEntity
            .created(new URI("/api/arrear-salary-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arrear-salary-items} : Updates an existing arrearSalaryItem.
     *
     * @param arrearSalaryItemDTO the arrearSalaryItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arrearSalaryItemDTO,
     * or with status {@code 400 (Bad Request)} if the arrearSalaryItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arrearSalaryItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arrear-salary-items")
    public ResponseEntity<ArrearSalaryItemDTO> updateArrearSalaryItem(@Valid @RequestBody ArrearSalaryItemDTO arrearSalaryItemDTO)
        throws URISyntaxException {
        log.debug("REST request to update ArrearSalaryItem : {}", arrearSalaryItemDTO);
        if (arrearSalaryItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArrearSalaryItemDTO result = arrearSalaryItemService.save(arrearSalaryItemDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "ArrearSalaryItem");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arrearSalaryItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /arrear-salary-items} : get all the arrearSalaryItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arrearSalaryItems in body.
     */
    @GetMapping("/arrear-salary-items")
    public ResponseEntity<List<ArrearSalaryItemDTO>> getAllArrearSalaryItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ArrearSalaryItems");
        Page<ArrearSalaryItemDTO> page = arrearSalaryItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "ArrearSalaryItem");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     *
     * @param arrearSalaryMasterId
     * @param pageable
     * @return
     */
    @GetMapping("/arrear-salary-items/get-by-arrear-salary-master/{arrearSalaryMasterId}")
    public ResponseEntity<List<ArrearSalaryItemDTO>> getAllArrearSalaryItemsByArrearSalaryMaster(
        @PathVariable long arrearSalaryMasterId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ArrearSalaryItems");
        Page<ArrearSalaryItemDTO> page = arrearSalaryItemService.findByArrearsSalaryMaster(arrearSalaryMasterId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "ArrearSalaryItem");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arrear-salary-items/:id} : get the "id" arrearSalaryItem.
     *
     * @param id the id of the arrearSalaryItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arrearSalaryItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arrear-salary-items/{id}")
    public ResponseEntity<ArrearSalaryItemDTO> getArrearSalaryItem(@PathVariable Long id) {
        log.debug("REST request to get ArrearSalaryItem : {}", id);
        Optional<ArrearSalaryItemDTO> arrearSalaryItemDTO = arrearSalaryItemService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (arrearSalaryItemDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, arrearSalaryItemDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(arrearSalaryItemDTO);
    }

    /**
     * {@code DELETE  /arrear-salary-items/:id} : delete the "id" arrearSalaryItem.
     *
     * @param id the id of the arrearSalaryItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arrear-salary-items/{id}")
    public ResponseEntity<Void> deleteArrearSalaryItem(@PathVariable Long id) {
        log.debug("REST request to delete ArrearSalaryItem : {}", id);
        Optional<ArrearSalaryItemDTO> arrearSalaryItemDTO = arrearSalaryItemService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (arrearSalaryItemDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, arrearSalaryItemDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        arrearSalaryItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/arrear-salary-items/soft-delete")
    public ResponseEntity<ArrearSalaryItemDTO> softDeleteArrearSalaryItem(@RequestBody ArrearSalaryItemDTO arrearSalaryItemDTO)
        throws URISyntaxException {
        log.debug("REST request to update ArrearSalaryItem : {}", arrearSalaryItemDTO);
        if (arrearSalaryItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArrearSalaryItemDTO result = arrearSalaryItemService.save(arrearSalaryItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arrearSalaryItemDTO.getId().toString()))
            .body(result);
    }

    @PostMapping("/arrear-salary-items/disburse")
    public ResponseEntity<Boolean> disburseSelectedArrearSalaryItem(@RequestBody ArrearSalaryItemDisburseDTO arrearSalaryItemDisburseDTO) {
        Boolean result = arrearSalaryItemService.disburseSelectedArrearSalaryItem(arrearSalaryItemDisburseDTO);
        return ResponseEntity.ok(result);
    }
}
