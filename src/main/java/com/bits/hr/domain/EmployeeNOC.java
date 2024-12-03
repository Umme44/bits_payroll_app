package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.PurposeOfNOC;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeeNOC.
 */
@Entity
@Table(name = "employeenoc")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeNOC implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

    @NotNull
    @Column(name = "leave_start_date", nullable = false)
    private LocalDate leaveStartDate;

    @NotNull
    @Column(name = "leave_end_date", nullable = false)
    private LocalDate leaveEndDate;

    @NotNull
    @Column(name = "country_to_visit", nullable = false)
    private String countryToVisit;

    @Column(name = "reference_number", unique = true)
    private String referenceNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "generated_at")
    private Instant generatedAt;

    @Column(name = "reason")
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "purpose_of_noc", nullable = false)
    private PurposeOfNOC purposeOfNOC;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "certificate_status", nullable = false)
    private CertificateStatus certificateStatus;

    @Column(name = "is_required_for_visa")
    private Boolean isRequiredForVisa;

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

    public EmployeeNOC id(Long id) {
        this.setId(id);
        return this;
    }

    public EmployeeNOC passportNumber(String passportNumber) {
        this.setPassportNumber(passportNumber);
        return this;
    }

    public EmployeeNOC leaveStartDate(LocalDate leaveStartDate) {
        this.setLeaveStartDate(leaveStartDate);
        return this;
    }

    public EmployeeNOC leaveEndDate(LocalDate leaveEndDate) {
        this.setLeaveEndDate(leaveEndDate);
        return this;
    }

    public EmployeeNOC countryToVisit(String countryToVisit) {
        this.setCountryToVisit(countryToVisit);
        return this;
    }

    public EmployeeNOC referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public EmployeeNOC issueDate(LocalDate issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public EmployeeNOC createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public EmployeeNOC updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public EmployeeNOC generatedAt(Instant generatedAt) {
        this.setGeneratedAt(generatedAt);
        return this;
    }

    public EmployeeNOC reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public EmployeeNOC purposeOfNOC(PurposeOfNOC purposeOfNOC) {
        this.setPurposeOfNOC(purposeOfNOC);
        return this;
    }

    public EmployeeNOC certificateStatus(CertificateStatus certificateStatus) {
        this.setCertificateStatus(certificateStatus);
        return this;
    }

    public EmployeeNOC isRequiredForVisa(Boolean isRequiredForVisa) {
        this.setIsRequiredForVisa(isRequiredForVisa);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeNOC employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Employee getSignatoryPerson() {
        return this.signatoryPerson;
    }

    public void setSignatoryPerson(Employee employee) {
        this.signatoryPerson = employee;
    }

    public EmployeeNOC signatoryPerson(Employee employee) {
        this.setSignatoryPerson(employee);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public EmployeeNOC createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public EmployeeNOC updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getGeneratedBy() {
        return this.generatedBy;
    }

    public void setGeneratedBy(User user) {
        this.generatedBy = user;
    }

    public EmployeeNOC generatedBy(User user) {
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
        if (!(o instanceof EmployeeNOC)) {
            return false;
        }
        return id != null && id.equals(((EmployeeNOC) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeNOC{" +
            "id=" + getId() +
            ", passportNumber='" + getPassportNumber() + "'" +
            ", leaveStartDate='" + getLeaveStartDate() + "'" +
            ", leaveEndDate='" + getLeaveEndDate() + "'" +
            ", countryToVisit='" + getCountryToVisit() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", generatedAt='" + getGeneratedAt() + "'" +
            ", reason='" + getReason() + "'" +
            ", purposeOfNOC='" + getPurposeOfNOC() + "'" +
            ", certificateStatus='" + getCertificateStatus() + "'" +
            ", isRequiredForVisa='" + getIsRequiredForVisa() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassportNumber() {
        return this.passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public LocalDate getLeaveStartDate() {
        return this.leaveStartDate;
    }

    public void setLeaveStartDate(LocalDate leaveStartDate) {
        this.leaveStartDate = leaveStartDate;
    }

    public LocalDate getLeaveEndDate() {
        return this.leaveEndDate;
    }

    public void setLeaveEndDate(LocalDate leaveEndDate) {
        this.leaveEndDate = leaveEndDate;
    }

    public String getCountryToVisit() {
        return this.countryToVisit;
    }

    public void setCountryToVisit(String countryToVisit) {
        this.countryToVisit = countryToVisit;
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

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getGeneratedAt() {
        return this.generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PurposeOfNOC getPurposeOfNOC() {
        return this.purposeOfNOC;
    }

    public void setPurposeOfNOC(PurposeOfNOC purposeOfNOC) {
        this.purposeOfNOC = purposeOfNOC;
    }

    public CertificateStatus getCertificateStatus() {
        return this.certificateStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCertificateStatus(CertificateStatus certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public Boolean getIsRequiredForVisa() {
        return this.isRequiredForVisa;
    }

    public void setIsRequiredForVisa(Boolean isRequiredForVisa) {
        this.isRequiredForVisa = isRequiredForVisa;
    }
}
