package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.LeaveApplicationEvent;
import com.bits.hr.util.DateUtil;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LeaveApplicationEventListener {

    private static final Logger log = LoggerFactory.getLogger(LeaveApplicationEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public LeaveApplicationEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleLeaveApplicationEvent(LeaveApplicationEvent event) {
        log.debug("Got leave application event with status: " + event.getType());
        LeaveApplicationDTO application = event.getLeaveApplication();
        EventType eventType = event.getType();

        Optional<Employee> applicantOptional = employeeRepository.findById(application.getEmployeeId());
        if (!applicantOptional.isPresent()) {
            log.error("Could not find employee from dto");
            return;
        }
        if (eventType.equals(EventType.CREATED) || eventType.equals(EventType.UPDATED)) {
            sendMailForLeaveApplicationApplicant(application);
            sendMailForLeaveApplicationApplicantLM(application);
        } else if (eventType.equals(EventType.APPROVED)) {
            sendMailForLeaveApplicationApplicationSanction(application, Status.APPROVED);
        } else if (eventType.equals(EventType.REJECTED)) {
            sendMailForLeaveApplicationApplicationSanction(application, Status.NOT_APPROVED);
        }
    }

    private void sendMailForLeaveApplicationApplicant(LeaveApplicationDTO leaveApplicationDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Leave Application";
        String templateName = MAIL_TEMPLATE_LEAVE_APPLICATION_APPLICANT;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(leaveApplicationDTO.getEmployeeId()).get();
        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getFullName());
        variableMap.put("numberOfDays", leaveApplicationDTO.getDurationInDay());
        variableMap.put("leaveType", leaveApplicationDTO.getLeaveType().getValue());
        variableMap.put("startDate", DateUtil.formatDateAsDDDDMMMMYYYY(leaveApplicationDTO.getStartDate()));
        variableMap.put("endDate", DateUtil.formatDateAsDDDDMMMMYYYY(leaveApplicationDTO.getEndDate()));

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForLeaveApplicationApplicantLM(LeaveApplicationDTO leaveApplicationDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Request to approve leave application";
        String templateName;
        if (leaveApplicationDTO.getStartDate().equals(leaveApplicationDTO.getEndDate())) {
            templateName = MAIL_TEMPLATE_LEAVE_SINGLE_APPLICATION_LM;
        } else {
            templateName = MAIL_TEMPLATE_LEAVE_APPLICATION_LM;
        }

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(leaveApplicationDTO.getEmployeeId()).get();

        to.add(employee.getReportingTo().getOfficialEmail().toLowerCase(Locale.ROOT));

        variableMap.put("name", employee.getReportingTo().getFullName());
        variableMap.put("employeeName", employee.getFullName());
        variableMap.put("designation", employee.getDesignation().getDesignationName());
        variableMap.put("pin", employee.getPin());
        variableMap.put("numberOfDays", leaveApplicationDTO.getDurationInDay());
        variableMap.put("leaveType", leaveApplicationDTO.getLeaveType().getValue());
        variableMap.put("startDate", DateUtil.formatDateAsDDDDMMMMYYYY(leaveApplicationDTO.getStartDate()));
        variableMap.put("endDate", DateUtil.formatDateAsDDDDMMMMYYYY(leaveApplicationDTO.getEndDate()));
        variableMap.put("website", DomainConfig.BASE_URL + "/leave-approval-superordinate");
        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForLeaveApplicationApplicationSanction(LeaveApplicationDTO leaveApplicationDTO, Status status) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Leave Approval";
        String templateName;
        if (leaveApplicationDTO.getStartDate().equals(leaveApplicationDTO.getEndDate())) {
            templateName = MAIL_TEMPLATE_LEAVE_APPROVAL_SINGLE_APPLICANT;
        } else {
            templateName = MAIL_TEMPLATE_LEAVE_APPROVAL_APPLICANT;
        }

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(leaveApplicationDTO.getEmployeeId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(employee.getReportingTo().getOfficialEmail());

        variableMap.put("name", employee.getFullName());
        variableMap.put("numberOfDays", leaveApplicationDTO.getDurationInDay());
        variableMap.put("leaveType", leaveApplicationDTO.getLeaveType().getValue());
        variableMap.put("startDate", DateUtil.formatDateAsDDDDMMMMYYYY(leaveApplicationDTO.getStartDate()));
        variableMap.put("endDate", DateUtil.formatDateAsDDDDMMMMYYYY(leaveApplicationDTO.getEndDate()));

        if (status.equals(Status.APPROVED)) {
            variableMap.put("status", "approved");
        } else {
            variableMap.put("status", "not approved");
        }

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForLeaveAdjustment(LeaveApplicationDTO leaveApplicationDTO, Status status) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Application for Leave";
        String templateName = MAIL_TEMPLATE_LEAVE_ADJUSTMENT;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(leaveApplicationDTO.getEmployeeId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

        cc.add(employee.getReportingTo().getOfficialEmail());
        variableMap.put("name", employee.getFullName());
        variableMap.put("absentDayList", leaveApplicationDTO.getDurationInDay());
        variableMap.put("leaveType", leaveApplicationDTO.getLeaveType().getValue());
        variableMap.put(
            "startDate",
            leaveApplicationDTO.getStartDate().format(DateTimeFormatter.ofPattern("dd MMMM uuuu", Locale.ENGLISH))
        );
        variableMap.put("endDate", leaveApplicationDTO.getEndDate().format(DateTimeFormatter.ofPattern("dd, MMMM uuuu", Locale.ENGLISH)));

        if (status.equals(Status.APPROVED)) {
            variableMap.put("status", "approved");
        } else {
            variableMap.put("status", "not approved");
        }

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForRemainderLeaveApplicationApplicant(LeaveApplicationDTO leaveApplicationDTO, Status status) {
        // TODO : Send Mail Remainder Leave Application Applicant
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Application for Leave";
        String templateName = MAIL_TEMPLATE_REMINDER_LEAVE_APPLICATION_APPLICANT;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(leaveApplicationDTO.getEmployeeId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

        cc.add(employee.getReportingTo().getOfficialEmail());
        variableMap.put("name", employee.getFullName());
        variableMap.put("absentDayList", leaveApplicationDTO.getDurationInDay());
        variableMap.put("leaveType", leaveApplicationDTO.getLeaveType().getValue());
        variableMap.put(
            "startDate",
            leaveApplicationDTO.getStartDate().format(DateTimeFormatter.ofPattern("dd, MMMM uuuu", Locale.ENGLISH))
        );
        variableMap.put("endDate", leaveApplicationDTO.getEndDate().format(DateTimeFormatter.ofPattern("dd, MMMM uuuu", Locale.ENGLISH)));

        if (status.equals(Status.APPROVED)) {
            variableMap.put("status", "approved");
        } else {
            variableMap.put("status", "not approved");
        }

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private String getUIFriendlyLeaveType(LeaveType leaveType) {
        if (leaveType == LeaveType.MENTIONABLE_ANNUAL_LEAVE) return "annual leave"; else if (
            leaveType == LeaveType.MENTIONABLE_CASUAL_LEAVE
        ) return "casual leave"; else if (leaveType == LeaveType.NON_MENTIONABLE_PATERNITY_LEAVE) return "paternity leave"; else if (
            leaveType == LeaveType.NON_MENTIONABLE_MATERNITY_LEAVE
        ) return "maternity leave"; else if (
            leaveType == LeaveType.NON_MENTIONABLE_COMPENSATORY_LEAVE
        ) return "compensatory leave"; else if (leaveType == LeaveType.NON_MENTIONABLE_PANDEMIC_LEAVE) return "pandemic leave"; else if (
            leaveType == LeaveType.OTHER
        ) return "other"; else return "";
    }
}
