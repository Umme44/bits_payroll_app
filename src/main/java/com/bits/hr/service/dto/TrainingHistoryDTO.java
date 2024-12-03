package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.TrainingHistory} entity.
 */
public class TrainingHistoryDTO implements Serializable {

    private Long id;

    @NotBlank
    @ValidateNaturalText
    private String trainingName;

    @NotBlank
    @ValidateNaturalText
    private String coordinatedBy;

    @NotNull
    private LocalDate dateOfCompletion;

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

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getCoordinatedBy() {
        return coordinatedBy;
    }

    public void setCoordinatedBy(String coordinatedBy) {
        this.coordinatedBy = coordinatedBy;
    }

    public LocalDate getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(LocalDate dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
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
        if (!(o instanceof TrainingHistoryDTO)) {
            return false;
        }

        return id != null && id.equals(((TrainingHistoryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingHistoryDTO{" +
            "id=" + getId() +
            ", trainingName='" + getTrainingName() + "'" +
            ", coordinatedBy='" + getCoordinatedBy() + "'" +
            ", dateOfCompletion='" + getDateOfCompletion() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
