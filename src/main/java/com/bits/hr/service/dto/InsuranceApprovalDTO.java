package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.domain.enumeration.Status;

public class InsuranceApprovalDTO {

    private long registrationId;

    private long claimId;

    private String insuranceCardId;

    private InsuranceStatus status;

    private String reason;

    private double acceptedAmount;

    public long getClaimId() {
        return claimId;
    }

    public void setClaimId(long claimId) {
        this.claimId = claimId;
    }

    public double getAcceptedAmount() {
        return acceptedAmount;
    }

    public void setAcceptedAmount(double acceptedAmount) {
        this.acceptedAmount = acceptedAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(long registrationId) {
        this.registrationId = registrationId;
    }

    public String getInsuranceCardId() {
        return insuranceCardId;
    }

    public void setInsuranceCardId(String insuranceCardId) {
        this.insuranceCardId = insuranceCardId;
    }

    public InsuranceStatus getStatus() {
        return status;
    }

    public void setStatus(InsuranceStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return (
            "InsuranceRegistrationApprovalDTO{" +
            "registrationId=" +
            registrationId +
            ", insuranceCardId=" +
            insuranceCardId +
            ", status=" +
            status +
            ", reason='" +
            reason +
            '\'' +
            '}'
        );
    }
}
