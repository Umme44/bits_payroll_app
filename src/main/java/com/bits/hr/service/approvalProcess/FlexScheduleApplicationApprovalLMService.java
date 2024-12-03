package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.FlexScheduleApplicationEvent;
import com.bits.hr.service.mapper.FlexScheduleApplicationMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class FlexScheduleApplicationApprovalLMService {

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private FlexScheduleApplicationMapper flexScheduleApplicationMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    public List<FlexScheduleApplicationDTO> getAllPending(long reportingToId) {
        return flexScheduleApplicationMapper.toDto(flexScheduleApplicationRepository.findAllPendingOfLM(reportingToId));
    }

    public Page<FlexScheduleApplicationDTO> getAllApprovedByUser(Pageable pageable, Long requesterId, User sanctionedBy) {
        return flexScheduleApplicationRepository
            .findAllApprovedByUser(pageable, requesterId, sanctionedBy)
            .map(flexScheduleApplicationMapper::toDto);
    }

    public void approveSelected(
        long reportingToId,
        List<Long> selectedIds,
        Instant sanctionedAt,
        User sanctionedBy,
        long currentEmployeeId
    ) throws Exception {
        List<FlexScheduleApplication> selectedFlexScheduleApplicationList = flexScheduleApplicationRepository.findAllById(selectedIds);
        List<FlexScheduleApplication> flexScheduleApplicationLMPendingList = flexScheduleApplicationRepository.findAllPendingOfLM(
            reportingToId
        );
        List<FlexScheduleApplication> pendingFlexScheduleOfRelieverList = new ArrayList<>();

        // find employees who set currentEmployee as Reliever
        //        List<Employee> employeeList = leaveApplicationRepository
        //            .findAllWhoAssignedReliever(currentEmployeeId, LocalDate.now());
        //        for (Employee employee: employeeList){
        //
        //            pendingFlexScheduleApplicationList.addAll(flexScheduleApplicationRepository
        //                .findAllPendingOfLM(employee.getId()));
        //        }

        List<FlexScheduleApplication> pendingFlexScheduleApplicationList = new ArrayList<>();
        pendingFlexScheduleApplicationList.addAll(flexScheduleApplicationLMPendingList);
        pendingFlexScheduleApplicationList.addAll(pendingFlexScheduleOfRelieverList);

        for (FlexScheduleApplication flexScheduleApplication : selectedFlexScheduleApplicationList) {
            if (pendingFlexScheduleApplicationList.contains(flexScheduleApplication)) {
                flexScheduleApplication.setSanctionedAt(sanctionedAt);
                flexScheduleApplication.setSanctionedBy(sanctionedBy);
                flexScheduleApplication.setStatus(Status.APPROVED);
                publishEmailEvent(flexScheduleApplication, EventType.APPROVED);
            }
        }
        flexScheduleApplicationRepository.saveAll(selectedFlexScheduleApplicationList);
    }

    public void denySelected(long reportingToId, List<Long> selectedIds, Instant sanctionedAt, User sanctionedBy, long currentEmployeeId)
        throws Exception {
        List<FlexScheduleApplication> selectedFlexScheduleApplicationList = flexScheduleApplicationRepository.findAllById(selectedIds);
        List<FlexScheduleApplication> flexScheduleApplicationLMPendingList = flexScheduleApplicationRepository.findAllPendingOfLM(
            reportingToId
        );
        List<FlexScheduleApplication> pendingFlexScheduleOfRelieverList = new ArrayList<>();

        // find employees who set currentEmployee as Reliever
        //        List<Employee> employeeList = leaveApplicationRepository
        //            .findAllWhoAssignedReliever(currentEmployeeId,LocalDate.now());
        //        List<FlexScheduleApplication> pendingFlexScheduleApplicationList = new ArrayList<>();
        //        for (Employee employee: employeeList){
        //
        //            pendingFlexScheduleApplicationList.addAll(flexScheduleApplicationRepository
        //                .findAllPendingOfLM(employee.getId()));
        //        }

        List<FlexScheduleApplication> pendingFlexScheduleApplicationList = new ArrayList<>();
        pendingFlexScheduleApplicationList.addAll(flexScheduleApplicationLMPendingList);
        pendingFlexScheduleApplicationList.addAll(pendingFlexScheduleOfRelieverList);

        for (FlexScheduleApplication flexScheduleApplication : selectedFlexScheduleApplicationList) {
            // continue, if application is not authorised to line manager,
            if (pendingFlexScheduleApplicationList.contains(flexScheduleApplication)) {
                flexScheduleApplication.setSanctionedAt(sanctionedAt);
                flexScheduleApplication.setSanctionedBy(sanctionedBy);
                flexScheduleApplication.setStatus(Status.NOT_APPROVED);
                publishEmailEvent(flexScheduleApplication, EventType.REJECTED);
            }
        }
        flexScheduleApplicationRepository.saveAll(selectedFlexScheduleApplicationList);
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
