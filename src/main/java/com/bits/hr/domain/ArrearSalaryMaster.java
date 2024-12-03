package com.bits.hr.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArrearSalaryMaster.
 */
@Entity
@Table(name = "arrear_salary_master")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArrearSalaryMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 250)
    @Column(name = "title", length = 250, nullable = false, unique = true)
    private String title;

    @NotNull
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ArrearSalaryMaster id(Long id) {
        this.setId(id);
        return this;
    }

    public ArrearSalaryMaster title(String title) {
        this.setTitle(title);
        return this;
    }

    public ArrearSalaryMaster isLocked(Boolean isLocked) {
        this.setIsLocked(isLocked);
        return this;
    }

    public ArrearSalaryMaster isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
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
        if (!(o instanceof ArrearSalaryMaster)) {
            return false;
        }
        return id != null && id.equals(((ArrearSalaryMaster) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArrearSalaryMaster{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", isLocked='" + getIsLocked() + "'" +
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

    public Boolean getIsLocked() {
        return this.isLocked;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
