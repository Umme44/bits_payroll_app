package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.CertificateStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmploymentCertificate.
 */
@Entity
@Table(name = "employment_certificate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmploymentCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "certificate_status", nullable = false)
    private CertificateStatus certificateStatus;

    @Column(name = "reference_number", unique = true)
    private String referenceNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "reason")
    private String reason;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "generated_at")
    private Instant generatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee signatoryPerson;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne
    private User generatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmploymentCertificate id(Long id) {
        this.setId(id);
        return this;
    }

    public EmploymentCertificate certificateStatus(CertificateStatus certificateStatus) {
        this.setCertificateStatus(certificateStatus);
        return this;
    }

    public EmploymentCertificate referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public EmploymentCertificate issueDate(LocalDate issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public EmploymentCertificate reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public EmploymentCertificate createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public EmploymentCertificate updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public EmploymentCertificate generatedAt(Instant generatedAt) {
        this.setGeneratedAt(generatedAt);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmploymentCertificate employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Employee getSignatoryPerson() {
        return this.signatoryPerson;
    }

    public void setSignatoryPerson(Employee employee) {
        this.signatoryPerson = employee;
    }

    public EmploymentCertificate signatoryPerson(Employee employee) {
        this.setSignatoryPerson(employee);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public EmploymentCertificate createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public EmploymentCertificate updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getGeneratedBy() {
        return this.generatedBy;
    }

    public void setGeneratedBy(User user) {
        this.generatedBy = user;
    }

    public EmploymentCertificate generatedBy(User user) {
        this.setGeneratedBy(user);
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
        if (!(o instanceof EmploymentCertificate)) {
            return false;
        }
        return id != null && id.equals(((EmploymentCertificate) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmploymentCertificate{" +
            "id=" + getId() +
            ", certificateStatus='" + getCertificateStatus() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", generatedAt='" + getGeneratedAt() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CertificateStatus getCertificateStatus() {
        return this.certificateStatus;
    }

    public void setCertificateStatus(CertificateStatus certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getIssueDate() {
        return this.issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getGeneratedAt() {
        return this.generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }
}
