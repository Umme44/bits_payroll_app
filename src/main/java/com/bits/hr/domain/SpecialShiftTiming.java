package com.bits.hr.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SpecialShiftTiming.
 */
@Entity
@Table(name = "special_shift_timing")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialShiftTiming implements Serializable {

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
    @Column(name = "override_roaster", nullable = false)
    private Boolean overrideRoaster;

    @NotNull
    @Column(name = "override_flex_schedule", nullable = false)
    private Boolean overrideFlexSchedule;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 250)
    @Column(name = "reason", length = 250)
    private String reason;

    @ManyToOne(optional = false)
    @NotNull
    private TimeSlot timeSlot;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SpecialShiftTiming id(Long id) {
        this.setId(id);
        return this;
    }

    public SpecialShiftTiming startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public SpecialShiftTiming endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public SpecialShiftTiming overrideRoaster(Boolean overrideRoaster) {
        this.setOverrideRoaster(overrideRoaster);
        return this;
    }

    public SpecialShiftTiming overrideFlexSchedule(Boolean overrideFlexSchedule) {
        this.setOverrideFlexSchedule(overrideFlexSchedule);
        return this;
    }

    public SpecialShiftTiming remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public SpecialShiftTiming createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public SpecialShiftTiming updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public SpecialShiftTiming reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public TimeSlot getTimeSlot() {
        return this.timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public SpecialShiftTiming timeSlot(TimeSlot timeSlot) {
        this.setTimeSlot(timeSlot);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public SpecialShiftTiming createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public SpecialShiftTiming updatedBy(User user) {
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
        if (!(o instanceof SpecialShiftTiming)) {
            return false;
        }
        return id != null && id.equals(((SpecialShiftTiming) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialShiftTiming{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", overrideRoaster='" + getOverrideRoaster() + "'" +
            ", overrideFlexSchedule='" + getOverrideFlexSchedule() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", reason='" + getReason() + "'" +
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

    public Boolean getOverrideRoaster() {
        return this.overrideRoaster;
    }

    public void setOverrideRoaster(Boolean overrideRoaster) {
        this.overrideRoaster = overrideRoaster;
    }

    public Boolean getOverrideFlexSchedule() {
        return this.overrideFlexSchedule;
    }

    public void setOverrideFlexSchedule(Boolean overrideFlexSchedule) {
        this.overrideFlexSchedule = overrideFlexSchedule;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
