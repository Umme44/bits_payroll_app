package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.attendanceTimeSheet.AtsDataProcessLevel;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.DateRangeDTO;
import com.bits.hr.service.dto.TimeRangeDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance-user/attendance-time-sheet")
public class AttendanceTimeSheetResource {

    private final Logger log = LoggerFactory.getLogger(AttendanceTimeSheetResource.class);

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    private static final String RESOURCE_NAME = "AttendanceTimeSheetResource";

    public AttendanceTimeSheetResource(EventLoggingPublisher eventLoggingPublisher) {
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @PostMapping("")
    public List<AttendanceTimeSheetDTO> getAttendanceTimeSheetBetweenDateRange(@RequestBody TimeRangeDTO timeRangeDTO) {
        log.debug("REST request to get all attendanceTimeSheets between Date range");
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) throw new NoEmployeeProfileException();

        //LocalDate dateOfJoining = employeeOptional.get().getDateOfJoining();
        if (timeRangeDTO.getStartDate() == null) {
            timeRangeDTO.setStartDate(LocalDate.now().minusDays(30));
        }
        // if DOJ is after startDate , minimum start date is joining date
        //        if (timeRangeDTO.getStartDate().isBefore(dateOfJoining)) {
        //            timeRangeDTO.setStartDate(dateOfJoining);
        //        }
        if (timeRangeDTO.getEndDate() == null) {
            timeRangeDTO.setEndDate(LocalDate.now());
        }

        List<AttendanceTimeSheetDTO> result = attendanceTimeSheetService.getAttendanceTimeSheet(
            employeeOptional.get().getId(),
            timeRangeDTO.getStartDate(),
            timeRangeDTO.getEndDate(),
            AtsDataProcessLevel.FULL_FEATURED_USER
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);
        return result;
    }

    @GetMapping("/dashboard-history")
    public List<AttendanceTimeSheetDTO> getAttendanceTimeSheetOfDashboard() {
        log.debug("REST request to get  attendanceTimeSheets");
        try {
            Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
            if (!employeeOptional.isPresent()) throw new NoEmployeeProfileException();

            LocalDate startDate = LocalDate.now().minusDays(6); //for last 07 days
            LocalDate endDate = LocalDate.now();

            List<AttendanceTimeSheetDTO> result = attendanceTimeSheetService.getAttendanceTimeSheet(
                employeeOptional.get().getId(),
                startDate,
                endDate,
                AtsDataProcessLevel.MINIMAL
            );
            return result;
        } catch (Exception ex) {
            List<AttendanceTimeSheetDTO> result = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                AttendanceTimeSheetDTO dummyAttendance = new AttendanceTimeSheetDTO();
                dummyAttendance.setDate(LocalDate.now().minusDays(i));
                dummyAttendance.setAttendanceStatus(AttendanceStatus.ABSENT);
                result.add(dummyAttendance);
            }
            User user = currentEmployeeService.getCurrentUser().get();
            eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
            return result;
        }
    }

    /**
     * Find Pending Applications of Attendance, Leave, Movement Entry
     *
     * @param dateRangeDTO
     * @return
     */
    @PostMapping("/pending-applications-between-date-range")
    public AttendanceTimeSheetDTO findAnyPendingApplicationsForCurrentEmployee(@RequestBody DateRangeDTO dateRangeDTO) {
        log.debug("REST request to get pending attendanceTimeSheet  for current employee");
        if (!currentEmployeeService.getCurrentEmployeePin().isPresent()) {
            throw new NoEmployeeProfileException();
        }
        String pin = currentEmployeeService.getCurrentEmployeePin().get();

        AttendanceTimeSheetDTO attendanceTimeSheetDTO = attendanceTimeSheetService.findAnyApplicationsBetweenDateRange(
            pin,
            dateRangeDTO.getStartDate(),
            dateRangeDTO.getEndDate()
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);
        return attendanceTimeSheetDTO;
    }
}
