package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeePinConfiguration.
 */
@Entity
@Table(name = "employee_pin_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeePinConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_category", nullable = false)
    private EmployeeCategory employeeCategory;

    @NotNull
    @Column(name = "sequence_start", nullable = false, unique = true)
    private String sequenceStart;

    @NotNull
    @Column(name = "sequence_end", nullable = false, unique = true)
    private String sequenceEnd;

    @Column(name = "last_sequence")
    private String lastSequence;

    @Column(name = "has_full_filled")
    private Boolean hasFullFilled;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "last_created_pin")
    private String lastCreatedPin;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmployeePinConfiguration id(Long id) {
        this.setId(id);
        return this;
    }

    public EmployeePinConfiguration employeeCategory(EmployeeCategory employeeCategory) {
        this.setEmployeeCategory(employeeCategory);
        return this;
    }

    public EmployeePinConfiguration sequenceStart(String sequenceStart) {
        this.setSequenceStart(sequenceStart);
        return this;
    }

    public EmployeePinConfiguration sequenceEnd(String sequenceEnd) {
        this.setSequenceEnd(sequenceEnd);
        return this;
    }

    public EmployeePinConfiguration lastSequence(String lastSequence) {
        this.setLastSequence(lastSequence);
        return this;
    }

    public EmployeePinConfiguration hasFullFilled(Boolean hasFullFilled) {
        this.setHasFullFilled(hasFullFilled);
        return this;
    }

    public EmployeePinConfiguration createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public EmployeePinConfiguration updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public EmployeePinConfiguration lastCreatedPin(String lastCreatedPin) {
        this.setLastCreatedPin(lastCreatedPin);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public EmployeePinConfiguration createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public EmployeePinConfiguration updatedBy(User user) {
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
        if (!(o instanceof EmployeePinConfiguration)) {
            return false;
        }
        return id != null && id.equals(((EmployeePinConfiguration) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeePinConfiguration{" +
            "id=" + getId() +
            ", employeeCategory='" + getEmployeeCategory() + "'" +
            ", sequenceStart='" + getSequenceStart() + "'" +
            ", sequenceEnd='" + getSequenceEnd() + "'" +
            ", lastSequence='" + getLastSequence() + "'" +
            ", hasFullFilled='" + getHasFullFilled() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", lastCreatedPin='" + getLastCreatedPin() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeCategory getEmployeeCategory() {
        return this.employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getSequenceStart() {
        return this.sequenceStart;
    }

    public void setSequenceStart(String sequenceStart) {
        this.sequenceStart = sequenceStart;
    }

    public String getSequenceEnd() {
        return this.sequenceEnd;
    }

    public void setSequenceEnd(String sequenceEnd) {
        this.sequenceEnd = sequenceEnd;
    }

    public String getLastSequence() {
        return this.lastSequence;
    }

    public void setLastSequence(String lastSequence) {
        this.lastSequence = lastSequence;
    }

    public Boolean getHasFullFilled() {
        return this.hasFullFilled;
    }

    public void setHasFullFilled(Boolean hasFullFilled) {
        this.hasFullFilled = hasFullFilled;
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

    public String getLastCreatedPin() {
        return this.lastCreatedPin;
    }

    public void setLastCreatedPin(String lastCreatedPin) {
        this.lastCreatedPin = lastCreatedPin;
    }
}
