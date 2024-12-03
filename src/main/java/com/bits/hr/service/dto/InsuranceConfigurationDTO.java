package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNumeric;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.InsuranceConfiguration} entity.
 */
public class InsuranceConfigurationDTO implements Serializable {

    private Long id;

    @NotNull
    private Double maxTotalClaimLimitPerYear;

    private Double maxAllowedChildAge;

    @ValidateNumeric
    private String insuranceClaimLink;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMaxTotalClaimLimitPerYear() {
        return maxTotalClaimLimitPerYear;
    }

    public void setMaxTotalClaimLimitPerYear(Double maxTotalClaimLimitPerYear) {
        this.maxTotalClaimLimitPerYear = maxTotalClaimLimitPerYear;
    }

    public Double getMaxAllowedChildAge() {
        return maxAllowedChildAge;
    }

    public void setMaxAllowedChildAge(Double maxAllowedChildAge) {
        this.maxAllowedChildAge = maxAllowedChildAge;
    }

    public String getInsuranceClaimLink() {
        return insuranceClaimLink;
    }

    public void setInsuranceClaimLink(String insuranceClaimLink) {
        this.insuranceClaimLink = insuranceClaimLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceConfigurationDTO)) {
            return false;
        }

        return id != null && id.equals(((InsuranceConfigurationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceConfigurationDTO{" +
            "id=" + getId() +
            ", maxTotalClaimLimitPerYear=" + getMaxTotalClaimLimitPerYear() +
            ", maxAllowedChildAge=" + getMaxAllowedChildAge() +
            ", insuranceClaimLink='" + getInsuranceClaimLink() + "'" +
            "}";
    }
}
