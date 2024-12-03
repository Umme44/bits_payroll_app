package com.bits.hr.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Building.
 */
@Entity
@Table(name = "building")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 0, max = 250)
    @Column(name = "building_name", length = 250, nullable = false, unique = true)
    private String buildingName;

    @Size(min = 0, max = 250)
    @Column(name = "building_location", length = 250)
    private String buildingLocation;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Building id(Long id) {
        this.setId(id);
        return this;
    }

    public Building buildingName(String buildingName) {
        this.setBuildingName(buildingName);
        return this;
    }

    public Building buildingLocation(String buildingLocation) {
        this.setBuildingLocation(buildingLocation);
        return this;
    }

    public Building remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public Building createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Building updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Building createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public Building updatedBy(User user) {
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
        if (!(o instanceof Building)) {
            return false;
        }
        return id != null && id.equals(((Building) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Building{" +
            "id=" + getId() +
            ", buildingName='" + getBuildingName() + "'" +
            ", buildingLocation='" + getBuildingLocation() + "'" +
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

    public String getBuildingName() {
        return this.buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingLocation() {
        return this.buildingLocation;
    }

    public void setBuildingLocation(String buildingLocation) {
        this.buildingLocation = buildingLocation;
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
