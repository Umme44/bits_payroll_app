package com.bits.hr.service.communication.sheduledEmail;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_FLEX_SCHEDULE_REMINDER;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FlexScheduleApplicationReminderService {

    @Value("${jhipster.mail.from}")
    private String from;

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private EmailService emailService;

    public void sendMailForFlexScheduleReminder(FlexScheduleApplication flexScheduleApplication) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Flexible Schedule Adjustment Reminder";
        String templateName = MAIL_TEMPLATE_FLEX_SCHEDULE_REMINDER;

        Map<String, Object> variableMap = new HashMap<>();

        Employee requester = flexScheduleApplication.getRequester();

        to.add(requester.getOfficialEmail().toLowerCase());

        variableMap.put("name", requester.getFullName());
        variableMap.put("effectiveTo", flexScheduleApplication.getEffectiveTo());
        variableMap.put("newShiftApplicableDate", flexScheduleApplication.getEffectiveTo().plusDays(1));
        variableMap.put("applicationLink", DomainConfig.BASE_URL + "/user-flex-schedule-application");

        try {
            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);

            log.info("Mail Sent Successfully of Flex Schedule reminder for {}", flexScheduleApplication);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Failed to sent mail of Flex Schedule reminder for {}", flexScheduleApplication);
        }
    }
}
