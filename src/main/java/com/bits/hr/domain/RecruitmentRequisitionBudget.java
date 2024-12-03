package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RecruitmentRequisitionBudget.
 */
@Entity
@Table(name = "recruitment_requisition_budget")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionBudget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 1970L)
    @Max(value = 2070L)
    @Column(name = "year", nullable = false)
    private Long year;

    @NotNull
    @Column(name = "budget", nullable = false)
    private Long budget;

    @NotNull
    @Column(name = "remaining_budget", nullable = false)
    private Long remainingBudget;

    @NotNull
    @Column(name = "remaining_manpower", nullable = false)
    private Long remainingManpower;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionBudgets", allowSetters = true)
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionBudgets", allowSetters = true)
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecruitmentRequisitionBudget id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getYear() {
        return this.year;
    }

    public RecruitmentRequisitionBudget year(Long year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getBudget() {
        return this.budget;
    }

    public RecruitmentRequisitionBudget budget(Long budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Long getRemainingBudget() {
        return this.remainingBudget;
    }

    public RecruitmentRequisitionBudget remainingBudget(Long remainingBudget) {
        this.setRemainingBudget(remainingBudget);
        return this;
    }

    public void setRemainingBudget(Long remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public Long getRemainingManpower() {
        return this.remainingManpower;
    }

    public RecruitmentRequisitionBudget remainingManpower(Long remainingManpower) {
        this.setRemainingManpower(remainingManpower);
        return this;
    }

    public void setRemainingManpower(Long remainingManpower) {
        this.remainingManpower = remainingManpower;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public RecruitmentRequisitionBudget employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public RecruitmentRequisitionBudget department(Department department) {
        this.setDepartment(department);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisitionBudget)) {
            return false;
        }
        return id != null && id.equals(((RecruitmentRequisitionBudget) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionBudget{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", budget=" + getBudget() +
            ", remainingBudget=" + getRemainingBudget() +
            ", remainingManpower=" + getRemainingManpower() +
            "}";
    }
}
