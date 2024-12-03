package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.bits.hr.domain.Designation} entity.
 */
public class DesignationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 250)
    @ValidateNaturalText
    private String designationName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DesignationDTO)) {
            return false;
        }

        return id != null && id.equals(((DesignationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DesignationDTO{" +
            "id=" + getId() +
            ", designationName='" + getDesignationName() + "'" +
            "}";
    }
}
