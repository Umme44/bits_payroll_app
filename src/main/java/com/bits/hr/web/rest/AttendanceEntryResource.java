package com.bits.hr.web.rest;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
 * REST controller for managing {@link com.bits.hr.domain.AttendanceEntry}.
 */
@RestController
@RequestMapping("/api/attendance-mgt/attendance-entries")
public class AttendanceEntryResource {

    private final Logger log = LoggerFactory.getLogger(AttendanceEntryResource.class);

    private static final String ENTITY_NAME = "attendanceEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttendanceEntryService attendanceEntryService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public AttendanceEntryResource(
        AttendanceEntryService attendanceEntryService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.attendanceEntryService = attendanceEntryService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /attendance-entries} : Create a new attendanceEntry.
     *
     * @param attendanceEntryDTO the attendanceEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attendanceEntryDTO, or with status {@code 400 (Bad Request)} if the attendanceEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AttendanceEntryDTO> createAttendanceEntry(@Valid @RequestBody AttendanceEntryDTO attendanceEntryDTO)
        throws URISyntaxException {
        log.debug("REST request to save AttendanceEntry : {}", attendanceEntryDTO);
        if (attendanceEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new attendanceEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        attendanceEntryDTO.setStatus(Status.APPROVED);
        AttendanceEntryDTO result = attendanceEntryService.create(attendanceEntryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "AttendanceEntry");

        return ResponseEntity
            .created(new URI("/api/attendance-mgt/attendance-entries" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attendance-entries} : Updates an existing attendanceEntry.
     *
     * @param attendanceEntryDTO the attendanceEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendanceEntryDTO,
     * or with status {@code 400 (Bad Request)} if the attendanceEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attendanceEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<AttendanceEntryDTO> updateAttendanceEntry(@Valid @RequestBody AttendanceEntryDTO attendanceEntryDTO)
        throws URISyntaxException {
        log.debug("REST request to update AttendanceEntry : {}", attendanceEntryDTO);
        if (attendanceEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttendanceEntryDTO result = attendanceEntryService.update(attendanceEntryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "AttendanceEntry");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendanceEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attendance-entries} : get all the attendanceEntries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attendanceEntries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AttendanceEntryDTO>> getAllAttendanceEntries(
        @RequestParam(required = false) Long employeeId,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AttendanceEntries");
        Page<AttendanceEntryDTO> page = attendanceEntryService.findAll(pageable, employeeId, startDate, endDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "AttendanceEntry");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attendance-entries/:id} : get the "id" attendanceEntry.
     *
     * @param id the id of the attendanceEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attendanceEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceEntryDTO> getAttendanceEntry(@PathVariable Long id) {
        log.debug("REST request to get AttendanceEntry : {}", id);
        Optional<AttendanceEntryDTO> attendanceEntryDTO = attendanceEntryService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (attendanceEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, attendanceEntryDTO.get(), RequestMethod.GET, "AttendanceEntry");
        }
        return ResponseUtil.wrapOrNotFound(attendanceEntryDTO);
    }

    /**
     * {@code DELETE  /attendance-entries/:id} : delete the "id" attendanceEntry.
     *
     * @param id the id of the attendanceEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendanceEntry(@PathVariable Long id) {
        log.debug("REST request to delete AttendanceEntry : {}", id);

        Optional<AttendanceEntryDTO> attendanceEntryDTO = attendanceEntryService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (attendanceEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, attendanceEntryDTO.get(), RequestMethod.DELETE, "AttendanceEntry");
        }
        attendanceEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/{id}/{date}")
    public ResponseEntity<AttendanceEntryDTO> checkExistingAttendanceEntry(@PathVariable Long id, @PathVariable LocalDate date) {
        log.debug("REST request to get AttendanceEntry : {}", id, date);
        Optional<AttendanceEntryDTO> attendanceEntryDTO = attendanceEntryService.findByDateAndEmployeeId(date, id);

        User user = currentEmployeeService.getCurrentUser().get();
        AttendanceEntryDTO attendanceEntryDTO1 = new AttendanceEntryDTO();

        if (attendanceEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, attendanceEntryDTO.get(), RequestMethod.GET, "AttendanceEntry");
            attendanceEntryDTO1 = attendanceEntryDTO.get();
        }

        return ResponseEntity.ok(attendanceEntryDTO1);
    }
}
