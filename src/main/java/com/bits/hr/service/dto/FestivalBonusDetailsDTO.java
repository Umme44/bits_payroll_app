package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.FestivalBonusDetails} entity.
 */
public class FestivalBonusDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private Double bonusAmount;

    @Size(min = 0, max = 255)
    private String remarks;

    @NotNull
    private Boolean isHold;

    private Long employeeId;

    private Long festivalId;

    private String fullName;
    private String pin;
    private String designationName;
    private String departmentName;
    private String unitName;
    private String bandName;
    private LocalDate doj;
    private LocalDate doc;

    private LocalDate contractPeriodEndDate;
    private LocalDate contractPeriodExtendedTo;

    private EmployeeCategory employeeCategory;
    private double gross;

    private double basic;

    private String title;
    private String festivalName;
    private LocalDate festivalDate;
    private LocalDate bonusDisbursementDate;

    private int yearlyFestivalOrderNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean isIsHold() {
        return isHold;
    }

    public void setIsHold(Boolean isHold) {
        this.isHold = isHold;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(Long festivalId) {
        this.festivalId = festivalId;
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

    public LocalDate getDoj() {
        return doj;
    }

    public void setDoj(LocalDate doj) {
        this.doj = doj;
    }

    public LocalDate getDoc() {
        return doc;
    }

    public void setDoc(LocalDate doc) {
        this.doc = doc;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public LocalDate getContractPeriodEndDate() {
        return contractPeriodEndDate;
    }

    public void setContractPeriodEndDate(LocalDate contractPeriodEndDate) {
        this.contractPeriodEndDate = contractPeriodEndDate;
    }

    public LocalDate getContractPeriodExtendedTo() {
        return contractPeriodExtendedTo;
    }

    public void setContractPeriodExtendedTo(LocalDate contractPeriodExtendedTo) {
        this.contractPeriodExtendedTo = contractPeriodExtendedTo;
    }

    public double getGross() {
        return gross;
    }

    public void setGross(double gross) {
        this.gross = gross;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public LocalDate getFestivalDate() {
        return festivalDate;
    }

    public void setFestivalDate(LocalDate festivalDate) {
        this.festivalDate = festivalDate;
    }

    public LocalDate getBonusDisbursementDate() {
        return bonusDisbursementDate;
    }

    public void setBonusDisbursementDate(LocalDate bonusDisbursementDate) {
        this.bonusDisbursementDate = bonusDisbursementDate;
    }

    public int getYearlyFestivalOrderNo() {
        return yearlyFestivalOrderNo;
    }

    public void setYearlyFestivalOrderNo(int yearlyFestivalOrderNo) {
        this.yearlyFestivalOrderNo = yearlyFestivalOrderNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FestivalBonusDetailsDTO)) {
            return false;
        }

        return id != null && id.equals(((FestivalBonusDetailsDTO) o).id);
    }

    public double getBasic() {
        return basic;
    }

    public void setBasic(double basic) {
        this.basic = basic;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FestivalBonusDetailsDTO{" +
            "id=" + getId() +
            ", bonusAmount=" + getBonusAmount() +
            ", remarks='" + getRemarks() + "'" +
            ", isHold='" + isIsHold() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", festivalId=" + getFestivalId() +
            "}";
    }
}
