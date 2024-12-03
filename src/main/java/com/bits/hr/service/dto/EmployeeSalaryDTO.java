package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Month;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Lob;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * A DTO for the {@link com.bits.hr.domain.EmployeeSalary} entity.
 */
public class EmployeeSalaryDTO implements Serializable {

    private Long id;

    private Month month;

    private Integer year;

    private LocalDate salaryGenerationDate;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    private String refPin;

    private String pin;

    private LocalDate joiningDate;

    private LocalDate confirmationDate;

    private EmployeeCategory employeeCategory;

    private String unit;

    private String department;

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

    private Double totalAddition;

    private Double netPay;

    private String netPayInWords;

    private String remarks;

    private Double pfContribution;

    private Double gfContribution;

    private Double provisionForFestivalBonus;

    private Double provisionForLeaveEncashment;

    private Boolean isFinalized;

    private Boolean isDispatched;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double entertainment;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double utility;

    private Double otherAddition;

    @DecimalMin(value = "-10000000")
    @DecimalMax(value = "10000000")
    private Double salaryAdjustment;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double providentFundArrear;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance01;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance02;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance03;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance04;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance05;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance06;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double provisionForProjectBonus;

    private Boolean isHold;

    private Long employeeId;

    private String employeeName;

    private String designation;

    private EmploymentStatus employmentStatus;

    private LocalDate attendanceRegularisationStartDate;

    private LocalDate attendanceRegularisationEndDate;

    private String title;

    private Boolean isVisibleToEmployee;

    private Double pfArrear;

    private Double festivalBonus;

    @Lob
    private String taxCalculationSnapshot;

    public EmployeeSalaryDTO() {}

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

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

    public LocalDate getSalaryGenerationDate() {
        return salaryGenerationDate;
    }

