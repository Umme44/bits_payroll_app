package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Month;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IncomeTaxChallan.
 */
@Entity
@Table(name = "income_tax_challan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncomeTaxChallan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "challan_no", length = 200, nullable = false)
    private String challanNo;

    @NotNull
    @Column(name = "challan_date", nullable = false)
    private LocalDate challanDate;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = false)
    private Month month;

    @NotNull
    @Min(value = 1990)
    @Max(value = 2199)
    @Column(name = "year", nullable = false)
    private Integer year;

    @Size(min = 0, max = 250)
    @Column(name = "remarks", length = 250)
    private String remarks;

    @ManyToOne
    private AitConfig aitConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public IncomeTaxChallan id(Long id) {
        this.setId(id);
        return this;
    }

    public IncomeTaxChallan challanNo(String challanNo) {
        this.setChallanNo(challanNo);
        return this;
    }

    public IncomeTaxChallan challanDate(LocalDate challanDate) {
        this.setChallanDate(challanDate);
        return this;
    }

    public IncomeTaxChallan amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public IncomeTaxChallan month(Month month) {
        this.setMonth(month);
        return this;
    }

    public IncomeTaxChallan year(Integer year) {
        this.setYear(year);
        return this;
    }

    public IncomeTaxChallan remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public AitConfig getAitConfig() {
        return this.aitConfig;
    }

    public void setAitConfig(AitConfig aitConfig) {
        this.aitConfig = aitConfig;
    }

    public IncomeTaxChallan aitConfig(AitConfig aitConfig) {
        this.setAitConfig(aitConfig);
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
        if (!(o instanceof IncomeTaxChallan)) {
            return false;
        }
        return id != null && id.equals(((IncomeTaxChallan) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncomeTaxChallan{" +
            "id=" + getId() +
            ", challanNo='" + getChallanNo() + "'" +
            ", challanDate='" + getChallanDate() + "'" +
            ", amount=" + getAmount() +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChallanNo() {
        return this.challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public LocalDate getChallanDate() {
        return this.challanDate;
    }

    public void setChallanDate(LocalDate challanDate) {
        this.challanDate = challanDate;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Month getMonth() {
        return this.month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return this.year;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
