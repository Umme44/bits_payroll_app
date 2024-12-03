package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.RecruitmentRequisitionFormRepository;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.RRFEvent;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RRFEventListener {

    private static final Logger log = LoggerFactory.getLogger(RRFEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private static final String RRF_SUBJECT = "Recruitment Requisition";

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public RRFEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleRRFEvent(RRFEvent event) {
        log.debug("Got recruitment requisition application event with status: " + event.getType());
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = event.getRecruitmentRequisitionFormDTO();

        EventType eventType = event.getType();

        Optional<Employee> applicantOptional = employeeRepository.findById(recruitmentRequisitionFormDTO.getRequesterId());
        if (!applicantOptional.isPresent()) {
            log.error("Could not find employee from dto");
            return;
        }

        if (eventType.equals(EventType.CREATED)) {
            if (event.isHasRaisedOnBehalf()) {
                sendMailForRRFRaiseOnBehalf(recruitmentRequisitionFormDTO, event.getCreatedBy());
            } else {
                sendMailForRRFCreate(recruitmentRequisitionFormDTO);
            }
            sendMailForFirstTOANotify(recruitmentRequisitionFormDTO);
        } else if (eventType.equals(EventType.APPROVED)) {
            sendMailForRRFApproval(recruitmentRequisitionFormDTO, event.getNextRecommendedById());
        } else if (eventType.equals(EventType.REJECTED)) {
            sendMailForRRFRejected(recruitmentRequisitionFormDTO);
        }
    }

    private void sendMailForRRFCreate(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = RRF_SUBJECT;
        String templateName = MAIL_TEMPLATE_RRF_RAISE;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(recruitmentRequisitionFormDTO.getRequesterId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL);

        variableMap.put("name", employee.getFullName());
        variableMap.put("rrfNumber", recruitmentRequisitionFormDTO.getRrfNumber());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForRRFRaiseOnBehalf(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO, Employee createdBy) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = RRF_SUBJECT;
        String templateName = MAIL_TEMPLATE_RRF_RAISE_ON_BEHALF;

        Map<String, Object> variableMap = new HashMap<>();

        Employee employee = employeeRepository.findById(recruitmentRequisitionFormDTO.getRequesterId()).get();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(createdBy.getOfficialEmail().toLowerCase());
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL);

        variableMap.put("name", employee.getFullName());
        variableMap.put("rrfNumber", recruitmentRequisitionFormDTO.getRrfNumber());
        variableMap.put("raisedOnBehalfFullName", createdBy.getFullName());
        variableMap.put("raisedOnBehalfPIN", createdBy.getPin());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForFirstTOANotify(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = RRF_SUBJECT;
        String templateName = MAIL_TEMPLATE_RRF_TOA_NOTIFY;

        Map<String, Object> variableMap = new HashMap<>();

        Employee firstTOA;

        if (recruitmentRequisitionFormDTO.getRecommendedBy01Id() != null) {
            firstTOA = employeeRepository.findById(recruitmentRequisitionFormDTO.getRecommendedBy01Id()).get();
        } else if (recruitmentRequisitionFormDTO.getRecommendedBy02Id() != null) {
            firstTOA = employeeRepository.findById(recruitmentRequisitionFormDTO.getRecommendedBy02Id()).get();
        } else if (recruitmentRequisitionFormDTO.getRecommendedBy03Id() != null) {
            firstTOA = employeeRepository.findById(recruitmentRequisitionFormDTO.getRecommendedBy03Id()).get();
        } else if (recruitmentRequisitionFormDTO.getRecommendedBy04Id() != null) {
            firstTOA = employeeRepository.findById(recruitmentRequisitionFormDTO.getRecommendedBy04Id()).get();
        } else {
            log.error("Failed to find any TOA");
            return;
        }

        Employee requester = employeeRepository.findById(recruitmentRequisitionFormDTO.getRequesterId()).get();

        to.add(firstTOA.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL);

        variableMap.put("name", firstTOA.getFullName());
        variableMap.put("employeeName", requester.getFullName());
        variableMap.put("designation", requester.getDesignation().getDesignationName());
        variableMap.put("status", recruitmentRequisitionFormDTO.getRequisitionStatus());
        variableMap.put("rrfNumber", recruitmentRequisitionFormDTO.getRrfNumber());
        variableMap.put("actionLink", DomainConfig.BASE_URL + "/recruitment-requisition-form/user/approval");

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForRRFApproval(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO, long nextRecommendedById) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = RRF_SUBJECT;

        Map<String, Object> variableMap = new HashMap<>();

        Employee requester = employeeRepository.findById(recruitmentRequisitionFormDTO.getRequesterId()).get();
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL);

        String templateName;
        if (recruitmentRequisitionFormDTO.getRequisitionStatus().equals(RequisitionStatus.CLOSED)) {
            templateName = MAIL_TEMPLATE_RRF_CLOSED;
            to.add(requester.getOfficialEmail().toLowerCase(Locale.ROOT));

            variableMap.put("name", requester.getFullName());
            variableMap.put("rrfNumber", recruitmentRequisitionFormDTO.getRrfNumber());
        } else {
            Employee nextRecommendationFrom;

            if (employeeRepository.findById(nextRecommendedById).isPresent()) {
                nextRecommendationFrom = employeeRepository.findById(nextRecommendedById).get();
            } else {
                log.error("Failed to find any TOA");
                return;
            }

            templateName = MAIL_TEMPLATE_RRF_TOA_NOTIFY;
            to.add(nextRecommendationFrom.getOfficialEmail().toLowerCase(Locale.ROOT));

            variableMap.put("name", nextRecommendationFrom.getFullName());
            variableMap.put("employeeName", requester.getFullName());
            variableMap.put("designation", requester.getDesignation().getDesignationName());
            variableMap.put("status", "pending");
            variableMap.put("rrfNumber", recruitmentRequisitionFormDTO.getRrfNumber());
        }

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private void sendMailForRRFRejected(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = RRF_SUBJECT;

        Map<String, Object> variableMap = new HashMap<>();

        Employee rejectedBy;
        if (recruitmentRequisitionFormDTO.getRejectedById() != null) {
            rejectedBy = employeeRepository.findById(recruitmentRequisitionFormDTO.getRejectedById()).get();
        } else {
            log.error("Rejected By Id is null");
            return;
        }

        Employee requester = employeeRepository.findById(recruitmentRequisitionFormDTO.getRequesterId()).get();
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL);

        final String templateName = MAIL_TEMPLATE_RRF_REJECTED;

        variableMap.put("name", requester.getFullName());
        variableMap.put("rrfNumber", recruitmentRequisitionFormDTO.getRrfNumber());
        variableMap.put("rejectedByName", rejectedBy.getFullName());
        variableMap.put("rejectedByDesignation", rejectedBy.getDesignation().getDesignationName());

        to.add(requester.getOfficialEmail().toLowerCase(Locale.ROOT));

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
