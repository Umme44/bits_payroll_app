package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FlexSchedule.
 */
@Entity
@Table(name = "flex_schedule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FlexSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @NotNull
    @Column(name = "in_time", nullable = false)
    private Instant inTime;

    @NotNull
    @Column(name = "out_time", nullable = false)
    private Instant outTime;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FlexSchedule id(Long id) {
        this.setId(id);
        return this;
    }

    public FlexSchedule effectiveDate(LocalDate effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public FlexSchedule inTime(Instant inTime) {
        this.setInTime(inTime);
        return this;
    }

    public FlexSchedule outTime(Instant outTime) {
        this.setOutTime(outTime);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public FlexSchedule employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public FlexSchedule createdBy(User user) {
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
        if (!(o instanceof FlexSchedule)) {
            return false;
        }
        return id != null && id.equals(((FlexSchedule) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexSchedule{" +
            "id=" + getId() +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", inTime='" + getInTime() + "'" +
            ", outTime='" + getOutTime() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Instant getInTime() {
        return this.inTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public Instant getOutTime() {
        return this.outTime;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }
}
