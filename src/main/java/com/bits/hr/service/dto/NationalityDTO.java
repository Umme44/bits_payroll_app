package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.bits.hr.domain.Nationality} entity.
 */
public class NationalityDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5, max = 25)
    @ValidateNaturalText
    private String nationalityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NationalityDTO)) {
            return false;
        }

        return id != null && id.equals(((NationalityDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NationalityDTO{" +
            "id=" + getId() +
            ", nationalityName='" + getNationalityName() + "'" +
            "}";
    }
}
