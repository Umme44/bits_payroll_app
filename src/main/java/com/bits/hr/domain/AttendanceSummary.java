package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AttendanceSummary.
 */
@Entity
@Table(name = "attendance_summary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttendanceSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "total_working_days")
    private Integer totalWorkingDays;

    @Column(name = "total_leave_days")
    private Integer totalLeaveDays;

    @Column(name = "total_absent_days")
    private Integer totalAbsentDays;

    @Column(name = "total_fraction_days")
    private Integer totalFractionDays;

    @Column(name = "attendance_regularisation_start_date")
    private LocalDate attendanceRegularisationStartDate;

    @Column(name = "attendance_regularisation_end_date")
    private LocalDate attendanceRegularisationEndDate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public AttendanceSummary id(Long id) {
        this.setId(id);
        return this;
    }

    public AttendanceSummary month(Integer month) {
        this.setMonth(month);
        return this;
    }

    public AttendanceSummary year(Integer year) {
        this.setYear(year);
        return this;
    }

    public AttendanceSummary totalWorkingDays(Integer totalWorkingDays) {
        this.setTotalWorkingDays(totalWorkingDays);
        return this;
    }

    public AttendanceSummary totalLeaveDays(Integer totalLeaveDays) {
        this.setTotalLeaveDays(totalLeaveDays);
        return this;
    }

    public AttendanceSummary totalAbsentDays(Integer totalAbsentDays) {
        this.setTotalAbsentDays(totalAbsentDays);
        return this;
    }

    public AttendanceSummary totalFractionDays(Integer totalFractionDays) {
        this.setTotalFractionDays(totalFractionDays);
        return this;
    }

    public AttendanceSummary attendanceRegularisationStartDate(LocalDate attendanceRegularisationStartDate) {
        this.setAttendanceRegularisationStartDate(attendanceRegularisationStartDate);
        return this;
    }

    public AttendanceSummary attendanceRegularisationEndDate(LocalDate attendanceRegularisationEndDate) {
        this.setAttendanceRegularisationEndDate(attendanceRegularisationEndDate);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public AttendanceSummary employee(Employee employee) {
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
        if (!(o instanceof AttendanceSummary)) {
            return false;
        }
        return id != null && id.equals(((AttendanceSummary) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttendanceSummary{" +
            "id=" + getId() +
            ", month=" + getMonth() +
            ", year=" + getYear() +
            ", totalWorkingDays=" + getTotalWorkingDays() +
            ", totalLeaveDays=" + getTotalLeaveDays() +
            ", totalAbsentDays=" + getTotalAbsentDays() +
            ", totalFractionDays=" + getTotalFractionDays() +
            ", attendanceRegularisationStartDate='" + getAttendanceRegularisationStartDate() + "'" +
            ", attendanceRegularisationEndDate='" + getAttendanceRegularisationEndDate() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTotalWorkingDays() {
        return this.totalWorkingDays;
    }

    public void setTotalWorkingDays(Integer totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
    }

    public Integer getTotalLeaveDays() {
        return this.totalLeaveDays;
    }

    public void setTotalLeaveDays(Integer totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }

    public Integer getTotalAbsentDays() {
        return this.totalAbsentDays;
    }

    public void setTotalAbsentDays(Integer totalAbsentDays) {
        this.totalAbsentDays = totalAbsentDays;
    }

    public Integer getTotalFractionDays() {
        return this.totalFractionDays;
    }

    public void setTotalFractionDays(Integer totalFractionDays) {
        this.totalFractionDays = totalFractionDays;
    }

    public LocalDate getAttendanceRegularisationStartDate() {
        return this.attendanceRegularisationStartDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setAttendanceRegularisationStartDate(LocalDate attendanceRegularisationStartDate) {
        this.attendanceRegularisationStartDate = attendanceRegularisationStartDate;
    }

    public LocalDate getAttendanceRegularisationEndDate() {
        return this.attendanceRegularisationEndDate;
    }

    public void setAttendanceRegularisationEndDate(LocalDate attendanceRegularisationEndDate) {
        this.attendanceRegularisationEndDate = attendanceRegularisationEndDate;
    }
}
