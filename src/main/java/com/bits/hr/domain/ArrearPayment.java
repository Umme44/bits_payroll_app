package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.ArrearPaymentType;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArrearPayment.
 */
@Entity
@Table(name = "arrear_payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArrearPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private ArrearPaymentType paymentType;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "salary_month")
    private Month salaryMonth;

    @Min(value = 1900)
    @Max(value = 2277)
    @Column(name = "salary_year")
    private Integer salaryYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private Status approvalStatus;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "100000000")
    @Column(name = "disbursement_amount", nullable = false)
    private Double disbursementAmount;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100000000")
    @Column(name = "arrear_pf", nullable = false)
    private Double arrearPF;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100000000")
    @Column(name = "tax_deduction", nullable = false)
    private Double taxDeduction;

    @NotNull
    @Column(name = "deduct_tax_upon_payment", nullable = false)
    private Boolean deductTaxUponPayment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "arrearSalaryMaster", "employee" }, allowSetters = true)
    private ArrearSalaryItem arrearSalaryItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ArrearPayment id(Long id) {
        this.setId(id);
        return this;
    }

    public ArrearPayment paymentType(ArrearPaymentType paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public ArrearPayment disbursementDate(LocalDate disbursementDate) {
        this.setDisbursementDate(disbursementDate);
        return this;
    }

    public ArrearPayment salaryMonth(Month salaryMonth) {
        this.setSalaryMonth(salaryMonth);
        return this;
    }

    public ArrearPayment salaryYear(Integer salaryYear) {
        this.setSalaryYear(salaryYear);
        return this;
    }

    public ArrearPayment approvalStatus(Status approvalStatus) {
        this.setApprovalStatus(approvalStatus);
        return this;
    }

    public ArrearPayment disbursementAmount(Double disbursementAmount) {
        this.setDisbursementAmount(disbursementAmount);
        return this;
    }

    public ArrearPayment isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ArrearPayment arrearPF(Double arrearPF) {
        this.setArrearPF(arrearPF);
        return this;
    }

    public ArrearPayment taxDeduction(Double taxDeduction) {
        this.setTaxDeduction(taxDeduction);
        return this;
    }

    public ArrearPayment deductTaxUponPayment(Boolean deductTaxUponPayment) {
        this.setDeductTaxUponPayment(deductTaxUponPayment);
        return this;
    }

    public ArrearSalaryItem getArrearSalaryItem() {
        return this.arrearSalaryItem;
    }

    public void setArrearSalaryItem(ArrearSalaryItem arrearSalaryItem) {
        this.arrearSalaryItem = arrearSalaryItem;
    }

    public ArrearPayment arrearSalaryItem(ArrearSalaryItem arrearSalaryItem) {
        this.setArrearSalaryItem(arrearSalaryItem);
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
        if (!(o instanceof ArrearPayment)) {
            return false;
        }
        return id != null && id.equals(((ArrearPayment) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArrearPayment{" +
            "id=" + getId() +
            ", paymentType='" + getPaymentType() + "'" +
            ", disbursementDate='" + getDisbursementDate() + "'" +
            ", salaryMonth='" + getSalaryMonth() + "'" +
            ", salaryYear=" + getSalaryYear() +
            ", approvalStatus='" + getApprovalStatus() + "'" +
            ", disbursementAmount=" + getDisbursementAmount() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", arrearPF=" + getArrearPF() +
            ", taxDeduction=" + getTaxDeduction() +
            ", deductTaxUponPayment='" + getDeductTaxUponPayment() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrearPaymentType getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(ArrearPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDate getDisbursementDate() {
        return this.disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public Month getSalaryMonth() {
        return this.salaryMonth;
    }

    public void setSalaryMonth(Month salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    public Integer getSalaryYear() {
        return this.salaryYear;
    }

    public void setSalaryYear(Integer salaryYear) {
        this.salaryYear = salaryYear;
    }

    public Status getApprovalStatus() {
        return this.approvalStatus;
    }

    public void setApprovalStatus(Status approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Double getDisbursementAmount() {
        return this.disbursementAmount;
    }

    public void setDisbursementAmount(Double disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Double getArrearPF() {
        return this.arrearPF;
    }

    public void setArrearPF(Double arrearPF) {
        this.arrearPF = arrearPF;
    }

    public Double getTaxDeduction() {
        return this.taxDeduction;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setTaxDeduction(Double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public Boolean getDeductTaxUponPayment() {
        return this.deductTaxUponPayment;
    }

    public void setDeductTaxUponPayment(Boolean deductTaxUponPayment) {
        this.deductTaxUponPayment = deductTaxUponPayment;
    }
}
