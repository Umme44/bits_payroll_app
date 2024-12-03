package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.LocationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    private LocationType locationType;

    @Size(max = 250)
    @Column(name = "location_name", length = 250)
    private String locationName;

    @Column(name = "has_parent")
    private Boolean hasParent = false;

    @Column(name = "is_last_child")
    private Boolean isLastChild = true;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "created_by_id")
    private Long createdById;

    @Column(name = "updated_by_id")
    private Long updatedById;

    @Size(max = 250)
    @Column(name = "location_code", length = 250)
    private String locationCode;

    @ManyToOne
    @JsonIgnoreProperties(value = "locations", allowSetters = true)
    private Location parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public Location locationType(LocationType locationType) {
        this.locationType = locationType;
        return this;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getLocationName() {
        return locationName;
    }

    public Location locationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Boolean isHasParent() {
        return hasParent;
    }

    public Location hasParent(Boolean hasParent) {
        this.hasParent = hasParent;
        return this;
    }

    public void setHasParent(Boolean hasParent) {
        this.hasParent = hasParent;
    }

    public Boolean isIsLastChild() {
        return isLastChild;
    }

    public Location isLastChild(Boolean isLastChild) {
        this.isLastChild = isLastChild;
        return this;
    }

    public void setIsLastChild(Boolean isLastChild) {
        this.isLastChild = isLastChild;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Location createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public Location updateAt(Instant updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public Location createdById(Long createdById) {
        this.createdById = createdById;
        return this;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public Location updatedById(Long updatedById) {
        this.updatedById = updatedById;
        return this;
    }

    public void setUpdatedById(Long updatedById) {
        this.updatedById = updatedById;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public Location locationCode(String locationCode) {
        this.locationCode = locationCode;
        return this;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public Location getParent() {
        return parent;
    }

    public Location parent(Location location) {
        this.parent = location;
        return this;
    }

    public void setParent(Location location) {
        this.parent = location;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", locationType='" + getLocationType() + "'" +
            ", locationName='" + getLocationName() + "'" +
            ", hasParent='" + isHasParent() + "'" +
            ", isLastChild='" + isIsLastChild() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", createdById=" + getCreatedById() +
            ", updatedById=" + getUpdatedById() +
            ", locationCode='" + getLocationCode() + "'" +
            "}";
    }
}
