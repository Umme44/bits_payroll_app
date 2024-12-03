package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.ClaimStatus;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.util.annotation.ValidateNumeric;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.InsuranceClaim} entity.
 */
public class InsuranceClaimDTO implements Serializable {

    private Long id;

    @ValidateNumeric
    private String insuranceCardId;

    private String policyHolderPin;
    private String policyHolderName;
    private String registrationName;
    private InsuranceRelation relation;

    private LocalDate settlementDate;

    private LocalDate paymentDate;

    private LocalDate regretDate;

    private String regretReason;

    private Double claimedAmount;

    private Double settledAmount;

    private ClaimStatus claimStatus;

    private Instant createdAt;

    private Instant updatedAt;

    private Long insuranceRegistrationId;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInsuranceCardId() {
        return insuranceCardId;
    }

    public void setInsuranceCardId(String insuranceCardId) {
        this.insuranceCardId = insuranceCardId;
    }

    public String getPolicyHolderPin() {
        return policyHolderPin;
    }

    public void setPolicyHolderPin(String policyHolderPin) {
        this.policyHolderPin = policyHolderPin;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public void setPolicyHolderName(String policyHolderName) {
        this.policyHolderName = policyHolderName;
    }

    public String getRegistrationName() {
        return registrationName;
    }

    public void setRegistrationName(String registrationName) {
        this.registrationName = registrationName;
    }

    public InsuranceRelation getRelation() {
        return relation;
    }

    public void setRelation(InsuranceRelation relation) {
        this.relation = relation;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getRegretDate() {
        return regretDate;
    }

    public void setRegretDate(LocalDate regretDate) {
        this.regretDate = regretDate;
    }

    public String getRegretReason() {
        return regretReason;
    }

    public void setRegretReason(String regretReason) {
        this.regretReason = regretReason;
    }

    public Double getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(Double claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public Double getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Double settledAmount) {
        this.settledAmount = settledAmount;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getInsuranceRegistrationId() {
        return insuranceRegistrationId;
    }

    public void setInsuranceRegistrationId(Long insuranceRegistrationId) {
        this.insuranceRegistrationId = insuranceRegistrationId;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long userId) {
        this.updatedById = userId;
    }

    public String getUpdatedByLogin() {
        return updatedByLogin;
    }

    public void setUpdatedByLogin(String userLogin) {
        this.updatedByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceClaimDTO)) {
            return false;
        }

        return id != null && id.equals(((InsuranceClaimDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceClaimDTO{" +
            "id=" + getId() +
            ", settlementDate='" + getSettlementDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", regretDate='" + getRegretDate() + "'" +
            ", regretReason='" + getRegretReason() + "'" +
            ", claimedAmount=" + getClaimedAmount() +
            ", settledAmount=" + getSettledAmount() +
            ", claimStatus='" + getClaimStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", insuranceRegistrationId=" + getInsuranceRegistrationId() +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            "}";
    }
}
