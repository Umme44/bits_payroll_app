package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeeResignation.
 */
@Entity
@Table(name = "employee_resignation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeResignation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "resignation_date")
    private LocalDate resignationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private Status approvalStatus;

    @Column(name = "approval_comment")
    private String approvalComment;

    @Column(name = "is_salary_hold")
    private Boolean isSalaryHold;

    @Column(name = "is_festival_bonus_hold")
    private Boolean isFestivalBonusHold;

    @Column(name = "resignation_reason")
    private String resignationReason;

    @NotNull
    @Column(name = "last_working_day", nullable = false)
    private LocalDate lastWorkingDay;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee createdBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee uodatedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmployeeResignation id(Long id) {
        this.setId(id);
        return this;
    }

    public EmployeeResignation createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public EmployeeResignation updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public EmployeeResignation resignationDate(LocalDate resignationDate) {
        this.setResignationDate(resignationDate);
        return this;
    }

    public EmployeeResignation approvalStatus(Status approvalStatus) {
        this.setApprovalStatus(approvalStatus);
        return this;
    }

    public EmployeeResignation approvalComment(String approvalComment) {
        this.setApprovalComment(approvalComment);
        return this;
    }

    public EmployeeResignation isSalaryHold(Boolean isSalaryHold) {
        this.setIsSalaryHold(isSalaryHold);
        return this;
    }

    public EmployeeResignation isFestivalBonusHold(Boolean isFestivalBonusHold) {
        this.setIsFestivalBonusHold(isFestivalBonusHold);
        return this;
    }

    public EmployeeResignation resignationReason(String resignationReason) {
        this.setResignationReason(resignationReason);
        return this;
    }

    public EmployeeResignation lastWorkingDay(LocalDate lastWorkingDay) {
        this.setLastWorkingDay(lastWorkingDay);
        return this;
    }

    public Employee getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Employee employee) {
        this.createdBy = employee;
    }

    public EmployeeResignation createdBy(Employee employee) {
        this.setCreatedBy(employee);
        return this;
    }

    public Employee getUodatedBy() {
        return this.uodatedBy;
    }

    public void setUodatedBy(Employee employee) {
        this.uodatedBy = employee;
    }

    public EmployeeResignation uodatedBy(Employee employee) {
        this.setUodatedBy(employee);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeResignation employee(Employee employee) {
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
        if (!(o instanceof EmployeeResignation)) {
            return false;
        }
        return id != null && id.equals(((EmployeeResignation) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeResignation{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", resignationDate='" + getResignationDate() + "'" +
            ", approvalStatus='" + getApprovalStatus() + "'" +
            ", approvalComment='" + getApprovalComment() + "'" +
            ", isSalaryHold='" + getIsSalaryHold() + "'" +
            ", isFestivalBonusHold='" + getIsFestivalBonusHold() + "'" +
            ", resignationReason='" + getResignationReason() + "'" +
            ", lastWorkingDay='" + getLastWorkingDay() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getResignationDate() {
        return this.resignationDate;
    }

    public void setResignationDate(LocalDate resignationDate) {
        this.resignationDate = resignationDate;
    }

    public Status getApprovalStatus() {
        return this.approvalStatus;
    }

    public void setApprovalStatus(Status approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalComment() {
        return this.approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }

    public Boolean getIsSalaryHold() {
        return this.isSalaryHold;
    }

    public void setIsSalaryHold(Boolean isSalaryHold) {
        this.isSalaryHold = isSalaryHold;
    }

    public Boolean getIsFestivalBonusHold() {
        return this.isFestivalBonusHold;
    }

    public void setIsFestivalBonusHold(Boolean isFestivalBonusHold) {
        this.isFestivalBonusHold = isFestivalBonusHold;
    }

    public String getResignationReason() {
        return this.resignationReason;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setResignationReason(String resignationReason) {
        this.resignationReason = resignationReason;
    }

    public LocalDate getLastWorkingDay() {
        return this.lastWorkingDay;
    }

    public void setLastWorkingDay(LocalDate lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }
}
