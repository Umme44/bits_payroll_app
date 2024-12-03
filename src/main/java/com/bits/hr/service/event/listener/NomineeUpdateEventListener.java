package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_NOMINEE_UPDATE;
import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_PF_LOAN_APPLY;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.NomineeUpdateEvent;
import com.bits.hr.service.event.PFLoanApplyEvent;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NomineeUpdateEventListener {

    private static final Logger log = LoggerFactory.getLogger(NomineeUpdateEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final PfAccountRepository pfAccountRepository;
    private final EmailService emailService;

    public NomineeUpdateEventListener(
        EmployeeRepository employeeRepository,
        PfAccountRepository pfAccountRepository,
        EmailService emailService
    ) {
        this.employeeRepository = employeeRepository;
        this.pfAccountRepository = pfAccountRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handlePfLoanApplyEvent(NomineeUpdateEvent event) {
        log.debug("Got Pf Loan Application event with status: " + event.getType());
        EventType eventType = event.getType();

        if ((eventType.equals(EventType.UPDATED))) {
            sendMailForNomineeUpdate(event.getEmployee(), event.getNomineeType());
        }
    }

    private void sendMailForNomineeUpdate(Employee employee, String nomineeType) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Nominee Update";
        String templateName = MAIL_TEMPLATE_NOMINEE_UPDATE;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));

        variableMap.put("employeeName", employee.getFullName());
        variableMap.put("pin", employee.getPin());
        variableMap.put("nomineeType", nomineeType);
        variableMap.put("date", LocalDate.now());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
