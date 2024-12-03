package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PfLoanApplication.
 */
@Entity
@Table(name = "pf_loan_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PfLoanApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "installment_amount")
    private Double installmentAmount;

    @Column(name = "no_of_installment")
    private Integer noOfInstallment;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "is_recommended")
    private Boolean isRecommended;

    @Column(name = "recommendation_date")
    private LocalDate recommendationDate;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "is_rejected")
    private Boolean isRejected;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "rejection_date")
    private LocalDate rejectionDate;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "disbursement_amount")
    private Double disbursementAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee recommendedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee approvedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee rejectedBy;

    @ManyToOne
    private PfAccount pfAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PfLoanApplication id(Long id) {
        this.setId(id);
        return this;
    }

    public PfLoanApplication installmentAmount(Double installmentAmount) {
        this.setInstallmentAmount(installmentAmount);
        return this;
    }

    public PfLoanApplication noOfInstallment(Integer noOfInstallment) {
        this.setNoOfInstallment(noOfInstallment);
        return this;
    }

    public PfLoanApplication remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public PfLoanApplication isRecommended(Boolean isRecommended) {
        this.setIsRecommended(isRecommended);
        return this;
    }

    public PfLoanApplication recommendationDate(LocalDate recommendationDate) {
        this.setRecommendationDate(recommendationDate);
        return this;
    }

    public PfLoanApplication isApproved(Boolean isApproved) {
        this.setIsApproved(isApproved);
        return this;
    }

    public PfLoanApplication approvalDate(LocalDate approvalDate) {
        this.setApprovalDate(approvalDate);
        return this;
    }

    public PfLoanApplication isRejected(Boolean isRejected) {
        this.setIsRejected(isRejected);
        return this;
    }

    public PfLoanApplication rejectionReason(String rejectionReason) {
        this.setRejectionReason(rejectionReason);
        return this;
    }

    public PfLoanApplication rejectionDate(LocalDate rejectionDate) {
        this.setRejectionDate(rejectionDate);
        return this;
    }

    public PfLoanApplication disbursementDate(LocalDate disbursementDate) {
        this.setDisbursementDate(disbursementDate);
        return this;
    }

    public PfLoanApplication disbursementAmount(Double disbursementAmount) {
        this.setDisbursementAmount(disbursementAmount);
        return this;
    }

    public PfLoanApplication status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Employee getRecommendedBy() {
        return this.recommendedBy;
    }

    public void setRecommendedBy(Employee employee) {
        this.recommendedBy = employee;
    }

    public PfLoanApplication recommendedBy(Employee employee) {
        this.setRecommendedBy(employee);
        return this;
    }

    public Employee getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(Employee employee) {
        this.approvedBy = employee;
    }

    public PfLoanApplication approvedBy(Employee employee) {
        this.setApprovedBy(employee);
        return this;
    }

    public Employee getRejectedBy() {
        return this.rejectedBy;
    }

    public void setRejectedBy(Employee employee) {
        this.rejectedBy = employee;
    }

    public PfLoanApplication rejectedBy(Employee employee) {
        this.setRejectedBy(employee);
        return this;
    }

    public PfAccount getPfAccount() {
        return this.pfAccount;
    }

    public void setPfAccount(PfAccount pfAccount) {
        this.pfAccount = pfAccount;
    }

    public PfLoanApplication pfAccount(PfAccount pfAccount) {
        this.setPfAccount(pfAccount);
        return this;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PfLoanApplication)) {
            return false;
        }
        return id != null && id.equals(((PfLoanApplication) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfLoanApplication{" +
            "id=" + getId() +
            ", installmentAmount=" + getInstallmentAmount() +
            ", noOfInstallment=" + getNoOfInstallment() +
            ", remarks='" + getRemarks() + "'" +
            ", isRecommended='" + getIsRecommended() + "'" +
            ", recommendationDate='" + getRecommendationDate() + "'" +
            ", isApproved='" + getIsApproved() + "'" +
            ", approvalDate='" + getApprovalDate() + "'" +
            ", isRejected='" + getIsRejected() + "'" +
            ", rejectionReason='" + getRejectionReason() + "'" +
            ", rejectionDate='" + getRejectionDate() + "'" +
            ", disbursementDate='" + getDisbursementDate() + "'" +
            ", disbursementAmount=" + getDisbursementAmount() +
            ", status='" + getStatus() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getInstallmentAmount() {
        return this.installmentAmount;
    }

    public void setInstallmentAmount(Double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public Integer getNoOfInstallment() {
        return this.noOfInstallment;
    }

    public void setNoOfInstallment(Integer noOfInstallment) {
        this.noOfInstallment = noOfInstallment;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getIsRecommended() {
        return this.isRecommended;
    }

    public void setIsRecommended(Boolean isRecommended) {
        this.isRecommended = isRecommended;
    }

    public LocalDate getRecommendationDate() {
        return this.recommendationDate;
    }

    public void setRecommendationDate(LocalDate recommendationDate) {
        this.recommendationDate = recommendationDate;
    }

    public Boolean getIsApproved() {
        return this.isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public LocalDate getApprovalDate() {
        return this.approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Boolean getIsRejected() {
        return this.isRejected;
    }

    public void setIsRejected(Boolean isRejected) {
        this.isRejected = isRejected;
    }

    public String getRejectionReason() {
        return this.rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDate getRejectionDate() {
        return this.rejectionDate;
    }

    public void setRejectionDate(LocalDate rejectionDate) {
        this.rejectionDate = rejectionDate;
    }

    public LocalDate getDisbursementDate() {
        return this.disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public Double getDisbursementAmount() {
        return this.disbursementAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setDisbursementAmount(Double disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
