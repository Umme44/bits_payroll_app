package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.ManualAttendanceEntry;
import com.bits.hr.repository.ManualAttendanceEntryRepository;
import com.bits.hr.service.approvalProcess.helperMethods.AttendanceEntryApproval;
import com.bits.hr.service.approvalProcess.helperMethods.SaveAttendanceEntry;
import com.bits.hr.service.approvalProcess.helperObjects.AttendanceHelper;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.ManualAttendanceEntryApplicationEvent;
import com.bits.hr.service.event.MovementEntryApplicationEvent;
import com.bits.hr.service.mapper.ManualAttendanceEntryMapper;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class AttendanceEntryLMServiceImpl implements ApprovalProcessService {

    @Autowired
    ManualAttendanceEntryRepository manualAttendanceEntryRepository;

    @Autowired
    SaveAttendanceEntry saveAttendanceEntry;

    @Autowired
    CurrentEmployeeService currentEmployeeService;

    @Autowired
    private ManualAttendanceEntryMapper manualAttendanceEntryMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public boolean approveSelected(List<Long> selectedIds) {
        try {
            List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.getUnapprovedAttendancesByIds(
                selectedIds
            );
            for (ManualAttendanceEntry manualAttendanceEntry : manualAttendanceEntryList) {
                AttendanceHelper attendanceHelper = AttendanceEntryApproval.processToApproveLM(manualAttendanceEntry);
                manualAttendanceEntryRepository.save(attendanceHelper.getManualAttendanceEntry());
                saveAttendanceEntry.save(attendanceHelper.getAttendanceEntry());

                publishEvent(manualAttendanceEntryMapper.toDto(manualAttendanceEntry), EventType.APPROVED);
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }

    @Override
    public boolean approveAll() {
        try {
            long currentEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();
            List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.getAllPendingAttendancesLM(
                currentEmployeeId
            );
            for (ManualAttendanceEntry manualAttendanceEntry : manualAttendanceEntryList) {
                AttendanceHelper attendanceHelper = AttendanceEntryApproval.processToApproveLM(manualAttendanceEntry);
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
                manualAttendanceEntryRepository.save(AttendanceEntryApproval.processToRejectLM(manualAttendanceEntry));

                publishEvent(manualAttendanceEntryMapper.toDto(manualAttendanceEntry), EventType.REJECTED);
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
                manualAttendanceEntryRepository.save(AttendanceEntryApproval.processToRejectLM(manualAttendanceEntry));
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }

    private void publishEvent(ManualAttendanceEntryDTO manualAttendanceEntryDTO, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        ManualAttendanceEntryApplicationEvent manualAttendanceEntryApplicationEvent = new ManualAttendanceEntryApplicationEvent(
            this,
            manualAttendanceEntryDTO,
            event
        );
        applicationEventPublisher.publishEvent(manualAttendanceEntryApplicationEvent);
    }
}
