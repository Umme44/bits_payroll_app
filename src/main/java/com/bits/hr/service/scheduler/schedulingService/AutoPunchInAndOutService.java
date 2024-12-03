package com.bits.hr.service.scheduler.schedulingService;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.AttendanceDeviceOrigin;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.attendanceTimeSheet.AtsDataProcessLevel;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoPunchInAndOutService {

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    public void autoPunchInAndOutForEligibleEmployee(LocalDate date) {
        List<Employee> employeeList = employeeRepository.findAllActiveAndEligibleForAutomatedAttendance(date);
        Instant inTime = getInstant(date, 10, 0, 0);
        Instant outTime = getInstant(date, 18, 0, 0);

        for (Employee employee : employeeList) {
            AttendanceStatus attendanceStatus = attendanceTimeSheetService
                .getAttendanceTimeSheet(employee.getId(), date, date, AtsDataProcessLevel.FULL_FEATURED_USER)
                .get(0)
                .getAttendanceStatus();

            if (attendanceStatus == AttendanceStatus.ABSENT) {
                AttendanceEntry attendanceEntry = new AttendanceEntry()
                    .date(date)
                    .inTime(inTime)
                    .outTime(outTime)
                    .employee(employee)
                    .inNote("system adjusted")
                    .outNote("system adjusted")
                    .punchInDeviceOrigin(AttendanceDeviceOrigin.WEB)
                    .punchOutDeviceOrigin(AttendanceDeviceOrigin.WEB)
                    .status(Status.APPROVED);
                attendanceEntryRepository.save(attendanceEntry);
            }
        }
    }

    private Instant getInstant(LocalDate date, int hour, int minute, int second) {
        return Instant
            .now()
            .atZone(ZoneId.systemDefault())
            .withDayOfYear(date.getDayOfYear())
            .withHour(hour)
            .withMinute(minute)
            .withSecond(second)
            .toInstant();
    }
}
