package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Month;
import java.io.Serializable;

/**
 * A DTO for the {@link com.bits.hr.domain.EmployeeSalaryTempData} entity.
 */
public class EmployeeSalaryTempDataDTO implements Serializable {

    private EmployeeSalaryDTO systemGeneratedSalary;

    private Long id;

    private Month month;

    private Integer year;

    private Double mainGrossSalary;

    private Double mainGrossBasicSalary;

    private Double mainGrossHouseRent;

    private Double mainGrossMedicalAllowance;

    private Double mainGrossConveyanceAllowance;

    private Integer absentDays;

    private Integer fractionDays;

    private Double payableGrossSalary;

    private Double payableGrossBasicSalary;

    private Double payableGrossHouseRent;

    private Double payableGrossMedicalAllowance;

    private Double payableGrossConveyanceAllowance;

    private Double arrearSalary;

    private Double pfDeduction;

    private Double taxDeduction;

    private Double welfareFundDeduction;

    private Double mobileBillDeduction;

    private Double otherDeduction;

    private Double totalDeduction;

    private Double netPay;

    private String remarks;

    private Double pfContribution;

    private Double gfContribution;

    private Double provisionForFestivalBonus;

    private Double provisionForLeaveEncashment;

    private Double provishionForProjectBonus;

    private Double livingAllowance;

    private Double otherAddition;

    private Double salaryAdjustment;

    private Double providentFundArrear;

    private Double entertainment;

    private Double utility;

    private Long employeeId;

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

    public Double getMainGrossSalary() {
        return mainGrossSalary;
    }

    public void setMainGrossSalary(Double mainGrossSalary) {
        this.mainGrossSalary = mainGrossSalary;
    }

    public Double getMainGrossBasicSalary() {
        return mainGrossBasicSalary;
    }

    public void setMainGrossBasicSalary(Double mainGrossBasicSalary) {
        this.mainGrossBasicSalary = mainGrossBasicSalary;
    }

    public Double getMainGrossHouseRent() {
        return mainGrossHouseRent;
    }

    public void setMainGrossHouseRent(Double mainGrossHouseRent) {
        this.mainGrossHouseRent = mainGrossHouseRent;
    }

    public Double getMainGrossMedicalAllowance() {
        return mainGrossMedicalAllowance;
    }

    public void setMainGrossMedicalAllowance(Double mainGrossMedicalAllowance) {
        this.mainGrossMedicalAllowance = mainGrossMedicalAllowance;
    }

    public Double getMainGrossConveyanceAllowance() {
        return mainGrossConveyanceAllowance;
    }

    public void setMainGrossConveyanceAllowance(Double mainGrossConveyanceAllowance) {
        this.mainGrossConveyanceAllowance = mainGrossConveyanceAllowance;
    }

    public Integer getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public Integer getFractionDays() {
        return fractionDays;
    }

    public void setFractionDays(Integer fractionDays) {
        this.fractionDays = fractionDays;
    }

    public Double getPayableGrossSalary() {
        return payableGrossSalary;
    }

    public void setPayableGrossSalary(Double payableGrossSalary) {
        this.payableGrossSalary = payableGrossSalary;
    }

    public Double getPayableGrossBasicSalary() {
        return payableGrossBasicSalary;
    }

    public void setPayableGrossBasicSalary(Double payableGrossBasicSalary) {
        this.payableGrossBasicSalary = payableGrossBasicSalary;
    }

    public Double getPayableGrossHouseRent() {
        return payableGrossHouseRent;
    }

    public void setPayableGrossHouseRent(Double payableGrossHouseRent) {
        this.payableGrossHouseRent = payableGrossHouseRent;
    }

    public Double getPayableGrossMedicalAllowance() {
        return payableGrossMedicalAllowance;
    }

    public void setPayableGrossMedicalAllowance(Double payableGrossMedicalAllowance) {
        this.payableGrossMedicalAllowance = payableGrossMedicalAllowance;
    }

    public Double getPayableGrossConveyanceAllowance() {
        return payableGrossConveyanceAllowance;
    }

    public void setPayableGrossConveyanceAllowance(Double payableGrossConveyanceAllowance) {
        this.payableGrossConveyanceAllowance = payableGrossConveyanceAllowance;
    }

    public Double getArrearSalary() {
        return arrearSalary;
    }

    public void setArrearSalary(Double arrearSalary) {
        this.arrearSalary = arrearSalary;
    }

    public Double getPfDeduction() {
        return pfDeduction;
    }

    public void setPfDeduction(Double pfDeduction) {
        this.pfDeduction = pfDeduction;
    }

