package com.bits.hr.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.IndividualArrearSalary} entity.
 */
public class IndividualArrearSalaryDTO implements Serializable {

    private Long id;

    private LocalDate effectiveDate;

    private String existingBand;

    private String newBand;

    private Double existingGross;

    private Double newGross;

    private Double increment;

    private Double arrearSalary;

    private Double arrearPfDeduction;

    private Double taxDeduction;

    private Double netPay;

    private Double pfContribution;

    private String title;

    private String titleEffectiveFrom;

    private String arrearRemarks;

    private Double festivalBonus;

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

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExistingBand() {
        return existingBand;
    }

    public void setExistingBand(String existingBand) {
        this.existingBand = existingBand;
    }

    public String getNewBand() {
        return newBand;
    }

    public void setNewBand(String newBand) {
        this.newBand = newBand;
    }

    public Double getExistingGross() {
        return existingGross;
    }

    public void setExistingGross(Double existingGross) {
        this.existingGross = existingGross;
    }

    public Double getNewGross() {
        return newGross;
    }

    public void setNewGross(Double newGross) {
        this.newGross = newGross;
    }

    public Double getIncrement() {
        return increment;
    }

    public void setIncrement(Double increment) {
        this.increment = increment;
    }

    public Double getArrearSalary() {
        return arrearSalary;
    }

    public void setArrearSalary(Double arrearSalary) {
        this.arrearSalary = arrearSalary;
    }

    public Double getArrearPfDeduction() {
        return arrearPfDeduction;
    }

    public void setArrearPfDeduction(Double arrearPfDeduction) {
        this.arrearPfDeduction = arrearPfDeduction;
    }

    public Double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(Double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public Double getNetPay() {
        return netPay;
    }

    public void setNetPay(Double netPay) {
        this.netPay = netPay;
    }

    public Double getPfContribution() {
        return pfContribution;
    }

    public void setPfContribution(Double pfContribution) {
        this.pfContribution = pfContribution;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getTitleEffectiveFrom() {
        return titleEffectiveFrom;
    }

    public void setTitleEffectiveFrom(String titleEffectiveFrom) {
        this.titleEffectiveFrom = titleEffectiveFrom;
    }

    public String getArrearRemarks() {
        return arrearRemarks;
    }

    public void setArrearRemarks(String arrearRemarks) {
        this.arrearRemarks = arrearRemarks;
    }

    public Double getFestivalBonus() {
        return festivalBonus;
    }

    public void setFestivalBonus(Double festivalBonus) {
        this.festivalBonus = festivalBonus;
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
        if (!(o instanceof IndividualArrearSalaryDTO)) {
            return false;
        }

        return id != null && id.equals(((IndividualArrearSalaryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndividualArrearSalaryDTO{" +
            "id=" + getId() +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", existingBand='" + getExistingBand() + "'" +
            ", newBand='" + getNewBand() + "'" +
            ", existingGross=" + getExistingGross() +
            ", newGross=" + getNewGross() +
            ", increment=" + getIncrement() +
            ", arrearSalary=" + getArrearSalary() +
            ", arrearPfDeduction=" + getArrearPfDeduction() +
            ", taxDeduction=" + getTaxDeduction() +
            ", netPay=" + getNetPay() +
            ", pfContribution=" + getPfContribution() +
            ", title='" + getTitle() + "'" +
            ", titleEffectiveFrom='" + getTitleEffectiveFrom() + "'" +
            ", arrearRemarks='" + getArrearRemarks() + "'" +
            ", festivalBonus=" + getFestivalBonus() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
