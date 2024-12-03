package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfLoanRepayment;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.PfLoanRepaymentDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.PFLoanRepaymentEvent;
import com.bits.hr.service.event.WorkFromHomeApprovalEvent;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PfLoanRepaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PfLoanRepaymentEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public PfLoanRepaymentEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handlePfLoanRepaymentEvent(PFLoanRepaymentEvent event) {
        log.debug("Got Pf Loan Repayment event with status: " + event.getType());
        EventType eventType = event.getType();

        PfLoanRepaymentDTO pfLoanRepaymentDTO = event.getPfLoanRepaymentDTO();

        if (eventType.equals(EventType.CREATED) || (eventType.equals(EventType.UPDATED))) {
            sendMailForPfLoanRepayment(pfLoanRepaymentDTO);
        }
    }

    private void sendMailForPfLoanRepayment(PfLoanRepaymentDTO pfLoanRepaymentDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "";
        String templateName = MAIL_TEMPLATE_PF_LOAN_REPAYMENT;

        Map<String, Object> variableMap = new HashMap<>();

        Optional<Employee> employeeOptional = employeeRepository.findByPin(pfLoanRepaymentDTO.getPin());

        if (!employeeOptional.isPresent()) {
            log.info("No Employee has found by PIN = " + pfLoanRepaymentDTO.getPin() + " for sending mail of PfLoanRepayment");
            return;
        }

        if (employeeOptional.get().getOfficialEmail() == null) {
            log.info("No Official Email has found by PIN = " + pfLoanRepaymentDTO.getPin() + " for sending mail of PfLoanRepayment");
            return;
        }

        to.add(employeeOptional.get().getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employeeOptional.get().getFullName());
        variableMap.put("amount", pfLoanRepaymentDTO.getAmount());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForWorkFromHomeDisabled(Employee employee) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "";
        String templateName = MAIL_TEMPLATE_WFH_STOP;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getFullName());
        variableMap.put("date", LocalDate.now());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
