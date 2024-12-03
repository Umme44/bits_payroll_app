package com.bits.hr.service.event;

import com.bits.hr.service.communication.NID.BatchNIDVerificationService;
import com.bits.hr.service.config.ApplicationStartConfigurationService;
import com.bits.hr.service.fileOperations.SubDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RunAfterStartup {

    @Autowired
    private SubDirectoryService subDirectoryService;

    @Autowired
    private ApplicationStartConfigurationService applicationStartConfigurationService;

    @Autowired
    private BatchNIDVerificationService batchNIDVerificationService;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        try {
            subDirectoryService.createDirectoryIfNotExist();
            applicationStartConfigurationService.registerDomainName();
            applicationStartConfigurationService.createRRFApprovalProcess();
            applicationStartConfigurationService.createPRFApprovalProcess();
            applicationStartConfigurationService.createMaxAllowedDaysForChangeAttendanceStatus();
            applicationStartConfigurationService.createMaxDurationInDaysForAttendanceDataLoad();
            applicationStartConfigurationService.createPRFConfiguration();
            applicationStartConfigurationService.createIsLeaveApplicationEnabledForUserEnd();
            applicationStartConfigurationService.createIsTaxStatementVisibleForUserEnd();
            applicationStartConfigurationService.createTimeSlot();
            applicationStartConfigurationService.setDefaultShift();
            //applicationStartConfigurationService.createInsuranceRelation();
            applicationStartConfigurationService.createInsuranceConfiguration();
            //        batchNIDVerificationService.routineNidVerificationService();
            applicationStartConfigurationService.createIsRRFEnabledForUserEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
