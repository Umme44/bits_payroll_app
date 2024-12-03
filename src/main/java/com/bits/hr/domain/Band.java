package com.bits.hr.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Band.
 */
@Entity
@Table(name = "band")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Band implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "band_name", length = 250, nullable = false, unique = true)
    private String bandName;

    @NotNull
    @Column(name = "min_salary", nullable = false)
    private Double minSalary;

    @NotNull
    @Column(name = "max_salary", nullable = false)
    private Double maxSalary;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100000")
    @Column(name = "welfare_fund")
    private Double welfareFund;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100000")
    @Column(name = "mobile_celling")
    private Double mobileCelling;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Band id(Long id) {
        this.setId(id);
        return this;
    }

    public Band bandName(String bandName) {
        this.setBandName(bandName);
        return this;
    }

    public Band minSalary(Double minSalary) {
        this.setMinSalary(minSalary);
        return this;
    }

    public Band maxSalary(Double maxSalary) {
        this.setMaxSalary(maxSalary);
        return this;
    }

    public Band welfareFund(Double welfareFund) {
        this.setWelfareFund(welfareFund);
        return this;
    }

    public Band mobileCelling(Double mobileCelling) {
        this.setMobileCelling(mobileCelling);
        return this;
    }

    public Band createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Band updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Band createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public Band updatedBy(User user) {
        this.setUpdatedBy(user);
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
        if (!(o instanceof Band)) {
            return false;
        }
        return id != null && id.equals(((Band) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Band{" +
            "id=" + getId() +
            ", bandName='" + getBandName() + "'" +
            ", minSalary=" + getMinSalary() +
            ", maxSalary=" + getMaxSalary() +
            ", welfareFund=" + getWelfareFund() +
            ", mobileCelling=" + getMobileCelling() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBandName() {
        return this.bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName.trim();
    }

    public Double getMinSalary() {
        return this.minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getMaxSalary() {
        return this.maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Double getWelfareFund() {
        return this.welfareFund;
    }

    public void setWelfareFund(Double welfareFund) {
        this.welfareFund = welfareFund;
    }

    public Double getMobileCelling() {
        return this.mobileCelling;
    }

    public void setMobileCelling(Double mobileCelling) {
        this.mobileCelling = mobileCelling;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
