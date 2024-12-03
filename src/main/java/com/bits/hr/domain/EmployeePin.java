package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmployeePinStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeePin.
 */
@Entity
@Table(name = "employee_pin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeePin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "pin", unique = true)
    private String pin;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_category", nullable = false)
    private EmployeeCategory employeeCategory;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_pin_status", nullable = false)
    private EmployeePinStatus employeePinStatus;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "departmentHead" }, allowSetters = true)
    private Department department;

    @ManyToOne(optional = false)
    @NotNull
    private Designation designation;

    @ManyToOne(optional = false)
    @NotNull
    private Unit unit;

    @ManyToOne
    private User updatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "employeePins", allowSetters = true)
    private RecruitmentRequisitionForm recruitmentRequisitionForm;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmployeePin id(Long id) {
        this.setId(id);
        return this;
    }

    public EmployeePin pin(String pin) {
        this.setPin(pin);
        return this;
    }

    public EmployeePin fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public EmployeePin employeeCategory(EmployeeCategory employeeCategory) {
        this.setEmployeeCategory(employeeCategory);
        return this;
    }

    public EmployeePin employeePinStatus(EmployeePinStatus employeePinStatus) {
        this.setEmployeePinStatus(employeePinStatus);
        return this;
    }

    public EmployeePin createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public EmployeePin updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public EmployeePin department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Designation getDesignation() {
        return this.designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public EmployeePin designation(Designation designation) {
        this.setDesignation(designation);
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public EmployeePin unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public EmployeePin updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public EmployeePin createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public RecruitmentRequisitionForm getRecruitmentRequisitionForm() {
        return this.recruitmentRequisitionForm;
    }

    public void setRecruitmentRequisitionForm(RecruitmentRequisitionForm recruitmentRequisitionForm) {
        this.recruitmentRequisitionForm = recruitmentRequisitionForm;
    }

    public EmployeePin recruitmentRequisitionForm(RecruitmentRequisitionForm recruitmentRequisitionForm) {
        this.setRecruitmentRequisitionForm(recruitmentRequisitionForm);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeePin)) {
            return false;
        }
        return id != null && id.equals(((EmployeePin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeePin{" +
            "id=" + getId() +
            ", pin='" + getPin() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", employeeCategory='" + getEmployeeCategory() + "'" +
            ", employeePinStatus='" + getEmployeePinStatus() + "'" +
            ", recruitmentRequisitionForm='" + getRecruitmentRequisitionForm() + "'" +
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

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public EmployeeCategory getEmployeeCategory() {
        return this.employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public EmployeePinStatus getEmployeePinStatus() {
        return this.employeePinStatus;
    }

    public void setEmployeePinStatus(EmployeePinStatus employeePinStatus) {
        this.employeePinStatus = employeePinStatus;
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
