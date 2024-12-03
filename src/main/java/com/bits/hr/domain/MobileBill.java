package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MobileBill.
 */
@Entity
@Table(name = "mobile_bill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MobileBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "month")
    private Integer month;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "year")
    private Integer year;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public MobileBill id(Long id) {
        this.setId(id);
        return this;
    }

    public MobileBill month(Integer month) {
        this.setMonth(month);
        return this;
    }

    public MobileBill amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public MobileBill year(Integer year) {
        this.setYear(year);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public MobileBill employee(Employee employee) {
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
        if (!(o instanceof MobileBill)) {
            return false;
        }
        return id != null && id.equals(((MobileBill) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MobileBill{" +
            "id=" + getId() +
            ", month=" + getMonth() +
            ", amount=" + getAmount() +
            ", year=" + getYear() +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getAmount() {
        return this.amount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
