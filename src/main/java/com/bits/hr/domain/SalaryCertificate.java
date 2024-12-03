package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SalaryCertificate.
 */
@Entity
@Table(name = "salary_certificate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalaryCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 250)
    @Column(name = "purpose", length = 250, nullable = false)
    private String purpose;

    @Size(min = 3, max = 250)
    @Column(name = "remarks", length = 250)
    private String remarks;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "sanction_at")
    private LocalDate sanctionAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = false)
    private Month month;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "reference_number", unique = true)
    private String referenceNumber;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne
    private User sanctionBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "salaryCertificates", allowSetters = true)
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(value = "salaryCertificates", allowSetters = true)
    private Employee signatoryPerson;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SalaryCertificate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public SalaryCertificate purpose(String purpose) {
        this.setPurpose(purpose);
        return this;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public SalaryCertificate remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Status getStatus() {
        return this.status;
    }

    public SalaryCertificate status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public SalaryCertificate createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public SalaryCertificate updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getSanctionAt() {
        return this.sanctionAt;
    }

    public SalaryCertificate sanctionAt(LocalDate sanctionAt) {
        this.setSanctionAt(sanctionAt);
        return this;
    }

    public void setSanctionAt(LocalDate sanctionAt) {
        this.sanctionAt = sanctionAt;
    }

    public Month getMonth() {
        return this.month;
    }

    public SalaryCertificate month(Month month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return this.year;
    }

    public SalaryCertificate year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public SalaryCertificate referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public SalaryCertificate createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public SalaryCertificate updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getSanctionBy() {
        return this.sanctionBy;
    }

    public void setSanctionBy(User user) {
        this.sanctionBy = user;
    }

    public SalaryCertificate sanctionBy(User user) {
        this.setSanctionBy(user);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public SalaryCertificate employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Employee getSignatoryPerson() {
        return this.signatoryPerson;
    }

    public void setSignatoryPerson(Employee employee) {
        this.signatoryPerson = employee;
    }

    public SalaryCertificate signatoryPerson(Employee employee) {
        this.setSignatoryPerson(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalaryCertificate)) {
            return false;
        }
        return id != null && id.equals(((SalaryCertificate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryCertificate{" +
            "id=" + getId() +
            ", purpose='" + getPurpose() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", sanctionAt='" + getSanctionAt() + "'" +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            "}";
    }
}
