package com.bits.hr.web.rest.scheduler;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.scheduler.schedulingService.FlexScheduleApplicationSchedulerService;
import java.time.LocalDate;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance-mgt")
@AllArgsConstructor
@Log4j2
public class FlexSchedulerApplicationSchedulerTestResource {

    private static final String RESOURCE_NAME = "FlexSchedulerApplicationSchedulerTestResource";
    private final FlexScheduleApplicationSchedulerService flexScheduleApplicationSchedulerService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @GetMapping("/flex-schedule-application-scheduler-reminder")
    public ResponseEntity<Void> reminderFlexScheduleApplication(@RequestParam LocalDate currentDate) {
        try {
            flexScheduleApplicationSchedulerService.reminderFlexScheduleApplication(currentDate, 7);
            User user = currentEmployeeService.getCurrentUser().get();
            eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw exception;
        }
    }
}
