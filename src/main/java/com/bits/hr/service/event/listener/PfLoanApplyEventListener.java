package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.PFLoanApplyEvent;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PfLoanApplyEventListener {

    private static final Logger log = LoggerFactory.getLogger(PfLoanApplyEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public PfLoanApplyEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handlePfLoanApplyEvent(PFLoanApplyEvent event) {
        log.debug("Got Pf Loan Application event with status: " + event.getType());
        EventType eventType = event.getType();

        PfLoanApplicationDTO pfLoanApplicationDTO = event.getPfLoanApplicationDTO();

        if (eventType.equals(EventType.CREATED) || (eventType.equals(EventType.UPDATED))) {
            sendMailForPfLoanEmployee(pfLoanApplicationDTO);
        }
    }

    private void sendMailForPfLoanEmployee(PfLoanApplicationDTO pfLoanApplicationDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Notification of PF Loan Disbursement";
        String templateName = MAIL_TEMPLATE_PF_LOAN_APPLY;

        Map<String, Object> variableMap = new HashMap<>();

        Optional<Employee> employeeOptional = employeeRepository.findByPin(pfLoanApplicationDTO.getPin());

        if (!employeeOptional.isPresent()) {
            log.info("No Employee has found by PIN = " + pfLoanApplicationDTO.getPin() + " for sending mail of PfLoanRepayment");
            return;
        }

        if (employeeOptional.get().getOfficialEmail() == null) {
            log.info("No Official Email has found by PIN = " + pfLoanApplicationDTO.getPin() + " for sending mail of PfLoanRepayment");
            return;
        }

        to.add(employeeOptional.get().getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employeeOptional.get().getFullName());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
