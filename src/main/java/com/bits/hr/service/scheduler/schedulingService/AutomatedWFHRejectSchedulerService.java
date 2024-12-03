package com.bits.hr.service.scheduler.schedulingService;

import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.WorkFromHomeApplicationRepository;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.event.ApprovalVia;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.WorkFromHomeApplicationEvent;
import com.bits.hr.service.workFromHomeApplication.WorkFromHomeRefreshService;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AutomatedWFHRejectSchedulerService {

    private final WorkFromHomeApplicationRepository workFromHomeApplicationRepository;
    private final WorkFromHomeRefreshService workFromHomeRefreshService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AutomatedWFHRejectSchedulerService(
        WorkFromHomeApplicationRepository workFromHomeApplicationRepository,
        EmployeeRepository employeeRepository,
        EmailService emailService,
        WorkFromHomeRefreshService workFromHomeRefreshService,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        this.workFromHomeApplicationRepository = workFromHomeApplicationRepository;
        this.workFromHomeRefreshService = workFromHomeRefreshService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // reject work from home applications which in pending status for 03 days
    public void workFromHomeRejectScheduler(LocalDate date) {
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findApplicationPendingForCertainDays(
            date
        );

        for (WorkFromHomeApplication workFromHomeApplication : workFromHomeApplicationList) {
            workFromHomeApplication.setStatus(Status.NOT_APPROVED);
            workFromHomeApplicationRepository.save(workFromHomeApplication);

            // refresh employee web punch
            workFromHomeRefreshService.refreshWFH(LocalDate.now(), workFromHomeApplication.getEmployee());
            publishEvent(workFromHomeApplication, EventType.REJECTED, ApprovalVia.LM);
        }
    }

    private void publishEvent(WorkFromHomeApplication workFromHomeApplication, EventType event, ApprovalVia approvalVia) {
        log.info("publishing WorkFromHomeApplication event with event: " + event);
        WorkFromHomeApplicationEvent workFromHomeApplicationEvent = new WorkFromHomeApplicationEvent(
            this,
            workFromHomeApplication,
            event,
            approvalVia
        );
        applicationEventPublisher.publishEvent(workFromHomeApplicationEvent);
    }
}
