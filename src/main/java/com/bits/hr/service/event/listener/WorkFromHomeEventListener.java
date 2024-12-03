package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.WorkFromHomeApprovalEvent;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WorkFromHomeEventListener {

    private static final Logger log = LoggerFactory.getLogger(WorkFromHomeEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public WorkFromHomeEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleWorkFromHomeEvent(WorkFromHomeApprovalEvent event) {
        log.debug("Got Work From Home event with status: " + event.getType());
        EventType eventType = event.getType();

        Employee employee = event.getEmployee();

        if (eventType.equals(EventType.APPROVED)) {
            sendMailForWorkFromHomeEnabled(employee);
        } else if (eventType.equals(EventType.REJECTED)) {
            sendMailForWorkFromHomeDisabled(employee);
        }
    }

    private void sendMailForWorkFromHomeEnabled(Employee employee) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Approval for Work From Home";
        String templateName = MAIL_TEMPLATE_WFH_START;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(employee.getReportingTo().getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getFullName());
        variableMap.put("date", LocalDate.now());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForWorkFromHomeDisabled(Employee employee) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Approval for Work From Home";
        String templateName = MAIL_TEMPLATE_WFH_STOP;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(employee.getReportingTo().getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getFullName());
        variableMap.put("date", LocalDate.now());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
