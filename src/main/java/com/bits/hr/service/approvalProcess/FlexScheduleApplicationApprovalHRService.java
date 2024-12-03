package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.FlexScheduleApplicationEvent;
import com.bits.hr.service.mapper.FlexScheduleApplicationMapper;
import java.time.Instant;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class FlexScheduleApplicationApprovalHRService {

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private FlexScheduleApplicationMapper flexScheduleApplicationMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public List<FlexScheduleApplicationDTO> getAllPending() {
        return flexScheduleApplicationMapper.toDto(flexScheduleApplicationRepository.findAllByStatus(Status.PENDING));
    }

    public void approveSelected(List<Long> selectedIds, Instant sanctionedAt, User sanctionedBy) throws Exception {
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAllById(selectedIds);

        for (FlexScheduleApplication flexScheduleApplication : flexScheduleApplicationList) {
            flexScheduleApplication.setSanctionedAt(sanctionedAt);
            flexScheduleApplication.setSanctionedBy(sanctionedBy);
            flexScheduleApplication.setStatus(Status.APPROVED);

            publishEmailEvent(flexScheduleApplication, EventType.APPROVED);
        }
        flexScheduleApplicationRepository.saveAll(flexScheduleApplicationList);
    }

    public void denySelected(List<Long> selectedIds, Instant sanctionedAt, User sanctionedBy) throws Exception {
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAllById(selectedIds);

        for (FlexScheduleApplication flexScheduleApplication : flexScheduleApplicationList) {
            flexScheduleApplication.setSanctionedAt(sanctionedAt);
            flexScheduleApplication.setSanctionedBy(sanctionedBy);
            flexScheduleApplication.setStatus(Status.NOT_APPROVED);

            publishEmailEvent(flexScheduleApplication, EventType.REJECTED);
        }
        flexScheduleApplicationRepository.saveAll(flexScheduleApplicationList);
    }

    private void publishEmailEvent(FlexScheduleApplication flexScheduleApplication, EventType eventType) {
        FlexScheduleApplicationEvent flexScheduleApplicationEvent = new FlexScheduleApplicationEvent(
            this,
            flexScheduleApplication.getId(),
            eventType
        );
        applicationEventPublisher.publishEvent(flexScheduleApplicationEvent);
    }
}
