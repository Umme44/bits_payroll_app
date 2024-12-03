package com.bits.hr.service.event.listener;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.communication.email.MailTemplate;
import com.bits.hr.service.communication.email.NameProcessUtil;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import com.bits.hr.service.event.PRFEvent;
import com.bits.hr.service.procurementRequisition.ProcReqMasterService;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class PRFEmailEventListener {

    @Value("${jhipster.mail.from}")
    private String from;

    @Autowired
    private ProcReqMasterService procReqMasterService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmailService emailService;

    @EventListener
    public void handlePRFEmailEvents(PRFEvent prfEvent) {
        long procReqMasterId = prfEvent.getProcReqMasterId();
        RequisitionStatus status = prfEvent.getRequisitionStatus();
        if (status.equals(RequisitionStatus.PENDING) || status.equals(RequisitionStatus.IN_PROGRESS)) {
            sendEmailForAskingPRFApproval(procReqMasterId);
        } else if (status.equals(RequisitionStatus.NOT_APPROVED)) {
            sendEmailForPRFRejection(procReqMasterId);
        } else if (status.equals(RequisitionStatus.OPEN)) {
            sendEmailAfterFinalApproval(procReqMasterId);
        }
    }

    private void sendEmailForAskingPRFApproval(long prfMasterId) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();

        final String subject = "Approval for Procurement Requisition";
        final String template = MailTemplate.MAIL_TEMPLATE_PRF_APPROVAL;

        Map<String, Object> variableMap = new HashedMap<>();

        // map of variables => nextApproverName, requesterName, requesterPin, requesterDesignation, requestedDate, actionLink
        Optional<ProcReqMasterDTO> procReqMasterDTOOptional = procReqMasterService.findOne(prfMasterId);
        if (!procReqMasterDTOOptional.isPresent()) {
            return;
        }

        String nextApprovalFromOfficialEmail = procReqMasterDTOOptional.get().getNextApprovalFromOfficialEmail();
        if (nextApprovalFromOfficialEmail == null) {
            log.error(
                "Next Approver Official Email has not set for Employee ID= {}",
                procReqMasterDTOOptional.get().getNextApprovalFromId()
            );
            return;
        }
        to.add(nextApprovalFromOfficialEmail);

        ProcReqMasterDTO procReqMasterDTO = procReqMasterDTOOptional.get();
        variableMap.put("nextApproverName", procReqMasterDTO.getNextApprovalFromFullName());
        variableMap.put("requestedDate", procReqMasterDTO.getRequestedDate());

        EmployeeDTO requester = employeeService.findOne(procReqMasterDTO.getRequestedById()).get();
        variableMap.put(
            "requesterName",
            NameProcessUtil.getNameWithTitle(requester.getFullName(), requester.getGender(), requester.getMaritalStatus())
        );
        variableMap.put("requesterPin", requester.getPin());
        variableMap.put("requesterDesignation", requester.getDesignationName());

        variableMap.put("actionLink", DomainConfig.BASE_URL + "/procurement-requisition/approval");

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, template, variableMap);
    }

    private void sendEmailForPRFRejection(long prfMasterId) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();

        final String subject = "Approval for Procurement Requisition";
        final String template = MailTemplate.MAIL_TEMPLATE_PRF_REJECTION;

        Map<String, Object> variableMap = new HashedMap<>();

        // map of variables => requesterName, prfNumber, rejectedByName, rejectedByDesignation, rejectionReason
        ProcReqMasterDTO procReqMasterDTO = procReqMasterService.findOne(prfMasterId).get();
        variableMap.put("prfNumber", procReqMasterDTO.getRequisitionNo());

        EmployeeDTO requester = employeeService.findOne(procReqMasterDTO.getRequestedById()).get();
        variableMap.put("requesterName", requester.getFullName());

        try {
            String requesterEmail = employeeService.findOne(procReqMasterDTO.getRequestedById()).get().getOfficialEmail();
            to.add(requesterEmail);
        } catch (NullPointerException | NoSuchElementException exception) {
            return;
        }

        EmployeeDTO rejectedBy = employeeService.findOne(procReqMasterDTO.getRejectedById()).get();
        variableMap.put("rejectedByName", rejectedBy.getFullName());
        variableMap.put("rejectedByDesignation", rejectedBy.getDesignationName());
        variableMap.put("rejectionReason", procReqMasterDTO.getRejectionReason());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, template, variableMap);
    }

    private void sendEmailAfterFinalApproval(long prfMasterId) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();

        final String subject = "Approval for Procurement Requisition";
        final String template = MailTemplate.MAIL_TEMPLATE_PRF_OPEN;

        String procurementOfficerPin = procReqMasterService.getProcurementOfficerPin();
        Employee procurementOfficer = employeeService
            .findEmployeeByPin(procurementOfficerPin)
            .orElseThrow(() -> new RuntimeException("No Employee Found with PIN: " + procurementOfficerPin));
        to.add(procurementOfficer.getOfficialEmail());

        Map<String, Object> variableMap = new HashedMap<>();

        // map of variables => procurementOfficerName, requesterName, requesterPIN, actionLink
        String procurementOfficerName = procurementOfficer.getFullName();
        variableMap.put("procurementOfficerName", procurementOfficerName);
        ProcReqMasterDTO procReqMasterDTO = procReqMasterService.findOne(prfMasterId).get();
        EmployeeDTO requester = employeeService.findOne(procReqMasterDTO.getRequestedById()).get();
        variableMap.put("requesterName", requester.getFullName());
        variableMap.put("requesterPIN", requester.getPin());
        variableMap.put("actionLink", DomainConfig.BASE_URL + "/procurement-requisition/manage");

        EmployeeDTO deptHead = employeeService.findDeptHeadByDeptId(procReqMasterDTO.getDepartmentId());
        cc.add(deptHead.getOfficialEmail());

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, template, variableMap);
    }
}