    public Double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(Double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public Double getWelfareFundDeduction() {
        return welfareFundDeduction;
    }

    public void setWelfareFundDeduction(Double welfareFundDeduction) {
        this.welfareFundDeduction = welfareFundDeduction;
    }

    public Double getMobileBillDeduction() {
        return mobileBillDeduction;
    }

    public void setMobileBillDeduction(Double mobileBillDeduction) {
        this.mobileBillDeduction = mobileBillDeduction;
    }

    public Double getOtherDeduction() {
        return otherDeduction;
    }

    public void setOtherDeduction(Double otherDeduction) {
        this.otherDeduction = otherDeduction;
    }

    public Double getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(Double totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public Double getNetPay() {
        return netPay;
    }

    public void setNetPay(Double netPay) {
        this.netPay = netPay;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Double getPfContribution() {
        return pfContribution;
    }

    public void setPfContribution(Double pfContribution) {
        this.pfContribution = pfContribution;
    }

    public Double getGfContribution() {
        return gfContribution;
    }

    public void setGfContribution(Double gfContribution) {
        this.gfContribution = gfContribution;
    }

    public Double getProvisionForFestivalBonus() {
        return provisionForFestivalBonus;
    }

    public void setProvisionForFestivalBonus(Double provisionForFestivalBonus) {
        this.provisionForFestivalBonus = provisionForFestivalBonus;
    }

    public Double getProvisionForLeaveEncashment() {
        return provisionForLeaveEncashment;
    }

    public void setProvisionForLeaveEncashment(Double provisionForLeaveEncashment) {
        this.provisionForLeaveEncashment = provisionForLeaveEncashment;
    }

    public Double getProvishionForProjectBonus() {
        return provishionForProjectBonus;
    }

    public void setProvishionForProjectBonus(Double provishionForProjectBonus) {
        this.provishionForProjectBonus = provishionForProjectBonus;
    }

    public Double getLivingAllowance() {
        return livingAllowance;
    }

    public void setLivingAllowance(Double livingAllowance) {
        this.livingAllowance = livingAllowance;
    }

    public Double getOtherAddition() {
        return otherAddition;
    }

    public void setOtherAddition(Double otherAddition) {
        this.otherAddition = otherAddition;
    }

    public Double getSalaryAdjustment() {
        return salaryAdjustment;
    }

    public void setSalaryAdjustment(Double salaryAdjustment) {
        this.salaryAdjustment = salaryAdjustment;
    }

    public Double getProvidentFundArrear() {
        return providentFundArrear;
    }

    public void setProvidentFundArrear(Double providentFundArrear) {
        this.providentFundArrear = providentFundArrear;
    }

    public Double getEntertainment() {
        return entertainment;
    }

    public void setEntertainment(Double entertainment) {
        this.entertainment = entertainment;
    }

    public Double getUtility() {
        return utility;
    }

    public void setUtility(Double utility) {
        this.utility = utility;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public EmployeeSalaryDTO getSystemGeneratedSalary() {
        return systemGeneratedSalary;
    }

    public void setSystemGeneratedSalary(EmployeeSalaryDTO systemGeneratedSalary) {
        this.systemGeneratedSalary = systemGeneratedSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeSalaryTempDataDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeSalaryTempDataDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeSalaryTempDataDTO{" +
            "id=" + getId() +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", mainGrossSalary=" + getMainGrossSalary() +
            ", mainGrossBasicSalary=" + getMainGrossBasicSalary() +
            ", mainGrossHouseRent=" + getMainGrossHouseRent() +
            ", mainGrossMedicalAllowance=" + getMainGrossMedicalAllowance() +
            ", mainGrossConveyanceAllowance=" + getMainGrossConveyanceAllowance() +
            ", absentDays=" + getAbsentDays() +
            ", fractionDays=" + getFractionDays() +
            ", payableGrossSalary=" + getPayableGrossSalary() +
            ", payableGrossBasicSalary=" + getPayableGrossBasicSalary() +
            ", payableGrossHouseRent=" + getPayableGrossHouseRent() +
            ", payableGrossMedicalAllowance=" + getPayableGrossMedicalAllowance() +
            ", payableGrossConveyanceAllowance=" + getPayableGrossConveyanceAllowance() +
            ", arrearSalary=" + getArrearSalary() +
            ", pfDeduction=" + getPfDeduction() +
            ", taxDeduction=" + getTaxDeduction() +
            ", welfareFundDeduction=" + getWelfareFundDeduction() +
            ", mobileBillDeduction=" + getMobileBillDeduction() +
            ", otherDeduction=" + getOtherDeduction() +
            ", totalDeduction=" + getTotalDeduction() +
            ", netPay=" + getNetPay() +
            ", remarks='" + getRemarks() + "'" +
            ", pfContribution=" + getPfContribution() +
            ", gfContribution=" + getGfContribution() +
            ", provisionForFestivalBonus=" + getProvisionForFestivalBonus() +
            ", provisionForLeaveEncashment=" + getProvisionForLeaveEncashment() +
            ", provishionForProjectBonus=" + getProvishionForProjectBonus() +
            ", livingAllowance=" + getLivingAllowance() +
            ", otherAddition=" + getOtherAddition() +
            ", salaryAdjustment=" + getSalaryAdjustment() +
            ", providentFundArrear=" + getProvidentFundArrear() +
            ", entertainment=" + getEntertainment() +
            ", utility=" + getUtility() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
