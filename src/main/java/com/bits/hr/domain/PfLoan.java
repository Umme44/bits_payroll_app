package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.PfLoanStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PfLoan.
 */
@Entity
@Table(name = "pf_loan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PfLoan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "disbursement_amount")
    private Double disbursementAmount;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_branch")
    private String bankBranch;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "cheque_number")
    private String chequeNumber;

    @Column(name = "instalment_number")
    private String instalmentNumber;

    @Column(name = "installment_amount")
    private Double installmentAmount;

    @Column(name = "instalment_start_from")
    private LocalDate instalmentStartFrom;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PfLoanStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "recommendedBy", "approvedBy", "rejectedBy", "pfAccount" }, allowSetters = true)
    private PfLoanApplication pfLoanApplication;

    @ManyToOne
    private PfAccount pfAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PfLoan id(Long id) {
        this.setId(id);
        return this;
    }

    public PfLoan disbursementAmount(Double disbursementAmount) {
        this.setDisbursementAmount(disbursementAmount);
        return this;
    }

    public PfLoan disbursementDate(LocalDate disbursementDate) {
        this.setDisbursementDate(disbursementDate);
        return this;
    }

    public PfLoan bankName(String bankName) {
        this.setBankName(bankName);
        return this;
    }

    public PfLoan bankBranch(String bankBranch) {
        this.setBankBranch(bankBranch);
        return this;
    }

    public PfLoan bankAccountNumber(String bankAccountNumber) {
        this.setBankAccountNumber(bankAccountNumber);
        return this;
    }

    public PfLoan chequeNumber(String chequeNumber) {
        this.setChequeNumber(chequeNumber);
        return this;
    }

    public PfLoan instalmentNumber(String instalmentNumber) {
        this.setInstalmentNumber(instalmentNumber);
        return this;
    }

    public PfLoan installmentAmount(Double installmentAmount) {
        this.setInstallmentAmount(installmentAmount);
        return this;
    }

    public PfLoan instalmentStartFrom(LocalDate instalmentStartFrom) {
        this.setInstalmentStartFrom(instalmentStartFrom);
        return this;
    }

    public PfLoan status(PfLoanStatus status) {
        this.setStatus(status);
        return this;
    }

    public PfLoanApplication getPfLoanApplication() {
        return this.pfLoanApplication;
    }

    public void setPfLoanApplication(PfLoanApplication pfLoanApplication) {
        this.pfLoanApplication = pfLoanApplication;
    }

    public PfLoan pfLoanApplication(PfLoanApplication pfLoanApplication) {
        this.setPfLoanApplication(pfLoanApplication);
        return this;
    }

    public PfAccount getPfAccount() {
        return this.pfAccount;
    }

    public void setPfAccount(PfAccount pfAccount) {
        this.pfAccount = pfAccount;
    }

    public PfLoan pfAccount(PfAccount pfAccount) {
        this.setPfAccount(pfAccount);
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
        if (!(o instanceof PfLoan)) {
            return false;
        }
        return id != null && id.equals(((PfLoan) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfLoan{" +
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
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDisbursementAmount() {
        return this.disbursementAmount;
    }

    public void setDisbursementAmount(Double disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public LocalDate getDisbursementDate() {
        return this.disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranch() {
        return this.bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankAccountNumber() {
        return this.bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getChequeNumber() {
        return this.chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getInstalmentNumber() {
        return this.instalmentNumber;
    }

    public void setInstalmentNumber(String instalmentNumber) {
        this.instalmentNumber = instalmentNumber;
    }

    public Double getInstallmentAmount() {
        return this.installmentAmount;
    }

    public void setInstallmentAmount(Double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public LocalDate getInstalmentStartFrom() {
        return this.instalmentStartFrom;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setInstalmentStartFrom(LocalDate instalmentStartFrom) {
        this.instalmentStartFrom = instalmentStartFrom;
    }

    public PfLoanStatus getStatus() {
        return this.status;
    }

    public void setStatus(PfLoanStatus status) {
        this.status = status;
    }
}
