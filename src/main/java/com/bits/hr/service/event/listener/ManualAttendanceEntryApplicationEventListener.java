package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_ATTENDANCE_APPLY;
import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_ATTENDANCE_APPLY_LM;
import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_ATTENDANCE_APPROVAL;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.ManualAttendanceEntryApplicationEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ManualAttendanceEntryApplicationEventListener {

    private static final Logger log = LoggerFactory.getLogger(ManualAttendanceEntryApplicationEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public ManualAttendanceEntryApplicationEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleManualAttendanceEntryEvent(ManualAttendanceEntryApplicationEvent event) {
        log.info("Got manual attendance application event with status: " + event.getType());
        ManualAttendanceEntryDTO manualAttendanceEntryDTO = event.getManualAttendanceEntryDTO();
        EventType eventType = event.getType();

        Optional<Employee> applicantOptional = employeeRepository.findById(manualAttendanceEntryDTO.getEmployeeId());
        if (!applicantOptional.isPresent()) {
            log.error("Could not find employee from dto");
            return;
        }

        if (eventType.equals(EventType.CREATED) || eventType.equals(EventType.UPDATED)) {
            sendMailForManualAttendanceEntryApply(manualAttendanceEntryDTO);
            sendMailForManualAttendanceEntryApplyLM(manualAttendanceEntryDTO);
        } else if (eventType.equals(EventType.APPROVED)) {
            sendMailForManualAttendanceEntryApproval(manualAttendanceEntryDTO, Status.APPROVED);
        } else if (eventType.equals(EventType.REJECTED)) {
            sendMailForManualAttendanceEntryApproval(manualAttendanceEntryDTO, Status.NOT_APPROVED);
        }
    }

    private void sendMailForManualAttendanceEntryApply(ManualAttendanceEntryDTO manualAttendanceEntryDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Manual Attendance";
        String templateName = MAIL_TEMPLATE_ATTENDANCE_APPLY;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(manualAttendanceEntryDTO.getEmployeeId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getFullName());
        variableMap.put("day", manualAttendanceEntryDTO.getDate());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForManualAttendanceEntryApplyLM(ManualAttendanceEntryDTO manualAttendanceEntryDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Request to approve missing attendance";
        String templateName = MAIL_TEMPLATE_ATTENDANCE_APPLY_LM;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(manualAttendanceEntryDTO.getEmployeeId()).get();

        to.add(employee.getReportingTo().getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getReportingTo().getFullName());
        variableMap.put("employeeName", employee.getFullName());
        variableMap.put("designation", employee.getDesignation().getDesignationName());
        variableMap.put("date", manualAttendanceEntryDTO.getDate());
        variableMap.put("pin", employee.getPin());
        variableMap.put("website", DomainConfig.BASE_URL + "/manual-attendance-entry/lm");

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForManualAttendanceEntryApproval(ManualAttendanceEntryDTO manualAttendanceEntryDTO, Status status) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Update on attendance approval";

        String templateName = MAIL_TEMPLATE_ATTENDANCE_APPROVAL;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(manualAttendanceEntryDTO.getEmployeeId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getFullName());
        variableMap.put("date", manualAttendanceEntryDTO.getDate());

        if (status.equals(Status.APPROVED)) {
            variableMap.put("status", "approved");
        } else {
            variableMap.put("status", "not approved");
        }

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForManualAttendanceEntryApprovalMissedReminder(ManualAttendanceEntryDTO manualAttendanceEntryDTO, Status status) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Approval - Attendance regularization";
        String templateName = "";

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(manualAttendanceEntryDTO.getEmployeeId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

        cc.add(employee.getReportingTo().getOfficialEmail());
        variableMap.put("name", employee.getFullName());
        variableMap.put("date", manualAttendanceEntryDTO.getDate());

        if (status.equals(Status.APPROVED)) {
            variableMap.put("status", "approved");
        } else {
            variableMap.put("status", "rejected");
        }

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
