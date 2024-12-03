package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IndividualArrearSalary.
 */
@Entity
@Table(name = "individual_arrear_salary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndividualArrearSalary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "existing_band")
    private String existingBand;

    @Column(name = "new_band")
    private String newBand;

    @Column(name = "existing_gross")
    private Double existingGross;

    @Column(name = "new_gross")
    private Double newGross;

    @Column(name = "increment")
    private Double increment;

    @Column(name = "arrear_salary")
    private Double arrearSalary;

    @Column(name = "arrear_pf_deduction")
    private Double arrearPfDeduction;

    @Column(name = "tax_deduction")
    private Double taxDeduction;

    @Column(name = "net_pay")
    private Double netPay;

    @Column(name = "pf_contribution")
    private Double pfContribution;

    @Column(name = "title")
    private String title;

    @Column(name = "title_effective_from")
    private String titleEffectiveFrom;

    @Column(name = "arrear_remarks")
    private String arrearRemarks;

    @Column(name = "festival_bonus")
    private Double festivalBonus;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public IndividualArrearSalary id(Long id) {
        this.setId(id);
        return this;
    }

    public IndividualArrearSalary effectiveDate(LocalDate effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public IndividualArrearSalary existingBand(String existingBand) {
        this.setExistingBand(existingBand);
        return this;
    }

    public IndividualArrearSalary newBand(String newBand) {
        this.setNewBand(newBand);
        return this;
    }

    public IndividualArrearSalary existingGross(Double existingGross) {
        this.setExistingGross(existingGross);
        return this;
    }

    public IndividualArrearSalary newGross(Double newGross) {
        this.setNewGross(newGross);
        return this;
    }

    public IndividualArrearSalary increment(Double increment) {
        this.setIncrement(increment);
        return this;
    }

    public IndividualArrearSalary arrearSalary(Double arrearSalary) {
        this.setArrearSalary(arrearSalary);
        return this;
    }

    public IndividualArrearSalary arrearPfDeduction(Double arrearPfDeduction) {
        this.setArrearPfDeduction(arrearPfDeduction);
        return this;
    }

    public IndividualArrearSalary taxDeduction(Double taxDeduction) {
        this.setTaxDeduction(taxDeduction);
        return this;
    }

    public IndividualArrearSalary netPay(Double netPay) {
        this.setNetPay(netPay);
        return this;
    }

    public IndividualArrearSalary pfContribution(Double pfContribution) {
        this.setPfContribution(pfContribution);
        return this;
    }

    public IndividualArrearSalary title(String title) {
        this.setTitle(title);
        return this;
    }

    public IndividualArrearSalary titleEffectiveFrom(String titleEffectiveFrom) {
        this.setTitleEffectiveFrom(titleEffectiveFrom);
        return this;
    }

    public IndividualArrearSalary arrearRemarks(String arrearRemarks) {
        this.setArrearRemarks(arrearRemarks);
        return this;
    }

    public IndividualArrearSalary festivalBonus(Double festivalBonus) {
        this.setFestivalBonus(festivalBonus);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public IndividualArrearSalary employee(Employee employee) {
        this.setEmployee(employee);
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
        if (!(o instanceof IndividualArrearSalary)) {
            return false;
        }
        return id != null && id.equals(((IndividualArrearSalary) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndividualArrearSalary{" +
            "id=" + getId() +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", existingBand='" + getExistingBand() + "'" +
            ", newBand='" + getNewBand() + "'" +
            ", existingGross=" + getExistingGross() +
            ", newGross=" + getNewGross() +
            ", increment=" + getIncrement() +
            ", arrearSalary=" + getArrearSalary() +
            ", arrearPfDeduction=" + getArrearPfDeduction() +
            ", taxDeduction=" + getTaxDeduction() +
            ", netPay=" + getNetPay() +
            ", pfContribution=" + getPfContribution() +
            ", title='" + getTitle() + "'" +
            ", titleEffectiveFrom='" + getTitleEffectiveFrom() + "'" +
            ", arrearRemarks='" + getArrearRemarks() + "'" +
            ", festivalBonus=" + getFestivalBonus() +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExistingBand() {
        return this.existingBand;
    }

    public void setExistingBand(String existingBand) {
        this.existingBand = existingBand;
    }

    public String getNewBand() {
        return this.newBand;
    }

    public void setNewBand(String newBand) {
        this.newBand = newBand;
    }

    public Double getExistingGross() {
        return this.existingGross;
    }

    public void setExistingGross(Double existingGross) {
        this.existingGross = existingGross;
    }

    public Double getNewGross() {
        return this.newGross;
    }

    public void setNewGross(Double newGross) {
        this.newGross = newGross;
    }

    public Double getIncrement() {
        return this.increment;
    }

    public void setIncrement(Double increment) {
        this.increment = increment;
    }

    public Double getArrearSalary() {
        return this.arrearSalary;
    }

    public void setArrearSalary(Double arrearSalary) {
        this.arrearSalary = arrearSalary;
    }

    public Double getArrearPfDeduction() {
        return this.arrearPfDeduction;
    }

    public void setArrearPfDeduction(Double arrearPfDeduction) {
        this.arrearPfDeduction = arrearPfDeduction;
    }

    public Double getTaxDeduction() {
        return this.taxDeduction;
    }

    public void setTaxDeduction(Double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public Double getNetPay() {
        return this.netPay;
    }

    public void setNetPay(Double netPay) {
        this.netPay = netPay;
    }

    public Double getPfContribution() {
        return this.pfContribution;
    }

    public void setPfContribution(Double pfContribution) {
        this.pfContribution = pfContribution;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEffectiveFrom() {
        return this.titleEffectiveFrom;
    }

    public void setTitleEffectiveFrom(String titleEffectiveFrom) {
        this.titleEffectiveFrom = titleEffectiveFrom;
    }

    public String getArrearRemarks() {
        return this.arrearRemarks;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setArrearRemarks(String arrearRemarks) {
        this.arrearRemarks = arrearRemarks;
    }

    public Double getFestivalBonus() {
        return this.festivalBonus;
    }

    public void setFestivalBonus(Double festivalBonus) {
        this.festivalBonus = festivalBonus;
    }
}
