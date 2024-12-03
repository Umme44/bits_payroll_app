package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkFromHomeApplication.
 */
@Entity
@Table(name = "work_from_home_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkFromHomeApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Size(min = 0, max = 250)
    @Column(name = "reason", length = 250, nullable = false)
    @ValidateNaturalText
    private String reason;

    @Column(name = "duration")
    private Integer duration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "applied_at")
    private LocalDate appliedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "sanctioned_at")
    private Instant sanctionedAt;

    @ManyToOne
    private User appliedBy;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne
    private User sanctionedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public WorkFromHomeApplication id(Long id) {
        this.setId(id);
        return this;
    }

    public WorkFromHomeApplication startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public WorkFromHomeApplication endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public WorkFromHomeApplication reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public WorkFromHomeApplication duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public WorkFromHomeApplication status(Status status) {
        this.setStatus(status);
        return this;
    }

    public WorkFromHomeApplication appliedAt(LocalDate appliedAt) {
        this.setAppliedAt(appliedAt);
        return this;
    }

    public WorkFromHomeApplication updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public WorkFromHomeApplication createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public WorkFromHomeApplication sanctionedAt(Instant sanctionedAt) {
        this.setSanctionedAt(sanctionedAt);
        return this;
    }

    public User getAppliedBy() {
        return this.appliedBy;
    }

    public void setAppliedBy(User user) {
        this.appliedBy = user;
    }

    public WorkFromHomeApplication appliedBy(User user) {
        this.setAppliedBy(user);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public WorkFromHomeApplication createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public WorkFromHomeApplication updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getSanctionedBy() {
        return this.sanctionedBy;
    }

    public void setSanctionedBy(User user) {
        this.sanctionedBy = user;
    }

    public WorkFromHomeApplication sanctionedBy(User user) {
        this.setSanctionedBy(user);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public WorkFromHomeApplication employee(Employee employee) {
        this.setEmployee(employee);
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
        if (!(o instanceof WorkFromHomeApplication)) {
            return false;
        }
        return id != null && id.equals(((WorkFromHomeApplication) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkFromHomeApplication{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", duration=" + getDuration() +
            ", status='" + getStatus() + "'" +
            ", appliedAt='" + getAppliedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", sanctionedAt='" + getSanctionedAt() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getAppliedAt() {
        return this.appliedAt;
    }

    public void setAppliedAt(LocalDate appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getSanctionedAt() {
        return this.sanctionedAt;
    }

    public void setSanctionedAt(Instant sanctionedAt) {
        this.sanctionedAt = sanctionedAt;
    }
}
