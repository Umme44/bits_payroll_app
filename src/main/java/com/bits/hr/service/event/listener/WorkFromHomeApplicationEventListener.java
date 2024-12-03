package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_WFH_APPROVED_HR;
import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_WFH_APPROVED_LM;
import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_WFH_CREATE;
import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_WFH_NOT_APPROVED_HR;
import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_WFH_NOT_APPROVED_LM;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.event.ApprovalVia;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.WorkFromHomeApplicationEvent;
import com.bits.hr.util.DateUtil;
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
public class WorkFromHomeApplicationEventListener {

    private static final Logger log = LoggerFactory.getLogger(WorkFromHomeApplicationEvent.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public WorkFromHomeApplicationEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleWorkFromHomeEvent(WorkFromHomeApplicationEvent event) {
        log.debug("Got Work From Home event with status: " + event.getType());
        EventType eventType = event.getType();

        WorkFromHomeApplication workFromHomeApplication = event.getWorkFromHomeApplication();

        if (eventType.equals(EventType.CREATED)) {
            sendMailForNewWHFApplicationToNotifyLM(workFromHomeApplication);
        } else if (eventType.equals(EventType.APPROVED)) {
            if (event.getApprovalVia() == ApprovalVia.LM) {
                sendMailForWorkFromHomeApplicationEnabledByLM(workFromHomeApplication);
            } else if (event.getApprovalVia() == ApprovalVia.HR) {
                sendMailForWorkFromHomeApplicationEnabledByHR(workFromHomeApplication);
            }
        } else if (eventType.equals(EventType.REJECTED)) {
            if (event.getApprovalVia() == ApprovalVia.LM) {
                sendMailForWorkFromHomeApplicationRejectedByLM(workFromHomeApplication);
            } else if (event.getApprovalVia() == ApprovalVia.HR) {
                sendMailForWorkFromHomeApplicationRejectedByHR(workFromHomeApplication);
            }
        }
    }

    private void sendMailForNewWHFApplicationToNotifyLM(WorkFromHomeApplication workFromHomeApplication) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Approval for Work from home";
        String templateName = MAIL_TEMPLATE_WFH_CREATE;

        Map<String, Object> variableMap = new HashMap<>();

        Optional<Employee> employeeOptional = employeeRepository.findById(workFromHomeApplication.getEmployee().getId());
        if (!employeeOptional.isPresent()) {
            return;
        }
        Employee employee = employeeOptional.get();

        to.add(employee.getReportingTo().getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));

        variableMap.put("lineManagerName", employee.getReportingTo().getFullName());
        variableMap.put("startDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getStartDate()));
        variableMap.put("endDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getEndDate()));
        variableMap.put("duration", workFromHomeApplication.getDuration());

        if (employee.getGender() == Gender.MALE) {
            variableMap.put("employeeName", "Mr. " + employee.getFullName());
        } else if (employee.getGender() == Gender.FEMALE) {
            variableMap.put("employeeName", "Ms. " + employee.getFullName());
        } else {
            variableMap.put("employeeName", employee.getFullName());
        }

        variableMap.put("pin", employee.getPin());
        variableMap.put("website", DomainConfig.BASE_URL + "/work-from-home-application/approvals/lm");

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForWorkFromHomeApplicationEnabledByLM(WorkFromHomeApplication workFromHomeApplication) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Approval for Work From Home";
        String templateName = MAIL_TEMPLATE_WFH_APPROVED_LM;

        Map<String, Object> variableMap = new HashMap<>();

        Optional<Employee> employeeOptional = employeeRepository.findById(workFromHomeApplication.getEmployee().getId());
        if (!employeeOptional.isPresent()) {
            return;
        }

        Employee employee = employeeOptional.get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));
        variableMap.put("employeeName", employee.getFullName());
        variableMap.put("startDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getStartDate()));
        variableMap.put("endDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getEndDate()));

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    public void sendMailForWorkFromHomeApplicationRejectedByLM(WorkFromHomeApplication workFromHomeApplication) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Update on Work from Home Request";
        String templateName = MAIL_TEMPLATE_WFH_NOT_APPROVED_LM;

        Map<String, Object> variableMap = new HashMap<>();

        Optional<Employee> employeeOptional = employeeRepository.findById(workFromHomeApplication.getEmployee().getId());
        if (!employeeOptional.isPresent()) {
            return;
        }

        Employee employee = employeeOptional.get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));
        variableMap.put("employeeName", employee.getFullName());
        variableMap.put("startDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getStartDate()));
        variableMap.put("endDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getEndDate()));

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForWorkFromHomeApplicationEnabledByHR(WorkFromHomeApplication workFromHomeApplication) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Approval for Work From Home";
        String templateName = MAIL_TEMPLATE_WFH_APPROVED_HR;

        Map<String, Object> variableMap = new HashMap<>();

        Optional<Employee> employeeOptional = employeeRepository.findById(workFromHomeApplication.getEmployee().getId());
        if (!employeeOptional.isPresent()) {
            return;
        }

        Employee employee = employeeOptional.get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(employee.getReportingTo().getOfficialEmail().toLowerCase());
        variableMap.put("employeeName", employee.getFullName());
        variableMap.put("startDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getStartDate()));
        variableMap.put("endDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getEndDate()));

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    public void sendMailForWorkFromHomeApplicationRejectedByHR(WorkFromHomeApplication workFromHomeApplication) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Update on Work from Home Request";
        String templateName = MAIL_TEMPLATE_WFH_NOT_APPROVED_HR;

        Map<String, Object> variableMap = new HashMap<>();

        Optional<Employee> employeeOptional = employeeRepository.findById(workFromHomeApplication.getEmployee().getId());
        if (!employeeOptional.isPresent()) {
            return;
        }

        Employee employee = employeeOptional.get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(employee.getReportingTo().getOfficialEmail().toLowerCase(Locale.ROOT));
        variableMap.put("employeeName", employee.getFullName());
        variableMap.put("startDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getStartDate()));
        variableMap.put("endDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getEndDate()));

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