    public void setSalaryGenerationDate(LocalDate salaryGenerationDate) {
        this.salaryGenerationDate = salaryGenerationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRefPin() {
        if (refPin != null) {
            return refPin.trim();
        } else {
            return refPin;
        }
    }

    public void setRefPin(String refPin) {
        if (refPin != null) {
            refPin = refPin.trim();
        }
        this.refPin = refPin;
    }

    public String getPin() {
        if (pin != null) {
            return pin.trim();
        } else {
            return pin;
        }
    }

    public void setPin(String pin) {
        if (pin != null) {
            pin = pin.trim();
        }
        this.pin = pin;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public LocalDate getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(LocalDate confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public Double getTotalAddition() {
        return totalAddition;
    }

    public void setTotalAddition(Double totalAddition) {
        this.totalAddition = totalAddition;
    }

    public Double getNetPay() {
        return netPay;
    }

    public void setNetPay(Double netPay) {
        this.netPay = netPay;
    }

    public String getNetPayInWords() {
        return netPayInWords;
    }

    public void setNetPayInWords(String netPayInWords) {
        this.netPayInWords = netPayInWords;
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

    public Boolean isIsFinalized() {
        return isFinalized;
    }

    public void setIsFinalized(Boolean isFinalized) {
        this.isFinalized = isFinalized;
    }

    public Boolean isIsDispatched() {
        return isDispatched;
    }

    public void setIsDispatched(Boolean isDispatched) {
        this.isDispatched = isDispatched;
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

    public Double getOtherAddition() {
        if (otherAddition == null) return 0d;
        return otherAddition;
    }

    public void setOtherAddition(Double otherAddition) {
        this.otherAddition = otherAddition;
    }

    public Double getSalaryAdjustment() {
        if (salaryAdjustment == null) return 0d;
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

    public Double getAllowance01() {
        if (allowance01 == null) return 0d;
        return allowance01;
    }

    public void setAllowance01(Double allowance01) {
        this.allowance01 = allowance01;
    }

    public Double getAllowance02() {
        if (allowance02 == null) return 0d;
        return allowance02;
    }

    public void setAllowance02(Double allowance02) {
        this.allowance02 = allowance02;
    }

    public Double getAllowance03() {
        if (allowance03 == null) return 0d;
        return allowance03;
    }

    public void setAllowance03(Double allowance03) {
        this.allowance03 = allowance03;
    }

    public Double getAllowance04() {
        if (allowance04 == null) return 0d;
        return allowance04;
    }

    public void setAllowance04(Double allowance04) {
        this.allowance04 = allowance04;
    }

    public Double getAllowance05() {
        if (allowance05 == null) return 0d;
        return allowance05;
    }

    public void setAllowance05(Double allowance05) {
        this.allowance05 = allowance05;
    }

    public Double getAllowance06() {
        if (allowance06 == null) return 0d;
        return allowance06;
    }

    public void setAllowance06(Double allowance06) {
        this.allowance06 = allowance06;
    }

    public Double getProvisionForProjectBonus() {
        if (provisionForProjectBonus == null) return 0d;
        return provisionForProjectBonus;
    }

    public void setProvisionForProjectBonus(Double provisionForProjectBonus) {
        this.provisionForProjectBonus = provisionForProjectBonus;
    }

    public Boolean isIsHold() {
        if (isHold == null) return false;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public LocalDate getAttendanceRegularisationStartDate() {
        return attendanceRegularisationStartDate;
    }

    public void setAttendanceRegularisationStartDate(LocalDate attendanceRegularisationStartDate) {
        this.attendanceRegularisationStartDate = attendanceRegularisationStartDate;
    }

    public LocalDate getAttendanceRegularisationEndDate() {
        return attendanceRegularisationEndDate;
    }

    public void setAttendanceRegularisationEndDate(LocalDate attendanceRegularisationEndDate) {
        this.attendanceRegularisationEndDate = attendanceRegularisationEndDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isIsVisibleToEmployee() {
        return isVisibleToEmployee;
    }

    public void setIsVisibleToEmployee(Boolean isVisibleToEmployee) {
        this.isVisibleToEmployee = isVisibleToEmployee;
    }

    public Double getPfArrear() {
        return pfArrear;
    }

    public void setPfArrear(Double pfArrear) {
        this.pfArrear = pfArrear;
    }

    public String getTaxCalculationSnapshot() {
        return taxCalculationSnapshot;
    }

    public void setTaxCalculationSnapshot(String taxCalculationSnapshot) {
        this.taxCalculationSnapshot = taxCalculationSnapshot;
    }

    public Double getFestivalBonus() {
        return festivalBonus;
    }

    public void setFestivalBonus(Double festivalBonus) {
        this.festivalBonus = festivalBonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeSalaryDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeSalaryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeSalaryDTO{" +
            "id=" + getId() +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", salaryGenerationDate='" + getSalaryGenerationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", refPin='" + getRefPin() + "'" +
            ", pin='" + getPin() + "'" +
            ", joiningDate='" + getJoiningDate() + "'" +
            ", confirmationDate='" + getConfirmationDate() + "'" +
            ", employeeCategory='" + getEmployeeCategory() + "'" +
            ", unit='" + getUnit() + "'" +
            ", department='" + getDepartment() + "'" +
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
            ", totalAddition=" + getTotalAddition() +
            ", netPay=" + getNetPay() +
            ", remarks='" + getRemarks() + "'" +
            ", pfContribution=" + getPfContribution() +
            ", gfContribution=" + getGfContribution() +
            ", provisionForFestivalBonus=" + getProvisionForFestivalBonus() +
            ", provisionForLeaveEncashment=" + getProvisionForLeaveEncashment() +
            ", isFinalized='" + isIsFinalized() + "'" +
            ", isDispatched='" + isIsDispatched() + "'" +
            ", entertainment=" + getEntertainment() +
            ", utility=" + getUtility() +
            ", otherAddition=" + getOtherAddition() +
            ", salaryAdjustment=" + getSalaryAdjustment() +
            ", providentFundArrear=" + getProvidentFundArrear() +
            ", allowance01=" + getAllowance01() +
            ", allowance02=" + getAllowance02() +
            ", allowance03=" + getAllowance03() +
            ", allowance04=" + getAllowance04() +
            ", allowance05=" + getAllowance05() +
            ", allowance06=" + getAllowance06() +
            ", provisionForProjectBonus=" + getProvisionForProjectBonus() +
            ", isHold='" + isIsHold() + "'" +
            ", attendanceRegularisationStartDate='" + getAttendanceRegularisationStartDate() + "'" +
            ", attendanceRegularisationEndDate='" + getAttendanceRegularisationEndDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", isVisibleToEmployee='" + isIsVisibleToEmployee() + "'" +
            ", pfArrear=" + getPfArrear() +
            ", taxCalculationSnapshot='" + getTaxCalculationSnapshot() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
