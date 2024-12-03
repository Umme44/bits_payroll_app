package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.ArrearSalary} entity.
 */
public class ArrearSalaryDTO implements Serializable {

    private Long id;

    @NotNull
    private Month month;

    @NotNull
    @Min(value = 1990)
    @Max(value = 2099)
    private Integer year;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "9999999")
    private Double amount;

    @ValidateNaturalText
    private String arrearType;

    private Long employeeId;

    private String pin;
    private String fullName;
    private String designationName;
    private String departmentName;
    private String unitName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getArrearType() {
        return arrearType;
    }

    public void setArrearType(String arrearType) {
        this.arrearType = arrearType;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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
        if (!(o instanceof ArrearSalaryDTO)) {
            return false;
        }

        return id != null && id.equals(((ArrearSalaryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArrearSalaryDTO{" +
            "id=" + getId() +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", amount=" + getAmount() +
            ", arrearType='" + getArrearType() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
