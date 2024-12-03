package com.bits.hr.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Nationality.
 */
@Entity
@Table(name = "nationality")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Nationality implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 5, max = 25)
    @Column(name = "nationality_name", length = 25, nullable = false, unique = true)
    private String nationalityName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Nationality id(Long id) {
        this.setId(id);
        return this;
    }

    public Nationality nationalityName(String nationalityName) {
        this.setNationalityName(nationalityName);
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
        if (!(o instanceof Nationality)) {
            return false;
        }
        return id != null && id.equals(((Nationality) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nationality{" +
            "id=" + getId() +
            ", nationalityName='" + getNationalityName() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationalityName() {
        return this.nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }
}
