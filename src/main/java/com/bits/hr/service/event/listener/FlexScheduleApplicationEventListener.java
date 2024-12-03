package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.domain.TimeSlot;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.repository.TimeSlotRepository;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.FlexScheduleApprovalDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.FlexScheduleApplicationEvent;
import com.bits.hr.service.event.FlexScheduleApprovalEvent;
import com.bits.hr.util.TimeUtil;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class FlexScheduleApplicationEventListener {

    private static final Logger log = LoggerFactory.getLogger(FlexScheduleApplicationEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final FlexScheduleApplicationRepository flexScheduleApplicationRepository;
    private final EmailService emailService;

    public FlexScheduleApplicationEventListener(
        FlexScheduleApplicationRepository flexScheduleApplicationRepository,
        EmailService emailService
    ) {
        this.flexScheduleApplicationRepository = flexScheduleApplicationRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleFlexScheduleEvent(FlexScheduleApplicationEvent event) {
        EventType eventType = event.getType();

        long flexScheduleApplicationId = event.getFlexScheduleApplicationId();
        FlexScheduleApplication flexScheduleApplication = flexScheduleApplicationRepository.findById(flexScheduleApplicationId).get();

        if (eventType.equals(EventType.CREATED)) {
            sendMailForFlexScheduleOnApplied(flexScheduleApplication);
        } else if (eventType.equals(EventType.APPROVED)) {
            sendMailForFlexScheduleOnApproval(flexScheduleApplication);
        } else if (eventType.equals(EventType.REJECTED)) {
            sendMailForFlexScheduleOnRejected(flexScheduleApplication);
        }
    }

    private void sendMailForFlexScheduleOnApplied(FlexScheduleApplication flexScheduleApplication) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();

        String subject = "Application for Flex Schedule";
        String templateName = MAIL_TEMPLATE_FLEX_SCHEDULE_APPLIED_LM_NOTIFY;

        Map<String, Object> variableMap = new HashMap<>();

        Employee requester = flexScheduleApplication.getRequester();

        // lm
        to.add(requester.getReportingTo().getOfficialEmail().toLowerCase());

        variableMap.put("requesterPIN", requester.getPin());
        variableMap.put("requesterName", requester.getFullName());
        variableMap.put("reportingToName", requester.getReportingTo().getFullName());
        variableMap.put("effectiveFrom", flexScheduleApplication.getEffectiveFrom());
        variableMap.put("effectiveTo", flexScheduleApplication.getEffectiveTo());
        variableMap.put("approvalLink", DomainConfig.BASE_URL + "/flex-schedule-application-approval/lm");

        try {
            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);

            log.info("Mail Sent Successfully of FlexSchedule, {0}", flexScheduleApplication);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Failed to sent mail of FlexSchedule for {0}", flexScheduleApplication);
        }
    }

    private void sendMailForFlexScheduleOnApproval(FlexScheduleApplication flexScheduleApplication) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Update on a Flex Schedule";
        String templateName = MAIL_TEMPLATE_FLEX_SCHEDULE_AFTER_APPROVED;

        Map<String, Object> variableMap = new HashMap<>();

        Employee requester = flexScheduleApplication.getRequester();

        to.add(requester.getOfficialEmail().toLowerCase());
        cc.add(requester.getReportingTo().getOfficialEmail().toLowerCase());

        TimeSlot timeSlot = flexScheduleApplication.getTimeSlot();

        variableMap.put("name", requester.getFullName());

        DateTimeFormatter formatter = DateTimeFormatter
            .ofLocalizedTime(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault());

        String officeStartTime = formatter.format(timeSlot.getInTime());
        String officeEndTime = formatter.format(timeSlot.getOutTime());

        variableMap.put("officeStartTime", officeStartTime);
        variableMap.put("officeEndTime", officeEndTime);
        variableMap.put("effectiveFrom", flexScheduleApplication.getEffectiveFrom());

        try {
            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);

            log.info("Mail Sent Successfully of FlexSchedule Change for {}", flexScheduleApplication);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Failed to sent mail of FlexSchedule Change for {}", flexScheduleApplication);
        }
    }

    private void sendMailForFlexScheduleOnRejected(FlexScheduleApplication flexScheduleApplication) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Rejection of an Application for Flex Schedule";
        String templateName = MAIL_TEMPLATE_FLEX_SCHEDULE_AFTER_NOT_APPROVED;

        Map<String, Object> variableMap = new HashMap<>();

        Employee requester = flexScheduleApplication.getRequester();

        to.add(requester.getOfficialEmail().toLowerCase());
        cc.add(requester.getReportingTo().getOfficialEmail().toLowerCase());

        variableMap.put("name", requester.getFullName());

        try {
            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);

            log.info("Mail Sent Successfully of FlexSchedule Change for {}", flexScheduleApplication);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Failed to sent mail of FlexSchedule Change for {}", flexScheduleApplication);
        }
    }
}
