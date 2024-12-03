package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.ManualAttendanceEntry;
import com.bits.hr.repository.ManualAttendanceEntryRepository;
import com.bits.hr.service.approvalProcess.helperMethods.AttendanceEntryApproval;
import com.bits.hr.service.approvalProcess.helperMethods.SaveAttendanceEntry;
import com.bits.hr.service.approvalProcess.helperObjects.AttendanceHelper;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class AttendanceEntryHRServiceImpl implements ApprovalProcessService {

    @Autowired
    private ManualAttendanceEntryRepository manualAttendanceEntryRepository;

    @Autowired
    private SaveAttendanceEntry saveAttendanceEntry;

    @Override
    public boolean approveSelected(List<Long> selectedIds) {
        try {
            List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.getUnapprovedAttendancesByIds(
                selectedIds
            );
            for (ManualAttendanceEntry manualAttendanceEntry : manualAttendanceEntryList) {
                AttendanceHelper attendanceHelper = AttendanceEntryApproval.processToApproveHR(manualAttendanceEntry);
                manualAttendanceEntryRepository.save(attendanceHelper.getManualAttendanceEntry());
                saveAttendanceEntry.save(attendanceHelper.getAttendanceEntry());
            }
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    @Override
    public boolean approveAll() {
        try {
            List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.getAllPendingAttendances();
            for (ManualAttendanceEntry manualAttendanceEntry : manualAttendanceEntryList) {
                AttendanceHelper attendanceHelper = AttendanceEntryApproval.processToApproveHR(manualAttendanceEntry);
                manualAttendanceEntryRepository.save(attendanceHelper.getManualAttendanceEntry());
                saveAttendanceEntry.save(attendanceHelper.getAttendanceEntry());
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }

    @Override
    public boolean denySelected(List<Long> selectedIds) {
        try {
            List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.getUnapprovedAttendancesByIds(
                selectedIds
            );
            for (ManualAttendanceEntry manualAttendanceEntry : manualAttendanceEntryList) {
                manualAttendanceEntryRepository.save(AttendanceEntryApproval.processToRejectHR(manualAttendanceEntry));
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }

    @Override
    public boolean denyAll() {
        try {
            List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.getAllPendingAttendances();
            for (ManualAttendanceEntry manualAttendanceEntry : manualAttendanceEntryList) {
                manualAttendanceEntryRepository.save(AttendanceEntryApproval.processToRejectHR(manualAttendanceEntry));
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }
}
