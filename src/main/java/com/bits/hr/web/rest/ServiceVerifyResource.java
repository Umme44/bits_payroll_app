package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FileService;
import com.bits.hr.service.communication.sheduledEmail.AbsentDaysRemainderService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.SampleEmailEvent;
import com.bits.hr.service.scheduler.ZeroHourScheduler;
import com.bits.hr.service.scheduler.schedulingService.AutoPunchOutService;
import com.bits.hr.service.scheduler.schedulingService.AutomatedWFHRejectSchedulerService;
import com.bits.hr.service.scheduler.schedulingService.MorningSchedulerService;
import com.bits.hr.service.scheduler.schedulingService.WorkFromHomeSchedulerService;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Service Testing
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class ServiceVerifyResource {

    private final Logger log = LoggerFactory.getLogger(ServiceVerifyResource.class);

    private static final String RESOURCE_NAME = "ServiceVerifyResource";

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MorningSchedulerService morningSchedulerService;

    @Autowired
    private AutoPunchOutService autoPunchOutService;

    @Autowired
    private FileService fileService;

    @Autowired
    private WorkFromHomeSchedulerService workFromHomeSchedulerService;

    @Autowired
    private AutomatedWFHRejectSchedulerService automatedWFHRejectSchedulerService;

    @Autowired
    private AbsentDaysRemainderService absentDaysRemainderService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private ZeroHourScheduler zeroHourScheduler;

    @GetMapping("/send-sample-email")
    public ResponseEntity<Boolean> sendSampleEmail() {
        log.debug("REST request to send sample email");
        publishEvent(EventType.CREATED);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(true);
    }

    private void publishEvent(EventType event) {
        log.debug("publishing leave application event with event: " + event);
        SampleEmailEvent sampleEmailEvent = new SampleEmailEvent(this, event);
        applicationEventPublisher.publishEvent(sampleEmailEvent);
    }

    @GetMapping("/test-morning-scheduler")
    public ResponseEntity<Void> testMorningScheduler() {
        log.debug("REST request to test morning scheduler");
        morningSchedulerService.morning();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test-zero-hour-scheduler")
    public ResponseEntity<Void> testZeroHourScheduler() {
        log.debug("REST request to test zero hour scheduler");
        autoPunchOutService.compileNotCompiledAttendances(LocalDate.now().minusDays(1));
        fileService.syncImages(true);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        zeroHourScheduler.zeroHourActions();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test-absent-days-scheduler/{employeePin}/{startDate}/{endDate}")
    public ResponseEntity<HttpStatus> testAbsentDaysScheduler(
        @PathVariable String employeePin,
        @PathVariable LocalDate startDate,
        @PathVariable LocalDate endDate
    ) {
        log.debug("REST request to test absent days scheduler");
        Employee optionalEmployee = employeeRepository.findByPin(employeePin).get();
        absentDaysRemainderService.absentDaysRemainder(optionalEmployee, startDate, endDate);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete-flex-schedule-applications")
    public ResponseEntity<Boolean> deleteAllFlexScheduleApplications() throws Exception {
        log.debug("REST request to delete all flex schedule application");
        User user = currentEmployeeService.getCurrentUser().get();
        flexScheduleApplicationRepository.deleteAll();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);
        return ResponseEntity.ok(true);
    }

    // check scheduler reminder mail for physical office
    @GetMapping("/work-from-home-applications/reminder-mail")
    public void getWFHReminder() {
        LocalDate today = LocalDate.now();
        workFromHomeSchedulerService.workFromHomeEndReminder(today);
    }

    // check scheduler reject mail
    @GetMapping("/work-from-home-applications/auto/reject-mail")
    public void getWFHAutoRejectMail(@RequestParam LocalDate today) {
        automatedWFHRejectSchedulerService.workFromHomeRejectScheduler(today.minusDays(3));
    }

    // check scheduler mail for enable/disable online attendance status
    @GetMapping("/work-from-home-applications/eligible-wfh-applications")
    public void dailyWFHRefreshServiceTest(@RequestParam LocalDate today) {
        workFromHomeSchedulerService.refreshWFHApplicationByScheduler(today);
    }
}
