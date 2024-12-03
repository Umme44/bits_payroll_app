package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.PfLoan} entity.
 */
public class PfLoanDTO implements Serializable {

    private Long id;

    private Double disbursementAmount;

    private LocalDate disbursementDate;

    private String bankName;

    private String bankBranch;

    private String bankAccountNumber;

    private String chequeNumber;

    private String instalmentNumber;

    private Double installmentAmount;

    private LocalDate instalmentStartFrom;

    private PfLoanStatus status;

    private Long pfLoanApplicationId;

    private Long pfAccountId;

    private String pfCode;

    private PfAccountStatus accountStatus;

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

    public Double getDisbursementAmount() {
        return disbursementAmount;
    }

    public void setDisbursementAmount(Double disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getInstalmentNumber() {
        return instalmentNumber;
    }

    public void setInstalmentNumber(String instalmentNumber) {
        this.instalmentNumber = instalmentNumber;
    }

    public Double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public LocalDate getInstalmentStartFrom() {
        return instalmentStartFrom;
    }

    public void setInstalmentStartFrom(LocalDate instalmentStartFrom) {
        this.instalmentStartFrom = instalmentStartFrom;
    }

    public PfLoanStatus getStatus() {
        return status;
    }

    public void setStatus(PfLoanStatus status) {
        this.status = status;
    }

    public Long getPfLoanApplicationId() {
        return pfLoanApplicationId;
    }

    public void setPfLoanApplicationId(Long pfLoanApplicationId) {
        this.pfLoanApplicationId = pfLoanApplicationId;
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

    public PfAccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(PfAccountStatus accountStatus) {
        this.accountStatus = accountStatus;
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
        if (!(o instanceof PfLoanDTO)) {
            return false;
        }

        return id != null && id.equals(((PfLoanDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfLoanDTO{" +
            "id=" + getId() +
            ", disbursementAmount=" + getDisbursementAmount() +
            ", disbursementDate='" + getDisbursementDate() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", bankBranch='" + getBankBranch() + "'" +
            ", bankAccountNumber='" + getBankAccountNumber() + "'" +
            ", chequeNumber='" + getChequeNumber() + "'" +
            ", instalmentNumber='" + getInstalmentNumber() + "'" +
            ", installmentAmount=" + getInstallmentAmount() +
            ", instalmentStartFrom='" + getInstalmentStartFrom() + "'" +
            ", status='" + getStatus() + "'" +
            ", pfLoanApplicationId=" + getPfLoanApplicationId() +
            ", pfAccountId=" + getPfAccountId() +
            "}";
    }
}
