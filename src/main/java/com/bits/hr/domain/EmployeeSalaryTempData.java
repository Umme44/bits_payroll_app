package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Month;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeeSalaryTempData.
 */
@Entity
@Table(name = "employee_salary_temp_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeSalaryTempData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "month")
    private Month month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "main_gross_salary")
    private Double mainGrossSalary;

    @Column(name = "main_gross_basic_salary")
    private Double mainGrossBasicSalary;

    @Column(name = "main_gross_house_rent")
    private Double mainGrossHouseRent;

    @Column(name = "main_gross_medical_allowance")
    private Double mainGrossMedicalAllowance;

    @Column(name = "main_gross_conveyance_allowance")
    private Double mainGrossConveyanceAllowance;

    @Column(name = "absent_days")
    private Integer absentDays;

    @Column(name = "fraction_days")
    private Integer fractionDays;

    @Column(name = "payable_gross_salary")
    private Double payableGrossSalary;

    @Column(name = "payable_gross_basic_salary")
    private Double payableGrossBasicSalary;

    @Column(name = "payable_gross_house_rent")
    private Double payableGrossHouseRent;

    @Column(name = "payable_gross_medical_allowance")
    private Double payableGrossMedicalAllowance;

    @Column(name = "payable_gross_conveyance_allowance")
    private Double payableGrossConveyanceAllowance;

    @Column(name = "arrear_salary")
    private Double arrearSalary;

    @Column(name = "pf_deduction")
    private Double pfDeduction;

    @Column(name = "tax_deduction")
    private Double taxDeduction;

    @Column(name = "welfare_fund_deduction")
    private Double welfareFundDeduction;

    @Column(name = "mobile_bill_deduction")
    private Double mobileBillDeduction;

    @Column(name = "other_deduction")
    private Double otherDeduction;

    @Column(name = "total_deduction")
    private Double totalDeduction;

    @Column(name = "net_pay")
    private Double netPay;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "pf_contribution")
    private Double pfContribution;

    @Column(name = "gf_contribution")
    private Double gfContribution;

    @Column(name = "provision_for_festival_bonus")
    private Double provisionForFestivalBonus;

    @Column(name = "provision_for_leave_encashment")
    private Double provisionForLeaveEncashment;

    @Column(name = "provishion_for_project_bonus")
    private Double provishionForProjectBonus;

    @Column(name = "living_allowance")
    private Double livingAllowance;

    @Column(name = "other_addition")
    private Double otherAddition;

    @Column(name = "salary_adjustment")
    private Double salaryAdjustment;

    @Column(name = "provident_fund_arrear")
    private Double providentFundArrear;

    @Column(name = "entertainment")
    private Double entertainment;

    @Column(name = "utility")
    private Double utility;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmployeeSalaryTempData id(Long id) {
        this.setId(id);
        return this;
    }

    public EmployeeSalaryTempData month(Month month) {
        this.setMonth(month);
        return this;
    }

    public EmployeeSalaryTempData year(Integer year) {
        this.setYear(year);
        return this;
    }

    public EmployeeSalaryTempData mainGrossSalary(Double mainGrossSalary) {
        this.setMainGrossSalary(mainGrossSalary);
        return this;
    }

    public EmployeeSalaryTempData mainGrossBasicSalary(Double mainGrossBasicSalary) {
        this.setMainGrossBasicSalary(mainGrossBasicSalary);
        return this;
    }

    public EmployeeSalaryTempData mainGrossHouseRent(Double mainGrossHouseRent) {
        this.setMainGrossHouseRent(mainGrossHouseRent);
        return this;
    }

    public EmployeeSalaryTempData mainGrossMedicalAllowance(Double mainGrossMedicalAllowance) {
        this.setMainGrossMedicalAllowance(mainGrossMedicalAllowance);
        return this;
    }

    public EmployeeSalaryTempData mainGrossConveyanceAllowance(Double mainGrossConveyanceAllowance) {
        this.setMainGrossConveyanceAllowance(mainGrossConveyanceAllowance);
        return this;
    }

    public EmployeeSalaryTempData absentDays(Integer absentDays) {
        this.setAbsentDays(absentDays);
        return this;
    }

    public EmployeeSalaryTempData fractionDays(Integer fractionDays) {
        this.setFractionDays(fractionDays);
        return this;
    }

    public EmployeeSalaryTempData payableGrossSalary(Double payableGrossSalary) {
        this.setPayableGrossSalary(payableGrossSalary);
        return this;
    }

    public EmployeeSalaryTempData payableGrossBasicSalary(Double payableGrossBasicSalary) {
        this.setPayableGrossBasicSalary(payableGrossBasicSalary);
        return this;
    }

    public EmployeeSalaryTempData payableGrossHouseRent(Double payableGrossHouseRent) {
        this.setPayableGrossHouseRent(payableGrossHouseRent);
        return this;
    }

    public EmployeeSalaryTempData payableGrossMedicalAllowance(Double payableGrossMedicalAllowance) {
        this.setPayableGrossMedicalAllowance(payableGrossMedicalAllowance);
        return this;
    }

    public EmployeeSalaryTempData payableGrossConveyanceAllowance(Double payableGrossConveyanceAllowance) {
        this.setPayableGrossConveyanceAllowance(payableGrossConveyanceAllowance);
        return this;
    }

    public EmployeeSalaryTempData arrearSalary(Double arrearSalary) {
        this.setArrearSalary(arrearSalary);
        return this;
    }

    public EmployeeSalaryTempData pfDeduction(Double pfDeduction) {
        this.setPfDeduction(pfDeduction);
        return this;
    }

    public EmployeeSalaryTempData taxDeduction(Double taxDeduction) {
        this.setTaxDeduction(taxDeduction);
        return this;
    }

    public EmployeeSalaryTempData welfareFundDeduction(Double welfareFundDeduction) {
        this.setWelfareFundDeduction(welfareFundDeduction);
        return this;
    }

    public EmployeeSalaryTempData mobileBillDeduction(Double mobileBillDeduction) {
        this.setMobileBillDeduction(mobileBillDeduction);
        return this;
    }

    public EmployeeSalaryTempData otherDeduction(Double otherDeduction) {
        this.setOtherDeduction(otherDeduction);
        return this;
    }

    public EmployeeSalaryTempData totalDeduction(Double totalDeduction) {
        this.setTotalDeduction(totalDeduction);
        return this;
    }

    public EmployeeSalaryTempData netPay(Double netPay) {
        this.setNetPay(netPay);
        return this;
    }

    public EmployeeSalaryTempData remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public EmployeeSalaryTempData pfContribution(Double pfContribution) {
        this.setPfContribution(pfContribution);
        return this;
    }

    public EmployeeSalaryTempData gfContribution(Double gfContribution) {
        this.setGfContribution(gfContribution);
        return this;
    }

    public EmployeeSalaryTempData provisionForFestivalBonus(Double provisionForFestivalBonus) {
        this.setProvisionForFestivalBonus(provisionForFestivalBonus);
        return this;
    }

    public EmployeeSalaryTempData provisionForLeaveEncashment(Double provisionForLeaveEncashment) {
        this.setProvisionForLeaveEncashment(provisionForLeaveEncashment);
        return this;
    }

    public EmployeeSalaryTempData provishionForProjectBonus(Double provishionForProjectBonus) {
        this.setProvishionForProjectBonus(provishionForProjectBonus);
        return this;
    }

    public EmployeeSalaryTempData livingAllowance(Double livingAllowance) {
        this.setLivingAllowance(livingAllowance);
        return this;
    }

    public EmployeeSalaryTempData otherAddition(Double otherAddition) {
        this.setOtherAddition(otherAddition);
        return this;
    }

    public EmployeeSalaryTempData salaryAdjustment(Double salaryAdjustment) {
        this.setSalaryAdjustment(salaryAdjustment);
        return this;
    }

    public EmployeeSalaryTempData providentFundArrear(Double providentFundArrear) {
        this.setProvidentFundArrear(providentFundArrear);
        return this;
    }

    public EmployeeSalaryTempData entertainment(Double entertainment) {
        this.setEntertainment(entertainment);
        return this;
    }

    public EmployeeSalaryTempData utility(Double utility) {
        this.setUtility(utility);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeSalaryTempData employee(Employee employee) {
        this.setEmployee(employee);
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
        if (!(o instanceof EmployeeSalaryTempData)) {
            return false;
        }
        return id != null && id.equals(((EmployeeSalaryTempData) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeSalaryTempData{" +
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
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Month getMonth() {
        return this.month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getMainGrossSalary() {
        return this.mainGrossSalary;
    }

    public void setMainGrossSalary(Double mainGrossSalary) {
        this.mainGrossSalary = mainGrossSalary;
    }

    public Double getMainGrossBasicSalary() {
        return this.mainGrossBasicSalary;
    }

    public void setMainGrossBasicSalary(Double mainGrossBasicSalary) {
        this.mainGrossBasicSalary = mainGrossBasicSalary;
    }

    public Double getMainGrossHouseRent() {
        return this.mainGrossHouseRent;
    }

    public void setMainGrossHouseRent(Double mainGrossHouseRent) {
        this.mainGrossHouseRent = mainGrossHouseRent;
    }

    public Double getMainGrossMedicalAllowance() {
        return this.mainGrossMedicalAllowance;
    }

    public void setMainGrossMedicalAllowance(Double mainGrossMedicalAllowance) {
        this.mainGrossMedicalAllowance = mainGrossMedicalAllowance;
    }

    public Double getMainGrossConveyanceAllowance() {
        return this.mainGrossConveyanceAllowance;
    }

    public void setMainGrossConveyanceAllowance(Double mainGrossConveyanceAllowance) {
        this.mainGrossConveyanceAllowance = mainGrossConveyanceAllowance;
    }

    public Integer getAbsentDays() {
        return this.absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public Integer getFractionDays() {
        return this.fractionDays;
    }

    public void setFractionDays(Integer fractionDays) {
        this.fractionDays = fractionDays;
    }

    public Double getPayableGrossSalary() {
        return this.payableGrossSalary;
    }

    public void setPayableGrossSalary(Double payableGrossSalary) {
        this.payableGrossSalary = payableGrossSalary;
    }

    public Double getPayableGrossBasicSalary() {
        return this.payableGrossBasicSalary;
    }

    public void setPayableGrossBasicSalary(Double payableGrossBasicSalary) {
        this.payableGrossBasicSalary = payableGrossBasicSalary;
    }

    public Double getPayableGrossHouseRent() {
        return this.payableGrossHouseRent;
    }

    public void setPayableGrossHouseRent(Double payableGrossHouseRent) {
        this.payableGrossHouseRent = payableGrossHouseRent;
    }

    public Double getPayableGrossMedicalAllowance() {
        return this.payableGrossMedicalAllowance;
    }

    public void setPayableGrossMedicalAllowance(Double payableGrossMedicalAllowance) {
        this.payableGrossMedicalAllowance = payableGrossMedicalAllowance;
    }

    public Double getPayableGrossConveyanceAllowance() {
        return this.payableGrossConveyanceAllowance;
    }

    public void setPayableGrossConveyanceAllowance(Double payableGrossConveyanceAllowance) {
        this.payableGrossConveyanceAllowance = payableGrossConveyanceAllowance;
    }

    public Double getArrearSalary() {
        return this.arrearSalary;
    }

    public void setArrearSalary(Double arrearSalary) {
        this.arrearSalary = arrearSalary;
    }

    public Double getPfDeduction() {
        return this.pfDeduction;
    }

    public void setPfDeduction(Double pfDeduction) {
        this.pfDeduction = pfDeduction;
    }

    public Double getTaxDeduction() {
        return this.taxDeduction;
    }

    public void setTaxDeduction(Double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public Double getWelfareFundDeduction() {
        return this.welfareFundDeduction;
    }

    public void setWelfareFundDeduction(Double welfareFundDeduction) {
        this.welfareFundDeduction = welfareFundDeduction;
    }

    public Double getMobileBillDeduction() {
        return this.mobileBillDeduction;
    }

    public void setMobileBillDeduction(Double mobileBillDeduction) {
        this.mobileBillDeduction = mobileBillDeduction;
    }

    public Double getOtherDeduction() {
        return this.otherDeduction;
    }

    public void setOtherDeduction(Double otherDeduction) {
        this.otherDeduction = otherDeduction;
    }

    public Double getTotalDeduction() {
        return this.totalDeduction;
    }

    public void setTotalDeduction(Double totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public Double getNetPay() {
        return this.netPay;
    }

    public void setNetPay(Double netPay) {
        this.netPay = netPay;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Double getPfContribution() {
        return this.pfContribution;
    }

    public void setPfContribution(Double pfContribution) {
        this.pfContribution = pfContribution;
    }

    public Double getGfContribution() {
        return this.gfContribution;
    }

    public void setGfContribution(Double gfContribution) {
        this.gfContribution = gfContribution;
    }

    public Double getProvisionForFestivalBonus() {
        return this.provisionForFestivalBonus;
    }

    public void setProvisionForFestivalBonus(Double provisionForFestivalBonus) {
        this.provisionForFestivalBonus = provisionForFestivalBonus;
    }

    public Double getProvisionForLeaveEncashment() {
        return this.provisionForLeaveEncashment;
    }

    public void setProvisionForLeaveEncashment(Double provisionForLeaveEncashment) {
        this.provisionForLeaveEncashment = provisionForLeaveEncashment;
    }

    public Double getProvishionForProjectBonus() {
        return this.provishionForProjectBonus;
    }

    public void setProvishionForProjectBonus(Double provishionForProjectBonus) {
        this.provishionForProjectBonus = provishionForProjectBonus;
    }

    public Double getLivingAllowance() {
        return this.livingAllowance;
    }

    public void setLivingAllowance(Double livingAllowance) {
        this.livingAllowance = livingAllowance;
    }

    public Double getOtherAddition() {
        return this.otherAddition;
    }

    public void setOtherAddition(Double otherAddition) {
        this.otherAddition = otherAddition;
    }

    public Double getSalaryAdjustment() {
        return this.salaryAdjustment;
    }

    public void setSalaryAdjustment(Double salaryAdjustment) {
        this.salaryAdjustment = salaryAdjustment;
    }

    public Double getProvidentFundArrear() {
        return this.providentFundArrear;
    }

    public void setProvidentFundArrear(Double providentFundArrear) {
        this.providentFundArrear = providentFundArrear;
    }

    public Double getEntertainment() {
        return this.entertainment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setEntertainment(Double entertainment) {
        this.entertainment = entertainment;
    }

    public Double getUtility() {
        return this.utility;
    }

    public void setUtility(Double utility) {
        this.utility = utility;
    }
}
