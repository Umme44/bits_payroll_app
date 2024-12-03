package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HoldSalaryDisbursement.
 */
@Entity
@Table(name = "hold_salary_disbursement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HoldSalaryDisbursement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private EmployeeSalary employeeSalary;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public HoldSalaryDisbursement id(Long id) {
        this.setId(id);
        return this;
    }

    public HoldSalaryDisbursement date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HoldSalaryDisbursement user(User user) {
        this.setUser(user);
        return this;
    }

    public EmployeeSalary getEmployeeSalary() {
        return this.employeeSalary;
    }

    public void setEmployeeSalary(EmployeeSalary employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public HoldSalaryDisbursement employeeSalary(EmployeeSalary employeeSalary) {
        this.setEmployeeSalary(employeeSalary);
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
        if (!(o instanceof HoldSalaryDisbursement)) {
            return false;
        }
        return id != null && id.equals(((HoldSalaryDisbursement) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HoldSalaryDisbursement{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
