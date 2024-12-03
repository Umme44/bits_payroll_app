package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmployeePinStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.EmployeePin} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeePinDTO implements Serializable {

    private Long id;

    private String pin;

    @NotNull
    private String fullName;

    @NotNull
    private EmployeeCategory employeeCategory;

    @NotNull
    private EmployeePinStatus employeePinStatus;

    private Instant createdAt;

    private Instant updatedAt;

    private Long departmentId;

    private String departmentName;

    private Long designationId;

    private String designationName;

    private Long unitId;

    private String unitName;

    private Long updatedById;

    private String updatedByLogin;

    private Long createdById;

    private String createdByLogin;

    private Long recruitmentRequisitionFormId;
    private String rrfNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public EmployeePinStatus getEmployeePinStatus() {
        return employeePinStatus;
    }

    public void setEmployeePinStatus(EmployeePinStatus employeePinStatus) {
        this.employeePinStatus = employeePinStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long userId) {
        this.updatedById = userId;
    }

    public String getUpdatedByLogin() {
        return updatedByLogin;
    }

    public void setUpdatedByLogin(String userLogin) {
        this.updatedByLogin = userLogin;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getRecruitmentRequisitionFormId() {
        return recruitmentRequisitionFormId;
    }

    public void setRecruitmentRequisitionFormId(Long recruitmentRequisitionFormId) {
        this.recruitmentRequisitionFormId = recruitmentRequisitionFormId;
    }

    public String getRrfNumber() {
        return rrfNumber;
    }

    public void setRrfNumber(String rrfNumber) {
        this.rrfNumber = rrfNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeePinDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeePinDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeePinDTO{" +
            "id=" + getId() +
            ", pin='" + getPin() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", employeeCategory='" + getEmployeeCategory() + "'" +
            ", employeePinStatus='" + getEmployeePinStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", departmentId=" + getDepartmentId() +
            ", designationId=" + getDesignationId() +
            ", unitId=" + getUnitId() +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", recruitmentRequisitionFormId=" + getRecruitmentRequisitionFormId() +
            ", rrfNumber='" + getRrfNumber() + "'" +
            "}";
    }
}
