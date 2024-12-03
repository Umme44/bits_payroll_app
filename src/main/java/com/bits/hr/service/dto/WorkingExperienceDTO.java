package com.bits.hr.service.dto;

import com.bits.hr.service.dtoValidationCustom.ValidDate;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.WorkingExperience} entity.
 */
@ValidDate(start = "dojOfLastOrganization", end = "dorOfLastOrganization")
public class WorkingExperienceDTO implements Serializable {

    private Long id;

    @NotBlank
    @ValidateNaturalText
    private String lastDesignation;

    @NotBlank
    @ValidateNaturalText
    private String organizationName;

    @NotNull
    private LocalDate dojOfLastOrganization;

    @NotNull
    private LocalDate dorOfLastOrganization;

    private String employeeName;

    private String pin;

    private String designationName;

    private String departmentName;

    private String unitName;

    private Long employeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastDesignation() {
        return lastDesignation;
    }

    public void setLastDesignation(String lastDesignation) {
        this.lastDesignation = lastDesignation;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public LocalDate getDojOfLastOrganization() {
        return dojOfLastOrganization;
    }

    public void setDojOfLastOrganization(LocalDate dojOfLastOrganization) {
        this.dojOfLastOrganization = dojOfLastOrganization;
    }

    public LocalDate getDorOfLastOrganization() {
        return dorOfLastOrganization;
    }

    public void setDorOfLastOrganization(LocalDate dorOfLastOrganization) {
        this.dorOfLastOrganization = dorOfLastOrganization;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkingExperienceDTO)) {
            return false;
        }

        return id != null && id.equals(((WorkingExperienceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkingExperienceDTO{" +
            "id=" + getId() +
            ", lastDesignation='" + getLastDesignation() + "'" +
            ", organizationName='" + getOrganizationName() + "'" +
            ", dojOfLastOrganization='" + getDojOfLastOrganization() + "'" +
            ", dorOfLastOrganization='" + getDorOfLastOrganization() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
