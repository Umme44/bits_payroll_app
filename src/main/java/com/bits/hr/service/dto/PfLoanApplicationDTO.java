package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.PfLoanApplication} entity.
 */
public class PfLoanApplicationDTO implements Serializable {

    private Long id;

    private Double installmentAmount;

    private Integer noOfInstallment;

    @ValidateNaturalText
    private String remarks;

    private Boolean isRecommended;

    private LocalDate recommendationDate;

    private Boolean isApproved;

    private LocalDate approvalDate;

    private Boolean isRejected;

    private String rejectionReason;

    private LocalDate rejectionDate;

    private LocalDate disbursementDate;

    private Double disbursementAmount;

    private Status status;

    private Long recommendedById;

    private Long approvedById;

    private String approvedByFullName;

    private Long rejectedById;

    private String rejectedByFullName;

    private Long pfAccountId;

    private String pfCode;

    private PfAccountStatus accountStatus;

    private String designationName;

    private String departmentName;

    private String unitName;

    private String accHolderName;

    private String pin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public Integer getNoOfInstallment() {
        return noOfInstallment;
    }

    public void setNoOfInstallment(Integer noOfInstallment) {
        this.noOfInstallment = noOfInstallment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean isIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Boolean isRecommended) {
        this.isRecommended = isRecommended;
    }

    public LocalDate getRecommendationDate() {
        return recommendationDate;
    }

    public void setRecommendationDate(LocalDate recommendationDate) {
        this.recommendationDate = recommendationDate;
    }

    public Boolean isIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Boolean isIsRejected() {
        return isRejected;
    }

    public void setIsRejected(Boolean isRejected) {
        this.isRejected = isRejected;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDate getRejectionDate() {
        return rejectionDate;
    }

    public void setRejectionDate(LocalDate rejectionDate) {
        this.rejectionDate = rejectionDate;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public Double getDisbursementAmount() {
        return disbursementAmount;
    }

    public void setDisbursementAmount(Double disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getRecommendedById() {
        return recommendedById;
    }

    public void setRecommendedById(Long employeeId) {
        this.recommendedById = employeeId;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long employeeId) {
        this.approvedById = employeeId;
    }

    public String getApprovedByFullName() {
        return approvedByFullName;
    }

    public void setApprovedByFullName(String approvedByFullName) {
        this.approvedByFullName = approvedByFullName;
    }

    public Long getRejectedById() {
        return rejectedById;
    }

    public void setRejectedById(Long employeeId) {
        this.rejectedById = employeeId;
    }

    public String getRejectedByFullName() {
        return rejectedByFullName;
    }

    public void setRejectedByFullName(String rejectedByFullName) {
        this.rejectedByFullName = rejectedByFullName;
    }

    public Long getPfAccountId() {
        return pfAccountId;
    }

    public void setPfAccountId(Long pfAccountId) {
        this.pfAccountId = pfAccountId;
    }

    public String getPfCode() {
        return pfCode;
    }

    public void setPfCode(String pfCode) {
        this.pfCode = pfCode;
    }

    public PfAccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(PfAccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAccHolderName() {
        return accHolderName;
    }

    public void setAccHolderName(String accHolderName) {
        this.accHolderName = accHolderName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PfLoanApplicationDTO)) {
            return false;
        }

        return id != null && id.equals(((PfLoanApplicationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfLoanApplicationDTO{" +
            "id=" + getId() +
            ", installmentAmount=" + getInstallmentAmount() +
            ", noOfInstallment=" + getNoOfInstallment() +
            ", remarks='" + getRemarks() + "'" +
            ", isRecommended='" + isIsRecommended() + "'" +
            ", recommendationDate='" + getRecommendationDate() + "'" +
            ", isApproved='" + isIsApproved() + "'" +
            ", approvalDate='" + getApprovalDate() + "'" +
            ", isRejected='" + isIsRejected() + "'" +
            ", rejectionReason='" + getRejectionReason() + "'" +
            ", rejectionDate='" + getRejectionDate() + "'" +
            ", disbursementDate='" + getDisbursementDate() + "'" +
            ", disbursementAmount=" + getDisbursementAmount() +
            ", status='" + getStatus() + "'" +
            ", recommendedById=" + getRecommendedById() +
            ", approvedById=" + getApprovedById() +
            ", rejectedById=" + getRejectedById() +
            ", pfAccountId=" + getPfAccountId() +
            "}";
    }
}
