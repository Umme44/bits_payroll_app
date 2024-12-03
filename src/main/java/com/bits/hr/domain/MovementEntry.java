package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.MovementType;
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
 * A MovementEntry.
 */
@Entity
@Table(name = "movement_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovementEntry implements Serializable {

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
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Size(min = 3, max = 250)
    @Column(name = "start_note", length = 250, nullable = false)
    private String startNote;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Size(min = 3, max = 250)
    @Column(name = "end_note", length = 250, nullable = false)
    private String endNote;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MovementType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "sanction_at")
    private LocalDate sanctionAt;

    @Size(min = 0, max = 250)
    @Column(name = "note", length = 250)
    @ValidateNaturalText
    private String note;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne
    private User sanctionBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public MovementEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public MovementEntry startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public MovementEntry startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public MovementEntry startNote(String startNote) {
        this.setStartNote(startNote);
        return this;
    }

    public MovementEntry endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public MovementEntry endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public MovementEntry endNote(String endNote) {
        this.setEndNote(endNote);
        return this;
    }

    public MovementEntry type(MovementType type) {
        this.setType(type);
        return this;
    }

    public MovementEntry status(Status status) {
        this.setStatus(status);
        return this;
    }

    public MovementEntry createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public MovementEntry updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public MovementEntry sanctionAt(LocalDate sanctionAt) {
        this.setSanctionAt(sanctionAt);
        return this;
    }

    public MovementEntry note(String note) {
        this.setNote(note);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public MovementEntry employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public MovementEntry createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public MovementEntry updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getSanctionBy() {
        return this.sanctionBy;
    }

    public void setSanctionBy(User user) {
        this.sanctionBy = user;
    }

    public MovementEntry sanctionBy(User user) {
        this.setSanctionBy(user);
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
        if (!(o instanceof MovementEntry)) {
            return false;
        }
        return id != null && id.equals(((MovementEntry) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovementEntry{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", startNote='" + getStartNote() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", endNote='" + getEndNote() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", sanctionAt='" + getSanctionAt() + "'" +
            ", note='" + getNote() + "'" +
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

    public Instant getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getStartNote() {
        return this.startNote;
    }

    public void setStartNote(String startNote) {
        this.startNote = startNote;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getEndNote() {
        return this.endNote;
    }

    public void setEndNote(String endNote) {
        this.endNote = endNote;
    }

    public MovementType getType() {
        return this.type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getSanctionAt() {
        return this.sanctionAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setSanctionAt(LocalDate sanctionAt) {
        this.sanctionAt = sanctionAt;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
