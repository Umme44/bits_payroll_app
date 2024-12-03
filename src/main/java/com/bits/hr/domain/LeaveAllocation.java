package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.LeaveType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaveAllocation.
 */
@Entity
@Table(name = "leave_allocation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2199)
    @Column(name = "year", nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type")
    private LeaveType leaveType;

    @NotNull
    @Min(value = 0)
    @Column(name = "allocated_days", nullable = false)
    private Integer allocatedDays;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public LeaveAllocation id(Long id) {
        this.setId(id);
        return this;
    }

    public LeaveAllocation year(Integer year) {
        this.setYear(year);
        return this;
    }

    public LeaveAllocation leaveType(LeaveType leaveType) {
        this.setLeaveType(leaveType);
        return this;
    }

    public LeaveAllocation allocatedDays(Integer allocatedDays) {
        this.setAllocatedDays(allocatedDays);
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
        if (!(o instanceof LeaveAllocation)) {
            return false;
        }
        return id != null && id.equals(((LeaveAllocation) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveAllocation{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", leaveType='" + getLeaveType() + "'" +
            ", allocatedDays=" + getAllocatedDays() +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getAllocatedDays() {
        return this.allocatedDays;
    }

    public void setAllocatedDays(Integer allocatedDays) {
        this.allocatedDays = allocatedDays;
    }
}
