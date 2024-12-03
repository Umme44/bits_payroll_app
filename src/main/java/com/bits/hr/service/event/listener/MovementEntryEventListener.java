package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.MovementEntryApplicationEvent;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MovementEntryEventListener {

    private static final Logger log = LoggerFactory.getLogger(MovementEntryEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public MovementEntryEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleManualAttendanceEntryEvent(MovementEntryApplicationEvent event) {
        log.debug("Got leave application event with status: " + event.getType());
        MovementEntryDTO movementEntryDTO = event.getMovementEntryDTO();
        EventType eventType = event.getType();

        Optional<Employee> applicantOptional = employeeRepository.findById(movementEntryDTO.getEmployeeId());
        if (!applicantOptional.isPresent()) {
            log.error("Could not find employee from dto");
            return;
        }

        if (eventType.equals(EventType.CREATED) || eventType.equals(EventType.UPDATED)) {
            sendMailForMovementEntryCreateEmployee(movementEntryDTO); //own
            sendMailForMovementEntryApplyLM(movementEntryDTO); // lm
        } else if (eventType.equals(EventType.APPROVED)) {
            sendMailForMovementEntryApproval(movementEntryDTO, event.isLmApproved(), Status.APPROVED);
        } else if (eventType.equals(EventType.REJECTED)) {
            sendMailForMovementEntryApproval(movementEntryDTO, event.isLmApproved(), Status.NOT_APPROVED);
        }
    }

    private void sendMailForMovementEntryCreateEmployee(MovementEntryDTO movementEntryDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Movement Entry";
        String templateName = MAIL_TEMPLATE_MOVEMENT_APPLY;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(movementEntryDTO.getEmployeeId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getFullName());
        variableMap.put("startDate", movementEntryDTO.getStartDate());
        variableMap.put("endDate", movementEntryDTO.getEndDate());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForMovementEntryApplyLM(MovementEntryDTO movementEntryDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Request for movement entry approval";
        String templateName = MAIL_TEMPLATE_MOVEMENT_APPLY_LM;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(movementEntryDTO.getEmployeeId()).get();

        to.add(employee.getReportingTo().getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getReportingTo().getFullName());
        variableMap.put("applicantPin", employee.getPin());
        variableMap.put("applicantName", employee.getFullName());
        variableMap.put("startDate", movementEntryDTO.getStartDate());
        variableMap.put("endDate", movementEntryDTO.getEndDate());
        variableMap.put("website", DomainConfig.BASE_URL + "/movement-entry/approval/lm");

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForMovementEntryApproval(MovementEntryDTO movementEntryDTO, boolean isLmApproved, Status status) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Update on movement entry approval";
        String templateName = MAIL_TEMPLATE_MOVEMENT_APPROVAL;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(movementEntryDTO.getEmployeeId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(employee.getReportingTo().getOfficialEmail());

        variableMap.put("name", employee.getFullName());
        variableMap.put("startDate", movementEntryDTO.getStartDate());
        variableMap.put("endDate", movementEntryDTO.getEndDate());

        if (isLmApproved) {
            variableMap.put("approvedBy", "your line manager");
        } else {
            variableMap.put("approvedBy", "HR");
        }

        if (status.equals(Status.APPROVED)) {
            variableMap.put("status", "approved");
        } else {
            variableMap.put("status", "not approved");
        }

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
