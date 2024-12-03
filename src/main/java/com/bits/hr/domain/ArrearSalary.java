package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Month;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArrearSalary.
 */
@Entity
@Table(name = "arrear_salary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArrearSalary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = false)
    private Month month;

    @NotNull
    @Min(value = 1990)
    @Max(value = 2099)
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "9999999")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "arrear_type")
    private String arrearType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ArrearSalary id(Long id) {
        this.setId(id);
        return this;
    }

    public ArrearSalary month(Month month) {
        this.setMonth(month);
        return this;
    }

    public ArrearSalary year(Integer year) {
        this.setYear(year);
        return this;
    }

    public ArrearSalary amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public ArrearSalary arrearType(String arrearType) {
        this.setArrearType(arrearType);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ArrearSalary employee(Employee employee) {
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
        if (!(o instanceof ArrearSalary)) {
            return false;
        }
        return id != null && id.equals(((ArrearSalary) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArrearSalary{" +
            "id=" + getId() +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", amount=" + getAmount() +
            ", arrearType='" + getArrearType() + "'" +
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

    public Double getAmount() {
        return this.amount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getArrearType() {
        return this.arrearType;
    }

    public void setArrearType(String arrearType) {
        this.arrearType = arrearType;
    }
}
