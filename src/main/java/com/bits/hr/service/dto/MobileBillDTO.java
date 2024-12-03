package com.bits.hr.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.bits.hr.domain.MobileBill} entity.
 */
public class MobileBillDTO implements Serializable {

    private Long id;

    private Integer month;

    private Double amount;

    private Integer year;

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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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
        if (!(o instanceof MobileBillDTO)) {
            return false;
        }

        return id != null && id.equals(((MobileBillDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MobileBillDTO{" +
            "id=" + getId() +
            ", month=" + getMonth() +
            ", amount=" + getAmount() +
            ", year=" + getYear() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
