package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.SalaryDeductionService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.SalaryDeductionDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.SalaryDeduction}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class SalaryDeductionResource {

    private final Logger log = LoggerFactory.getLogger(SalaryDeductionResource.class);

    private static final String ENTITY_NAME = "salaryDeduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaryDeductionService salaryDeductionService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public SalaryDeductionResource(
        SalaryDeductionService salaryDeductionService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.salaryDeductionService = salaryDeductionService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /salary-deductions} : Create a new salaryDeduction.
     *
     * @param salaryDeductionDTO the salaryDeductionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salaryDeductionDTO, or with status {@code 400 (Bad Request)} if the salaryDeduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salary-deductions")
    public ResponseEntity<SalaryDeductionDTO> createSalaryDeduction(@Valid @RequestBody SalaryDeductionDTO salaryDeductionDTO)
        throws URISyntaxException {
        log.debug("REST request to save SalaryDeduction : {}", salaryDeductionDTO);
        if (salaryDeductionDTO.getId() != null) {
            throw new BadRequestAlertException("A new salaryDeduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalaryDeductionDTO result = salaryDeductionService.save(salaryDeductionDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "SalaryDeduction");
        return ResponseEntity
            .created(new URI("/api/salary-deductions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salary-deductions} : Updates an existing salaryDeduction.
     *
     * @param salaryDeductionDTO the salaryDeductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryDeductionDTO,
     * or with status {@code 400 (Bad Request)} if the salaryDeductionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salaryDeductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salary-deductions")
    public ResponseEntity<SalaryDeductionDTO> updateSalaryDeduction(@Valid @RequestBody SalaryDeductionDTO salaryDeductionDTO)
        throws URISyntaxException {
        log.debug("REST request to update SalaryDeduction : {}", salaryDeductionDTO);
        if (salaryDeductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SalaryDeductionDTO result = salaryDeductionService.save(salaryDeductionDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "SalaryDeduction");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salaryDeductionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /salary-deductions} : get all the salaryDeductions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaryDeductions in body.
     */
    @GetMapping("/salary-deductions")
    public ResponseEntity<List<SalaryDeductionDTO>> getAllSalaryDeductions(
        @RequestParam(name = "searchText", required = false) String searchText,
        @RequestParam(required = false) int month,
        @RequestParam(required = false) int year,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SalaryDeductions");
        Page<SalaryDeductionDTO> page = salaryDeductionService.findAll(pageable, searchText, month, year);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "SalaryDeduction");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /salary-deductions} : get all the salaryDeductions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaryDeductions in body.
     */
    @GetMapping("/salary-deductions/{year}/{month}")
    public ResponseEntity<List<SalaryDeductionDTO>> getSalaryDeductionsByYearMonth(
        @RequestParam(name = "searchText", required = false) String searchText,
        @PathVariable int year,
        @PathVariable int month,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SalaryDeductions");
        Page<SalaryDeductionDTO> page = salaryDeductionService.findAllByYearAndMonth(year, month, pageable, searchText);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /salary-deductions/:id} : get the "id" salaryDeduction.
     *
     * @param id the id of the salaryDeductionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salaryDeductionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salary-deductions/{id}")
    public ResponseEntity<SalaryDeductionDTO> getSalaryDeduction(@PathVariable Long id) {
        log.debug("REST request to get SalaryDeduction : {}", id);
        Optional<SalaryDeductionDTO> salaryDeductionDTO = salaryDeductionService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (salaryDeductionDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "SalaryDeduction");
        }
        return ResponseUtil.wrapOrNotFound(salaryDeductionDTO);
    }

    /**
     * {@code DELETE  /salary-deductions/:id} : delete the "id" salaryDeduction.
     *
     * @param id the id of the salaryDeductionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salary-deductions/{id}")
    public ResponseEntity<Void> deleteSalaryDeduction(@PathVariable Long id) {
        log.debug("REST request to delete SalaryDeduction : {}", id);
        Optional<SalaryDeductionDTO> salaryDeductionDTO = salaryDeductionService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (salaryDeductionDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, salaryDeductionDTO.get(), RequestMethod.DELETE, "SalaryDeduction");
        }
        salaryDeductionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
