package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.ArrearPaymentType;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.ArrearPayment} entity.
 */
public class ArrearPaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private ArrearPaymentType paymentType;

    private LocalDate disbursementDate;

    private Month salaryMonth;

    @Min(value = 1900)
    @Max(value = 2277)
    private Integer salaryYear;

    private Status approvalStatus;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "100000000")
    private Double disbursementAmount;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100000000")
    private Double arrearPF;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100000000")
    private Double taxDeduction;

    @NotNull
    private Boolean deductTaxUponPayment;

    private Long arrearSalaryItemId;

    private String arrearSalaryItemTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrearPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(ArrearPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public Month getSalaryMonth() {
        return salaryMonth;
    }

    public void setSalaryMonth(Month salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    public Integer getSalaryYear() {
        return salaryYear;
    }

    public void setSalaryYear(Integer salaryYear) {
        this.salaryYear = salaryYear;
    }

    public Status getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Status approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Double getDisbursementAmount() {
        return disbursementAmount;
    }

    public void setDisbursementAmount(Double disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Double getArrearPF() {
        return arrearPF;
    }

    public void setArrearPF(Double arrearPF) {
        this.arrearPF = arrearPF;
    }

    public Double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(Double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public Boolean isDeductTaxUponPayment() {
        return deductTaxUponPayment;
    }

    public void setDeductTaxUponPayment(Boolean deductTaxUponPayment) {
        this.deductTaxUponPayment = deductTaxUponPayment;
    }

    public Long getArrearSalaryItemId() {
        return arrearSalaryItemId;
    }

    public void setArrearSalaryItemId(Long arrearSalaryItemId) {
        this.arrearSalaryItemId = arrearSalaryItemId;
    }

    public String getArrearSalaryItemTitle() {
        return arrearSalaryItemTitle;
    }

    public void setArrearSalaryItemTitle(String arrearSalaryItemTitle) {
        this.arrearSalaryItemTitle = arrearSalaryItemTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrearPaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((ArrearPaymentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArrearPaymentDTO{" +
            "id=" + getId() +
            ", paymentType='" + getPaymentType() + "'" +
            ", disbursementDate='" + getDisbursementDate() + "'" +
            ", salaryMonth='" + getSalaryMonth() + "'" +
            ", salaryYear=" + getSalaryYear() +
            ", approvalStatus='" + getApprovalStatus() + "'" +
            ", disbursementAmount=" + getDisbursementAmount() +
            ", isDeleted='" + isIsDeleted() + "'" +
            ", arrearPF=" + getArrearPF() +
            ", taxDeduction=" + getTaxDeduction() +
            ", deductTaxUponPayment='" + isDeductTaxUponPayment() + "'" +
            ", arrearSalaryItemId=" + getArrearSalaryItemId() +
            "}";
    }
}
