package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.ManualAttendanceEntryRepository;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.ManualAttendanceEntryService;
import com.bits.hr.service.attendanceTimeSheet.AttendanceSharedService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import com.bits.hr.service.dto.DateRangeDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.ManualAttendanceEntryApplicationEvent;
import com.bits.hr.service.search.FilterDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * REST controller for managing {@link com.bits.hr.domain.ManualAttendanceEntry}.
 */
@RestController
@RequestMapping("/api/common/manual-attendances")
public class UserManualAttendanceEntryResource {

    private final Logger log = LoggerFactory.getLogger(UserManualAttendanceEntryResource.class);

    private static final String ENTITY_NAME = "manualAttendanceEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManualAttendanceEntryService manualAttendanceEntryService;
    private final CurrentEmployeeService currentEmployeeService;
    private final AttendanceEntryService attendanceEntryService;
    private final ManualAttendanceEntryRepository manualAttendanceEntryRepository;
    private final AttendanceSharedService attendanceSharedService;

    private final EventLoggingPublisher eventLoggingPublisher;

    private final ApplicationEventPublisher applicationEventPublisher;

    public UserManualAttendanceEntryResource(
        ManualAttendanceEntryService manualAttendanceEntryService,
        CurrentEmployeeService currentEmployeeService,
        AttendanceEntryService attendanceEntryService,
        ManualAttendanceEntryRepository manualAttendanceEntryRepository,
        AttendanceSharedService attendanceSharedService,
        EventLoggingPublisher eventLoggingPublisher,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        this.manualAttendanceEntryService = manualAttendanceEntryService;
        this.currentEmployeeService = currentEmployeeService;
        this.attendanceEntryService = attendanceEntryService;
        this.manualAttendanceEntryRepository = manualAttendanceEntryRepository;
        this.attendanceSharedService = attendanceSharedService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("")
    public ResponseEntity<List<ManualAttendanceEntryDTO>> findAll(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.info("REST Request to get Page of Manual Attendance order by date descending");
        Optional<Long> employeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!employeeId.isPresent()) {
            throw new BadRequestAlertException("You are not logged user", "ManualAttendanceEntry", "noEmployee");
        }
        Page<ManualAttendanceEntryDTO> page = manualAttendanceEntryService.findAllDTOs(pageable, employeeId.get());
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "manualAttendanceEntry");
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/find-by-date-range")
    public ResponseEntity<List<ManualAttendanceEntryDTO>> findAllByDateRange(@RequestBody FilterDto filterDto) {
        log.debug("All Manual Attendances of between two date range");
        try {
            String pin = currentEmployeeService.getCurrentEmployeePin().get();
            List<ManualAttendanceEntryDTO> manualAttendanceEntryList = manualAttendanceEntryService.findAllByPinAndDateRange(
                pin,
                filterDto
            );

            User user = currentEmployeeService.getCurrentUser().get();
            eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, "manualAttendanceEntry");

            return ResponseEntity.ok(manualAttendanceEntryList);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("")
    public ResponseEntity<ManualAttendanceEntryDTO> createManualAttendanceEntry(
        @RequestBody ManualAttendanceEntryDTO manualAttendanceEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ManualAttendanceEntry : {}", manualAttendanceEntryDTO);
        if (manualAttendanceEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new manualAttendanceEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<Long> employeeId = currentEmployeeService.getCurrentEmployeeId();

        // date validation
        if (manualAttendanceEntryDTO.getDate().isBefore(attendanceSharedService.getMaxAllowedPreviousDate())) {
            String msg = attendanceSharedService.getMaxAllowedPreviousDateCrossingValidationMessage();
            throw new BadRequestAlertException(msg, ENTITY_NAME, "maxAllowedDateCrossed");
        }

        if (manualAttendanceEntryDTO.getInTime() == null) {
            throw new BadRequestAlertException("AttendanceEntry inTime can not be null", ENTITY_NAME, "NotNull");
        }

        if (!manualAttendanceEntryDTO.getDate().equals(LocalDate.now()) && manualAttendanceEntryDTO.getOutTime() == null) {
            throw new BadRequestAlertException("AttendanceEntry outTime can not be null", ENTITY_NAME, "NotNull");
        }

        manualAttendanceEntryDTO.setEmployeeId(employeeId.get());
        manualAttendanceEntryDTO.setIsHRApproved(false);
        manualAttendanceEntryDTO.setIsLineManagerApproved(false);
        manualAttendanceEntryDTO.setIsRejected(false);

        ManualAttendanceEntryDTO result = manualAttendanceEntryService.create(manualAttendanceEntryDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "manualAttendanceEntry");
        publishEvent(result, EventType.CREATED);

        return ResponseEntity
            .created(new URI("/api/common/manual-attendance-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("")
    public ResponseEntity<ManualAttendanceEntryDTO> updateManualAttendanceEntry(
        @RequestBody ManualAttendanceEntryDTO manualAttendanceEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ManualAttendanceEntry : {}", manualAttendanceEntryDTO.getId());

        if (manualAttendanceEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idnull");
        }

        Optional<ManualAttendanceEntryDTO> previousManualAttendanceEntryDTO = manualAttendanceEntryService.findOne(
            manualAttendanceEntryDTO.getId()
        );

        if (!previousManualAttendanceEntryDTO.isPresent()) {
            throw new BadRequestAlertException("ManualAttendanceEntry is not found", "ManualAttendanceEntry", "accessForbidden");
        }

        if (previousManualAttendanceEntryDTO.get().getEmployeeId() == null) {
            throw new BadRequestAlertException("EmployeeId is not found", "ManualAttendanceEntry", "accessForbidden");
        }

        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        if (!previousManualAttendanceEntryDTO.get().getEmployeeId().equals(currentEmployeeId.get())) {
            throw new BadRequestAlertException("Access Forbidden", "ManualAttendanceEntry", "accessForbidden");
        }

        // date validation
        if (manualAttendanceEntryDTO.getDate().isBefore(attendanceSharedService.getMaxAllowedPreviousDate())) {
            String msg = attendanceSharedService.getMaxAllowedPreviousDateCrossingValidationMessage();
            throw new BadRequestAlertException(msg, ENTITY_NAME, "maxAllowedDateCrossed");
        }
        /*

        // check status
        if(previousManualAttendanceEntryDTO.get().isIsHRApproved() || previousManualAttendanceEntryDTO.get().isIsLineManagerApproved() ||
                previousManualAttendanceEntryDTO.get().isIsRejected()){
            log.error("Only pending requests are allowed to edit.", previousManualAttendanceEntryDTO.get().getId());
            throw new BadRequestAlertException("Only pending requests are allowed to edit.", "ManualAttendanceEntry", "statusIsNotPending");
        }
*/

        manualAttendanceEntryDTO.setIsHRApproved(false);
        manualAttendanceEntryDTO.setIsLineManagerApproved(false);
        manualAttendanceEntryDTO.setIsRejected(false);

        ManualAttendanceEntryDTO result = manualAttendanceEntryService.update(manualAttendanceEntryDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "manualAttendanceEntry");
        publishEvent(result, EventType.UPDATED);

        return ResponseEntity
            .created(new URI("/api/common/manual-attendance-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<ManualAttendanceEntryDTO> manualAttendanceEntryDTO = manualAttendanceEntryService.findOne(id);

        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        if (!manualAttendanceEntryDTO.get().getEmployeeId().equals(currentEmployeeId.get())) {
            log.error("Access Forbidden", manualAttendanceEntryDTO.get().getId());
            throw new BadRequestAlertException("Access Forbidden", "ManualAttendanceEntry", "accessForbidden");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        if (manualAttendanceEntryDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, manualAttendanceEntryDTO.get(), RequestMethod.DELETE, "manualAttendanceEntry");
        }

        if (manualAttendanceEntryDTO.get().isIsHRApproved() || manualAttendanceEntryDTO.get().isIsLineManagerApproved()) {
            log.error("Only pending requests are allowed to edit.", manualAttendanceEntryDTO.get().getId());
            throw new BadRequestAlertException("Only pending requests are allowed to edit.", "ManualAttendanceEntry", "statusIsNotPending");
        } else {
            manualAttendanceEntryRepository.deleteById(id);
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/date-wise-entry-search/{date}")
    public ResponseEntity<AttendanceEntryDTO> getByDate(@PathVariable LocalDate date) {
        Optional<AttendanceEntryDTO> attendanceEntryDTO1 = attendanceEntryService.findByDateAndEmployeeId(
            date,
            currentEmployeeService.getCurrentEmployeeId().get()
        );
        if (!attendanceEntryDTO1.isPresent()) {
            attendanceEntryDTO1 = Optional.of(new AttendanceEntryDTO());
        }
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "manualAttendanceEntry");
        return ResponseUtil.wrapOrNotFound(attendanceEntryDTO1);
    }

    @PostMapping("/conflict-between-date-range")
    public boolean findAnyConflictForCurrentEmployee(@RequestBody DateRangeDTO dateRangeDTO) {
        if (!currentEmployeeService.getCurrentUserId().isPresent()) {
            return false;
        }

        long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        List<ManualAttendanceEntryDTO> manualAttendanceEntryList = manualAttendanceEntryService.findAllPendingManualAttendance(
            employeeId,
            dateRangeDTO.getStartDate(),
            dateRangeDTO.getEndDate()
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, "manualAttendanceEntry");
        if (manualAttendanceEntryList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void publishEvent(ManualAttendanceEntryDTO manualAttendanceEntryDTO, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        ManualAttendanceEntryApplicationEvent manualAttendanceEntryApplicationEvent = new ManualAttendanceEntryApplicationEvent(
            this,
            manualAttendanceEntryDTO,
            event
        );
        applicationEventPublisher.publishEvent(manualAttendanceEntryApplicationEvent);
    }
}
