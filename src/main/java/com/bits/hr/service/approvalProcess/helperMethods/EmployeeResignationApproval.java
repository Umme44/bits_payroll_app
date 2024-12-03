package com.bits.hr.service.approvalProcess.helperMethods;

import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.Status;
import java.util.Optional;

public class EmployeeResignationApproval {

    public static Optional<EmployeeResignation> processToApprove(EmployeeResignation employeeResignation) {
        if (employeeResignation.getApprovalStatus() == Status.PENDING) {
            employeeResignation.approvalStatus(Status.APPROVED);
            return Optional.of(employeeResignation);
        }
        return Optional.empty();
    }

    public static Optional<EmployeeResignation> processToReject(EmployeeResignation employeeResignation) {
        if (employeeResignation.getApprovalStatus() == Status.PENDING) {
            employeeResignation.approvalStatus(Status.NOT_APPROVED);
            return Optional.of(employeeResignation);
        }
        return Optional.empty();
    }
}
