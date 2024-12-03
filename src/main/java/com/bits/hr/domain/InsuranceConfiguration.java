package com.bits.hr.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InsuranceConfiguration.
 */
@Entity
@Table(name = "insurance_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "max_total_claim_limit_per_year", nullable = false)
    private Double maxTotalClaimLimitPerYear;

    @Column(name = "max_allowed_child_age")
    private Double maxAllowedChildAge;

    @Column(name = "insurance_claim_link")
    private String insuranceClaimLink;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public InsuranceConfiguration id(Long id) {
        this.setId(id);
        return this;
    }

    public InsuranceConfiguration maxTotalClaimLimitPerYear(Double maxTotalClaimLimitPerYear) {
        this.setMaxTotalClaimLimitPerYear(maxTotalClaimLimitPerYear);
        return this;
    }

    public InsuranceConfiguration maxAllowedChildAge(Double maxAllowedChildAge) {
        this.setMaxAllowedChildAge(maxAllowedChildAge);
        return this;
    }

    public InsuranceConfiguration insuranceClaimLink(String insuranceClaimLink) {
        this.setInsuranceClaimLink(insuranceClaimLink);
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
        if (!(o instanceof InsuranceConfiguration)) {
            return false;
        }
        return id != null && id.equals(((InsuranceConfiguration) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceConfiguration{" +
            "id=" + getId() +
            ", maxTotalClaimLimitPerYear=" + getMaxTotalClaimLimitPerYear() +
            ", maxAllowedChildAge=" + getMaxAllowedChildAge() +
            ", insuranceClaimLink='" + getInsuranceClaimLink() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMaxTotalClaimLimitPerYear() {
        return this.maxTotalClaimLimitPerYear;
    }

    public void setMaxTotalClaimLimitPerYear(Double maxTotalClaimLimitPerYear) {
        this.maxTotalClaimLimitPerYear = maxTotalClaimLimitPerYear;
    }

    public Double getMaxAllowedChildAge() {
        return this.maxAllowedChildAge;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setMaxAllowedChildAge(Double maxAllowedChildAge) {
        this.maxAllowedChildAge = maxAllowedChildAge;
    }

    public String getInsuranceClaimLink() {
        return this.insuranceClaimLink;
    }

    public void setInsuranceClaimLink(String insuranceClaimLink) {
        this.insuranceClaimLink = insuranceClaimLink;
    }
}
