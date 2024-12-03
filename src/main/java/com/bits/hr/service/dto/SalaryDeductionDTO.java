package com.bits.hr.service.dto;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.SalaryDeduction} entity.
 */
public class SalaryDeductionDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000000")
    private Double deductionAmount;

    @NotNull
    @Min(value = 1990)
    @Max(value = 2100)
    private Integer deductionYear;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private Integer deductionMonth;

    private Long deductionTypeId;

    private String deductionTypeName;

    private Long employeeId;

    private String fullName;

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

    public Double getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(Double deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public Integer getDeductionYear() {
        return deductionYear;
    }

    public void setDeductionYear(Integer deductionYear) {
        this.deductionYear = deductionYear;
    }

    public Integer getDeductionMonth() {
        return deductionMonth;
    }

    public void setDeductionMonth(Integer deductionMonth) {
        this.deductionMonth = deductionMonth;
    }

    public Long getDeductionTypeId() {
        return deductionTypeId;
    }

    public void setDeductionTypeId(Long deductionTypeId) {
        this.deductionTypeId = deductionTypeId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getDeductionTypeName() {
        return deductionTypeName;
    }

    public void setDeductionTypeName(String deductionTypeName) {
        this.deductionTypeName = deductionTypeName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        if (!(o instanceof SalaryDeductionDTO)) {
            return false;
        }

        return id != null && id.equals(((SalaryDeductionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryDeductionDTO{" +
            "id=" + getId() +
            ", deductionAmount=" + getDeductionAmount() +
            ", deductionYear=" + getDeductionYear() +
            ", deductionMonth=" + getDeductionMonth() +
            ", deductionTypeId=" + getDeductionTypeId() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
