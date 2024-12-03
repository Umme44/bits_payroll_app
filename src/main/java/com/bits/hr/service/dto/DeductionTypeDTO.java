package com.bits.hr.service.dto;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.DeductionType} entity.
 */
public class DeductionTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 250)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeductionTypeDTO)) {
            return false;
        }

        return id != null && id.equals(((DeductionTypeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeductionTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
