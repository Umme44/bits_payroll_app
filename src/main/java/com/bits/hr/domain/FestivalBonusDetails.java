package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FestivalBonusDetails.
 */
@Entity
@Table(name = "festival_bonus_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FestivalBonusDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "bonus_amount", nullable = false)
    private Double bonusAmount;

    @Size(min = 0, max = 255)
    @Column(name = "remarks", length = 255)
    private String remarks;

    @NotNull
    @Column(name = "is_hold", nullable = false)
    private Boolean isHold;

    @Column(name = "basic")
    private Double basic;

    @Column(name = "gross")
    private Double gross;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne(optional = false)
    @NotNull
    private Festival festival;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FestivalBonusDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public FestivalBonusDetails bonusAmount(Double bonusAmount) {
        this.setBonusAmount(bonusAmount);
        return this;
    }

    public FestivalBonusDetails remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public FestivalBonusDetails isHold(Boolean isHold) {
        this.setIsHold(isHold);
        return this;
    }

    public FestivalBonusDetails basic(Double basic) {
        this.setBasic(basic);
        return this;
    }

    public FestivalBonusDetails gross(Double gross) {
        this.setGross(gross);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public FestivalBonusDetails employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Festival getFestival() {
        return this.festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public FestivalBonusDetails festival(Festival festival) {
        this.setFestival(festival);
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
        if (!(o instanceof FestivalBonusDetails)) {
            return false;
        }
        return id != null && id.equals(((FestivalBonusDetails) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FestivalBonusDetails{" +
            "id=" + getId() +
            ", bonusAmount=" + getBonusAmount() +
            ", remarks='" + getRemarks() + "'" +
            ", isHold='" + getIsHold() + "'" +
            ", basic=" + getBasic() +
            ", gross=" + getGross() +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBonusAmount() {
        return this.bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getIsHold() {
        return this.isHold;
    }

    public void setIsHold(Boolean isHold) {
        this.isHold = isHold;
    }

    public Double getBasic() {
        return this.basic;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setBasic(Double basic) {
        this.basic = basic;
    }

    public Double getGross() {
        return this.gross;
    }

    public void setGross(Double gross) {
        this.gross = gross;
    }
}
