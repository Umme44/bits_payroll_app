package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.MyTeamService;
import com.bits.hr.service.attendanceTimeSheet.AtsDataProcessLevel;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.MyTeamMemberAttendancesDTO;
import com.bits.hr.service.dto.TimeRangeAndEmployeeIdDTO;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common")
@Log4j2
public class MyTeamResource {

    private final MyTeamService myTeamService;
    private final CurrentEmployeeService currentEmployeeService;
    private final EmployeeRepository employeeRepository;

    private final EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    @Autowired
    private EmployeeMinimalMapper employeeMinimalMapper;

    @Autowired
    private EmployeeService employeeService;

    private static final String RESOURCE_NAME = "MyTeamResource";

    public MyTeamResource(
        MyTeamService myTeamService,
        CurrentEmployeeService currentEmployeeService,
        EmployeeRepository employeeRepository,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.myTeamService = myTeamService;
        this.currentEmployeeService = currentEmployeeService;
        this.employeeRepository = employeeRepository;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * GET My All Team Member Attendance List
     * @return List<MyTeamMemberAttendancesDTO>
     */
    @GetMapping("my-team-members-attendance-list")
    public ResponseEntity<List<MyTeamMemberAttendancesDTO>> getMyTeamMemberAttendanceList() {
        log.debug("REST Request to Get My Team Member Attendance List");
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        List<MyTeamMemberAttendancesDTO> result = myTeamService.getMyTeamAttendance(employeeOptional.get());
        // Sort Team Members by PIN
        Collections.sort(result, Comparator.comparing((MyTeamMemberAttendancesDTO o) -> o.getEmployee().getPin()));
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok(result);
    }

    /**
     * Get My Team Member List
     * @return
     */
    @GetMapping("/my-team-member/list")
    public ResponseEntity<List<EmployeeMinimalDTO>> getMyTeamMembers() {
        log.debug("REST Request to Get My Team Member Attendance List");
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        if (!employee.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        List<Employee> employeeList = employeeService.getAllReportingTo(employee.get());
        employeeList.sort(Comparator.comparing(Employee::getPin));

        List<EmployeeMinimalDTO> employeeMinimalDTOS = employeeMinimalMapper.toDto(employeeList);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok(employeeMinimalDTOS);
    }

    /**
     * Get Selected Team Member Attendance Time Sheet in a TimeRange
     * @param timeRangeAndEmployeeIdDTO
     * @return
     */
    @PostMapping("/my-team-member/attendance-time-sheet")
    public List<AttendanceTimeSheetDTO> getEmployeesAttendanceTimeSheetBetweenDates(
        @RequestBody TimeRangeAndEmployeeIdDTO timeRangeAndEmployeeIdDTO
    ) {
        log.debug("REST Request to Get My Team Member Attendance List between two dates");
        Optional<Employee> employeeOptional = employeeRepository.findById(timeRangeAndEmployeeIdDTO.getEmployeeId());
        if (!employeeOptional.isPresent()) throw new NoEmployeeProfileException();

        Optional<Employee> lineManager = currentEmployeeService.getCurrentEmployee();
        if (!lineManager.isPresent()) {
            return new ArrayList<>();
        }

        /* only allow my team members */
        boolean isMyTeamMember = myTeamService.isMyTeamMember(lineManager.get(), timeRangeAndEmployeeIdDTO.getEmployeeId());
        if (isMyTeamMember) {
            List<AttendanceTimeSheetDTO> result = attendanceTimeSheetService.getAttendanceTimeSheet(
                employeeOptional.get().getId(),
                timeRangeAndEmployeeIdDTO.getStartDate(),
                timeRangeAndEmployeeIdDTO.getEndDate(),
                AtsDataProcessLevel.MINIMAL
            );
            User user = currentEmployeeService.getCurrentUser().get();
            eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/my-team-member/attendance-time-sheet/{id}")
    public ResponseEntity<EmployeeMinimalDTO> getEmployeeById(@PathVariable long id) {
        log.debug("REST Request to Get My Team Member Attendance List by employeeId");
        Optional<Employee> employee = employeeRepository.findById(id);
        EmployeeMinimalDTO employeeMinimalDTO = employeeMinimalMapper.toDto(employee.get());
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeeMinimalDTO, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity.ok().body(employeeMinimalDTO);
    }
}
