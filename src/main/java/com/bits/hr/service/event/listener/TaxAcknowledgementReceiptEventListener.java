package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_TAX_ACKNOWLEDGEMENT_RECEIPT;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.TaxAcknowledgementReceiptEvent;
import com.bits.hr.service.event.WorkFromHomeApprovalEvent;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;

public class TaxAcknowledgementReceiptEventListener {

    @Value("${jhipster.mail.from}")
    private String from;

    private static final Logger log = LoggerFactory.getLogger(TaxAcknowledgementReceiptEvent.class);

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public TaxAcknowledgementReceiptEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleWorkFromHomeEvent(WorkFromHomeApprovalEvent event) {
        log.debug("Got Tax Acknowledgement Receipt event with status: " + event.getType());
        EventType eventType = event.getType();

        Employee employee = event.getEmployee();

        if (eventType.equals(EventType.APPROVED)) {
            taxAcknowledgementReceiptMail(employee);
        }
    }

    private void taxAcknowledgementReceiptMail(Employee employee) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Requesting to Upload Tax Acknowledgement Receipt";
        String templateName = MAIL_TEMPLATE_TAX_ACKNOWLEDGEMENT_RECEIPT;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add("rownaqul.hossain@bracits.com");

        if (employee.getGender() == Gender.MALE) {
            variableMap.put("formalPrefix", "Mr.");
        } else if (employee.getGender() == Gender.FEMALE) {
            variableMap.put("formalPrefix", "Ms.");
        } else {
            variableMap.put("formalPrefix", "");
        }
        variableMap.put("employee", employee.getFullName());
        variableMap.put("website", DomainConfig.BASE_URL + "/tax-acknowledgement-receipt");

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
