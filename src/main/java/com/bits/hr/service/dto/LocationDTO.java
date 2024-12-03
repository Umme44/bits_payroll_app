package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.LocationType;
import com.bits.hr.util.annotation.ValidateAlphaNumeric;
import com.bits.hr.util.annotation.ValidateNaturalText;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.bits.hr.domain.Location} entity.
 */
public class LocationDTO implements Serializable {

    private Long id;

    private LocationType locationType;

    @Size(max = 250)
    @ValidateNaturalText
    private String locationName;

    @Size(max = 250)
    @ValidateAlphaNumeric
    private String locationCode;

    private Long parentId;

    private String parentName;

    private String parentCode;

    private String fullLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long locationId) {
        this.parentId = locationId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getFullLocation() {
        return fullLocation;
    }

    public void setFullLocation(String fullLocation) {
        this.fullLocation = fullLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDTO)) {
            return false;
        }

        return id != null && id.equals(((LocationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", locationType='" + getLocationType() + "'" +
            ", locationName='" + getLocationName() + "'" +
            ", locationCode='" + getLocationCode() + "'" +
            ", parentId=" + getParentId() +
            "}";
    }
}
