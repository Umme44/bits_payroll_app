package com.bits.hr.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A AitConfig.
 */
@Entity
@Table(name = "ait_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AitConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "tax_config")
    private String taxConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public AitConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public AitConfig startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public AitConfig endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public AitConfig taxConfig(String taxConfig) {
        this.setTaxConfig(taxConfig);
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
        if (!(o instanceof AitConfig)) {
            return false;
        }
        return id != null && id.equals(((AitConfig) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AitConfig{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", taxConfig='" + getTaxConfig() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTaxConfig() {
        return this.taxConfig;
    }

    public void setTaxConfig(String taxConfig) {
        this.taxConfig = taxConfig;
    }
}
