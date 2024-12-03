package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

/**
 * A DTO for the {@link com.bits.hr.domain.EducationDetails} entity.
 */
public class EducationDetailsDTO implements Serializable {

    private Long id;

    @NotBlank
    @ValidateNaturalText
    private String nameOfDegree;

    @NotBlank
    @ValidateNaturalText
    private String subject;

    @NotBlank
    @ValidateNaturalText
    private String institute;

    @NotBlank
    @ValidateNaturalText
    private String yearOfDegreeCompletion;

    private Long employeeId;

    private String employeeName;

    private String pin;

    private String designationName;

    private String departmentName;

    private String unitName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameOfDegree() {
        return nameOfDegree;
    }

    public void setNameOfDegree(String nameOfDegree) {
        this.nameOfDegree = nameOfDegree;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getYearOfDegreeCompletion() {
        return yearOfDegreeCompletion;
    }

    public void setYearOfDegreeCompletion(String yearOfDegreeCompletion) {
        this.yearOfDegreeCompletion = yearOfDegreeCompletion;
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
        if (!(o instanceof EducationDetailsDTO)) {
            return false;
        }

        return id != null && id.equals(((EducationDetailsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EducationDetailsDTO{" +
            "id=" + getId() +
            ", nameOfDegree='" + getNameOfDegree() + "'" +
            ", subject='" + getSubject() + "'" +
            ", institute='" + getInstitute() + "'" +
            ", yearOfDegreeCompletion='" + getYearOfDegreeCompletion() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
