package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeNOC;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.EmployeeNOCDTO;
import com.bits.hr.service.event.EmployeeNOCApplicationEvent;
import com.bits.hr.service.event.EventType;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmployeeNOCApplicationEventListener {

    private static final Logger log = LoggerFactory.getLogger(LeaveApplicationEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    @Autowired
    private EmailService emailService;

    @EventListener
    public void handleEmployeeNOCApplicationEventListener(EmployeeNOCApplicationEvent event) {
        log.debug("Got Employee NOC application event with status: " + event.getEventType());

        EmployeeNOC employeeNOC = event.getEmployeeNOC();
        EventType eventType = event.getEventType();

        if (eventType.equals(EventType.CREATED)) {
            sendMailToTheHR(employeeNOC);
        } else if (eventType.equals(EventType.APPROVED)) {
            sendMailToTheApplicant(employeeNOC);
        }
    }

    private void sendMailToTheApplicant(EmployeeNOC employeeNOC) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Apply for the certificate.";
        String templateName = MAIL_TEMPLATE_EMPLOYEE_NOC_APPROVE;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(employeeNOC.getEmployee().getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("recipientName", employeeNOC.getEmployee().getFullName());
        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailToTheHR(EmployeeNOC employeeNOC) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Apply for the certificate.";
        String templateName = MAIL_TEMPLATE_EMPLOYEE_NOC_APPLY;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));

        variableMap.put("recipientName", "HR");
        variableMap.put("employeeName", employeeNOC.getEmployee().getFullName());
        variableMap.put("employeePin", employeeNOC.getEmployee().getPin());
        variableMap.put("mx", employeeNOC.getEmployee().getGender() == Gender.MALE ? "Mr." : "Ms.");
        variableMap.put("approvalUrl", DomainConfig.BASE_URL + "/employee-docs-admin");
        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
