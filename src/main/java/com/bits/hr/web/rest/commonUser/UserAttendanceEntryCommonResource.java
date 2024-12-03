package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.ManualAttendanceEntryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.AttendanceEntry}.
 */
@RestController("userAttendanceEntryCommonResource")
@RequestMapping("/api/common/attendance-entry")
public class UserAttendanceEntryCommonResource {

    private final Logger log = LoggerFactory.getLogger(UserAttendanceEntryCommonResource.class);

    private static final String ENTITY_NAME = "manualAttendanceEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManualAttendanceEntryService manualAttendanceEntryService;
    private final CurrentEmployeeService currentEmployeeService;
    private final AttendanceEntryService attendanceEntryService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public UserAttendanceEntryCommonResource(
        ManualAttendanceEntryService manualAttendanceEntryService,
        CurrentEmployeeService currentEmployeeService,
        AttendanceEntryService attendanceEntryService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.manualAttendanceEntryService = manualAttendanceEntryService;
        this.currentEmployeeService = currentEmployeeService;
        this.attendanceEntryService = attendanceEntryService;
        this.eventLoggingPublisher = eventLoggingPublisher;
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
}
