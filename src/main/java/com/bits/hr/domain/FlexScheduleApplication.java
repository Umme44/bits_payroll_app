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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A FlexScheduleApplication.
 */
@Entity
@Table(name = "flex_schedule_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FlexScheduleApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @NotNull
    @Column(name = "effective_to", nullable = false)
    private LocalDate effectiveTo;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "reason")
    @ValidateNaturalText
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "sanctioned_at")
    private Instant sanctionedAt;

    @NotNull
    @Column(name = "applied_at", nullable = false)
    private LocalDate appliedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee requester;

    @ManyToOne
    private User sanctionedBy;

    @ManyToOne
    private User appliedBy;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private TimeSlot timeSlot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FlexScheduleApplication id(Long id) {
        this.setId(id);
        return this;
    }

    public FlexScheduleApplication effectiveFrom(LocalDate effectiveFrom) {
        this.setEffectiveFrom(effectiveFrom);
        return this;
    }

    public FlexScheduleApplication effectiveTo(LocalDate effectiveTo) {
        this.setEffectiveTo(effectiveTo);
        return this;
    }

    public FlexScheduleApplication reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public FlexScheduleApplication status(Status status) {
        this.setStatus(status);
        return this;
    }

    public FlexScheduleApplication sanctionedAt(Instant sanctionedAt) {
        this.setSanctionedAt(sanctionedAt);
        return this;
    }

    public FlexScheduleApplication appliedAt(LocalDate appliedAt) {
        this.setAppliedAt(appliedAt);
        return this;
    }

    public FlexScheduleApplication createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public FlexScheduleApplication updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Employee getRequester() {
        return this.requester;
    }

    public void setRequester(Employee employee) {
        this.requester = employee;
    }

    public FlexScheduleApplication requester(Employee employee) {
        this.setRequester(employee);
        return this;
    }

    public User getSanctionedBy() {
        return this.sanctionedBy;
    }

    public void setSanctionedBy(User user) {
        this.sanctionedBy = user;
    }

    public FlexScheduleApplication sanctionedBy(User user) {
        this.setSanctionedBy(user);
        return this;
    }

    public User getAppliedBy() {
        return this.appliedBy;
    }

    public void setAppliedBy(User user) {
        this.appliedBy = user;
    }

    public FlexScheduleApplication appliedBy(User user) {
        this.setAppliedBy(user);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public FlexScheduleApplication createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public FlexScheduleApplication updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public TimeSlot getTimeSlot() {
        return this.timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public FlexScheduleApplication timeSlot(TimeSlot timeSlot) {
        this.setTimeSlot(timeSlot);
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
        if (!(o instanceof FlexScheduleApplication)) {
            return false;
        }
        return id != null && id.equals(((FlexScheduleApplication) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexScheduleApplication{" +
            "id=" + getId() +
            ", effectiveFrom='" + getEffectiveFrom() + "'" +
            ", effectiveTo='" + getEffectiveTo() + "'" +
            ", reason='" + getReason() + "'" +
            ", status='" + getStatus() + "'" +
            ", sanctionedAt='" + getSanctionedAt() + "'" +
            ", appliedAt='" + getAppliedAt() + "'" +
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

    public LocalDate getEffectiveFrom() {
        return this.effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return this.effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getSanctionedAt() {
        return this.sanctionedAt;
    }

    public void setSanctionedAt(Instant sanctionedAt) {
        this.sanctionedAt = sanctionedAt;
    }

    public LocalDate getAppliedAt() {
        return this.appliedAt;
    }

    public void setAppliedAt(LocalDate appliedAt) {
        this.appliedAt = appliedAt;
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
