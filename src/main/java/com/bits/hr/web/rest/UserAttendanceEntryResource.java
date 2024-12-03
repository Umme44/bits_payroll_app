package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.attendanceSync.OnlinePunchService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/user-attendance-entry")
public class UserAttendanceEntryResource {

    private final Logger log = LoggerFactory.getLogger(UnitResource.class);

    @Autowired
    private OnlinePunchService onlinePunchService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @PutMapping("")
    public ResponseEntity<String> createAttendanceEntry(@RequestBody AttendanceEntryDTO attendanceEntryDTO) throws URISyntaxException {
        return ResponseEntity.ok().body(onlinePunchService.createAttendanceEntry(attendanceEntryDTO));
    }

    @GetMapping("/status")
    public AttendanceEntryDTO getTodayStatus() {
        // get employee from security utils
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserAttendanceEntry");
        log.debug("Current date online status hit");
        try {
            return onlinePunchService.currentDateStatus();
        } catch (Exception ex) {
            return new AttendanceEntryDTO();
        }
    }

    @PutMapping("/online-punch")
    public ResponseEntity<Boolean> onlinePunch() throws URISyntaxException {
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserAttendanceEntry");
        return ResponseEntity.ok().body(onlinePunchService.punch());
    }

    @GetMapping("/is-online-punch-enabled")
    public ResponseEntity<Boolean> checkEligibility() {
        log.debug("REST request to check employee's Work From Home (online punch) has enabled");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserAttendanceEntry");
        return ResponseEntity.ok().body(onlinePunchService.isEligible());
    }
}
