package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

/**
 * A DTO for the {@link com.bits.hr.domain.Unit} entity.
 */
public class UnitDTO implements Serializable {

    private Long id;

    @NotBlank
    @ValidateNaturalText
    private String unitName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitDTO)) {
            return false;
        }

        return id != null && id.equals(((UnitDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitDTO{" +
            "id=" + getId() +
            ", unitName='" + getUnitName() + "'" +
            "}";
    }
}
