package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArrearSalaryItem.
 */
@Entity
@Table(name = "arrear_salary_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArrearSalaryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "arrear_amount", nullable = false)
    private Double arrearAmount;

    @Column(name = "has_pf_arrear_deduction")
    private Boolean hasPfArrearDeduction;

    @Column(name = "pf_arrear_deduction")
    private Double pfArrearDeduction;

    @Column(name = "is_festival_bonus")
    private Boolean isFestivalBonus;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne(optional = false)
    @NotNull
    private ArrearSalaryMaster arrearSalaryMaster;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ArrearSalaryItem id(Long id) {
        this.setId(id);
        return this;
    }

    public ArrearSalaryItem title(String title) {
        this.setTitle(title);
        return this;
    }

    public ArrearSalaryItem arrearAmount(Double arrearAmount) {
        this.setArrearAmount(arrearAmount);
        return this;
    }

    public ArrearSalaryItem hasPfArrearDeduction(Boolean hasPfArrearDeduction) {
        this.setHasPfArrearDeduction(hasPfArrearDeduction);
        return this;
    }

    public ArrearSalaryItem pfArrearDeduction(Double pfArrearDeduction) {
        this.setPfArrearDeduction(pfArrearDeduction);
        return this;
    }

    public ArrearSalaryItem isFestivalBonus(Boolean isFestivalBonus) {
        this.setIsFestivalBonus(isFestivalBonus);
        return this;
    }

    public ArrearSalaryItem isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ArrearSalaryMaster getArrearSalaryMaster() {
        return this.arrearSalaryMaster;
    }

    public void setArrearSalaryMaster(ArrearSalaryMaster arrearSalaryMaster) {
        this.arrearSalaryMaster = arrearSalaryMaster;
    }

    public ArrearSalaryItem arrearSalaryMaster(ArrearSalaryMaster arrearSalaryMaster) {
        this.setArrearSalaryMaster(arrearSalaryMaster);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ArrearSalaryItem employee(Employee employee) {
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
        if (!(o instanceof ArrearSalaryItem)) {
            return false;
        }
        return id != null && id.equals(((ArrearSalaryItem) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArrearSalaryItem{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", arrearAmount=" + getArrearAmount() +
            ", hasPfArrearDeduction='" + getHasPfArrearDeduction() + "'" +
            ", pfArrearDeduction=" + getPfArrearDeduction() +
            ", isFestivalBonus='" + getIsFestivalBonus() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getArrearAmount() {
        return this.arrearAmount;
    }

    public void setArrearAmount(Double arrearAmount) {
        this.arrearAmount = arrearAmount;
    }

    public Boolean getHasPfArrearDeduction() {
        return this.hasPfArrearDeduction;
    }

    public void setHasPfArrearDeduction(Boolean hasPfArrearDeduction) {
        this.hasPfArrearDeduction = hasPfArrearDeduction;
    }

    public Double getPfArrearDeduction() {
        return this.pfArrearDeduction;
    }

    public void setPfArrearDeduction(Double pfArrearDeduction) {
        this.pfArrearDeduction = pfArrearDeduction;
    }

    public Boolean getIsFestivalBonus() {
        return this.isFestivalBonus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setIsFestivalBonus(Boolean isFestivalBonus) {
        this.isFestivalBonus = isFestivalBonus;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
