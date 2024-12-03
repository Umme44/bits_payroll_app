package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.ArrearSalaryMasterService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.ArrearSalaryMasterDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.ArrearSalaryMaster}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class ArrearSalaryMasterResource {

    private final Logger log = LoggerFactory.getLogger(ArrearSalaryMasterResource.class);

    private static final String ENTITY_NAME = "arrearSalaryMaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArrearSalaryMasterService arrearSalaryMasterService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public ArrearSalaryMasterResource(
        ArrearSalaryMasterService arrearSalaryMasterService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.arrearSalaryMasterService = arrearSalaryMasterService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /arrear-salary-masters} : Create a new arrearSalaryMaster.
     *
     * @param arrearSalaryMasterDTO the arrearSalaryMasterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arrearSalaryMasterDTO, or with status {@code 400 (Bad Request)} if the arrearSalaryMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arrear-salary-masters")
    public ResponseEntity<ArrearSalaryMasterDTO> createArrearSalaryMaster(@Valid @RequestBody ArrearSalaryMasterDTO arrearSalaryMasterDTO)
        throws URISyntaxException {
        log.debug("REST request to save ArrearSalaryMaster : {}", arrearSalaryMasterDTO);
        if (arrearSalaryMasterDTO.getId() != null) {
            throw new BadRequestAlertException("A new arrearSalaryMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArrearSalaryMasterDTO result = arrearSalaryMasterService.save(arrearSalaryMasterDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "ArrearSalaryMaster");

        return ResponseEntity
            .created(new URI("/api/arrear-salary-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arrear-salary-masters} : Updates an existing arrearSalaryMaster.
     *
     * @param arrearSalaryMasterDTO the arrearSalaryMasterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arrearSalaryMasterDTO,
     * or with status {@code 400 (Bad Request)} if the arrearSalaryMasterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arrearSalaryMasterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arrear-salary-masters")
    public ResponseEntity<ArrearSalaryMasterDTO> updateArrearSalaryMaster(@Valid @RequestBody ArrearSalaryMasterDTO arrearSalaryMasterDTO)
        throws URISyntaxException {
        log.debug("REST request to update ArrearSalaryMaster : {}", arrearSalaryMasterDTO);
        if (arrearSalaryMasterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArrearSalaryMasterDTO result = arrearSalaryMasterService.save(arrearSalaryMasterDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "ArrearSalaryMaster");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arrearSalaryMasterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /arrear-salary-masters} : get all the arrearSalaryMasters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arrearSalaryMasters in body.
     */
    @GetMapping("/arrear-salary-masters")
    public ResponseEntity<List<ArrearSalaryMasterDTO>> getAllArrearSalaryMasters(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ArrearSalaryMasters");
        Page<ArrearSalaryMasterDTO> page = arrearSalaryMasterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "ArrearSalaryMaster");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arrear-salary-masters/:id} : get the "id" arrearSalaryMaster.
     *
     * @param id the id of the arrearSalaryMasterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arrearSalaryMasterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arrear-salary-masters/{id}")
    public ResponseEntity<ArrearSalaryMasterDTO> getArrearSalaryMaster(@PathVariable Long id) {
        log.debug("REST request to get ArrearSalaryMaster : {}", id);
        Optional<ArrearSalaryMasterDTO> arrearSalaryMasterDTO = arrearSalaryMasterService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        if (arrearSalaryMasterDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, arrearSalaryMasterDTO.get(), RequestMethod.GET, "ArrearSalaryMaster");
        }
        return ResponseUtil.wrapOrNotFound(arrearSalaryMasterDTO);
    }

    /**
     * {@code DELETE  /arrear-salary-masters/:id} : delete the "id" arrearSalaryMaster.
     *
     * @param id the id of the arrearSalaryMasterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arrear-salary-masters/{id}")
    public ResponseEntity<Void> deleteArrearSalaryMaster(@PathVariable Long id) {
        log.debug("REST request to delete ArrearSalaryMaster : {}", id);
        Optional<ArrearSalaryMasterDTO> arrearSalaryMasterDTO = arrearSalaryMasterService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (arrearSalaryMasterDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, arrearSalaryMasterDTO.get(), RequestMethod.DELETE, "ArrearSalaryMaster");
        }
        arrearSalaryMasterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
