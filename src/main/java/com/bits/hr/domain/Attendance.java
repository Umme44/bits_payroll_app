package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Attendance.
 */
@Entity
@Table(name = "attendance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Min(value = 1990)
    @Max(value = 2099)
    @Column(name = "year")
    private Integer year;

    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "month")
    private Integer month;

    @Column(name = "absent_days")
    private Integer absentDays;

    @Column(name = "fraction_days")
    private Integer fractionDays;

    @Column(name = "compensatory_leave_gained")
    private Integer compensatoryLeaveGained;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Attendance id(Long id) {
        this.setId(id);
        return this;
    }

    public Attendance year(Integer year) {
        this.setYear(year);
        return this;
    }

    public Attendance month(Integer month) {
        this.setMonth(month);
        return this;
    }

    public Attendance absentDays(Integer absentDays) {
        this.setAbsentDays(absentDays);
        return this;
    }

    public Attendance fractionDays(Integer fractionDays) {
        this.setFractionDays(fractionDays);
        return this;
    }

    public Attendance compensatoryLeaveGained(Integer compensatoryLeaveGained) {
        this.setCompensatoryLeaveGained(compensatoryLeaveGained);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Attendance employee(Employee employee) {
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
        if (!(o instanceof Attendance)) {
            return false;
        }
        return id != null && id.equals(((Attendance) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attendance{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", month=" + getMonth() +
            ", absentDays=" + getAbsentDays() +
            ", fractionDays=" + getFractionDays() +
            ", compensatoryLeaveGained=" + getCompensatoryLeaveGained() +
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

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getAbsentDays() {
        return this.absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public Integer getFractionDays() {
        return this.fractionDays;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setFractionDays(Integer fractionDays) {
        this.fractionDays = fractionDays;
    }

    public Integer getCompensatoryLeaveGained() {
        return this.compensatoryLeaveGained;
    }

    public void setCompensatoryLeaveGained(Integer compensatoryLeaveGained) {
        this.compensatoryLeaveGained = compensatoryLeaveGained;
    }
}
