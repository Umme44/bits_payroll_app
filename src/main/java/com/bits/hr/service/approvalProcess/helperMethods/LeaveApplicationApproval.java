package com.bits.hr.service.approvalProcess.helperMethods;

import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.User;
import com.bits.hr.service.config.CurrentEmployeeService;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class LeaveApplicationApproval {

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    private User sanctionedBy;
    private Instant sanctionedAt;

    public LeaveApplicationApproval() {}

    public static LeaveApplication processToApproveLM(LeaveApplication leaveApplication) {
        leaveApplication.setIsHRApproved(false);
        leaveApplication.setIsLineManagerApproved(true);
        leaveApplication.setIsRejected(false);
        leaveApplication.setRejectionComment("");
        return leaveApplication;
    }

    public static LeaveApplication processToApproveHR(LeaveApplication leaveApplication) {
        leaveApplication.setIsHRApproved(true);
        leaveApplication.setIsLineManagerApproved(false);
        leaveApplication.setIsRejected(false);
        leaveApplication.setRejectionComment("");
        return leaveApplication;
    }

    public static LeaveApplication processToRejectLM(LeaveApplication leaveApplication) {
        leaveApplication.setIsHRApproved(false);
        leaveApplication.setIsLineManagerApproved(false);
        leaveApplication.setIsRejected(true);
        leaveApplication.setRejectionComment("Line manager Rejected");
        return leaveApplication;
    }

    public static LeaveApplication processToRejectHR(LeaveApplication leaveApplication) {
        leaveApplication.setIsHRApproved(false);
        leaveApplication.setIsLineManagerApproved(false);
        leaveApplication.setIsRejected(true);
        leaveApplication.setRejectionComment("HR Rejected");
        return leaveApplication;
    }

    public static LeaveApplication processForSanction(LeaveApplication leaveApplication, Optional<User> sanctionedBy) {
        if (sanctionedBy.isPresent()) {
            leaveApplication.setSanctionedBy(sanctionedBy.get());
        }
        Instant sanctionedAt = Instant.from(Instant.now().atZone(ZoneOffset.systemDefault()));
        leaveApplication.setSanctionedAt(sanctionedAt);

        return leaveApplication;
    }
}
