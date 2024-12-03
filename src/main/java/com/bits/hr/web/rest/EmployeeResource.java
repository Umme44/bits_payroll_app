package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeSalaryService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FileService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.EmployeeDashboardAnalyticsDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.event.EmployeeRegistrationEvent;
import com.bits.hr.service.importXL.employee.EmployeeLegacyImportService;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.service.utility.PinTrimUtilService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Employee}.
 */
@RestController
@RequestMapping("/api/employee-mgt/employees")
@RequiredArgsConstructor
public class EmployeeResource {

    private static final String ENTITY_NAME = "employee";
    private static final String RESOURCE_NAME = "EmployeeResource";

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);
    private final EmployeeService employeeService;
    private final EmployeeLegacyImportService employeeXlsxImportService;
    private final FileService fileService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;
    private final ApplicationEventPublisher eventPublisher;
    private final EmployeeRepository employeeRepository;

    private final EmployeeMinimalMapper employeeMinimalMapper;

    private final PinTrimUtilService pinTrimUtilService;

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code POST  /employees} : Create a new employee.
     *
     * @param employeeDTO the employeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeDTO, or with status {@code 400 (Bad Request)} if the employee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employeeDTO);
        if (employeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new employee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        employeeDTO.setEmploymentStatus(EmploymentStatus.ACTIVE);
        employeeDTO.setCreatedBy(user.getEmail());
        employeeDTO.setCreatedAt(Instant.now());
        EmployeeDTO result = employeeService.create(employeeDTO);
        if (result == null) {
            throw new BadRequestAlertException("No user exists with the given official email", "User", "noUserEmail");
        }
        // make employment history entry on new create / post
        employeeService.createJoiningEntry(result);
        // publish employee registration event
        publishEvent(result.getOfficialEmail(), result.getPin());

        eventLoggingPublisher.publishEvent(user, employeeDTO, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/employee-mgt/employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employees} : Updates an existing employee.
     *
     * @param employeeDTO the employeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeDTO,
     * or with status {@code 400 (Bad Request)} if the employeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<EmployeeDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employeeDTO);
        if (employeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        User user = currentEmployeeService.getCurrentUser().get();

        employeeDTO.setUpdatedBy(user.getEmail());
        employeeDTO.setUpdatedAt(LocalDate.now());
        EmployeeDTO result = employeeService.update(employeeDTO);
        // publish employee registration event
        publishEvent(result.getOfficialEmail(), result.getPin());

        eventLoggingPublisher.publishEvent(user, employeeDTO, RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employees} : get all the employees.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(Pageable pageable) {
        log.debug("REST request to get all Employees");
        Page<EmployeeDTO> page = employeeService.findAll(pageable);

        User user = currentEmployeeService.getCurrentUser().get();

        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employees/:id} : get the "id" employee.
     *
     * @param id the id of the employeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        Optional<EmployeeDTO> employeeDTO = employeeService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        eventLoggingPublisher.publishEvent(user, employeeDTO.get(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(employeeDTO);
    }

    @GetMapping("/minimal/{id}")
    public ResponseEntity<EmployeeMinimalDTO> getEmployeeMinimal(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        EmployeeMinimalDTO employeeMinimalDTO = employeeService.findEmployeeMinimalById(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeeMinimalDTO, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(employeeMinimalDTO);
    }

    @GetMapping("/sync-images")
    public DeferredResult<ResponseEntity<?>> syncImages(
        @RequestParam(value = "force", required = false, defaultValue = "false") boolean force
    ) {
        log.debug("Received async-deferred result request");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        ForkJoinPool
            .commonPool()
            .submit(() -> {
                int synced = fileService.syncImages(force);
                output.setResult(ResponseEntity.ok(synced));
            });

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return output;
    }

    /**
     * {@code DELETE  /employees/:id} : delete the "id" employee.
     *
     * @param id the id of the employeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.debug("REST request to delete Employee : {}", id);

        Optional<EmployeeDTO> employeeDTO = employeeService.findOne(id);
        if (!employeeDTO.isPresent()) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        employeeService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeeDTO.get(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/room-requisition")
    public ResponseEntity<List<EmployeeMinimalDTO>> getAllEmployeesForRoomRequisition() {
        log.debug("REST request to get Employees for room requisitions");
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeMinimalDTO> employeeMinimalDTOS = employeeMinimalMapper.toDto(employeeList);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok(employeeMinimalDTOS);
    }

    @GetMapping("/get-employee-dashboard-analytics")
    public ResponseEntity<EmployeeDashboardAnalyticsDTO> getEmployeeDashboardAnalytics() {
        log.debug("REST request to get Employee Dashboard Analytics");

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(employeeService.getEmployeeDashboardAnalytics(LocalDate.now()));
    }

    @GetMapping("/fix-employee-pin")
    public ResponseEntity<Boolean> trimPIN(@RequestParam String type) {
        log.error("REST request to fix employee pin");

        boolean result;
        switch (type) {
            case "employee":
                result = pinTrimUtilService.batchEmployeePINTrim();
                break;
            case "employee-salary":
                result = pinTrimUtilService.batchEmployeeSalaryPINTrim();
                break;
            default:
                result = false;
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/fix-multiple-joining-history")
    public ResponseEntity<Boolean> fixMultipleJoiningHistory() {
        log.error("REST request to fix multiple joining history");

        boolean result = employeeService.fixMultipleJoiningEntry();

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    private void publishEvent(String officialEmail, String pin) {
        eventPublisher.publishEvent(new EmployeeRegistrationEvent(this, officialEmail, pin));
    }
}
