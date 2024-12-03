package com.bits.hr.service.scheduler.schedulingService;

import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.service.communication.sheduledEmail.FlexScheduleApplicationReminderService;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@EnableAsync
public class FlexScheduleApplicationSchedulerService {

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private FlexScheduleApplicationReminderService flexScheduleApplicationReminderService;

    @Async
    public void reminderFlexScheduleApplication(LocalDate date, int daysBeforeReminder) {
        try {
            // find all approved and effective to will end after 07 days
            LocalDate effectiveToDate = date.plusDays(daysBeforeReminder);
            List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAllForSendingEmailReminder(
                effectiveToDate
            );

            for (FlexScheduleApplication flexScheduleApplication : flexScheduleApplicationList) {
                flexScheduleApplicationReminderService.sendMailForFlexScheduleReminder(flexScheduleApplication);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
