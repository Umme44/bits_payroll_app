package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FestivalBonusConfig.
 */
@Entity
@Table(name = "festival_bonus_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FestivalBonusConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_category", nullable = false, unique = true)
    private EmployeeCategory employeeCategory;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "5000")
    @Column(name = "percentage_from_gross", nullable = false)
    private Double percentageFromGross;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FestivalBonusConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public FestivalBonusConfig employeeCategory(EmployeeCategory employeeCategory) {
        this.setEmployeeCategory(employeeCategory);
        return this;
    }

    public FestivalBonusConfig percentageFromGross(Double percentageFromGross) {
        this.setPercentageFromGross(percentageFromGross);
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
        if (!(o instanceof FestivalBonusConfig)) {
            return false;
        }
        return id != null && id.equals(((FestivalBonusConfig) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FestivalBonusConfig{" +
            "id=" + getId() +
            ", employeeCategory='" + getEmployeeCategory() + "'" +
            ", percentageFromGross=" + getPercentageFromGross() +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeCategory getEmployeeCategory() {
        return this.employeeCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public Double getPercentageFromGross() {
        return this.percentageFromGross;
    }

    public void setPercentageFromGross(Double percentageFromGross) {
        this.percentageFromGross = percentageFromGross;
    }
}
