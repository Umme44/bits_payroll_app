package com.bits.hr.service.scheduler;

import com.bits.hr.service.FileService;
import com.bits.hr.service.communication.NID.BatchNIDVerificationService;
import com.bits.hr.service.scheduler.schedulingService.*;
import com.bits.hr.service.scheduler.schedulingService.FlexScheduleApplicationSchedulerService;
import com.bits.hr.service.scheduler.schedulingService.ScheduledDataFixingService;
import com.bits.hr.service.scheduler.schedulingService.ScheduledInsuranceService;
import com.bits.hr.service.scheduler.schedulingService.TimeSlotSchedulerService;
import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ZeroHourScheduler {

    @Autowired
    private AutoPunchOutService autoPunchOutService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ScheduledDataFixingService scheduledDataFixingService;

    @Autowired
    private ScheduledInsuranceService scheduledInsuranceService;

    @Autowired
    WorkFromHomeSchedulerService workFromHomeSchedulerService;

    @Autowired
    private BatchNIDVerificationService batchNIDVerificationService;

    @Autowired
    private EmployeePinStatusUpdateSchedulerService employeePinStatusUpdateSchedulerService;

    @Autowired
    private FlexScheduleApplicationSchedulerService flexScheduleApplicationSchedulerService;

    @Autowired
    private TimeSlotSchedulerService timeSlotSchedulerService;

    //****************************************************************************
    // Common Pattern for scheduler
    // second, minute, hour, day, month, weekday
    //****************************************************************************
    //  0 0 * * * * = the top of every hour of every day.
    //  */10 * * * * * = every ten seconds.
    //  0 0 8-10 * * * = 8, 9 and 10 o'clock of every day.
    //  0 0 6,19 * * * = 6:00 AM and 7:00 PM every day.
    //  0 0/30 8-10 * * * = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
    //  0 0 9-17 * * MON-FRI = on the hour nine-to-five weekdays
    //  0 0 0 25 12 ? = every Christmas Day at midnight
    //****************************************************************************
    //  * "0 0 * * * *" = the top of every hour of every day.
    //  * "*/10 * * * * *" = every ten seconds.
    //  * "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
    //  * "0 0 8,10 * * *" = 8 and 10 o'clock of every day.
    //  * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
    //  * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
    //  * "0 0 0 25 12 ?" = every Christmas Day at midnight
    //****************************************************************************
    //  (*) means match any
    //  */X means "every X"
    //  ? ("no specific value") - useful when you need to specify something in one
    //  of the two fields in which the character is allowed, but not the other. For
    //  example, if I want my trigger to fire on a particular day of the month
    //  (say, the 10th), but I don't care what day of the week that happens to be,
    //  I would put "10" in the day-of-month field and "?" in the day-of-week field.
    //****************************************************************************

    @Scheduled(cron = "0 0 0 * * ?")
    public void zeroHourActions() {
        try {
            log.info("zero hour scheduler called for :: auto punch out service, sync profile pictures at {}", LocalDate.now());
            autoPunchOutService.compileNotCompiledAttendances(LocalDate.now().minusDays(1));
            workFromHomeSchedulerService.refreshWFHApplicationByScheduler(LocalDate.now());
            employeePinStatusUpdateSchedulerService.updateEmployeePinStatusIfEmployeeResigns();
            fileService.syncImages(true);
            scheduledDataFixingService.changeAllInternProfileMarkedAsContractual();
            flexScheduleApplicationSchedulerService.reminderFlexScheduleApplication(LocalDate.now(), 7);
            scheduledDataFixingService.addDojAndDocToAllExistingEmployeePfAccount();
            timeSlotSchedulerService.checkEmptyTimeSlotCode();
            scheduledInsuranceService.excludeFromInsuranceIfEmployeeResigns();
            scheduledInsuranceService.excludeFromInsuranceIfChileAgeExceedMaxAgeLimit();
            // batchNIDVerificationService.routineNidVerificationService();
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
