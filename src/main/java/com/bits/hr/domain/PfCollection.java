package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.PfCollectionType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PfCollection.
 */
@Entity
@Table(name = "pf_collection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PfCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_contribution")
    private Double employeeContribution;

    @Column(name = "employer_contribution")
    private Double employerContribution;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Enumerated(EnumType.STRING)
    @Column(name = "collection_type")
    private PfCollectionType collectionType;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "employee_interest")
    private Double employeeInterest;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "employer_interest")
    private Double employerInterest;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "gross")
    private Double gross;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "basic")
    private Double basic;

    @ManyToOne
    private PfAccount pfAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PfCollection id(Long id) {
        this.setId(id);
        return this;
    }

    public PfCollection employeeContribution(Double employeeContribution) {
        this.setEmployeeContribution(employeeContribution);
        return this;
    }

    public PfCollection employerContribution(Double employerContribution) {
        this.setEmployerContribution(employerContribution);
        return this;
    }

    public PfCollection transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public PfCollection year(Integer year) {
        this.setYear(year);
        return this;
    }

    public PfCollection month(Integer month) {
        this.setMonth(month);
        return this;
    }

    public PfCollection collectionType(PfCollectionType collectionType) {
        this.setCollectionType(collectionType);
        return this;
    }

    public PfCollection employeeInterest(Double employeeInterest) {
        this.setEmployeeInterest(employeeInterest);
        return this;
    }

    public PfCollection employerInterest(Double employerInterest) {
        this.setEmployerInterest(employerInterest);
        return this;
    }

    public PfCollection gross(Double gross) {
        this.setGross(gross);
        return this;
    }

    public PfCollection basic(Double basic) {
        this.setBasic(basic);
        return this;
    }

    public PfAccount getPfAccount() {
        return this.pfAccount;
    }

    public void setPfAccount(PfAccount pfAccount) {
        this.pfAccount = pfAccount;
    }

    public PfCollection pfAccount(PfAccount pfAccount) {
        this.setPfAccount(pfAccount);
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
        if (!(o instanceof PfCollection)) {
            return false;
        }
        return id != null && id.equals(((PfCollection) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfCollection{" +
            "id=" + getId() +
            ", employeeContribution=" + getEmployeeContribution() +
            ", employerContribution=" + getEmployerContribution() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", year=" + getYear() +
            ", month=" + getMonth() +
            ", collectionType='" + getCollectionType() + "'" +
            ", employeeInterest=" + getEmployeeInterest() +
            ", employerInterest=" + getEmployerInterest() +
            ", gross=" + getGross() +
            ", basic=" + getBasic() +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEmployeeContribution() {
        return this.employeeContribution;
    }

    public void setEmployeeContribution(Double employeeContribution) {
        this.employeeContribution = employeeContribution;
    }

    public Double getEmployerContribution() {
        return this.employerContribution;
    }

    public void setEmployerContribution(Double employerContribution) {
        this.employerContribution = employerContribution;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public PfCollectionType getCollectionType() {
        return this.collectionType;
    }

    public void setCollectionType(PfCollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public Double getEmployeeInterest() {
        return this.employeeInterest;
    }

    public void setEmployeeInterest(Double employeeInterest) {
        this.employeeInterest = employeeInterest;
    }

    public Double getEmployerInterest() {
        return this.employerInterest;
    }

    public void setEmployerInterest(Double employerInterest) {
        this.employerInterest = employerInterest;
    }

    public Double getGross() {
        return this.gross;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setGross(Double gross) {
        this.gross = gross;
    }

    public Double getBasic() {
        return this.basic;
    }

    public void setBasic(Double basic) {
        this.basic = basic;
    }
}
