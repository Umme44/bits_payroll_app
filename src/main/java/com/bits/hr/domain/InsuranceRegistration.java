package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A InsuranceRegistration.
 */
@Entity
@Table(name = "insurance_registration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceRegistration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Size(min = 0, max = 250)
    @Column(name = "name", length = 250)
    private String name;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "photo")
    private String photo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_relation", nullable = false)
    private InsuranceRelation insuranceRelation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_status", nullable = false)
    private InsuranceStatus insuranceStatus;

    @Column(name = "unapproval_reason")
    private String unapprovalReason;

    @NotNull
    @Column(name = "available_balance", nullable = false)
    private Double availableBalance;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "insurance_id", unique = true)
    private String insuranceId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne
    private User approvedBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public InsuranceRegistration id(Long id) {
        this.setId(id);
        return this;
    }

    public InsuranceRegistration name(String name) {
        this.setName(name);
        return this;
    }

    public InsuranceRegistration dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public InsuranceRegistration photo(String photo) {
        this.setPhoto(photo);
        return this;
    }

    public InsuranceRegistration insuranceRelation(InsuranceRelation insuranceRelation) {
        this.setInsuranceRelation(insuranceRelation);
        return this;
    }

    public InsuranceRegistration insuranceStatus(InsuranceStatus insuranceStatus) {
        this.setInsuranceStatus(insuranceStatus);
        return this;
    }

    public InsuranceRegistration unapprovalReason(String unapprovalReason) {
        this.setUnapprovalReason(unapprovalReason);
        return this;
    }

    public InsuranceRegistration availableBalance(Double availableBalance) {
        this.setAvailableBalance(availableBalance);
        return this;
    }

    public InsuranceRegistration updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public InsuranceRegistration approvedAt(Instant approvedAt) {
        this.setApprovedAt(approvedAt);
        return this;
    }

    public InsuranceRegistration insuranceId(String insuranceId) {
        this.setInsuranceId(insuranceId);
        return this;
    }

    public InsuranceRegistration createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public InsuranceRegistration employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public User getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(User user) {
        this.approvedBy = user;
    }

    public InsuranceRegistration approvedBy(User user) {
        this.setApprovedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public InsuranceRegistration updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public InsuranceRegistration createdBy(User user) {
        this.setCreatedBy(user);
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
        if (!(o instanceof InsuranceRegistration)) {
            return false;
        }
        return id != null && id.equals(((InsuranceRegistration) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceRegistration{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", insuranceRelation='" + getInsuranceRelation() + "'" +
            ", insuranceStatus='" + getInsuranceStatus() + "'" +
            ", unapprovalReason='" + getUnapprovalReason() + "'" +
            ", availableBalance=" + getAvailableBalance() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", insuranceId='" + getInsuranceId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public InsuranceRelation getInsuranceRelation() {
        return this.insuranceRelation;
    }

    public void setInsuranceRelation(InsuranceRelation insuranceRelation) {
        this.insuranceRelation = insuranceRelation;
    }

    public InsuranceStatus getInsuranceStatus() {
        return this.insuranceStatus;
    }

    public void setInsuranceStatus(InsuranceStatus insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    public String getUnapprovalReason() {
        return this.unapprovalReason;
    }

    public void setUnapprovalReason(String unapprovalReason) {
        this.unapprovalReason = unapprovalReason;
    }

    public Double getAvailableBalance() {
        return this.availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getApprovedAt() {
        return this.approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getInsuranceId() {
        return this.insuranceId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
