package com.bits.hr.service.dto;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.ArrearSalaryItem} entity.
 */
public class ArrearSalaryItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    private String title;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double arrearAmount;

    private Boolean hasPfArrearDeduction;

    private Double pfArrearDeduction;

    private Boolean isFestivalBonus;

    @NotNull
    private Boolean isDeleted;

    private Long arrearSalaryMasterId;

    private String arrearSalaryMasterTitle;

    private Long employeeId;

    private String employeeName;

    private String pin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getArrearAmount() {
        return arrearAmount;
    }

    public void setArrearAmount(Double arrearAmount) {
        this.arrearAmount = arrearAmount;
    }

    public Boolean isHasPfArrearDeduction() {
        return hasPfArrearDeduction;
    }

    public void setHasPfArrearDeduction(Boolean hasPfArrearDeduction) {
        this.hasPfArrearDeduction = hasPfArrearDeduction;
    }

    public Double getPfArrearDeduction() {
        return pfArrearDeduction;
    }

    public void setPfArrearDeduction(Double pfArrearDeduction) {
        this.pfArrearDeduction = pfArrearDeduction;
    }

    public Boolean isIsFestivalBonus() {
        return isFestivalBonus;
    }

    public void setIsFestivalBonus(Boolean isFestivalBonus) {
        this.isFestivalBonus = isFestivalBonus;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getArrearSalaryMasterId() {
        return arrearSalaryMasterId;
    }

    public void setArrearSalaryMasterId(Long arrearSalaryMasterId) {
        this.arrearSalaryMasterId = arrearSalaryMasterId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getHasPfArrearDeduction() {
        return hasPfArrearDeduction;
    }

    public String getArrearSalaryMasterTitle() {
        return arrearSalaryMasterTitle;
    }

    public void setArrearSalaryMasterTitle(String arrearSalaryMasterTitle) {
        this.arrearSalaryMasterTitle = arrearSalaryMasterTitle;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrearSalaryItemDTO)) {
            return false;
        }

        return id != null && id.equals(((ArrearSalaryItemDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArrearSalaryItemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", arrearAmount=" + getArrearAmount() +
            ", hasPfArrearDeduction='" + isHasPfArrearDeduction() + "'" +
            ", pfArrearDeduction=" + getPfArrearDeduction() +
            ", isFestivalBonus='" + isIsFestivalBonus() + "'" +
            ", isDeleted='" + isIsDeleted() + "'" +
            ", arrearSalaryMasterId=" + getArrearSalaryMasterId() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
