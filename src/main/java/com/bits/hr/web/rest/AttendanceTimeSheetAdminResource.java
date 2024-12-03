package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.attendanceTimeSheet.*;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TimeRangeAndEmployeeIdDTO;
import com.bits.hr.service.dto.TimeRangeDTO;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.service.search.FilterDto;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/attendance-mgt/attendance-time-sheet-admin")
public class AttendanceTimeSheetAdminResource {

    private final Logger log = LoggerFactory.getLogger(AttendanceTimeSheetAdminResource.class);

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMinimalMapper employeeMinimalMapper;

    @Autowired
    private MonthlyAttendanceTimeSheetGenerationService monthlyAttendanceTimeSheetGenerationService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    private static final String RESOURCE_NAME = "AttendanceTimeSheetAdminResource";

    @PostMapping("")
    public List<AttendanceTimeSheetDTO> getEmployeesAttendanceTimeSheetBetweenDates(
        @RequestBody TimeRangeAndEmployeeIdDTO timeRangeAndEmployeeIdDTO
    ) {
        log.debug("REST request to get all AttendanceTimeSheet between two dates");
        Optional<Employee> employee = employeeRepository.findById(timeRangeAndEmployeeIdDTO.getEmployeeId());
        if (!employee.isPresent()) throw new NoEmployeeProfileException();
        String pin = employee.get().getPin();
        List<AttendanceTimeSheetDTO> result = attendanceTimeSheetService.getAttendanceTimeSheet(
            employee.get().getId(),
            timeRangeAndEmployeeIdDTO.getStartDate(),
            timeRangeAndEmployeeIdDTO.getEndDate(),
            AtsDataProcessLevel.FULL_FEATURED_ADMIN
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return result;
    }

    @PostMapping("/time-ranged-report")
    public List<MonthlyAttendanceTimeSheetDTO> getAllEligibleEmployeeAts(@RequestBody TimeRangeDTO timeRangeDTO) throws Exception {
        log.debug("REST request to get all month AttendanceTimeSheet for all eligible employees");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);
        return monthlyAttendanceTimeSheetGenerationService.getAllEmployeeATS(timeRangeDTO.getStartDate(), timeRangeDTO.getEndDate());
    }

    @PostMapping("/filtered-report")
    public List<MonthlyAttendanceTimeSheetDTO> getAllReportUsingFilter(@RequestBody @Valid FilterDto filterDto) throws Exception {
        log.debug("REST request to get monthly AttendanceTimeSheet");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);
        return monthlyAttendanceTimeSheetGenerationService.getAllEmployeeAtsUsingFilter(filterDto);
    }
}
