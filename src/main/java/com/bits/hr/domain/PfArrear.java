package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Month;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PfArrear.
 */
@Entity
@Table(name = "pf_arrear")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PfArrear implements Serializable {

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
    @Min(value = 1900)
    @Max(value = 2100)
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Size(min = 3, max = 250)
    @Column(name = "remarks", length = 250, nullable = false)
    private String remarks;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PfArrear id(Long id) {
        this.setId(id);
        return this;
    }

    public PfArrear month(Month month) {
        this.setMonth(month);
        return this;
    }

    public PfArrear year(Integer year) {
        this.setYear(year);
        return this;
    }

    public PfArrear amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public PfArrear remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PfArrear employee(Employee employee) {
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
        if (!(o instanceof PfArrear)) {
            return false;
        }
        return id != null && id.equals(((PfArrear) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfArrear{" +
            "id=" + getId() +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", amount=" + getAmount() +
            ", remarks='" + getRemarks() + "'" +
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

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
