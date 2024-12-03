package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import com.bits.hr.service.workFromHomeApplication.WorkFromHomeApplicationHRService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employee-mgt")
public class WorkFromHomeApplicationResource {

    private final Logger log = LoggerFactory.getLogger(WorkFromHomeApplicationCommonResource.class);

    private static final String ENTITY_NAME = "workFromHomeApplication";
    private static final String RESOURCE_NAME = "WorkFromHomeApplicationResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkFromHomeApplicationHRService workFromHomeApplicationHRService;

    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public WorkFromHomeApplicationResource(
        WorkFromHomeApplicationHRService workFromHomeApplicationHRService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.workFromHomeApplicationHRService = workFromHomeApplicationHRService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @PostMapping("/work-from-home-applications")
    public ResponseEntity<WorkFromHomeApplicationDTO> createWorkFromHomeApplication(
        @Valid @RequestBody WorkFromHomeApplicationDTO workFromHomeApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WorkFromHomeApplication : {}", workFromHomeApplicationDTO);
        if (workFromHomeApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new workFromHomeApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        Optional<User> user = currentEmployeeService.getCurrentUser();
        if (employee.isPresent() && user.isPresent()) {
            workFromHomeApplicationDTO.setEmployeeId(workFromHomeApplicationDTO.getEmployeeId());
            workFromHomeApplicationDTO.setCreatedById(user.get().getId());
            workFromHomeApplicationDTO.setAppliedById(user.get().getId());
            workFromHomeApplicationDTO.setAppliedAt(LocalDate.now());
            workFromHomeApplicationDTO.setCreatedAt(Instant.now());
            workFromHomeApplicationDTO.setStatus(Status.APPROVED);
            workFromHomeApplicationDTO.setSanctionedById(user.get().getId());
            workFromHomeApplicationDTO.setSanctionedAt(Instant.now());
        } else {
            throw new NoEmployeeProfileException();
        }
        WorkFromHomeApplicationDTO result = workFromHomeApplicationHRService.create(workFromHomeApplicationDTO);
        eventLoggingPublisher.publishEvent(user.get(), result, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/common/work-from-home-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-from-home-applications} : Updates an existing workFromHomeApplication.
     *
     * @param workFromHomeApplicationDTO the workFromHomeApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workFromHomeApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the workFromHomeApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workFromHomeApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-from-home-applications")
    public ResponseEntity<WorkFromHomeApplicationDTO> updateWorkFromHomeApplication(
        @Valid @RequestBody WorkFromHomeApplicationDTO workFromHomeApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkFromHomeApplication : {}", workFromHomeApplicationDTO);
        if (workFromHomeApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        Optional<User> user = currentEmployeeService.getCurrentUser();
        if (employee.isPresent()) {
            workFromHomeApplicationDTO.setUpdatedById(user.get().getId());
            workFromHomeApplicationDTO.setUpdatedAt(Instant.now());
            workFromHomeApplicationDTO.setStatus(Status.APPROVED);
            workFromHomeApplicationDTO.setSanctionedById(user.get().getId());
            workFromHomeApplicationDTO.setSanctionedAt(Instant.now());
        } else {
            throw new NoEmployeeProfileException();
        }
        WorkFromHomeApplicationDTO result = workFromHomeApplicationHRService.update(workFromHomeApplicationDTO);
        eventLoggingPublisher.publishEvent(user.get(), result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workFromHomeApplicationDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/work-from-home-applications/{id}")
    public ResponseEntity<WorkFromHomeApplicationDTO> getWorkFromHomeApplication(@PathVariable Long id) {
        log.debug("REST request to get WorkFromHomeApplication : {}", id);
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        Optional<User> user = currentEmployeeService.getCurrentUser();
        long employeeId;
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        Optional<WorkFromHomeApplicationDTO> workFromHomeApplicationDTO = workFromHomeApplicationHRService.findOne(id);
        eventLoggingPublisher.publishEvent(user.get(), workFromHomeApplicationDTO, RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(workFromHomeApplicationDTO);
    }

    /**
     * {@code DELETE  /work-from-home-applications/:id} : delete the "id" workFromHomeApplication.
     *
     * @param id the id of the workFromHomeApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-from-home-applications/{id}")
    public ResponseEntity<Void> deleteWorkFromHomeApplication(@PathVariable Long id) {
        log.debug("REST request to delete WorkFromHomeApplication : {}", id);
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        Optional<User> user = currentEmployeeService.getCurrentUser();
        Optional<WorkFromHomeApplicationDTO> workFromHomeApplicationDTO = workFromHomeApplicationHRService.findOne(id);
        long employeeId;
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        eventLoggingPublisher.publishEvent(user.get(), workFromHomeApplicationDTO, RequestMethod.DELETE, RESOURCE_NAME);
        workFromHomeApplicationHRService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/work-from-home-applications/all-pending-work-applications")
    public ResponseEntity<List<WorkFromHomeApplicationDTO>> getAllPendingApplicationsForHr(
        @RequestParam(required = false) String searchText,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws URISyntaxException {
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        Optional<User> user = currentEmployeeService.getCurrentUser();
        long employeeId;
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        if (searchText == null) {
            searchText = "";
        }
        Page<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOS = workFromHomeApplicationHRService.getAllPendingApplicationsForHr(
            searchText,
            pageable
        );
        eventLoggingPublisher.publishEvent(user.get(), Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            workFromHomeApplicationDTOS
        );
        return ResponseEntity.ok().headers(headers).body(workFromHomeApplicationDTOS.getContent());
    }

    @GetMapping("/work-from-home-applications/by-date-ranges")
    public ResponseEntity<List<WorkFromHomeApplicationDTO>> getAllWorkFromHomeApplicationsBetweenTwoDates(
        @RequestParam(required = false) Long employeeId,
        @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(required = false) Status status,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get All Work From Home Applications");
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();

        if (!employee.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        Page<WorkFromHomeApplicationDTO> result = workFromHomeApplicationHRService.findWorkFromApplicationBetweenDates(
            employeeId,
            startDate,
            endDate,
            status,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    // get all employee List for work-from-home admin approval page(HR)
    @GetMapping("/work-from-home-applications/all-employees")
    public ResponseEntity<List<WorkFromHomeApplicationDTO>> getAllWorkFromHomeApplicationsHr(
        @RequestParam(required = false, defaultValue = "") String searchText,
        @RequestParam(required = false) Boolean onlineAttendance,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of WorkFromHomeApplications");
        Page<WorkFromHomeApplicationDTO> page = workFromHomeApplicationHRService.getAllAppliedApplicationsHr(
            searchText,
            onlineAttendance,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    // get all employee List for work-from-home admin inactive online attendance(HR)
    @GetMapping("/work-from-home-applications/all-inactive-employees")
    public ResponseEntity<List<WorkFromHomeApplicationDTO>> getAllInactiveWorkFromHomeApplicationsHr(
        @RequestParam(required = false) String searchText,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of WorkFromHomeApplications");
        if (searchText == null) {
            searchText = "";
        }
        Page<WorkFromHomeApplicationDTO> page = workFromHomeApplicationHRService.getAllInactiveAppliedApplicationsHr(searchText, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    // get all employee List for work-from-home admin active online attendance(HR)
    @GetMapping("/work-from-home-applications/all-active-employees")
    public ResponseEntity<List<WorkFromHomeApplicationDTO>> getAllActiveWorkFromHomeApplicationsHr(
        @RequestParam(required = false) String searchText,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of WorkFromHomeApplications");
        User user = currentEmployeeService.getCurrentUser().get();
        if (searchText == null) {
            searchText = "";
        }
        Page<WorkFromHomeApplicationDTO> page = workFromHomeApplicationHRService.getAllActiveAppliedApplicationsHr(searchText, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/work-from-home-applications/all-active-employees-numbers")
    public ResponseEntity<Integer> getAllActiveOnlineAttendanceEmployee() {
        log.debug("REST request to get a total active work from home employees");
        User user = currentEmployeeService.getCurrentUser().get();

        int count = workFromHomeApplicationHRService.totalWorkFromHomeActiveEmployees();
        eventLoggingPublisher.publishEvent(user, count, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/work-from-home-applications/all-inactive-employees-numbers")
    public ResponseEntity<Integer> getAllInActiveOnlineAttendanceEmployee() {
        log.debug("REST request to get a total inactive work from home employees");
        User user = currentEmployeeService.getCurrentUser().get();

        int count = workFromHomeApplicationHRService.totalWorkFromHomeInActiveEmployees();
        eventLoggingPublisher.publishEvent(user, count, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(count);
    }

    @PostMapping("/work-from-home-applications/isApplied")
    public ResponseEntity<Boolean> checkIsApplied(@RequestBody WorkFromHomeApplicationDTO workFromHomeApplicationDTO) {
        boolean isApplied = false;
        Long requesterId = workFromHomeApplicationDTO.getEmployeeId();
        LocalDate startDate = workFromHomeApplicationDTO.getStartDate();
        LocalDate endDate = workFromHomeApplicationDTO.getEndDate();
        if (startDate != null && endDate != null) {
            if (workFromHomeApplicationDTO.getId() != null) {
                isApplied =
                    workFromHomeApplicationHRService.checkPreviousApplicationBetweenDateRangeOnUpdate(
                        requesterId,
                        workFromHomeApplicationDTO.getId(),
                        startDate,
                        endDate
                    );
            } else {
                isApplied =
                    workFromHomeApplicationHRService.checkPreviousApplicationBetweenDateRangeOnCreate(requesterId, startDate, endDate);
            }
        } else {
            throw new RuntimeException("startDate or endDate can not be null");
        }

        return ResponseEntity.ok(isApplied);
    }
    @PostMapping("/work-from-home-applications/import-work-from-home-applications")
    public ResponseEntity<Boolean> importWorkFromHomeApplicationFromXlsx(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(workFromHomeApplicationHRService.workFromHomeBatchUploadByAdmin(file));
    }
}
