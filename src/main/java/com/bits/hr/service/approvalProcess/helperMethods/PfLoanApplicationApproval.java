package com.bits.hr.service.approvalProcess.helperMethods;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfLoanApplication;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.service.config.CurrentEmployeeService;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class PfLoanApplicationApproval {

    public static PfLoanApplication processToApproveHR(PfLoanApplication pfLoanApplication) {
        pfLoanApplication.setStatus(Status.APPROVED);
        pfLoanApplication.setIsApproved(true);
        pfLoanApplication.setIsRejected(false);
        pfLoanApplication.setApprovalDate(LocalDate.now());
        return pfLoanApplication;
    }

    public static PfLoanApplication processToRejectHR(PfLoanApplication pfLoanApplication) {
        pfLoanApplication.setStatus(Status.NOT_APPROVED);
        pfLoanApplication.isApproved(false);
        pfLoanApplication.rejectionDate(LocalDate.now());
        pfLoanApplication.isRejected(true);
        return pfLoanApplication;
    }
}
