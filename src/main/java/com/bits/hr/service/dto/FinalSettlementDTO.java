package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.FinalSettlement} entity.
 */
public class FinalSettlementDTO implements Serializable {

    private Long id;

    private LocalDate dateOfResignation;

    private Integer noticePeriod;

    private LocalDate lastWorkingDay;

    private LocalDate dateOfRelease;

    private String serviceTenure;

    private Double mBasic;

    private Double mHouseRent;

    private Double mMedical;

    private Double mConveyance;

    private Double salaryPayable;

    private String salaryPayableRemarks;

    private Double totalDaysForLeaveEncashment;

    private Double totalLeaveEncashment;

    private Double mobileBillInCash;

    private String allowance01Name;

    private Double allowance01Amount;

    private String allowance01Remarks;

    private String allowance02Name;

    private Double allowance02Amount;

    private String allowance02Remarks;

    private String allowance03Name;

    private Double allowance03Amount;

    private String allowance03Remarks;

    private String allowance04Name;

    private Double allowance04Amount;

    private String allowance04Remarks;

    private Double deductionNoticePay;

    private Double deductionPf;

    private Double deductionHaf;

    private Double deductionExcessCellBill;

    private Double deductionAbsentDaysAdjustment;

    private Double totalSalaryPayable;

    private Double deductionAnnualIncomeTax;

    private Double netSalaryPayable;

    private Double totalPayablePf;

    private Double totalPayableGf;

    private Double totalFinalSettlementAmount;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Integer deductionNoticePayDays;

    private Integer deductionAbsentDaysAdjustmentDays;

    private Double deductionOther;

    private Double totalSalary;

    private Double totalGrossSalary;

    private Double totalDeduction;

    private LocalDate finalSettlementDate;

    private Boolean isFinalized;

    private Integer salaryNumOfMonth;

    private String remarks;

    private Long employeeId;

    private String employeeFullName;

    private String employeePin;

    private LocalDate dateOfJoining;

    private LocalDate dateOfConfirmation;

    private EmployeeCategory employeeCategory;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfResignation() {
        return dateOfResignation;
    }

    public void setDateOfResignation(LocalDate dateOfResignation) {
        this.dateOfResignation = dateOfResignation;
    }

    public Integer getNoticePeriod() {
        return noticePeriod;
    }

