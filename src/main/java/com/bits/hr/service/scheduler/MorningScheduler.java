package com.bits.hr.service.scheduler;

import com.bits.hr.service.scheduler.schedulingService.MorningSchedulerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MorningScheduler {

    @Autowired
    private MorningSchedulerService morningSchedulerService;

    /**
     * this scheduler will run every-day at 7 PM morning
     * ATS checkup
     **/
    @Async
    @Scheduled(cron = "0 0 7 * * ?")
    public void morningHourActions() {
        try {
            log.info("morning scheduler called.");
            morningSchedulerService.morning();
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
