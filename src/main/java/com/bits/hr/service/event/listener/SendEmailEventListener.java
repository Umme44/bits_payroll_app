package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.event.SampleEmailEvent;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SendEmailEventListener {

    private static final Logger log = LoggerFactory.getLogger(SendEmailEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public SendEmailEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleSampleEmail(SampleEmailEvent event) {
        log.debug("Send Sample Email event");
        sendSampleEmail();
    }

    private void sendSampleEmail() {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Success! Email configured";
        String templateName = MAIL_TEMPLATE_SAMPLE_EMAIL;

        Map<String, Object> variableMap = new HashMap<>();

        to.add("saroj.roy@bracits.com".toLowerCase(Locale.ROOT));
        to.add("abdul.ahad@bracits.com".toLowerCase(Locale.ROOT));
        to.add("shakibul.alam@bracits.com".toLowerCase(Locale.ROOT));

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
