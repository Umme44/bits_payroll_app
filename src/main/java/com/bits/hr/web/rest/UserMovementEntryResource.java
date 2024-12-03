package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.UserMovementEntryService;
import com.bits.hr.service.attendanceTimeSheet.AttendanceSharedService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.MovementEntryApplicationEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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
 * REST controller for managing {@link com.bits.hr.domain.MovementEntry}.
 */
@RestController
@RequestMapping("/api/common")
@Log4j2
public class UserMovementEntryResource {

    private static final String ENTITY_NAME = "movementEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private UserMovementEntryService userMovementEntryService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AttendanceSharedService attendanceSharedService;

    @PostMapping("/user-movement-entries")
    public ResponseEntity<MovementEntryDTO> createMovementEntry(@RequestBody @Valid MovementEntryDTO movementEntryDTO) throws URISyntaxException {
        log.debug("REST request to save MovementEntry : {}", movementEntryDTO);
        if (movementEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new movementEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // date validation
        if (movementEntryDTO.getStartDate().isBefore(attendanceSharedService.getMaxAllowedPreviousDate())) {
            String msg = attendanceSharedService.getMaxAllowedPreviousDateCrossingValidationMessage();
            throw new BadRequestAlertException(msg, ENTITY_NAME, "maxAllowedDateCrossed");
        }

        MovementEntryDTO result = userMovementEntryService.create(movementEntryDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "UserMovementEntry");
        publishEvent(movementEntryDTO, EventType.CREATED);

        return ResponseEntity
            .created(new URI("/api/common/user-movement-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user-movement-entries")
    public ResponseEntity<MovementEntryDTO> updateMovementEntry(@RequestBody @Valid MovementEntryDTO movementEntryDTO) throws URISyntaxException {
        log.debug("REST request to update MovementEntry : {}", movementEntryDTO);
        if (movementEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // date validation
        if (movementEntryDTO.getStartDate().isBefore(attendanceSharedService.getMaxAllowedPreviousDate())) {
            String msg = attendanceSharedService.getMaxAllowedPreviousDateCrossingValidationMessage();
            throw new BadRequestAlertException(msg, ENTITY_NAME, "maxAllowedDateCrossed");
        }

        Optional<MovementEntryDTO> previousMovementEntryOptional = userMovementEntryService.findOne(movementEntryDTO.getId());

        if (!previousMovementEntryOptional.isPresent()) {
            log.error("MovementEntry is not found for", movementEntryDTO.getId());
            throw new BadRequestAlertException("MovementEntry is not found", ENTITY_NAME, "accessForbidden");
        }

        if (previousMovementEntryOptional.get().getEmployeeId() == null) {
            log.error("EmployeeId is not found", movementEntryDTO.getId());
            throw new BadRequestAlertException("EmployeeId is not found", ENTITY_NAME, "accessForbidden");
        }
        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            log.error("EmployeeId is not found", movementEntryDTO.getId());
            throw new NoEmployeeProfileException();
        }
        if (!previousMovementEntryOptional.get().getEmployeeId().equals(currentEmployeeId.get())) {
            log.error("Access Forbidden", movementEntryDTO.getId());
            throw new BadRequestAlertException("Access Forbidden", "UserMovementEntry", "accessForbidden");
        }

        // check status
        if (
            previousMovementEntryOptional.get().getStatus() == Status.APPROVED ||
            previousMovementEntryOptional.get().getStatus() == Status.NOT_APPROVED
        ) {
            log.error("Only pending requests are allowed to edit.", previousMovementEntryOptional.get().getStatus());
            throw new BadRequestAlertException("Only pending requests are allowed to edit.", "UserMovementEntry", "statusIsNotPending");
        }

        MovementEntryDTO result = userMovementEntryService.update(movementEntryDTO);
        User user = currentEmployeeService.getCurrentUser().get();

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "UserMovementEntry");
        publishEvent(movementEntryDTO, EventType.UPDATED);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movementEntryDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/user-movement-entries")
    public ResponseEntity<List<MovementEntryDTO>> getAllMovementEntries(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of MovementEntries");
        Page<MovementEntryDTO> page = userMovementEntryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserMovementEntry");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/user-movement-entries/{id}")
    public ResponseEntity<MovementEntryDTO> getMovementEntry(@PathVariable Long id) {
        log.debug("REST request to get MovementEntry : {}", id);
        Optional<MovementEntryDTO> movementEntryDTO = userMovementEntryService.findOne(id);

        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        if (!movementEntryDTO.get().getEmployeeId().equals(currentEmployeeId.get())) {
            throw new BadRequestAlertException("Access Forbidden", "UserMovementEntry", "accessForbidden");
        }

        User user = currentEmployeeService.getCurrentUser().get();

        if (movementEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserMovementEntry");
        }
        return ResponseUtil.wrapOrNotFound(movementEntryDTO);
    }

    @DeleteMapping("/user-movement-entries/{id}")
    public ResponseEntity<Void> deleteMovementEntry(@PathVariable Long id) {
        log.debug("REST request to delete MovementEntry : {}", id);
        Optional<MovementEntryDTO> movementEntryDTO = userMovementEntryService.findOne(id);

        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        if (!movementEntryDTO.get().getEmployeeId().equals(currentEmployeeId.get())) {
            throw new BadRequestAlertException("Access Forbidden", "UserMovementEntry", "accessForbidden");
        }

        User user = currentEmployeeService.getCurrentUser().get();

        if (movementEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, movementEntryDTO.get(), RequestMethod.GET, "UserMovementEntry");
        }

        if (movementEntryDTO.get().getStatus() == Status.APPROVED || movementEntryDTO.get().getStatus() == Status.NOT_APPROVED) {
            log.error("Only pending requests are allowed to edit.", movementEntryDTO.get().getStatus());
            throw new BadRequestAlertException("Only pending requests are allowed to edit.", "UserMovementEntry", "statusIsNotPending");
        } else {
            userMovementEntryService.delete(id);
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user-movement-entries/attendance-conflict/{startDate}/{endDate}")
    public ResponseEntity<Boolean> anyAttendanceConflict(
        @PathVariable(required = false) LocalDate startDate,
        @PathVariable(required = false) LocalDate endDate
    ) {
        Boolean result = userMovementEntryService.findAnyConflictWithAttendance(startDate, endDate);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserMovementEntry");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/user-movement-entries/manual-attendance-conflict/{startDate}/{endDate}")
    public ResponseEntity<Boolean> anyManualAttendanceConflict(
        @PathVariable(required = false) LocalDate startDate,
        @PathVariable(required = false) LocalDate endDate
    ) {
        Boolean result = userMovementEntryService.findAnyConflictWithAttendance(startDate, endDate);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserMovementEntry");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/user-movement-entries/movement-entry-conflict/{startDate}/{endDate}")
    public ResponseEntity<Boolean> anyMovementConflict(
        @RequestParam(required = false) Long movementEntryId,
        @PathVariable(required = false) LocalDate startDate,
        @PathVariable(required = false) LocalDate endDate
    ) {
        Boolean result = userMovementEntryService.findAnyConflictWithMovementEntry(movementEntryId, startDate, endDate);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserMovementEntry");
        return ResponseEntity.ok().body(result);
    }

    private void publishEvent(MovementEntryDTO movementEntryDTO, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        MovementEntryApplicationEvent movementEntryApplicationEvent = new MovementEntryApplicationEvent(this, movementEntryDTO, event);
        applicationEventPublisher.publishEvent(movementEntryApplicationEvent);
    }
}
