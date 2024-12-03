package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.PfRepaymentStatus;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.PfLoanRepayment} entity.
 */
public class PfLoanRepaymentDTO implements Serializable {

    private Long id;

    private Double amount;

    private PfRepaymentStatus status;

    private Integer deductionMonth;

    private Integer deductionYear;

    private LocalDate deductionDate;

    private Long pfLoanId;

    private Long pfAccountId;

    private String pfCode;

    private String designationName;

    private String departmentName;

    private String unitName;

    private String accHolderName;

    private String pin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PfRepaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PfRepaymentStatus status) {
        this.status = status;
    }

    public Integer getDeductionMonth() {
        return deductionMonth;
    }

    public void setDeductionMonth(Integer deductionMonth) {
        this.deductionMonth = deductionMonth;
    }

    public Integer getDeductionYear() {
        return deductionYear;
    }

    public void setDeductionYear(Integer deductionYear) {
        this.deductionYear = deductionYear;
    }

    public LocalDate getDeductionDate() {
        return deductionDate;
    }

    public void setDeductionDate(LocalDate deductionDate) {
        this.deductionDate = deductionDate;
    }

    public Long getPfLoanId() {
        return pfLoanId;
    }

    public void setPfLoanId(Long pfLoanId) {
        this.pfLoanId = pfLoanId;
    }

    public Long getPfAccountId() {
        return pfAccountId;
    }

    public void setPfAccountId(Long pfAccountId) {
        this.pfAccountId = pfAccountId;
    }

    public String getPfCode() {
        return pfCode;
    }

    public void setPfCode(String pfCode) {
        this.pfCode = pfCode;
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

    public String getAccHolderName() {
        return accHolderName;
    }

    public void setAccHolderName(String accHolderName) {
        this.accHolderName = accHolderName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PfLoanRepaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((PfLoanRepaymentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfLoanRepaymentDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", deductionMonth=" + getDeductionMonth() +
            ", deductionYear=" + getDeductionYear() +
            ", deductionDate='" + getDeductionDate() + "'" +
            ", pfLoanId=" + getPfLoanId() +
            "}";
    }
}
