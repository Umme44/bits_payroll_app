package com.bits.hr.service.scheduler;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.approvalProcess.ApprovalProcessService;
import com.bits.hr.service.approvalProcess.PendingApplicationService;
import com.bits.hr.service.attendanceTimeSheet.AtsDataProcessLevel;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import com.bits.hr.service.config.DefinedKeys;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/*
 * todo
 *   subordinates
 *       auto leave approval
 *       auto attendance approval
 *   self
 *       auto attendance application and approval
 *  */

@EnableAsync
@EnableScheduling
@Log4j2
public class AutomatedApprovalScheduler {

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PendingApplicationService pendingApplicationService;

    @Autowired
    private ApprovalProcessService leaveApplicationsLMServiceImpl;

    @Autowired
    private ApprovalProcessService attendanceEntryLMServiceImpl;

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    @Async
    @Scheduled(cron = "0 3 * * *") // daily scheduled at night 3 AM
    public void autoApproval() {
        autoPendingLeaveApproval();
        autoPendingAttendanceApproval();
        autoAttendanceOfPreviousDays();
    }

    private void autoPendingLeaveApproval() {
        try {
            List<Employee> employeeList = getEmployeeToProcess(DefinedKeys.subordinate_auto_leave_approval);
            for (Employee employee : employeeList) {
                List<LeaveApplicationDTO> leaveApplicationDTOList = pendingApplicationService.getAllPendingLeaveApplicationsLM(
                    employee.getId()
                );
                List<Long> ids = leaveApplicationDTOList.stream().map(LeaveApplicationDTO::getId).collect(Collectors.toList());
                leaveApplicationsLMServiceImpl.approveSelected(ids);
            }
        } catch (Exception ex) {
            log.error("error on auto pending leave approval scheduler");
            log.error(ex);
        }
    }

    private void autoPendingAttendanceApproval() {
        try {
            List<Employee> employeeList = getEmployeeToProcess(DefinedKeys.subordinate_auto_attendance_approval);
            for (Employee employee : employeeList) {
                List<ManualAttendanceEntryDTO> leaveApplicationDTOList = pendingApplicationService.getAllPendingAttendanceLM(
                    employee.getId()
                );
                List<Long> ids = leaveApplicationDTOList.stream().map(ManualAttendanceEntryDTO::getId).collect(Collectors.toList());
                attendanceEntryLMServiceImpl.approveSelected(ids);
            }
        } catch (Exception ex) {
            log.error("error on auto pending attendance approval scheduler");
            log.error(ex);
        }
    }

    private void autoAttendanceOfPreviousDays() {
        try {
            // get ATS of previous 3 days
            // fill absent with present data
            // night at 3 means today = current day - 1
            LocalDate endDate = LocalDate.now().minusDays(1);
            LocalDate startDate = endDate.minusDays(3);
            List<Employee> employeeList = getEmployeeToProcess(DefinedKeys.self_auto_attendance_application);
            for (Employee employee : employeeList) {
                List<AttendanceTimeSheetDTO> absentDaysList = attendanceTimeSheetService
                    .getAttendanceTimeSheet(employee.getId(), startDate, endDate, AtsDataProcessLevel.FULL_FEATURED_USER)
                    .stream()
                    .filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT)
                    .collect(Collectors.toList());

                for (AttendanceTimeSheetDTO attendanceTimeSheetDTO : absentDaysList) {
                    AttendanceEntry attendanceEntry = new AttendanceEntry();

                    Instant inTime = Instant.now();
                    inTime = inTime.atZone(ZoneOffset.systemDefault()).withHour(10).withMinute(0).withSecond(0).withNano(0).toInstant();

                    Instant outTime = Instant.now();
                    outTime = outTime.atZone(ZoneOffset.systemDefault()).withHour(18).withMinute(0).withSecond(0).withNano(0).toInstant();
                    attendanceEntry
                        .employee(employee)
                        .date(attendanceTimeSheetDTO.getDate())
                        .inTime(inTime)
                        .inNote("Scheduler Applied Attendance")
                        .outTime(outTime)
                        .outNote("Scheduler Applied attendance")
                        .setStatus(Status.APPROVED);
                    attendanceEntryRepository.save(attendanceEntry);
                }
            }
        } catch (Exception ex) {
            log.error("error on auto attendance giving scheduler");
            log.error(ex);
        }
    }

    private List<Employee> getEmployeeToProcess(String operationKey) {
        List<Employee> employeeList = new ArrayList<>();
        List<String> pinList = getConfigValueByKeyService.getListOfPinForAutomatedApprovalProcess(operationKey);
        for (String pin : pinList) {
            Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin.trim());
            if (employeeOptional.isPresent()) {
                employeeList.add(employeeOptional.get());
            }
        }
        return employeeList
            .stream()
            .filter(x -> x.getEmploymentStatus() != null && x.getEmploymentStatus() != EmploymentStatus.RESIGNED)
            .collect(Collectors.toList());
    }
}
