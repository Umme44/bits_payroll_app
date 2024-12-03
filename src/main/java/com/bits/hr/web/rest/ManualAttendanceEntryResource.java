package com.bits.hr.web.rest;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.ManualAttendanceEntryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing {@link com.bits.hr.domain.ManualAttendanceEntry}.
 */
@RestController
@RequestMapping("/api/attendance-mgt")
public class ManualAttendanceEntryResource {

    private final Logger log = LoggerFactory.getLogger(ManualAttendanceEntryResource.class);

    private static final String ENTITY_NAME = "manualAttendanceEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManualAttendanceEntryService manualAttendanceEntryService;

    @Autowired
    private AttendanceEntryService attendanceEntryService;

    @Autowired
    private EmployeeService employeeService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public ManualAttendanceEntryResource(
        ManualAttendanceEntryService manualAttendanceEntryService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.manualAttendanceEntryService = manualAttendanceEntryService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /manual-attendance-entries} : Create a new manualAttendanceEntry.
     *
     * @param manualAttendanceEntryDTO the manualAttendanceEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manualAttendanceEntryDTO, or with status {@code 400 (Bad Request)} if the manualAttendanceEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manual-attendance-entries")
    public ResponseEntity<ManualAttendanceEntryDTO> createManualAttendanceEntry(
        @RequestBody ManualAttendanceEntryDTO manualAttendanceEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ManualAttendanceEntry : {}", manualAttendanceEntryDTO);
        if (manualAttendanceEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new manualAttendanceEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManualAttendanceEntryDTO result = manualAttendanceEntryService.create(manualAttendanceEntryDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "ManualAttendanceEntry");
        return ResponseEntity
            .created(new URI("/api/manual-attendance-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * Apply manual attendance entry and approve by HR with one click
     *
     * @param manualAttendanceEntryDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/manual-attendance-entries/apply-and-approve-by-hr")
    public ResponseEntity<ManualAttendanceEntryDTO> applyAndApproveByHR(
        @Valid @RequestBody ManualAttendanceEntryDTO manualAttendanceEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ManualAttendanceEntry : {}", manualAttendanceEntryDTO);
        if (manualAttendanceEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new manualAttendanceEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ManualAttendanceEntryDTO result = manualAttendanceEntryService.applyAndApproveByHR(manualAttendanceEntryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "ManualAttendanceEntry");
        return ResponseEntity
            .created(new URI("/api/manual-attendance-entries/apply-and-approve-by-hr" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * Apply manual attendance entry and approve by HR with one click
     *
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/manual-attendance-entries/date-wise-entry-search/{date}/{employeeId}")
    public ResponseEntity<AttendanceEntryDTO> findInOrOutTimeByDateAndEmployeeId(
        @PathVariable LocalDate date,
        @PathVariable Long employeeId
    ) throws URISyntaxException {
        if (date == null || employeeId == null) {
            throw new BadRequestAlertException("Date of Employee Id is missing", ENTITY_NAME, "dateOrEmployeeIdMissing");
        }

        Optional<AttendanceEntryDTO> attendanceEntryOptional = attendanceEntryService.findByDateAndEmployeeId(date, employeeId);
        if (attendanceEntryOptional.isPresent()) {
            return ResponseEntity.ok(attendanceEntryOptional.get());
        } else {
            AttendanceEntryDTO attendanceEntry = new AttendanceEntryDTO();
            if (employeeService.findOne(employeeId).isPresent()) {
                Optional<EmployeeDTO> employee = employeeService.findOne(employeeId);
                if (employee.get().getCurrentInTime() != null) {
                    attendanceEntry.setInTime(employee.get().getCurrentInTime());
                }
                if (employee.get().getCurrentOutTime() != null) {
                    attendanceEntry.setOutTime(employee.get().getCurrentOutTime());
                }
            }
            return ResponseEntity.ok().body(attendanceEntry);
        }
    }

    /**
     * {@code PUT  /manual-attendance-entries} : Updates an existing manualAttendanceEntry.
     *
     * @param manualAttendanceEntryDTO the manualAttendanceEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualAttendanceEntryDTO,
     * or with status {@code 400 (Bad Request)} if the manualAttendanceEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manualAttendanceEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manual-attendance-entries")
    public ResponseEntity<ManualAttendanceEntryDTO> updateManualAttendanceEntry(
        @RequestBody ManualAttendanceEntryDTO manualAttendanceEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ManualAttendanceEntry : {}", manualAttendanceEntryDTO);
        if (manualAttendanceEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ManualAttendanceEntryDTO result = manualAttendanceEntryService.save(manualAttendanceEntryDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "ManualAttendanceEntry");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualAttendanceEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /manual-attendance-entries} : get all the manualAttendanceEntries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manualAttendanceEntries in body.
     */
    @GetMapping("/manual-attendance-entries")
    public ResponseEntity<List<ManualAttendanceEntryDTO>> getAllManualAttendanceEntries(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ManualAttendanceEntries");
        Page<ManualAttendanceEntryDTO> page = manualAttendanceEntryService.findAll(pageable);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "ManualAttendanceEntry");
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manual-attendance-entries/:id} : get the "id" manualAttendanceEntry.
     *
     * @param id the id of the manualAttendanceEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manualAttendanceEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manual-attendance-entries/{id}")
    public ResponseEntity<ManualAttendanceEntryDTO> getManualAttendanceEntry(@PathVariable Long id) {
        log.debug("REST request to get ManualAttendanceEntry : {}", id);
        Optional<ManualAttendanceEntryDTO> manualAttendanceEntryDTO = manualAttendanceEntryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (manualAttendanceEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, manualAttendanceEntryDTO.get(), RequestMethod.GET, "AitConfig");
        }

        return ResponseUtil.wrapOrNotFound(manualAttendanceEntryDTO);
    }

    /**
     * {@code DELETE  /manual-attendance-entries/:id} : delete the "id" manualAttendanceEntry.
     *
     * @param id the id of the manualAttendanceEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manual-attendance-entries/{id}")
    public ResponseEntity<Void> deleteManualAttendanceEntry(@PathVariable Long id) {
        log.debug("REST request to delete ManualAttendanceEntry : {}", id);
        Optional<ManualAttendanceEntryDTO> manualAttendanceEntryDTO = manualAttendanceEntryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (manualAttendanceEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, manualAttendanceEntryDTO.get(), RequestMethod.GET, "AitConfig");
        }
        manualAttendanceEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
