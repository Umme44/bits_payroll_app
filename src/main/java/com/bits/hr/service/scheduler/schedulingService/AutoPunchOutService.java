package com.bits.hr.service.scheduler.schedulingService;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.enumeration.AttendanceDeviceOrigin;
import com.bits.hr.repository.AttendanceEntryRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@EnableAsync
public class AutoPunchOutService {

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    @Async
    public void compileNotCompiledAttendances(LocalDate date) {
        try {
            List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.getAllNotCompiledAttendances(date);

            Instant inTime = date.atStartOfDay(ZoneOffset.systemDefault()).toInstant();
            inTime = inTime.atZone(ZoneOffset.systemDefault()).withHour(10).withMinute(0).withSecond(0).withNano(0).toInstant();

            Instant outTime = date.atStartOfDay(ZoneOffset.systemDefault()).toInstant();
            outTime = outTime.atZone(ZoneOffset.systemDefault()).withHour(23).withMinute(59).withSecond(0).withNano(0).toInstant();

            for (AttendanceEntry attendanceEntry : attendanceEntryList) {
                if (attendanceEntry.getOutTime() == null) {
                    attendanceEntry.outTime(outTime);
                    attendanceEntry.setIsAutoPunchOut(true);
                    attendanceEntry.setPunchOutDeviceOrigin(AttendanceDeviceOrigin.WEB);
                }
                if (attendanceEntry.getInTime() == null) {
                    attendanceEntry.setInTime(inTime);
                }

                attendanceEntryRepository.save(attendanceEntry);

                String checkIn = attendanceEntry.getInTime() == null ? "" : attendanceEntry.getInTime().toString();
                String checkOut = attendanceEntry.getOutTime() == null ? "" : attendanceEntry.getOutTime().toString();

                log.info(
                    "Auto Punch out for employee: {} {} {} {} {} {} {}",
                    attendanceEntry.getEmployee().getPin(),
                    "-",
                    attendanceEntry.getEmployee().getFullName(),
                    "InTime:",
                    checkIn,
                    "OutTime:",
                    checkOut
                );
            }
        } catch (Exception ex) {
            log.info(ex);
        }
    }
}
