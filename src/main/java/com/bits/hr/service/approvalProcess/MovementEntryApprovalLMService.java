package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.MovementEntry;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.repository.MovementEntryRepository;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.MovementEntryApplicationEvent;
import com.bits.hr.service.mapper.MovementEntryMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Log4j2
public class MovementEntryApprovalLMService {

    @Autowired
    private MovementEntryRepository movementEntryRepository;

    @Autowired
    private MovementEntryMapper movementEntryMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    public List<MovementEntryDTO> getAllPending(long lineManagerId) {
        List<MovementEntry> allPendingLM = movementEntryRepository.getAllPendingLM(lineManagerId);
        return movementEntryMapper.toDto(allPendingLM);
    }

    public boolean approveSelected(List<Long> selectedIds, User sanctionedBy) {
        try {
            List<MovementEntry> allPending = movementEntryRepository.getAllPendingByIds(selectedIds);
            for (MovementEntry movementEntry : allPending) {
                movementEntry.setStatus(Status.APPROVED);
                movementEntry.setSanctionBy(sanctionedBy);
                movementEntryRepository.save(movementEntry);

                publishEvent(movementEntryMapper.toDto(movementEntry), true, EventType.APPROVED);
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public boolean denySelected(List<Long> selectedIds, User sanctionedBy) {
        try {
            List<MovementEntry> allPending = movementEntryRepository.getAllPendingByIds(selectedIds);
            for (MovementEntry movementEntry : allPending) {
                movementEntry.setStatus(Status.NOT_APPROVED);
                movementEntry.setSanctionBy(sanctionedBy);
                movementEntryRepository.save(movementEntry);
                publishEvent(movementEntryMapper.toDto(movementEntry), true, EventType.REJECTED);
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
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
