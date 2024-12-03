package com.bits.hr.service.scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;

/*
 * todo
 *     in employee all boolean flag if null make false.
 *     if contract period end , make those employee inactive
 *  */

@Log4j2
public class AutomatedDataConsistencyScheduler {

    @Scheduled(cron = "0 2 * * *") // daily scheduled at night 2 AM
    public void dataConsistency() {
        // employee data consistency
        //
    }
}
