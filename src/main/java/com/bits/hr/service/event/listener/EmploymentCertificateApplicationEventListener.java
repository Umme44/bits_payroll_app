package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.EmployeeNOC;
import com.bits.hr.domain.EmploymentCertificate;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.event.EmploymentCertificateApplicationEvent;
import com.bits.hr.service.event.EventType;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmploymentCertificateApplicationEventListener {

    private static final Logger log = LoggerFactory.getLogger(LeaveApplicationEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    @Autowired
    private EmailService emailService;

    @EventListener
    public void handleEmploymentCertificateApplicationEventListener(EmploymentCertificateApplicationEvent event) {
        log.debug("Got Employment certificate application event with status: " + event.getEventType());

        EmploymentCertificate employmentCertificate = event.getEmploymentCertificate();
        EventType eventType = event.getEventType();

        if (eventType.equals(EventType.CREATED)) {
            sendMailToTheHR(employmentCertificate);
        } else if (eventType.equals(EventType.APPROVED)) {
            sendMailToTheApplicant(employmentCertificate);
        }
    }

    private void sendMailToTheApplicant(EmploymentCertificate employmentCertificate) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Apply for the certificate.";
        String templateName = MAIL_TEMPLATE_EMPLOYMENT_CERTIFICATE_APPROVE;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(employmentCertificate.getEmployee().getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("recipientName", employmentCertificate.getEmployee().getFullName());
        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailToTheHR(EmploymentCertificate employmentCertificate) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Apply for the certificate.";
        String templateName = MAIL_TEMPLATE_EMPLOYMENT_CERTIFICATE_APPLY;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));

        variableMap.put("recipientName", "HR");
        variableMap.put("employeeName", employmentCertificate.getEmployee().getFullName());
        variableMap.put("employeePin", employmentCertificate.getEmployee().getPin());
        variableMap.put("mx", employmentCertificate.getEmployee().getGender() == Gender.MALE ? "Mr." : "Ms.");
        variableMap.put("approvalUrl", DomainConfig.BASE_URL + "/employee-docs-admin");
        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
