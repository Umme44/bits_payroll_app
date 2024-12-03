package com.bits.hr.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UnitOfMeasurement.
 */
@Entity
@Table(name = "unit_of_measurement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitOfMeasurement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Size(max = 255)
    @Column(name = "remarks", length = 255)
    private String remarks;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UnitOfMeasurement id(Long id) {
        this.setId(id);
        return this;
    }

    public UnitOfMeasurement name(String name) {
        this.setName(name);
        return this;
    }

    public UnitOfMeasurement remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public UnitOfMeasurement createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public UnitOfMeasurement updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public UnitOfMeasurement createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public UnitOfMeasurement updatedBy(User user) {
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
        if (!(o instanceof UnitOfMeasurement)) {
            return false;
        }
        return id != null && id.equals(((UnitOfMeasurement) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitOfMeasurement{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", remarks='" + getRemarks() + "'" +
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
