package com.bits.hr.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeductionType.
 */
@Entity
@Table(name = "deduction_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeductionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 250)
    @Column(name = "name", length = 250, nullable = false, unique = true)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public DeductionType id(Long id) {
        this.setId(id);
        return this;
    }

    public DeductionType name(String name) {
        this.setName(name);
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
        if (!(o instanceof DeductionType)) {
            return false;
        }
        return id != null && id.equals(((DeductionType) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeductionType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
