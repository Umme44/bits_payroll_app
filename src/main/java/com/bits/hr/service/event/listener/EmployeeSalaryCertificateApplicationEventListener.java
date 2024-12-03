package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_EMPLOYEE_SALARY_CERTIFICATE_APPLY;
import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_EMPLOYEE_SALARY_CERTIFICATE_APPROVE;

import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.event.EmployeeSalaryCertificateApplicationEvent;
import com.bits.hr.service.event.EventType;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmployeeSalaryCertificateApplicationEventListener {

    private static final Logger log = LoggerFactory.getLogger(LeaveApplicationEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    @Autowired
    private EmailService emailService;

    @EventListener
    public void handleEmployeeSalaryCertificateApplicationEventListener(EmployeeSalaryCertificateApplicationEvent event) {
        log.debug("Got Employee NOC application event with status: " + event.getEventType());

        SalaryCertificate salaryCertificate = event.getSalaryCertificate();
        EventType eventType = event.getEventType();

        if (eventType.equals(EventType.CREATED)) {
            sendMailToTheHR(salaryCertificate);
        } else if (eventType.equals(EventType.APPROVED)) {
            sendMailToTheApplicant(salaryCertificate);
        }
    }

    private void sendMailToTheApplicant(SalaryCertificate salaryCertificate) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Apply for the certificate.";
        String templateName = MAIL_TEMPLATE_EMPLOYEE_SALARY_CERTIFICATE_APPROVE;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(salaryCertificate.getEmployee().getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("recipientName", salaryCertificate.getEmployee().getFullName());
        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailToTheHR(SalaryCertificate salaryCertificate) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Apply for the certificate.";
        String templateName = MAIL_TEMPLATE_EMPLOYEE_SALARY_CERTIFICATE_APPLY;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));

        variableMap.put("recipientName", "HR");
        variableMap.put("employeeName", salaryCertificate.getEmployee().getFullName());
        variableMap.put("employeePin", salaryCertificate.getEmployee().getPin());
        variableMap.put("mx", salaryCertificate.getEmployee().getGender() == Gender.MALE ? "Mr." : "Ms.");
        variableMap.put("approvalUrl", DomainConfig.BASE_URL + "/employee-docs-admin");
        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