    public void setNoticePeriod(Integer noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    public LocalDate getLastWorkingDay() {
        return lastWorkingDay;
    }

    public void setLastWorkingDay(LocalDate lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }

    public LocalDate getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(LocalDate dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public String getServiceTenure() {
        return serviceTenure;
    }

    public void setServiceTenure(String serviceTenure) {
        this.serviceTenure = serviceTenure;
    }

    public Double getmBasic() {
        return mBasic;
    }

    public void setmBasic(Double mBasic) {
        this.mBasic = mBasic;
    }

    public Double getmHouseRent() {
        return mHouseRent;
    }

    public void setmHouseRent(Double mHouseRent) {
        this.mHouseRent = mHouseRent;
    }

    public Double getmMedical() {
        return mMedical;
    }

    public void setmMedical(Double mMedical) {
        this.mMedical = mMedical;
    }

    public Double getmConveyance() {
        return mConveyance;
    }

    public void setmConveyance(Double mConveyance) {
        this.mConveyance = mConveyance;
    }

    public Double getSalaryPayable() {
        return salaryPayable;
    }

    public void setSalaryPayable(Double salaryPayable) {
        this.salaryPayable = salaryPayable;
    }

    public String getSalaryPayableRemarks() {
        return salaryPayableRemarks;
    }

    public void setSalaryPayableRemarks(String salaryPayableRemarks) {
        this.salaryPayableRemarks = salaryPayableRemarks;
    }

    public Double getTotalDaysForLeaveEncashment() {
        return totalDaysForLeaveEncashment;
    }

    public void setTotalDaysForLeaveEncashment(Double totalDaysForLeaveEncashment) {
        this.totalDaysForLeaveEncashment = totalDaysForLeaveEncashment;
    }

    public Double getTotalLeaveEncashment() {
        return totalLeaveEncashment;
    }

    public void setTotalLeaveEncashment(Double totalLeaveEncashment) {
        this.totalLeaveEncashment = totalLeaveEncashment;
    }

    public Double getMobileBillInCash() {
        return mobileBillInCash;
    }

    public void setMobileBillInCash(Double mobileBillInCash) {
        this.mobileBillInCash = mobileBillInCash;
    }

    public String getAllowance01Name() {
        return allowance01Name;
    }

    public void setAllowance01Name(String allowance01Name) {
        this.allowance01Name = allowance01Name;
    }

    public Double getAllowance01Amount() {
        return allowance01Amount;
    }

    public void setAllowance01Amount(Double allowance01Amount) {
        this.allowance01Amount = allowance01Amount;
    }

    public String getAllowance01Remarks() {
        return allowance01Remarks;
    }

    public void setAllowance01Remarks(String allowance01Remarks) {
        this.allowance01Remarks = allowance01Remarks;
    }

    public String getAllowance02Name() {
        return allowance02Name;
    }

    public void setAllowance02Name(String allowance02Name) {
        this.allowance02Name = allowance02Name;
    }

    public Double getAllowance02Amount() {
        return allowance02Amount;
    }

    public void setAllowance02Amount(Double allowance02Amount) {
        this.allowance02Amount = allowance02Amount;
    }

    public String getAllowance02Remarks() {
        return allowance02Remarks;
    }

    public void setAllowance02Remarks(String allowance02Remarks) {
        this.allowance02Remarks = allowance02Remarks;
    }

    public String getAllowance03Name() {
        return allowance03Name;
    }

    public void setAllowance03Name(String allowance03Name) {
        this.allowance03Name = allowance03Name;
    }

    public Double getAllowance03Amount() {
        return allowance03Amount;
    }

    public void setAllowance03Amount(Double allowance03Amount) {
        this.allowance03Amount = allowance03Amount;
    }

    public String getAllowance03Remarks() {
        return allowance03Remarks;
    }

    public void setAllowance03Remarks(String allowance03Remarks) {
        this.allowance03Remarks = allowance03Remarks;
    }

    public String getAllowance04Name() {
        return allowance04Name;
    }

    public void setAllowance04Name(String allowance04Name) {
        this.allowance04Name = allowance04Name;
    }

    public Double getAllowance04Amount() {
        return allowance04Amount;
    }

    public void setAllowance04Amount(Double allowance04Amount) {
        this.allowance04Amount = allowance04Amount;
    }

    public String getAllowance04Remarks() {
        return allowance04Remarks;
    }

    public void setAllowance04Remarks(String allowance04Remarks) {
        this.allowance04Remarks = allowance04Remarks;
    }

    public Double getDeductionNoticePay() {
        return deductionNoticePay;
    }

    public void setDeductionNoticePay(Double deductionNoticePay) {
        this.deductionNoticePay = deductionNoticePay;
    }

    public Double getDeductionPf() {
        return deductionPf;
    }

    public void setDeductionPf(Double deductionPf) {
        this.deductionPf = deductionPf;
    }

    public Double getDeductionHaf() {
        return deductionHaf;
    }

    public void setDeductionHaf(Double deductionHaf) {
        this.deductionHaf = deductionHaf;
    }

    public Double getDeductionExcessCellBill() {
        return deductionExcessCellBill;
    }

    public void setDeductionExcessCellBill(Double deductionExcessCellBill) {
        this.deductionExcessCellBill = deductionExcessCellBill;
    }

    public Double getDeductionAbsentDaysAdjustment() {
        return deductionAbsentDaysAdjustment;
    }

    public void setDeductionAbsentDaysAdjustment(Double deductionAbsentDaysAdjustment) {
        this.deductionAbsentDaysAdjustment = deductionAbsentDaysAdjustment;
    }

    public Double getTotalSalaryPayable() {
        return totalSalaryPayable;
    }

    public void setTotalSalaryPayable(Double totalSalaryPayable) {
        this.totalSalaryPayable = totalSalaryPayable;
    }

    public Double getDeductionAnnualIncomeTax() {
        return deductionAnnualIncomeTax;
    }

    public void setDeductionAnnualIncomeTax(Double deductionAnnualIncomeTax) {
        this.deductionAnnualIncomeTax = deductionAnnualIncomeTax;
    }

    public Double getNetSalaryPayable() {
        return netSalaryPayable;
    }

    public void setNetSalaryPayable(Double netSalaryPayable) {
        this.netSalaryPayable = netSalaryPayable;
    }

    public Double getTotalPayablePf() {
        return totalPayablePf;
    }

    public void setTotalPayablePf(Double totalPayablePf) {
        this.totalPayablePf = totalPayablePf;
    }

    public Double getTotalPayableGf() {
        return totalPayableGf;
    }

    public void setTotalPayableGf(Double totalPayableGf) {
        this.totalPayableGf = totalPayableGf;
    }

    public Double getTotalFinalSettlementAmount() {
        return totalFinalSettlementAmount;
    }

    public void setTotalFinalSettlementAmount(Double totalFinalSettlementAmount) {
        this.totalFinalSettlementAmount = totalFinalSettlementAmount;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDeductionNoticePayDays() {
        return deductionNoticePayDays;
    }

    public void setDeductionNoticePayDays(Integer deductionNoticePayDays) {
        this.deductionNoticePayDays = deductionNoticePayDays;
    }

    public Integer getDeductionAbsentDaysAdjustmentDays() {
        return deductionAbsentDaysAdjustmentDays;
    }

    public void setDeductionAbsentDaysAdjustmentDays(Integer deductionAbsentDaysAdjustmentDays) {
        this.deductionAbsentDaysAdjustmentDays = deductionAbsentDaysAdjustmentDays;
    }

    public Double getDeductionOther() {
        return deductionOther;
    }

    public void setDeductionOther(Double deductionOther) {
        this.deductionOther = deductionOther;
    }

    public Double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Double getTotalGrossSalary() {
        return totalGrossSalary;
    }

    public void setTotalGrossSalary(Double totalGrossSalary) {
        this.totalGrossSalary = totalGrossSalary;
    }

    public Double getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(Double totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public LocalDate getFinalSettlementDate() {
        return finalSettlementDate;
    }

    public void setFinalSettlementDate(LocalDate finalSettlementDate) {
        this.finalSettlementDate = finalSettlementDate;
    }

    public Boolean isIsFinalized() {
        return isFinalized;
    }

    public void setIsFinalized(Boolean isFinalized) {
        this.isFinalized = isFinalized;
    }

    public Integer getSalaryNumOfMonth() {
        return salaryNumOfMonth;
    }

    public void setSalaryNumOfMonth(Integer salaryNumOfMonth) {
        this.salaryNumOfMonth = salaryNumOfMonth;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public String getEmployeePin() {
        return employeePin;
    }

    public void setEmployeePin(String employeePin) {
        this.employeePin = employeePin;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDateOfConfirmation() {
        return dateOfConfirmation;
    }

    public void setDateOfConfirmation(LocalDate dateOfConfirmation) {
        this.dateOfConfirmation = dateOfConfirmation;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long userId) {
        this.updatedById = userId;
    }

    public String getUpdatedByLogin() {
        return updatedByLogin;
    }

    public void setUpdatedByLogin(String userLogin) {
        this.updatedByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinalSettlementDTO)) {
            return false;
        }

        return id != null && id.equals(((FinalSettlementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinalSettlementDTO{" +
            "id=" + getId() +
            ", dateOfResignation='" + getDateOfResignation() + "'" +
            ", noticePeriod=" + getNoticePeriod() +
            ", lastWorkingDay='" + getLastWorkingDay() + "'" +
            ", dateOfRelease='" + getDateOfRelease() + "'" +
            ", serviceTenure='" + getServiceTenure() + "'" +
            ", mBasic=" + getmBasic() +
            ", mHouseRent=" + getmHouseRent() +
            ", mMedical=" + getmMedical() +
            ", mConveyance=" + getmConveyance() +
            ", salaryPayable=" + getSalaryPayable() +
            ", salaryPayableRemarks='" + getSalaryPayableRemarks() + "'" +
            ", totalDaysForLeaveEncashment=" + getTotalDaysForLeaveEncashment() +
            ", totalLeaveEncashment=" + getTotalLeaveEncashment() +
            ", mobileBillInCash=" + getMobileBillInCash() +
            ", allowance01Name='" + getAllowance01Name() + "'" +
            ", allowance01Amount=" + getAllowance01Amount() +
            ", allowance01Remarks='" + getAllowance01Remarks() + "'" +
            ", allowance02Name='" + getAllowance02Name() + "'" +
            ", allowance02Amount=" + getAllowance02Amount() +
            ", allowance02Remarks='" + getAllowance02Remarks() + "'" +
            ", allowance03Name='" + getAllowance03Name() + "'" +
            ", allowance03Amount=" + getAllowance03Amount() +
            ", allowance03Remarks='" + getAllowance03Remarks() + "'" +
            ", allowance04Name='" + getAllowance04Name() + "'" +
            ", allowance04Amount=" + getAllowance04Amount() +
            ", allowance04Remarks='" + getAllowance04Remarks() + "'" +
            ", deductionNoticePay=" + getDeductionNoticePay() +
            ", deductionPf=" + getDeductionPf() +
            ", deductionHaf=" + getDeductionHaf() +
            ", deductionExcessCellBill=" + getDeductionExcessCellBill() +
            ", deductionAbsentDaysAdjustment=" + getDeductionAbsentDaysAdjustment() +
            ", totalSalaryPayable=" + getTotalSalaryPayable() +
            ", deductionAnnualIncomeTax=" + getDeductionAnnualIncomeTax() +
            ", netSalaryPayable=" + getNetSalaryPayable() +
            ", totalPayablePf=" + getTotalPayablePf() +
            ", totalPayableGf=" + getTotalPayableGf() +
            ", totalFinalSettlementAmount=" + getTotalFinalSettlementAmount() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deductionNoticePayDays=" + getDeductionNoticePayDays() +
            ", deductionAbsentDaysAdjustmentDays=" + getDeductionAbsentDaysAdjustmentDays() +
            ", deductionOther=" + getDeductionOther() +
            ", totalSalary=" + getTotalSalary() +
            ", totalGrossSalary=" + getTotalGrossSalary() +
            ", totalDeduction=" + getTotalDeduction() +
            ", finalSettlementDate='" + getFinalSettlementDate() + "'" +
            ", isFinalized='" + isIsFinalized() + "'" +
            ", salaryNumOfMonth=" + getSalaryNumOfMonth() +
            ", remarks='" + getRemarks() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            "}";
    }
}
