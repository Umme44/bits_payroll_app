package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.PfRepaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PfLoanRepayment.
 */
@Entity
@Table(name = "pf_loan_repayment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PfLoanRepayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PfRepaymentStatus status;

    @Column(name = "deduction_month")
    private Integer deductionMonth;

    @Column(name = "deduction_year")
    private Integer deductionYear;

    @Column(name = "deduction_date")
    private LocalDate deductionDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pfLoanApplication", "pfAccount" }, allowSetters = true)
    private PfLoan pfLoan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PfLoanRepayment id(Long id) {
        this.setId(id);
        return this;
    }

    public PfLoanRepayment amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public PfLoanRepayment status(PfRepaymentStatus status) {
        this.setStatus(status);
        return this;
    }

    public PfLoanRepayment deductionMonth(Integer deductionMonth) {
        this.setDeductionMonth(deductionMonth);
        return this;
    }

    public PfLoanRepayment deductionYear(Integer deductionYear) {
        this.setDeductionYear(deductionYear);
        return this;
    }

    public PfLoanRepayment deductionDate(LocalDate deductionDate) {
        this.setDeductionDate(deductionDate);
        return this;
    }

    public PfLoan getPfLoan() {
        return this.pfLoan;
    }

    public void setPfLoan(PfLoan pfLoan) {
        this.pfLoan = pfLoan;
    }

    public PfLoanRepayment pfLoan(PfLoan pfLoan) {
        this.setPfLoan(pfLoan);
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
        if (!(o instanceof PfLoanRepayment)) {
            return false;
        }
        return id != null && id.equals(((PfLoanRepayment) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfLoanRepayment{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", deductionMonth=" + getDeductionMonth() +
            ", deductionYear=" + getDeductionYear() +
            ", deductionDate='" + getDeductionDate() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PfRepaymentStatus getStatus() {
        return this.status;
    }

    public void setStatus(PfRepaymentStatus status) {
        this.status = status;
    }

    public Integer getDeductionMonth() {
        return this.deductionMonth;
    }

    public void setDeductionMonth(Integer deductionMonth) {
        this.deductionMonth = deductionMonth;
    }

    public Integer getDeductionYear() {
        return this.deductionYear;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setDeductionYear(Integer deductionYear) {
        this.deductionYear = deductionYear;
    }

    public LocalDate getDeductionDate() {
        return this.deductionDate;
    }

    public void setDeductionDate(LocalDate deductionDate) {
        this.deductionDate = deductionDate;
    }
}
