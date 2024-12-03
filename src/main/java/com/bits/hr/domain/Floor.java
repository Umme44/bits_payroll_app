package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Floor.
 */
@Entity
@Table(name = "floor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 0, max = 250)
    @Column(name = "floor_name", length = 250, nullable = false)
    private String floorName;

    @Size(min = 0, max = 250)
    @Column(name = "remarks", length = 250)
    private String remarks;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "createdBy", "updatedBy" }, allowSetters = true)
    private Building building;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Floor id(Long id) {
        this.setId(id);
        return this;
    }

    public Floor floorName(String floorName) {
        this.setFloorName(floorName);
        return this;
    }

    public Floor remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public Floor createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Floor updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Floor createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public Floor updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public Building getBuilding() {
        return this.building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Floor building(Building building) {
        this.setBuilding(building);
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
        if (!(o instanceof Floor)) {
            return false;
        }
        return id != null && id.equals(((Floor) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Floor{" +
            "id=" + getId() +
            ", floorName='" + getFloorName() + "'" +
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

    public String getFloorName() {
        return this.floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
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
