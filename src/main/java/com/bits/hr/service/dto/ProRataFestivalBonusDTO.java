package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.ProRataFestivalBonus} entity.
 */
public class ProRataFestivalBonusDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private Double amount;

    @ValidateNaturalText
    private String description;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof ProRataFestivalBonusDTO)) {
            return false;
        }

        return id != null && id.equals(((ProRataFestivalBonusDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProRataFestivalBonusDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
