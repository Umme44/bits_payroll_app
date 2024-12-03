package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SalaryDeduction.
 */
@Entity
@Table(name = "salary_deduction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalaryDeduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000000")
    @Column(name = "deduction_amount", nullable = false)
    private Double deductionAmount;

    @NotNull
    @Min(value = 1990)
    @Max(value = 2100)
    @Column(name = "deduction_year", nullable = false)
    private Integer deductionYear;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "deduction_month", nullable = false)
    private Integer deductionMonth;

    @ManyToOne(optional = false)
    @NotNull
    private DeductionType deductionType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SalaryDeduction id(Long id) {
        this.setId(id);
        return this;
    }

    public SalaryDeduction deductionAmount(Double deductionAmount) {
        this.setDeductionAmount(deductionAmount);
        return this;
    }

    public SalaryDeduction deductionYear(Integer deductionYear) {
        this.setDeductionYear(deductionYear);
        return this;
    }

    public SalaryDeduction deductionMonth(Integer deductionMonth) {
        this.setDeductionMonth(deductionMonth);
        return this;
    }

    public DeductionType getDeductionType() {
        return this.deductionType;
    }

    public void setDeductionType(DeductionType deductionType) {
        this.deductionType = deductionType;
    }

    public SalaryDeduction deductionType(DeductionType deductionType) {
        this.setDeductionType(deductionType);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public SalaryDeduction employee(Employee employee) {
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
        if (!(o instanceof SalaryDeduction)) {
            return false;
        }
        return id != null && id.equals(((SalaryDeduction) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryDeduction{" +
            "id=" + getId() +
            ", deductionAmount=" + getDeductionAmount() +
            ", deductionYear=" + getDeductionYear() +
            ", deductionMonth=" + getDeductionMonth() +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDeductionAmount() {
        return this.deductionAmount;
    }

    public void setDeductionAmount(Double deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public Integer getDeductionYear() {
        return this.deductionYear;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setDeductionYear(Integer deductionYear) {
        this.deductionYear = deductionYear;
    }

    public Integer getDeductionMonth() {
        return this.deductionMonth;
    }

    public void setDeductionMonth(Integer deductionMonth) {
        this.deductionMonth = deductionMonth;
    }
}
