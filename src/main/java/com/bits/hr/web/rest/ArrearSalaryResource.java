package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.ArrearSalaryService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ArrearSalaryDTO;
import com.bits.hr.service.importXL.batchOperations.ArrearImportServiceImpl;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.ArrearSalary}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class ArrearSalaryResource {

    private final Logger log = LoggerFactory.getLogger(ArrearSalaryResource.class);

    private static final String ENTITY_NAME = "arrearSalary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArrearSalaryService arrearSalaryService;
    private final ArrearImportServiceImpl arrearImportService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public ArrearSalaryResource(
        ArrearSalaryService arrearSalaryService,
        ArrearImportServiceImpl arrearImportService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.arrearSalaryService = arrearSalaryService;
        this.arrearImportService = arrearImportService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /arrear-salaries} : Create a new arrearSalary.
     *
     * @param arrearSalaryDTO the arrearSalaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arrearSalaryDTO, or with status {@code 400 (Bad Request)} if the arrearSalary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arrear-salaries")
    public ResponseEntity<ArrearSalaryDTO> createArrearSalary(@Valid @RequestBody ArrearSalaryDTO arrearSalaryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ArrearSalary : {}", arrearSalaryDTO);

        if (arrearSalaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new arrearSalary cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (arrearSalaryDTO.getEmployeeId() == null) {
            throw new BadRequestAlertException("Employee is required", ENTITY_NAME, "employeenull");
        }

        ArrearSalaryDTO result = arrearSalaryService.save(arrearSalaryDTO);

        User user = currentEmployeeService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "ArrearSalary");

        return ResponseEntity
            .created(new URI("/api/payroll-mgt/arrear-salaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arrear-salaries} : Updates an existing arrearSalary.
     *
     * @param arrearSalaryDTO the arrearSalaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arrearSalaryDTO,
     * or with status {@code 400 (Bad Request)} if the arrearSalaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arrearSalaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arrear-salaries")
    public ResponseEntity<ArrearSalaryDTO> updateArrearSalary(@Valid @RequestBody ArrearSalaryDTO arrearSalaryDTO)
        throws URISyntaxException {
        log.debug("REST request to update ArrearSalary : {}", arrearSalaryDTO);
        if (arrearSalaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArrearSalaryDTO result = arrearSalaryService.save(arrearSalaryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "ArrearSalary");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arrearSalaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /arrear-salaries} : get all the arrearSalaries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arrearSalaries in body.
     */

    @GetMapping("/arrear-salaries")
    public ResponseEntity<List<ArrearSalaryDTO>> getAllArrearSalaries(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false, defaultValue = "-1") int month,
        @RequestParam(required = false, defaultValue = "-1") int year,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of ArrearSalaries");

        //        if (month == -1) {
        //            month = getDefaultMonth();
        //        }
        //        if (year == -1) {
        //            year = getDefaultYear();
        //        }

        //        Month monthEnum = Month.of(month);

        Page<ArrearSalaryDTO> page = arrearSalaryService.findAll(searchText, month, year, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "ArrearSalary");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/arrear-salaries/{id}")
    public ResponseEntity<ArrearSalaryDTO> getArrearSalary(@PathVariable Long id) {
        log.debug("REST request to get ArrearSalary : {}", id);
        Optional<ArrearSalaryDTO> arrearSalaryDTO = arrearSalaryService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (arrearSalaryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, arrearSalaryDTO.get(), RequestMethod.GET, "ArrearSalary");
        }

        return ResponseUtil.wrapOrNotFound(arrearSalaryDTO);
    }

    /**
     * {@code DELETE  /arrear-salaries/:id} : delete the "id" arrearSalary.
     *
     * @param id the id of the arrearSalaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arrear-salaries/{id}")
    public ResponseEntity<Void> deleteArrearSalary(@PathVariable Long id) {
        log.debug("REST request to delete ArrearSalary : {}", id);
        Optional<ArrearSalaryDTO> arrearSalaryDTO = arrearSalaryService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (arrearSalaryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, arrearSalaryDTO.get(), RequestMethod.DELETE, "ArrearSalary");
        }
        arrearSalaryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/batch-arrear")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = arrearImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    private int getDefaultMonth() {
        return LocalDate.now().getMonthValue();
    }

    private int getDefaultYear() {
        return LocalDate.now().getYear();
    }
}
