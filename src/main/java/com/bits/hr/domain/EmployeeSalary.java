package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A EmployeeSalary.
 */
@Entity
@Table(name = "employee_salary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeSalary implements Serializable {

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

    @Column(name = "salary_generation_date")
    private LocalDate salaryGenerationDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "ref_pin")
    private String refPin;

    @Column(name = "pin")
    private String pin;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "confirmation_date")
    private LocalDate confirmationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_category")
    private EmployeeCategory employeeCategory;

    @Column(name = "unit")
    private String unit;

    @Column(name = "department")
    private String department;

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

    @Column(name = "is_finalized")
    private Boolean isFinalized;

    @Column(name = "is_dispatched")
    private Boolean isDispatched;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "entertainment")
    private Double entertainment;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "utility")
    private Double utility;

    @Column(name = "other_addition")
    private Double otherAddition;

    @DecimalMin(value = "-10000000")
    @DecimalMax(value = "10000000")
    @Column(name = "salary_adjustment")
    private Double salaryAdjustment;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "provident_fund_arrear")
    private Double providentFundArrear;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_01")
    private Double allowance01;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_02")
    private Double allowance02;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_03")
    private Double allowance03;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_04")
    private Double allowance04;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_05")
    private Double allowance05;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_06")
    private Double allowance06;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "provision_for_project_bonus")
    private Double provisionForProjectBonus;

    @Column(name = "is_hold")
    private Boolean isHold;

    @Column(name = "attendance_regularisation_start_date")
    private LocalDate attendanceRegularisationStartDate;

    @Column(name = "attendance_regularisation_end_date")
    private LocalDate attendanceRegularisationEndDate;

    @Column(name = "title")
    private String title;

    @Column(name = "is_visible_to_employee")
    private Boolean isVisibleToEmployee;

    @Column(name = "pf_arrear")
    private Double pfArrear;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "tax_calculation_snapshot")
    private String taxCalculationSnapshot;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmployeeSalary id(Long id) {
        this.setId(id);
        return this;
    }

    public EmployeeSalary month(Month month) {
        this.setMonth(month);
        return this;
    }

    public EmployeeSalary year(Integer year) {
        this.setYear(year);
        return this;
    }

    public EmployeeSalary salaryGenerationDate(LocalDate salaryGenerationDate) {
        this.setSalaryGenerationDate(salaryGenerationDate);
        return this;
    }

    public EmployeeSalary createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public EmployeeSalary createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public EmployeeSalary updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public EmployeeSalary updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public EmployeeSalary refPin(String refPin) {
        this.setRefPin(refPin);
        return this;
    }

    public EmployeeSalary pin(String pin) {
        this.setPin(pin);
        return this;
    }

    public EmployeeSalary joiningDate(LocalDate joiningDate) {
        this.setJoiningDate(joiningDate);
        return this;
    }

    public EmployeeSalary confirmationDate(LocalDate confirmationDate) {
        this.setConfirmationDate(confirmationDate);
        return this;
    }

    public EmployeeSalary employeeCategory(EmployeeCategory employeeCategory) {
        this.setEmployeeCategory(employeeCategory);
        return this;
    }

    public EmployeeSalary unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public EmployeeSalary department(String department) {
        this.setDepartment(department);
        return this;
    }

    public EmployeeSalary mainGrossSalary(Double mainGrossSalary) {
        this.setMainGrossSalary(mainGrossSalary);
        return this;
    }

    public EmployeeSalary mainGrossBasicSalary(Double mainGrossBasicSalary) {
        this.setMainGrossBasicSalary(mainGrossBasicSalary);
        return this;
    }

    public EmployeeSalary mainGrossHouseRent(Double mainGrossHouseRent) {
        this.setMainGrossHouseRent(mainGrossHouseRent);
        return this;
    }

    public EmployeeSalary mainGrossMedicalAllowance(Double mainGrossMedicalAllowance) {
        this.setMainGrossMedicalAllowance(mainGrossMedicalAllowance);
        return this;
    }

    public EmployeeSalary mainGrossConveyanceAllowance(Double mainGrossConveyanceAllowance) {
        this.setMainGrossConveyanceAllowance(mainGrossConveyanceAllowance);
        return this;
    }

    public EmployeeSalary absentDays(Integer absentDays) {
        this.setAbsentDays(absentDays);
        return this;
    }

    public EmployeeSalary fractionDays(Integer fractionDays) {
        this.setFractionDays(fractionDays);
        return this;
    }

    public EmployeeSalary payableGrossSalary(Double payableGrossSalary) {
        this.setPayableGrossSalary(payableGrossSalary);
        return this;
    }

    public EmployeeSalary payableGrossBasicSalary(Double payableGrossBasicSalary) {
        this.setPayableGrossBasicSalary(payableGrossBasicSalary);
        return this;
    }

    public EmployeeSalary payableGrossHouseRent(Double payableGrossHouseRent) {
        this.setPayableGrossHouseRent(payableGrossHouseRent);
        return this;
    }

    public EmployeeSalary payableGrossMedicalAllowance(Double payableGrossMedicalAllowance) {
        this.setPayableGrossMedicalAllowance(payableGrossMedicalAllowance);
        return this;
    }

    public EmployeeSalary payableGrossConveyanceAllowance(Double payableGrossConveyanceAllowance) {
        this.setPayableGrossConveyanceAllowance(payableGrossConveyanceAllowance);
        return this;
    }

    public EmployeeSalary arrearSalary(Double arrearSalary) {
        this.setArrearSalary(arrearSalary);
        return this;
    }

    public EmployeeSalary pfDeduction(Double pfDeduction) {
        this.setPfDeduction(pfDeduction);
        return this;
    }

    public EmployeeSalary taxDeduction(Double taxDeduction) {
        this.setTaxDeduction(taxDeduction);
        return this;
    }

    public EmployeeSalary welfareFundDeduction(Double welfareFundDeduction) {
        this.setWelfareFundDeduction(welfareFundDeduction);
        return this;
    }

    public EmployeeSalary mobileBillDeduction(Double mobileBillDeduction) {
        this.setMobileBillDeduction(mobileBillDeduction);
        return this;
    }

    public EmployeeSalary otherDeduction(Double otherDeduction) {
        this.setOtherDeduction(otherDeduction);
        return this;
    }

    public EmployeeSalary totalDeduction(Double totalDeduction) {
        this.setTotalDeduction(totalDeduction);
        return this;
    }

    public EmployeeSalary netPay(Double netPay) {
        this.setNetPay(netPay);
        return this;
    }

    public EmployeeSalary remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public EmployeeSalary pfContribution(Double pfContribution) {
        this.setPfContribution(pfContribution);
        return this;
    }

    public EmployeeSalary gfContribution(Double gfContribution) {
        this.setGfContribution(gfContribution);
        return this;
    }

    public EmployeeSalary provisionForFestivalBonus(Double provisionForFestivalBonus) {
        this.setProvisionForFestivalBonus(provisionForFestivalBonus);
        return this;
    }

    public EmployeeSalary provisionForLeaveEncashment(Double provisionForLeaveEncashment) {
        this.setProvisionForLeaveEncashment(provisionForLeaveEncashment);
        return this;
    }

    public EmployeeSalary isFinalized(Boolean isFinalized) {
        this.setIsFinalized(isFinalized);
        return this;
    }

    public EmployeeSalary isDispatched(Boolean isDispatched) {
        this.setIsDispatched(isDispatched);
        return this;
    }

    public EmployeeSalary entertainment(Double entertainment) {
        this.setEntertainment(entertainment);
        return this;
    }

    public EmployeeSalary utility(Double utility) {
        this.setUtility(utility);
        return this;
    }

    public EmployeeSalary otherAddition(Double otherAddition) {
        this.setOtherAddition(otherAddition);
        return this;
    }

    public EmployeeSalary salaryAdjustment(Double salaryAdjustment) {
        this.setSalaryAdjustment(salaryAdjustment);
        return this;
    }

    public EmployeeSalary providentFundArrear(Double providentFundArrear) {
        this.setProvidentFundArrear(providentFundArrear);
        return this;
    }

    public EmployeeSalary allowance01(Double allowance01) {
        this.setAllowance01(allowance01);
        return this;
    }

    public EmployeeSalary allowance02(Double allowance02) {
        this.setAllowance02(allowance02);
        return this;
    }

    public EmployeeSalary allowance03(Double allowance03) {
        this.setAllowance03(allowance03);
        return this;
    }

    public EmployeeSalary allowance04(Double allowance04) {
        this.setAllowance04(allowance04);
        return this;
    }

    public EmployeeSalary allowance05(Double allowance05) {
        this.setAllowance05(allowance05);
        return this;
    }

    public EmployeeSalary allowance06(Double allowance06) {
        this.setAllowance06(allowance06);
        return this;
    }

    public EmployeeSalary provisionForProjectBonus(Double provisionForProjectBonus) {
        this.setProvisionForProjectBonus(provisionForProjectBonus);
        return this;
    }

    public EmployeeSalary isHold(Boolean isHold) {
        this.setIsHold(isHold);
        return this;
    }

    public EmployeeSalary attendanceRegularisationStartDate(LocalDate attendanceRegularisationStartDate) {
        this.setAttendanceRegularisationStartDate(attendanceRegularisationStartDate);
        return this;
    }

    public EmployeeSalary attendanceRegularisationEndDate(LocalDate attendanceRegularisationEndDate) {
        this.setAttendanceRegularisationEndDate(attendanceRegularisationEndDate);
        return this;
    }

    public EmployeeSalary title(String title) {
        this.setTitle(title);
        return this;
    }

    public EmployeeSalary isVisibleToEmployee(Boolean isVisibleToEmployee) {
        this.setIsVisibleToEmployee(isVisibleToEmployee);
        return this;
    }

    public EmployeeSalary pfArrear(Double pfArrear) {
        this.setPfArrear(pfArrear);
        return this;
    }

    public EmployeeSalary taxCalculationSnapshot(String taxCalculationSnapshot) {
        this.setTaxCalculationSnapshot(taxCalculationSnapshot);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeSalary employee(Employee employee) {
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
        if (!(o instanceof EmployeeSalary)) {
            return false;
        }
        return id != null && id.equals(((EmployeeSalary) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeSalary{" +
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
            ", netPay=" + getNetPay() +
            ", remarks='" + getRemarks() + "'" +
            ", pfContribution=" + getPfContribution() +
            ", gfContribution=" + getGfContribution() +
            ", provisionForFestivalBonus=" + getProvisionForFestivalBonus() +
            ", provisionForLeaveEncashment=" + getProvisionForLeaveEncashment() +
            ", isFinalized='" + getIsFinalized() + "'" +
            ", isDispatched='" + getIsDispatched() + "'" +
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
            ", isHold='" + getIsHold() + "'" +
            ", attendanceRegularisationStartDate='" + getAttendanceRegularisationStartDate() + "'" +
            ", attendanceRegularisationEndDate='" + getAttendanceRegularisationEndDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", isVisibleToEmployee='" + getIsVisibleToEmployee() + "'" +
            ", pfArrear=" + getPfArrear() +
            ", taxCalculationSnapshot='" + getTaxCalculationSnapshot() + "'" +
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

    public LocalDate getSalaryGenerationDate() {
        return this.salaryGenerationDate;
    }

    public void setSalaryGenerationDate(LocalDate salaryGenerationDate) {
        this.salaryGenerationDate = salaryGenerationDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRefPin() {
        return this.refPin;
    }

    public void setRefPin(String refPin) {
        this.refPin = refPin;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getJoiningDate() {
        return this.joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public LocalDate getConfirmationDate() {
        return this.confirmationDate;
    }

    public void setConfirmationDate(LocalDate confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public EmployeeCategory getEmployeeCategory() {
        return this.employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public Boolean getIsFinalized() {
        return this.isFinalized;
    }

    public void setIsFinalized(Boolean isFinalized) {
        this.isFinalized = isFinalized;
    }

    public Boolean getIsDispatched() {
        return this.isDispatched;
    }

    public void setIsDispatched(Boolean isDispatched) {
        this.isDispatched = isDispatched;
    }

    public Double getEntertainment() {
        return this.entertainment;
    }

    public void setEntertainment(Double entertainment) {
        this.entertainment = entertainment;
    }

    public Double getUtility() {
        return this.utility;
    }

    public void setUtility(Double utility) {
        this.utility = utility;
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

    public Double getAllowance01() {
        return this.allowance01;
    }

    public void setAllowance01(Double allowance01) {
        this.allowance01 = allowance01;
    }

    public Double getAllowance02() {
        return this.allowance02;
    }

    public void setAllowance02(Double allowance02) {
        this.allowance02 = allowance02;
    }

    public Double getAllowance03() {
        return this.allowance03;
    }

    public void setAllowance03(Double allowance03) {
        this.allowance03 = allowance03;
    }

    public Double getAllowance04() {
        return this.allowance04;
    }

    public void setAllowance04(Double allowance04) {
        this.allowance04 = allowance04;
    }

    public Double getAllowance05() {
        return this.allowance05;
    }

    public void setAllowance05(Double allowance05) {
        this.allowance05 = allowance05;
    }

    public Double getAllowance06() {
        return this.allowance06;
    }

    public void setAllowance06(Double allowance06) {
        this.allowance06 = allowance06;
    }

    public Double getProvisionForProjectBonus() {
        return this.provisionForProjectBonus;
    }

    public void setProvisionForProjectBonus(Double provisionForProjectBonus) {
        this.provisionForProjectBonus = provisionForProjectBonus;
    }

    public Boolean getIsHold() {
        return this.isHold;
    }

    public void setIsHold(Boolean isHold) {
        this.isHold = isHold;
    }

    public LocalDate getAttendanceRegularisationStartDate() {
        return this.attendanceRegularisationStartDate;
    }

    public void setAttendanceRegularisationStartDate(LocalDate attendanceRegularisationStartDate) {
        this.attendanceRegularisationStartDate = attendanceRegularisationStartDate;
    }

    public LocalDate getAttendanceRegularisationEndDate() {
        return this.attendanceRegularisationEndDate;
    }

    public void setAttendanceRegularisationEndDate(LocalDate attendanceRegularisationEndDate) {
        this.attendanceRegularisationEndDate = attendanceRegularisationEndDate;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsVisibleToEmployee() {
        return this.isVisibleToEmployee;
    }

    public void setIsVisibleToEmployee(Boolean isVisibleToEmployee) {
        this.isVisibleToEmployee = isVisibleToEmployee;
    }

    public Double getPfArrear() {
        return this.pfArrear;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setPfArrear(Double pfArrear) {
        this.pfArrear = pfArrear;
    }

    public String getTaxCalculationSnapshot() {
        return this.taxCalculationSnapshot;
    }

    public void setTaxCalculationSnapshot(String taxCalculationSnapshot) {
        this.taxCalculationSnapshot = taxCalculationSnapshot;
    }
}
