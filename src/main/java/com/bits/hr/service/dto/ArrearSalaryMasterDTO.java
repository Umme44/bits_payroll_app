package com.bits.hr.service.dto;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.ArrearSalaryMaster} entity.
 */
public class ArrearSalaryMasterDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 250)
    private String title;

    @NotNull
    private Boolean isLocked;

    @NotNull
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrearSalaryMasterDTO)) {
            return false;
        }

        return id != null && id.equals(((ArrearSalaryMasterDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArrearSalaryMasterDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", isLocked='" + isIsLocked() + "'" +
            ", isDeleted='" + isIsDeleted() + "'" +
            "}";
    }
}
