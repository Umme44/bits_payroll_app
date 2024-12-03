package com.bits.hr.service.approvalProcess.helperMethods;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.enumeration.AttendanceDeviceOrigin;
import com.bits.hr.repository.AttendanceEntryRepository;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SaveAttendanceEntry {

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    public boolean save(AttendanceEntry attendanceEntry) {
        try {
            Optional<AttendanceEntry> attendanceEntryOptional = attendanceEntryRepository.findAttendanceEntryByDateAndEmployee(
                attendanceEntry.getDate(),
                attendanceEntry.getEmployee().getId()
            );
            if (attendanceEntryOptional.isPresent()) {
                attendanceEntryRepository.deleteById(attendanceEntryOptional.get().getId());
            }

            attendanceEntry.punchInDeviceOrigin(AttendanceDeviceOrigin.WEB);
            if (attendanceEntry.getOutTime() == null) {
                attendanceEntry.setPunchOutDeviceOrigin(null);
            } else {
                attendanceEntry.setPunchOutDeviceOrigin(AttendanceDeviceOrigin.WEB);
            }

            attendanceEntryRepository.save(attendanceEntry);
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }
}
