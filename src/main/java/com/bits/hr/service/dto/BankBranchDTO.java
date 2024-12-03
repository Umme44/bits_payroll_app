package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.bits.hr.domain.BankBranch} entity.
 */
public class BankBranchDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5, max = 25)
    @ValidateNaturalText
    private String branchName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankBranchDTO)) {
            return false;
        }

        return id != null && id.equals(((BankBranchDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankBranchDTO{" +
            "id=" + getId() +
            ", branchName='" + getBranchName() + "'" +
            "}";
    }
}
