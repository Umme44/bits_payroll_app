package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.ClaimStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InsuranceClaim.
 */
@Entity
@Table(name = "insurance_claim")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceClaim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "regret_date")
    private LocalDate regretDate;

    @Column(name = "regret_reason")
    private String regretReason;

    @Column(name = "claimed_amount")
    private Double claimedAmount;

    @Column(name = "settled_amount")
    private Double settledAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_status")
    private ClaimStatus claimStatus;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "employee", "approvedBy", "updatedBy", "createdBy" }, allowSetters = true)
    private InsuranceRegistration insuranceRegistration;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public InsuranceClaim id(Long id) {
        this.setId(id);
        return this;
    }

    public InsuranceClaim settlementDate(LocalDate settlementDate) {
        this.setSettlementDate(settlementDate);
        return this;
    }

    public InsuranceClaim paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public InsuranceClaim regretDate(LocalDate regretDate) {
        this.setRegretDate(regretDate);
        return this;
    }

    public InsuranceClaim regretReason(String regretReason) {
        this.setRegretReason(regretReason);
        return this;
    }

    public InsuranceClaim claimedAmount(Double claimedAmount) {
        this.setClaimedAmount(claimedAmount);
        return this;
    }

    public InsuranceClaim settledAmount(Double settledAmount) {
        this.setSettledAmount(settledAmount);
        return this;
    }

    public InsuranceClaim claimStatus(ClaimStatus claimStatus) {
        this.setClaimStatus(claimStatus);
        return this;
    }

    public InsuranceClaim createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public InsuranceClaim updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public InsuranceRegistration getInsuranceRegistration() {
        return this.insuranceRegistration;
    }

    public void setInsuranceRegistration(InsuranceRegistration insuranceRegistration) {
        this.insuranceRegistration = insuranceRegistration;
    }

    public InsuranceClaim insuranceRegistration(InsuranceRegistration insuranceRegistration) {
        this.setInsuranceRegistration(insuranceRegistration);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public InsuranceClaim createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public InsuranceClaim updatedBy(User user) {
        this.setUpdatedBy(user);
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
        if (!(o instanceof InsuranceClaim)) {
            return false;
        }
        return id != null && id.equals(((InsuranceClaim) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceClaim{" +
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
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSettlementDate() {
        return this.settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getRegretDate() {
        return this.regretDate;
    }

    public void setRegretDate(LocalDate regretDate) {
        this.regretDate = regretDate;
    }

    public String getRegretReason() {
        return this.regretReason;
    }

    public void setRegretReason(String regretReason) {
        this.regretReason = regretReason;
    }

    public Double getClaimedAmount() {
        return this.claimedAmount;
    }

    public void setClaimedAmount(Double claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public Double getSettledAmount() {
        return this.settledAmount;
    }

    public void setSettledAmount(Double settledAmount) {
        this.settledAmount = settledAmount;
    }

    public ClaimStatus getClaimStatus() {
        return this.claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
