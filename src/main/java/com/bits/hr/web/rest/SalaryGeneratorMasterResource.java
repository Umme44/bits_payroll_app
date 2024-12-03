package com.bits.hr.web.rest;

import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.SalaryGeneratorMaster}.
 */
@RestController
@RequestMapping("/api/payroll-mgt/salary-generator-masters")
@Transactional
public class SalaryGeneratorMasterResource {

    private static final String ENTITY_NAME = "salaryGeneratorMaster";
    private final Logger log = LoggerFactory.getLogger(SalaryGeneratorMasterResource.class);
    private final SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;
    private final EmployeeSalaryRepository employeeSalaryRepository;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public SalaryGeneratorMasterResource(
        SalaryGeneratorMasterRepository salaryGeneratorMasterRepository,
        EmployeeSalaryRepository employeeSalaryRepository,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.salaryGeneratorMasterRepository = salaryGeneratorMasterRepository;
        this.employeeSalaryRepository = employeeSalaryRepository;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /salary-generator-masters} : Create a new salaryGeneratorMaster.
     *
     * @param salaryGeneratorMaster the salaryGeneratorMaster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salaryGeneratorMaster, or with status {@code 400 (Bad Request)} if the salaryGeneratorMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SalaryGeneratorMaster> createSalaryGeneratorMaster(@RequestBody SalaryGeneratorMaster salaryGeneratorMaster)
        throws URISyntaxException {
        log.debug("REST request to save SalaryGeneratorMaster : {}", salaryGeneratorMaster);
        if (salaryGeneratorMaster.getId() != null) {
            throw new BadRequestAlertException("A new salaryGeneratorMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalaryGeneratorMaster result = salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "SalaryGeneratorMaster");
        return ResponseEntity
            .created(new URI("/api/payroll-mgt/salary-generator-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salary-generator-masters} : Updates an existing salaryGeneratorMaster.
     *
     * @param salaryGeneratorMaster the salaryGeneratorMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryGeneratorMaster,
     * or with status {@code 400 (Bad Request)} if the salaryGeneratorMaster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salaryGeneratorMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<SalaryGeneratorMaster> updateSalaryGeneratorMaster(@RequestBody SalaryGeneratorMaster salaryGeneratorMaster)
        throws URISyntaxException {
        log.debug("REST request to update SalaryGeneratorMaster : {}", salaryGeneratorMaster);
        if (salaryGeneratorMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SalaryGeneratorMaster result = salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "SalaryGeneratorMaster");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salaryGeneratorMaster.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /salary-generator-masters} : get all the salaryGeneratorMasters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaryGeneratorMasters in body.
     */
    @GetMapping("")
    public List<SalaryGeneratorMaster> getAllSalaryGeneratorMasters() {
        log.debug("REST request to get all SalaryGeneratorMasters");
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAllByOrderByYearDescMonthDesc();
        salaryGeneratorMasterList.sort((s1, s2) -> {
            if (s1.yearAsInt() - s2.yearAsInt() == 0) {
                return s2.monthAsInt() - s1.monthAsInt();
            } else {
                return s2.yearAsInt() - s1.yearAsInt();
            }
        });
        return salaryGeneratorMasterList;
    }

    /**
     * {@code GET  /salary-generator-masters/:id} : get the "id" salaryGeneratorMaster.
     *
     * @param id the id of the salaryGeneratorMaster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salaryGeneratorMaster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalaryGeneratorMaster> getSalaryGeneratorMaster(@PathVariable Long id) {
        log.debug("REST request to get SalaryGeneratorMaster : {}", id);
        Optional<SalaryGeneratorMaster> salaryGeneratorMaster = salaryGeneratorMasterRepository.findById(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (salaryGeneratorMaster.isPresent()) {
            eventLoggingPublisher.publishEvent(user, salaryGeneratorMaster.get(), RequestMethod.GET, "SalaryGeneratorMaster");
        }
        return ResponseUtil.wrapOrNotFound(salaryGeneratorMaster);
    }

    /**
     * {@code DELETE  /salary-generator-masters/:id} : delete the "id" salaryGeneratorMaster.
     *
     * @param id the id of the salaryGeneratorMaster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalaryGeneratorMaster(@PathVariable Long id) {
        log.debug("REST request to delete SalaryGeneratorMaster : {}", id);
        Optional<SalaryGeneratorMaster> salaryGeneratorMaster = salaryGeneratorMasterRepository.findById(id);
        salaryGeneratorMaster.ifPresent(generatorMaster ->
            eventLoggingPublisher.publishEvent(
                currentEmployeeService.getCurrentUser().get(),
                generatorMaster,
                RequestMethod.GET,
                "SalaryGeneratorMaster"
            )
        );
        salaryGeneratorMasterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
