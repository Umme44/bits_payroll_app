package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FinalSettlement.
 */
@Entity
@Table(name = "final_settlement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinalSettlement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_of_resignation")
    private LocalDate dateOfResignation;

    @Column(name = "notice_period")
    private Integer noticePeriod;

    @Column(name = "last_working_day")
    private LocalDate lastWorkingDay;

    @Column(name = "date_of_release")
    private LocalDate dateOfRelease;

    @Column(name = "service_tenure")
    private String serviceTenure;

    @Column(name = "m_basic")
    private Double mBasic;

    @Column(name = "m_house_rent")
    private Double mHouseRent;

    @Column(name = "m_medical")
    private Double mMedical;

    @Column(name = "m_conveyance")
    private Double mConveyance;

    @Column(name = "salary_payable")
    private Double salaryPayable;

    @Column(name = "salary_payable_remarks")
    private String salaryPayableRemarks;

    @Column(name = "total_days_for_leave_encashment")
    private Double totalDaysForLeaveEncashment;

    @Column(name = "total_leave_encashment")
    private Double totalLeaveEncashment;

    @Column(name = "mobile_bill_in_cash")
    private Double mobileBillInCash;

    @Column(name = "allowance_01_name")
    private String allowance01Name;

    @Column(name = "allowance_01_amount")
    private Double allowance01Amount;

    @Column(name = "allowance_01_remarks")
    private String allowance01Remarks;

    @Column(name = "allowance_02_name")
    private String allowance02Name;

    @Column(name = "allowance_02_amount")
    private Double allowance02Amount;

    @Column(name = "allowance_02_remarks")
    private String allowance02Remarks;

    @Column(name = "allowance_03_name")
    private String allowance03Name;

    @Column(name = "allowance_03_amount")
    private Double allowance03Amount;

    @Column(name = "allowance_03_remarks")
    private String allowance03Remarks;

    @Column(name = "allowance_04_name")
    private String allowance04Name;

    @Column(name = "allowance_04_amount")
    private Double allowance04Amount;

    @Column(name = "allowance_04_remarks")
    private String allowance04Remarks;

    @Column(name = "deduction_notice_pay")
    private Double deductionNoticePay;

    @Column(name = "deduction_pf")
    private Double deductionPf;

    @Column(name = "deduction_haf")
    private Double deductionHaf;

    @Column(name = "deduction_excess_cell_bill")
    private Double deductionExcessCellBill;

    @Column(name = "deduction_absent_days_adjustment")
    private Double deductionAbsentDaysAdjustment;

    @Column(name = "total_salary_payable")
    private Double totalSalaryPayable;

    @Column(name = "deduction_annual_income_tax")
    private Double deductionAnnualIncomeTax;

    @Column(name = "net_salary_payable")
    private Double netSalaryPayable;

    @Column(name = "total_payable_pf")
    private Double totalPayablePf;

    @Column(name = "total_payable_gf")
    private Double totalPayableGf;

    @Column(name = "total_final_settlement_amount")
    private Double totalFinalSettlementAmount;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "deduction_notice_pay_days")
    private Integer deductionNoticePayDays;

    @Column(name = "deduction_absent_days_adjustment_days")
    private Integer deductionAbsentDaysAdjustmentDays;

    @Column(name = "deduction_other")
    private Double deductionOther;

    @Column(name = "total_salary")
    private Double totalSalary;

    @Column(name = "total_gross_salary")
    private Double totalGrossSalary;

    @Column(name = "total_deduction")
    private Double totalDeduction;

    @Column(name = "final_settlement_date")
    private LocalDate finalSettlementDate;

    @Column(name = "is_finalized")
    private Boolean isFinalized;

    @Column(name = "salary_num_of_month")
    private Integer salaryNumOfMonth;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FinalSettlement id(Long id) {
        this.setId(id);
        return this;
    }

    public FinalSettlement dateOfResignation(LocalDate dateOfResignation) {
        this.setDateOfResignation(dateOfResignation);
        return this;
    }

    public FinalSettlement noticePeriod(Integer noticePeriod) {
        this.setNoticePeriod(noticePeriod);
        return this;
    }

    public FinalSettlement lastWorkingDay(LocalDate lastWorkingDay) {
        this.setLastWorkingDay(lastWorkingDay);
        return this;
    }

    public FinalSettlement dateOfRelease(LocalDate dateOfRelease) {
        this.setDateOfRelease(dateOfRelease);
        return this;
    }

    public FinalSettlement serviceTenure(String serviceTenure) {
        this.setServiceTenure(serviceTenure);
        return this;
    }

    public FinalSettlement mBasic(Double mBasic) {
        this.setmBasic(mBasic);
        return this;
    }

    public FinalSettlement mHouseRent(Double mHouseRent) {
        this.setmHouseRent(mHouseRent);
        return this;
    }

    public FinalSettlement mMedical(Double mMedical) {
        this.setmMedical(mMedical);
        return this;
    }

    public FinalSettlement mConveyance(Double mConveyance) {
        this.setmConveyance(mConveyance);
        return this;
    }

    public FinalSettlement salaryPayable(Double salaryPayable) {
        this.setSalaryPayable(salaryPayable);
        return this;
    }

    public FinalSettlement salaryPayableRemarks(String salaryPayableRemarks) {
        this.setSalaryPayableRemarks(salaryPayableRemarks);
        return this;
    }

    public FinalSettlement totalDaysForLeaveEncashment(Double totalDaysForLeaveEncashment) {
        this.setTotalDaysForLeaveEncashment(totalDaysForLeaveEncashment);
        return this;
    }

    public FinalSettlement totalLeaveEncashment(Double totalLeaveEncashment) {
        this.setTotalLeaveEncashment(totalLeaveEncashment);
        return this;
    }

    public FinalSettlement mobileBillInCash(Double mobileBillInCash) {
        this.setMobileBillInCash(mobileBillInCash);
        return this;
    }

    public FinalSettlement allowance01Name(String allowance01Name) {
        this.setAllowance01Name(allowance01Name);
        return this;
    }

    public FinalSettlement allowance01Amount(Double allowance01Amount) {
        this.setAllowance01Amount(allowance01Amount);
        return this;
    }

    public FinalSettlement allowance01Remarks(String allowance01Remarks) {
        this.setAllowance01Remarks(allowance01Remarks);
        return this;
    }

    public FinalSettlement allowance02Name(String allowance02Name) {
        this.setAllowance02Name(allowance02Name);
        return this;
    }

    public FinalSettlement allowance02Amount(Double allowance02Amount) {
        this.setAllowance02Amount(allowance02Amount);
        return this;
    }

    public FinalSettlement allowance02Remarks(String allowance02Remarks) {
        this.setAllowance02Remarks(allowance02Remarks);
        return this;
    }

    public FinalSettlement allowance03Name(String allowance03Name) {
        this.setAllowance03Name(allowance03Name);
        return this;
    }

    public FinalSettlement allowance03Amount(Double allowance03Amount) {
        this.setAllowance03Amount(allowance03Amount);
        return this;
    }

    public FinalSettlement allowance03Remarks(String allowance03Remarks) {
        this.setAllowance03Remarks(allowance03Remarks);
        return this;
    }

    public FinalSettlement allowance04Name(String allowance04Name) {
        this.setAllowance04Name(allowance04Name);
        return this;
    }

    public FinalSettlement allowance04Amount(Double allowance04Amount) {
        this.setAllowance04Amount(allowance04Amount);
        return this;
    }

    public FinalSettlement allowance04Remarks(String allowance04Remarks) {
        this.setAllowance04Remarks(allowance04Remarks);
        return this;
    }

    public FinalSettlement deductionNoticePay(Double deductionNoticePay) {
        this.setDeductionNoticePay(deductionNoticePay);
        return this;
    }

    public FinalSettlement deductionPf(Double deductionPf) {
        this.setDeductionPf(deductionPf);
        return this;
    }

    public FinalSettlement deductionHaf(Double deductionHaf) {
        this.setDeductionHaf(deductionHaf);
        return this;
    }

    public FinalSettlement deductionExcessCellBill(Double deductionExcessCellBill) {
        this.setDeductionExcessCellBill(deductionExcessCellBill);
        return this;
    }

    public FinalSettlement deductionAbsentDaysAdjustment(Double deductionAbsentDaysAdjustment) {
        this.setDeductionAbsentDaysAdjustment(deductionAbsentDaysAdjustment);
        return this;
    }

    public FinalSettlement totalSalaryPayable(Double totalSalaryPayable) {
        this.setTotalSalaryPayable(totalSalaryPayable);
        return this;
    }

    public FinalSettlement deductionAnnualIncomeTax(Double deductionAnnualIncomeTax) {
        this.setDeductionAnnualIncomeTax(deductionAnnualIncomeTax);
        return this;
    }

    public FinalSettlement netSalaryPayable(Double netSalaryPayable) {
        this.setNetSalaryPayable(netSalaryPayable);
        return this;
    }

    public FinalSettlement totalPayablePf(Double totalPayablePf) {
        this.setTotalPayablePf(totalPayablePf);
        return this;
    }

    public FinalSettlement totalPayableGf(Double totalPayableGf) {
        this.setTotalPayableGf(totalPayableGf);
        return this;
    }

    public FinalSettlement totalFinalSettlementAmount(Double totalFinalSettlementAmount) {
        this.setTotalFinalSettlementAmount(totalFinalSettlementAmount);
        return this;
    }

    public FinalSettlement createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public FinalSettlement updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public FinalSettlement deductionNoticePayDays(Integer deductionNoticePayDays) {
        this.setDeductionNoticePayDays(deductionNoticePayDays);
        return this;
    }

    public FinalSettlement deductionAbsentDaysAdjustmentDays(Integer deductionAbsentDaysAdjustmentDays) {
        this.setDeductionAbsentDaysAdjustmentDays(deductionAbsentDaysAdjustmentDays);
        return this;
    }

    public FinalSettlement deductionOther(Double deductionOther) {
        this.setDeductionOther(deductionOther);
        return this;
    }

    public FinalSettlement totalSalary(Double totalSalary) {
        this.setTotalSalary(totalSalary);
        return this;
    }

    public FinalSettlement totalGrossSalary(Double totalGrossSalary) {
        this.setTotalGrossSalary(totalGrossSalary);
        return this;
    }

    public FinalSettlement totalDeduction(Double totalDeduction) {
        this.setTotalDeduction(totalDeduction);
        return this;
    }

    public FinalSettlement finalSettlementDate(LocalDate finalSettlementDate) {
        this.setFinalSettlementDate(finalSettlementDate);
        return this;
    }

    public FinalSettlement isFinalized(Boolean isFinalized) {
        this.setIsFinalized(isFinalized);
        return this;
    }

    public FinalSettlement salaryNumOfMonth(Integer salaryNumOfMonth) {
        this.setSalaryNumOfMonth(salaryNumOfMonth);
        return this;
    }

    public FinalSettlement remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public FinalSettlement employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public FinalSettlement createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public FinalSettlement updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinalSettlement)) {
            return false;
        }
        return id != null && id.equals(((FinalSettlement) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinalSettlement{" +
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
            ", isFinalized='" + getIsFinalized() + "'" +
            ", salaryNumOfMonth=" + getSalaryNumOfMonth() +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfResignation() {
        return this.dateOfResignation;
    }

    public void setDateOfResignation(LocalDate dateOfResignation) {
        this.dateOfResignation = dateOfResignation;
    }

    public Integer getNoticePeriod() {
        return this.noticePeriod;
    }

    public void setNoticePeriod(Integer noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    public LocalDate getLastWorkingDay() {
        return this.lastWorkingDay;
    }

    public void setLastWorkingDay(LocalDate lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }

    public LocalDate getDateOfRelease() {
        return this.dateOfRelease;
    }

    public void setDateOfRelease(LocalDate dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public String getServiceTenure() {
        return this.serviceTenure;
    }

    public void setServiceTenure(String serviceTenure) {
        this.serviceTenure = serviceTenure;
    }

    public Double getmBasic() {
        return this.mBasic;
    }

    public void setmBasic(Double mBasic) {
        this.mBasic = mBasic;
    }

    public Double getmHouseRent() {
        return this.mHouseRent;
    }

    public void setmHouseRent(Double mHouseRent) {
        this.mHouseRent = mHouseRent;
    }

    public Double getmMedical() {
        return this.mMedical;
    }

    public void setmMedical(Double mMedical) {
        this.mMedical = mMedical;
    }

    public Double getmConveyance() {
        return this.mConveyance;
    }

    public void setmConveyance(Double mConveyance) {
        this.mConveyance = mConveyance;
    }

    public Double getSalaryPayable() {
        return this.salaryPayable;
    }

    public void setSalaryPayable(Double salaryPayable) {
        this.salaryPayable = salaryPayable;
    }

    public String getSalaryPayableRemarks() {
        return this.salaryPayableRemarks;
    }

    public void setSalaryPayableRemarks(String salaryPayableRemarks) {
        this.salaryPayableRemarks = salaryPayableRemarks;
    }

    public Double getTotalDaysForLeaveEncashment() {
        return this.totalDaysForLeaveEncashment;
    }

    public void setTotalDaysForLeaveEncashment(Double totalDaysForLeaveEncashment) {
        this.totalDaysForLeaveEncashment = totalDaysForLeaveEncashment;
    }

    public Double getTotalLeaveEncashment() {
        return this.totalLeaveEncashment;
    }

    public void setTotalLeaveEncashment(Double totalLeaveEncashment) {
        this.totalLeaveEncashment = totalLeaveEncashment;
    }

    public Double getMobileBillInCash() {
        return this.mobileBillInCash;
    }

    public void setMobileBillInCash(Double mobileBillInCash) {
        this.mobileBillInCash = mobileBillInCash;
    }

    public String getAllowance01Name() {
        return this.allowance01Name;
    }

    public void setAllowance01Name(String allowance01Name) {
        this.allowance01Name = allowance01Name;
    }

    public Double getAllowance01Amount() {
        return this.allowance01Amount;
    }

    public void setAllowance01Amount(Double allowance01Amount) {
        this.allowance01Amount = allowance01Amount;
    }

    public String getAllowance01Remarks() {
        return this.allowance01Remarks;
    }

    public void setAllowance01Remarks(String allowance01Remarks) {
        this.allowance01Remarks = allowance01Remarks;
    }

    public String getAllowance02Name() {
        return this.allowance02Name;
    }

    public void setAllowance02Name(String allowance02Name) {
        this.allowance02Name = allowance02Name;
    }

    public Double getAllowance02Amount() {
        return this.allowance02Amount;
    }

    public void setAllowance02Amount(Double allowance02Amount) {
        this.allowance02Amount = allowance02Amount;
    }

    public String getAllowance02Remarks() {
        return this.allowance02Remarks;
    }

    public void setAllowance02Remarks(String allowance02Remarks) {
        this.allowance02Remarks = allowance02Remarks;
    }

    public String getAllowance03Name() {
        return this.allowance03Name;
    }

    public void setAllowance03Name(String allowance03Name) {
        this.allowance03Name = allowance03Name;
    }

    public Double getAllowance03Amount() {
        return this.allowance03Amount;
    }

    public void setAllowance03Amount(Double allowance03Amount) {
        this.allowance03Amount = allowance03Amount;
    }

    public String getAllowance03Remarks() {
        return this.allowance03Remarks;
    }

    public void setAllowance03Remarks(String allowance03Remarks) {
        this.allowance03Remarks = allowance03Remarks;
    }

    public String getAllowance04Name() {
        return this.allowance04Name;
    }

    public void setAllowance04Name(String allowance04Name) {
        this.allowance04Name = allowance04Name;
    }

    public Double getAllowance04Amount() {
        return this.allowance04Amount;
    }

    public void setAllowance04Amount(Double allowance04Amount) {
        this.allowance04Amount = allowance04Amount;
    }

    public String getAllowance04Remarks() {
        return this.allowance04Remarks;
    }

    public void setAllowance04Remarks(String allowance04Remarks) {
        this.allowance04Remarks = allowance04Remarks;
    }

    public Double getDeductionNoticePay() {
        return this.deductionNoticePay;
    }

    public void setDeductionNoticePay(Double deductionNoticePay) {
        this.deductionNoticePay = deductionNoticePay;
    }

    public Double getDeductionPf() {
        return this.deductionPf;
    }

    public void setDeductionPf(Double deductionPf) {
        this.deductionPf = deductionPf;
    }

    public Double getDeductionHaf() {
        return this.deductionHaf;
    }

    public void setDeductionHaf(Double deductionHaf) {
        this.deductionHaf = deductionHaf;
    }

    public Double getDeductionExcessCellBill() {
        return this.deductionExcessCellBill;
    }

    public void setDeductionExcessCellBill(Double deductionExcessCellBill) {
        this.deductionExcessCellBill = deductionExcessCellBill;
    }

    public Double getDeductionAbsentDaysAdjustment() {
        return this.deductionAbsentDaysAdjustment;
    }

    public void setDeductionAbsentDaysAdjustment(Double deductionAbsentDaysAdjustment) {
        this.deductionAbsentDaysAdjustment = deductionAbsentDaysAdjustment;
    }

    public Double getTotalSalaryPayable() {
        return this.totalSalaryPayable;
    }

    public void setTotalSalaryPayable(Double totalSalaryPayable) {
        this.totalSalaryPayable = totalSalaryPayable;
    }

    public Double getDeductionAnnualIncomeTax() {
        return this.deductionAnnualIncomeTax;
    }

    public void setDeductionAnnualIncomeTax(Double deductionAnnualIncomeTax) {
        this.deductionAnnualIncomeTax = deductionAnnualIncomeTax;
    }

    public Double getNetSalaryPayable() {
        return this.netSalaryPayable;
    }

    public void setNetSalaryPayable(Double netSalaryPayable) {
        this.netSalaryPayable = netSalaryPayable;
    }

    public Double getTotalPayablePf() {
        return this.totalPayablePf;
    }

    public void setTotalPayablePf(Double totalPayablePf) {
        this.totalPayablePf = totalPayablePf;
    }

    public Double getTotalPayableGf() {
        return this.totalPayableGf;
    }

    public void setTotalPayableGf(Double totalPayableGf) {
        this.totalPayableGf = totalPayableGf;
    }

    public Double getTotalFinalSettlementAmount() {
        return this.totalFinalSettlementAmount;
    }

    public void setTotalFinalSettlementAmount(Double totalFinalSettlementAmount) {
        this.totalFinalSettlementAmount = totalFinalSettlementAmount;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDeductionNoticePayDays() {
        return this.deductionNoticePayDays;
    }

    public void setDeductionNoticePayDays(Integer deductionNoticePayDays) {
        this.deductionNoticePayDays = deductionNoticePayDays;
    }

    public Integer getDeductionAbsentDaysAdjustmentDays() {
        return this.deductionAbsentDaysAdjustmentDays;
    }

    public void setDeductionAbsentDaysAdjustmentDays(Integer deductionAbsentDaysAdjustmentDays) {
        this.deductionAbsentDaysAdjustmentDays = deductionAbsentDaysAdjustmentDays;
    }

    public Double getDeductionOther() {
        return this.deductionOther;
    }

    public void setDeductionOther(Double deductionOther) {
        this.deductionOther = deductionOther;
    }

    public Double getTotalSalary() {
        return this.totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Double getTotalGrossSalary() {
        return this.totalGrossSalary;
    }

    public void setTotalGrossSalary(Double totalGrossSalary) {
        this.totalGrossSalary = totalGrossSalary;
    }

    public Double getTotalDeduction() {
        return this.totalDeduction;
    }

    public void setTotalDeduction(Double totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public LocalDate getFinalSettlementDate() {
        return this.finalSettlementDate;
    }

    public void setFinalSettlementDate(LocalDate finalSettlementDate) {
        this.finalSettlementDate = finalSettlementDate;
    }

    public Boolean getIsFinalized() {
        return this.isFinalized;
    }

    public void setIsFinalized(Boolean isFinalized) {
        this.isFinalized = isFinalized;
    }

    public Integer getSalaryNumOfMonth() {
        return this.salaryNumOfMonth;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setSalaryNumOfMonth(Integer salaryNumOfMonth) {
        this.salaryNumOfMonth = salaryNumOfMonth;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
