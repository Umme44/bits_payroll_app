package com.bits.hr.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Unit.
 */
@Entity
@Table(name = "unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Unit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "unit_name", nullable = false, unique = true)
    private String unitName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Unit id(Long id) {
        this.setId(id);
        return this;
    }

    public Unit unitName(String unitName) {
        this.setUnitName(unitName);
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
        if (!(o instanceof Unit)) {
            return false;
        }
        return id != null && id.equals(((Unit) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Unit{" +
            "id=" + getId() +
            ", unitName='" + getUnitName() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
