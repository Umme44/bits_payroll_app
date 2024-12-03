package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.MovementEntry;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.MovementEntryRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.LeaveApplicationEvent;
import com.bits.hr.service.event.MovementEntryApplicationEvent;
import com.bits.hr.service.mapper.MovementEntryMapper;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MovementEntryApprovalHRService {

    @Autowired
    private MovementEntryRepository movementEntryRepository;

    @Autowired
    private MovementEntryMapper movementEntryMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public List<MovementEntryDTO> getAllPending() {
        List<MovementEntry> allPending = movementEntryRepository.getAllPending();
        return movementEntryMapper.toDto(allPending);
    }

    public boolean approveSelected(List<Long> selectedIds) {
        try {
            List<MovementEntry> movementEntrySelectedList = movementEntryRepository.getAllPendingByIds(selectedIds);
            for (MovementEntry movementEntry : movementEntrySelectedList) {
                movementEntry.setStatus(Status.APPROVED);
                movementEntry.setSanctionAt(LocalDate.now());
                movementEntry.setSanctionBy(currentEmployeeService.getCurrentUser().get());
                movementEntryRepository.save(movementEntry);
                publishEvent(movementEntryMapper.toDto(movementEntry), false, EventType.APPROVED);
            }
            return true;
        } catch (Exception exception) {
            log.error(exception);
            return false;
        }
    }

    public boolean approveAll() {
        return false;
    }

    public boolean denySelected(List<Long> selectedIds) {
        try {
            List<MovementEntry> movementEntrySelectedList = movementEntryRepository.getAllPendingByIds(selectedIds);
            for (MovementEntry movementEntry : movementEntrySelectedList) {
                movementEntry.setStatus(Status.NOT_APPROVED);
                movementEntry.setSanctionAt(LocalDate.now());
                movementEntry.setSanctionBy(currentEmployeeService.getCurrentUser().get());
                movementEntryRepository.save(movementEntry);
                publishEvent(movementEntryMapper.toDto(movementEntry), false, EventType.REJECTED);
            }
            return true;
        } catch (Exception exception) {
            log.error(exception);
            return false;
        }
    }

    public boolean denyAll() {
        return false;
    }

    private void publishEvent(MovementEntryDTO movementEntryDTO, boolean isLmApproved, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        MovementEntryApplicationEvent movementEntryApplicationEvent = new MovementEntryApplicationEvent(
            this,
            movementEntryDTO,
            isLmApproved,
            event
        );
        applicationEventPublisher.publishEvent(movementEntryApplicationEvent);
    }
}
