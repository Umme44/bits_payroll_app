package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.AttendanceService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AttendanceDTO;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.bits.hr.domain.Attendance}.
 */
@RestController
@RequestMapping("/api/attendance-mgt/attendances")
public class AttendanceResource {

    private static final String ENTITY_NAME = "attendance";
    private final Logger log = LoggerFactory.getLogger(AttendanceResource.class);
    private final AttendanceService attendanceService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public AttendanceResource(
        AttendanceService attendanceService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.attendanceService = attendanceService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /attendances} : Create a new attendance.
     *
     * @param attendanceDTO the attendanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attendanceDTO, or with status {@code 400 (Bad Request)} if the attendance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AttendanceDTO> createAttendance(@Valid @RequestBody AttendanceDTO attendanceDTO) throws URISyntaxException {
        log.debug("REST request to save Attendance : {}", attendanceDTO);
        if (attendanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new attendance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttendanceDTO result = attendanceService.save(attendanceDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, " Attendance");

        return ResponseEntity
            .created(new URI("/api/attendance-mgt/attendances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attendances} : Updates an existing attendance.
     *
     * @param attendanceDTO the attendanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendanceDTO,
     * or with status {@code 400 (Bad Request)} if the attendanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attendanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<AttendanceDTO> updateAttendance(@Valid @RequestBody AttendanceDTO attendanceDTO) throws URISyntaxException {
        log.debug("REST request to update Attendance : {}", attendanceDTO);
        if (attendanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttendanceDTO result = attendanceService.save(attendanceDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "Attendance");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attendances} : get all the attendances.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attendances in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendances(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Attendances");
        Page<AttendanceDTO> page = attendanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "Attendance");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attendances/:id} : get the "id" attendance.
     *
     * @param id the id of the attendanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attendanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDTO> getAttendance(@PathVariable Long id) {
        log.debug("REST request to get Attendance : {}", id);
        Optional<AttendanceDTO> attendanceDTO = attendanceService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (attendanceDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, attendanceDTO.get(), RequestMethod.GET, "Attendance");
        }
        return ResponseUtil.wrapOrNotFound(attendanceDTO);
    }

    /**
     * {@code DELETE  /attendances/:id} : delete the "id" attendance.
     *
     * @param id the id of the attendanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        log.debug("REST request to delete Attendance : {}", id);
        Optional<AttendanceDTO> attendanceDTO = attendanceService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (attendanceDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, attendanceDTO.get(), RequestMethod.DELETE, "Attendance");
        }
        attendanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
