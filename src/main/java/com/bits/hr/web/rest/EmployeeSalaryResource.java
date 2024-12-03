package com.bits.hr.web.rest;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.service.EmployeeSalaryService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.approvalProcess.SalaryLockService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.incomeTaxManagement.IncomeTaxCalculatePerMonth;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import com.bits.hr.service.salaryGenerationFractional.Fraction;
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

/**
 * REST controller for managing {@link com.bits.hr.domain.EmployeeSalary}.
 */
@RestController
@RequestMapping("/api/payroll-mgt/employee-salaries")
public class EmployeeSalaryResource {

    private static final String ENTITY_NAME = "employeeSalary";
    private final Logger log = LoggerFactory.getLogger(EmployeeSalaryResource.class);
    private final EmployeeSalaryService employeeSalaryService;
    private final IncomeTaxCalculatePerMonth incomeTaxCalculatePerMonth;
    private final EmployeeSalaryMapper employeeSalaryMapper;
    private final SalaryLockService salaryLockService;
    private final EmployeeSalaryRepository employeeSalaryRepository;
    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public EmployeeSalaryResource(
        EmployeeSalaryService employeeSalaryService,
        IncomeTaxCalculatePerMonth incomeTaxCalculatePerMonth,
        EmployeeSalaryMapper employeeSalaryMapper,
        SalaryLockService salaryLockService,
        EmployeeRepository employeeRepository,
        EmployeeSalaryRepository employeeSalaryRepository,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.employeeSalaryService = employeeSalaryService;
        this.incomeTaxCalculatePerMonth = incomeTaxCalculatePerMonth;
        this.employeeSalaryMapper = employeeSalaryMapper;
        this.salaryLockService = salaryLockService;
        this.employeeSalaryRepository = employeeSalaryRepository;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /employee-salaries} : Create a new employeeSalary.
     *
     * @param employeeSalaryDTO the employeeSalaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeSalaryDTO, or with status {@code 400 (Bad Request)} if the employeeSalary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeSalaryDTO> createEmployeeSalary(@RequestBody EmployeeSalaryDTO employeeSalaryDTO)
        throws URISyntaxException {
        String year = employeeSalaryDTO.getYear().toString();
        String month = String.valueOf(employeeSalaryDTO.getMonth().ordinal() + 1);
        if (salaryLockService.isLocked(year, month)) {
            throw new BadRequestAlertException(" salary locked!! ", "Payroll Management", "entryLocked");
        }

        log.debug("REST request to save EmployeeSalary : {}", employeeSalaryDTO);
        if (employeeSalaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeSalary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeSalaryDTO result = employeeSalaryService.save(employeeSalaryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "EmployeeSalary");
        return ResponseEntity
            .created(new URI("/api/payroll-mgt/employee-salaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("")
    public ResponseEntity<EmployeeSalaryDTO> updateEmployeeSalary(@RequestBody EmployeeSalaryDTO employeeSalaryDTO)
        throws URISyntaxException {
        String year = employeeSalaryDTO.getYear().toString();
        String month = String.valueOf(employeeSalaryDTO.getMonth().ordinal() + 1);
        if (salaryLockService.isLocked(year, month)) {
            throw new BadRequestAlertException(" salary locked!! ", "Payroll Management", "entryLocked");
        }
        log.debug("REST request to update EmployeeSalary : {}", employeeSalaryDTO);
        if (employeeSalaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        EmployeeSalaryDTO previousEntry = employeeSalaryService.findOne(employeeSalaryDTO.getId()).get();
        employeeSalaryDTO.setTaxCalculationSnapshot(previousEntry.getTaxCalculationSnapshot());
        EmployeeSalaryDTO result = employeeSalaryService.update(employeeSalaryDTO);

        eventLoggingPublisher.publishEvent(currentEmployeeService.getCurrentUser().get(), result, RequestMethod.PUT, "EmployeeSalary");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeSalaryDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<?> getAllSalariesByYearAndMonth(
        @PathVariable int year,
        @PathVariable int month,
        @RequestParam(required = false) String searchText,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all MobileBills By year and month");
        Page<EmployeeSalaryDTO> page = employeeSalaryService.findAllByYearAndMonth(year, month, searchText, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{employeeId}/{year}/{month}")
    public ResponseEntity<List<EmployeeSalaryDTO>> getSalaryByEmployeeIdYearAndMonth(
        @PathVariable long employeeId,
        @PathVariable int year,
        @PathVariable int month
    ) {
        log.debug("REST request to get all MobileBills By year and month");
        List<EmployeeSalaryDTO> result = employeeSalaryService.findByEmployeeIdAndYearAndMonth(employeeId, year, month);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/breakdown/{id}")
    public List<Fraction> getBreakdownForSalary(@PathVariable("id") Long id) {
        log.debug("REST request to get salary breakdown by year and month");
        return employeeSalaryService.getFractions(id);
    }

    /**
     * {@code GET  /employee-salaries/:id} : get the "id" employeeSalary.
     *
     * @param id the id of the employeeSalaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeSalaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeSalaryDTO> getEmployeeSalary(@PathVariable Long id) {
        log.debug("REST request to get EmployeeSalary : {}", id);
        Optional<EmployeeSalaryDTO> employeeSalaryDTO = employeeSalaryService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (employeeSalaryDTO.isPresent()) {
            double grossPay = employeeSalaryDTO.get().getPayableGrossSalary() != null
                ? employeeSalaryDTO.get().getPayableGrossSalary()
                : 0d;
            double arrear = employeeSalaryDTO.get().getArrearSalary() != null ? employeeSalaryDTO.get().getArrearSalary() : 0d;
            double livingAllowance = employeeSalaryDTO.get().getAllowance01() != null ? employeeSalaryDTO.get().getAllowance01() : 0d;

            double totalAddition = grossPay + arrear + livingAllowance;
            employeeSalaryDTO.get().setTotalAddition(totalAddition);

            double netPayable = employeeSalaryDTO.get().getNetPay() != null ? employeeSalaryDTO.get().getNetPay() : 0d;
            netPayable = netPayable + livingAllowance;
            employeeSalaryDTO.get().setNetPay(netPayable);

            eventLoggingPublisher.publishEvent(user, employeeSalaryDTO.get(), RequestMethod.GET, "employeeSalary");
        }

        return ResponseUtil.wrapOrNotFound(employeeSalaryDTO);
    }

    /**
     * {@code DELETE  /employee-salaries/:id} : delete the "id" employeeSalary.
     *
     * @param id the id of the employeeSalaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeSalary(@PathVariable Long id) {
        Optional<EmployeeSalaryDTO> employeeSalaryDTOOptional = employeeSalaryService.findOne(id);
        if (employeeSalaryDTOOptional.isPresent()) {
            EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryDTOOptional.get();
            String year = employeeSalaryDTO.getYear().toString();
            String month = String.valueOf(employeeSalaryDTO.getMonth().ordinal() + 1);
            if (salaryLockService.isLocked(year, month)) {
                throw new BadRequestAlertException(" salary locked!! ", "Payroll Management", "entryLocked");
            }
        }

        log.debug("REST request to delete EmployeeSalary : {}", id);
        employeeSalaryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/hold/{employeeSalaryId}")
    public ResponseEntity<Boolean> holdEmployeeSalary(@PathVariable long employeeSalaryId) {
        log.debug("REST request to hold EmployeeSalary");

        Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findById(employeeSalaryId);

        if (!employeeSalaryOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Salary Not Found!", "EMPLOYEE_SALARY", "notFound");
        }

        String year = employeeSalaryOptional.get().getYear().toString();
        String month = String.valueOf(Month.fromEnum(employeeSalaryOptional.get().getMonth()));

        if (salaryLockService.isLocked(year, month)) {
            throw new BadRequestAlertException(" salary locked!! ", "Payroll Management", "entryLocked");
        }

        boolean result = employeeSalaryService.changeHoldStatusTo("hold", employeeSalaryOptional.get());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/unHold/{employeeSalaryId}")
    public ResponseEntity<Boolean> unHoldEmployeeSalary(@PathVariable long employeeSalaryId) {
        log.debug("REST request to unHold EmployeeSalary");

        Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findById(employeeSalaryId);

        if (!employeeSalaryOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Salary Not Found!", "EMPLOYEE_SALARY", "notFound");
        }

        String year = employeeSalaryOptional.get().getYear().toString();
        String month = String.valueOf(Month.fromEnum(employeeSalaryOptional.get().getMonth()));

        if (salaryLockService.isLocked(year, month)) {
            throw new BadRequestAlertException(" salary locked!! ", "Payroll Management", "entryLocked");
        }

        boolean result = employeeSalaryService.changeHoldStatusTo("unHold", employeeSalaryOptional.get());
        return ResponseEntity.ok().body(result);
    }
}
