package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.IndividualArrearSalaryRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.IndividualArrearSalaryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.IndividualArrearSalaryDTO;
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

import javax.validation.Valid;

/**
 * REST controller for managing {@link com.bits.hr.domain.IndividualArrearSalary}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class IndividualArrearSalaryResource {

    private final Logger log = LoggerFactory.getLogger(IndividualArrearSalaryResource.class);

    private static final String ENTITY_NAME = "individualArrearSalary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndividualArrearSalaryService individualArrearSalaryService;
    private final IndividualArrearSalaryRepository individualArrearSalaryRepository;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public IndividualArrearSalaryResource(
        IndividualArrearSalaryService individualArrearSalaryService,
        IndividualArrearSalaryRepository individualArrearSalaryRepository,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.individualArrearSalaryService = individualArrearSalaryService;
        this.individualArrearSalaryRepository = individualArrearSalaryRepository;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /individual-arrear-salaries} : Create a new individualArrearSalary.
     *
     * @param individualArrearSalaryDTO the individualArrearSalaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new individualArrearSalaryDTO, or with status {@code 400 (Bad Request)} if the individualArrearSalary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/individual-arrear-salaries")
    public ResponseEntity<IndividualArrearSalaryDTO> createIndividualArrearSalary(
        @RequestBody @Valid IndividualArrearSalaryDTO individualArrearSalaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save IndividualArrearSalary : {}", individualArrearSalaryDTO);
        if (individualArrearSalaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new individualArrearSalary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndividualArrearSalaryDTO result = individualArrearSalaryService.save(individualArrearSalaryDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "IndividualArrearSalary");
        return ResponseEntity
            .created(new URI("/api/individual-arrear-salaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /individual-arrear-salaries} : Updates an existing individualArrearSalary.
     *
     * @param individualArrearSalaryDTO the individualArrearSalaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated individualArrearSalaryDTO,
     * or with status {@code 400 (Bad Request)} if the individualArrearSalaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the individualArrearSalaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/individual-arrear-salaries")
    public ResponseEntity<IndividualArrearSalaryDTO> updateIndividualArrearSalary(
        @Valid
        @RequestBody IndividualArrearSalaryDTO individualArrearSalaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IndividualArrearSalary : {}", individualArrearSalaryDTO);
        if (individualArrearSalaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IndividualArrearSalaryDTO result = individualArrearSalaryService.save(individualArrearSalaryDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "IndividualArrearSalary");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, individualArrearSalaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /individual-arrear-salaries} : get all the individualArrearSalaries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of individualArrearSalaries in body.
     */
    @GetMapping("/individual-arrear-salaries")
    public ResponseEntity<List<IndividualArrearSalaryDTO>> getAllIndividualArrearSalaries(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of IndividualArrearSalaries");
        Page<IndividualArrearSalaryDTO> page = individualArrearSalaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "IndividualArrearSalary");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /individual-arrear-salaries/:id} : get the "id" individualArrearSalary.
     *
     * @param id the id of the individualArrearSalaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the individualArrearSalaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/individual-arrear-salaries/{id}")
    public ResponseEntity<IndividualArrearSalaryDTO> getIndividualArrearSalary(@PathVariable Long id) {
        log.debug("REST request to get IndividualArrearSalary : {}", id);
        Optional<IndividualArrearSalaryDTO> individualArrearSalaryDTO = individualArrearSalaryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (individualArrearSalaryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, individualArrearSalaryDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(individualArrearSalaryDTO);
    }

    /**
     * {@code DELETE  /individual-arrear-salaries/:id} : delete the "id" individualArrearSalary.
     *
     * @param id the id of the individualArrearSalaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/individual-arrear-salaries/{id}")
    public ResponseEntity<Void> deleteIndividualArrearSalary(@PathVariable Long id) {
        log.debug("REST request to delete IndividualArrearSalary : {}", id);
        Optional<IndividualArrearSalaryDTO> individualArrearSalaryDTO = individualArrearSalaryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (individualArrearSalaryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, individualArrearSalaryDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        individualArrearSalaryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
