package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.service.approvalProcess.SalaryCertificateHRServiceImpl;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.CertificateApprovalDto;
import com.bits.hr.service.dto.SalaryCertificateDTO;
import com.bits.hr.service.event.EmployeeSalaryCertificateApplicationEvent;
import com.bits.hr.service.event.EventType;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/payroll-mgt/salary-certificate-approval")
public class SalaryCertificateHRApprovalResource {

    @Autowired
    private final SalaryCertificateHRServiceImpl salaryCertificateHRService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public SalaryCertificateHRApprovalResource(SalaryCertificateHRServiceImpl salaryCertificateHRService) {
        this.salaryCertificateHRService = salaryCertificateHRService;
    }

    @GetMapping("/hr")
    public List<SalaryCertificateDTO> getAllPending(){
        log.debug("REST request to get pending salary certificates");
        return salaryCertificateHRService.getAllPending();
    }

    @PutMapping("/approve-selected-hr/{signatoryPersonId}")
    public boolean approveSelected(@RequestBody ApprovalDTO approvalDTO, @PathVariable Long signatoryPersonId){
        log.debug("REST request to Approve selected salary certificates HR");
        return salaryCertificateHRService.approveSelected(approvalDTO.getListOfIds(), signatoryPersonId);
    }

    @PutMapping("/approve/{salaryCertificateId}")
    public boolean approve(@RequestBody CertificateApprovalDto dto, @PathVariable Long salaryCertificateId){
        log.debug("REST request to Approve salary certificates HR");

        boolean result=salaryCertificateHRService.approveSalaryCertificate(salaryCertificateId, dto);
        if(result){
            SalaryCertificate salaryCertificate=salaryCertificateHRService.findById(salaryCertificateId).get();
            publishEvent(salaryCertificate, EventType.APPROVED);
            return true;
        }else{
            return false;
        }
    }

    @PutMapping("/reject-selected-hr")
    public boolean rejectSelected(@RequestBody ApprovalDTO approvalDTO){
        log.debug("REST request to Reject selected salary certificates HR");
        return salaryCertificateHRService.denySelected(approvalDTO.getListOfIds());
    }

    private void publishEvent(SalaryCertificate salaryCertificate, EventType eventType){
        log.debug("publishing employee salary certificate application event with event: " + eventType);
        EmployeeSalaryCertificateApplicationEvent event = new EmployeeSalaryCertificateApplicationEvent(this, salaryCertificate, eventType);
        applicationEventPublisher.publishEvent(event);
    }
}
