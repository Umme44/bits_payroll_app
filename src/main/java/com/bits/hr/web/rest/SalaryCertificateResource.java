package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.SalaryCertificateService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeSalaryCertificateReportDTO;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.SalaryCertificateDTO;
import com.bits.hr.service.salaryCertificates.NumberToWord;
import com.bits.hr.service.search.FilterDto;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.bits.hr.domain.SalaryCertificate}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class SalaryCertificateResource {

    private static final String RESOURCE_NAME = "SalaryCertificateResource";

    private final Logger log = LoggerFactory.getLogger(SalaryCertificateResource.class);

    private static final String ENTITY_NAME = "salaryCertificate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaryCertificateService salaryCertificateService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EmployeeService employeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public SalaryCertificateResource(SalaryCertificateService salaryCertificateService, CurrentEmployeeService currentEmployeeService, EventLoggingPublisher eventLoggingPublisher, EmployeeService employeeService) {
        this.salaryCertificateService = salaryCertificateService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.employeeService = employeeService;
    }

    @GetMapping("/salary-certificates")
    public ResponseEntity<List<SalaryCertificateDTO>> getAllESalaryCertificate(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) CertificateStatus status,
        @RequestParam(required = false) Integer selectedYear,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of EmployeeNOCS");
        Page<SalaryCertificateDTO> page = salaryCertificateService.findAll(searchText, status, selectedYear, pageable);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, page.getContent(), com.bits.hr.domain.enumeration.RequestMethod.GET, RESOURCE_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/salary-certificates/list-of-salaries/employee")
    public ResponseEntity<List<EmployeeSalaryDTO>> getEmployeeListOfSalaries(@RequestBody String pin) {
        log.debug("REST request to get list of salary years");

        Optional<Employee> optionalEmployee = employeeService.findEmployeeByPin(pin);
        if(!optionalEmployee.isPresent()){
            throw new BadRequestAlertException("User Not Fount", "SalaryCertificate", "internalServerError");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "SalaryCertificate");

        List<EmployeeSalaryDTO> result = salaryCertificateService.getSalariesForDropDown(optionalEmployee.get().getId());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/salary-certificates")
    public ResponseEntity<SalaryCertificateDTO> createSalaryCertificate(@Valid @RequestBody SalaryCertificateDTO salaryCertificateDTO) throws URISyntaxException {
        log.debug("REST request to save SalaryCertificate : {}", salaryCertificateDTO);
        if (salaryCertificateDTO.getId() != null) {
            throw new BadRequestAlertException("A new salaryCertificate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> currentUser = currentEmployeeService.getCurrentUser();
        if(!currentUser.isPresent()){
            throw new BadRequestAlertException("User Not Fount", "SalaryCertificate", "internalServerError");
        }

        salaryCertificateDTO.setStatus(Status.PENDING);
        salaryCertificateDTO.setCreatedById(currentUser.get().getId());
        salaryCertificateDTO.setCreatedAt(LocalDate.now());
        SalaryCertificateDTO result = salaryCertificateService.save(salaryCertificateDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, com.bits.hr.domain.enumeration.RequestMethod.POST, "SalaryCertificate");
        return ResponseEntity.created(new URI("/api/salary-certificates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salary-certificates} : Updates an existing salaryCertificate.
     *
     * @param salaryCertificateDTO the salaryCertificateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryCertificateDTO,
     * or with status {@code 400 (Bad Request)} if the salaryCertificateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salaryCertificateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salary-certificates")
    public ResponseEntity<SalaryCertificateDTO> updateSalaryCertificate(@Valid @RequestBody SalaryCertificateDTO salaryCertificateDTO) throws URISyntaxException {
        log.debug("REST request to update SalaryCertificate : {}", salaryCertificateDTO);
        if (salaryCertificateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<User> currentUser = currentEmployeeService.getCurrentUser();
        if(!currentUser.isPresent()){
            throw new BadRequestAlertException("User Not Fount", "SalaryCertificate", "internalServerError");
        }

        salaryCertificateDTO.setUpdatedById(currentUser.get().getId());
        salaryCertificateDTO.setUpdatedAt(LocalDate.now());
        SalaryCertificateDTO result = salaryCertificateService.save(salaryCertificateDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, com.bits.hr.domain.enumeration.RequestMethod.PUT, "SalaryCertificate");
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salaryCertificateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /salary-certificates} : get all the salaryCertificates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaryCertificates in body.
     */
    @PostMapping("/salary-certificates/all")
    public ResponseEntity<List<SalaryCertificateDTO>> getAllSalaryCertificates(
        @RequestBody FilterDto filter,
        Pageable pageable) {
        log.debug("REST request to get a page of SalaryCertificates");
        Page<SalaryCertificateDTO> page = salaryCertificateService.findAll(filter,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), com.bits.hr.domain.enumeration.RequestMethod.GET, "SalaryCertificate");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /salary-certificates/:id} : get the "id" salaryCertificate.
     *
     * @param id the id of the salaryCertificateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salaryCertificateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salary-certificates/{id}")
    public ResponseEntity<SalaryCertificateDTO> getSalaryCertificate(@PathVariable Long id) {
        log.debug("REST request to get SalaryCertificate : {}", id);

        Optional<SalaryCertificateDTO> salaryCertificateDTO = salaryCertificateService.getSalaryCertificateById(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (salaryCertificateDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, salaryCertificateDTO.get(), com.bits.hr.domain.enumeration.RequestMethod.GET, "SalaryCertificate");
        }
        return ResponseUtil.wrapOrNotFound(salaryCertificateDTO);
    }

    /**
     * {@code DELETE  /salary-certificates/:id} : delete the "id" salaryCertificate.
     *
     * @param id the id of the salaryCertificateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salary-certificates/{id}")
    public ResponseEntity<Void> deleteSalaryCertificate(@PathVariable Long id) {
        log.debug("REST request to delete SalaryCertificate : {}", id);
        Optional<SalaryCertificateDTO> salaryCertificateDTO = salaryCertificateService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (salaryCertificateDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, salaryCertificateDTO.get(), com.bits.hr.domain.enumeration.RequestMethod.DELETE, "SalaryCertificate");
        }
        salaryCertificateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/salary-certificates/is-unique")
    public ResponseEntity<Boolean> isSalaryCertificateReferenceNumberUnique(@RequestParam(required = true) String referenceNumber) {
        log.debug("REST request to check if reference number is unique : {}", referenceNumber);
        User user = currentEmployeeService.getCurrentUser().get();
        boolean result =  salaryCertificateService.isReferenceNumberUnique(referenceNumber);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/salary-certificates/{id}/list-of-salaries")
    public ResponseEntity<List<EmployeeSalaryDTO>> getEmployeeListOfSalaries(@PathVariable Long id) {
        List<EmployeeSalaryDTO> result = salaryCertificateService.getSalariesForDropDown(id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/salary-certificates/employee-salary/{id}")
    public ResponseEntity<EmployeeSalaryDTO> getEmployeeSalaryByCertificateId(@PathVariable Long id) {
        log.debug("REST Request get EmployeeSalaryDTO by salary certificate id");
        EmployeeSalaryDTO  result = salaryCertificateService.getSalaryForSalaryCertificates(id).get();
        result.setNetPayInWords(NumberToWord.convertNumberToWord(result.getNetPay().longValue()));
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/salary-certificates/salary-certificate-report/{id}")
    public ResponseEntity<EmployeeSalaryCertificateReportDTO> getSalaryCertificateReportByCertificateId(@PathVariable Long id) {
        log.debug("REST Request get EmployeeSalaryDTO by salary certificate id");
        EmployeeSalaryCertificateReportDTO result = salaryCertificateService.getSalaryCertificateReportByCertificateId(id);
        return ResponseEntity.ok().body(result);
    }
}
