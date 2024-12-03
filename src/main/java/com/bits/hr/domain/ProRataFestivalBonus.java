package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProRataFestivalBonus.
 */
@Entity
@Table(name = "pro_rata_festival_bonus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProRataFestivalBonus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ProRataFestivalBonus id(Long id) {
        this.setId(id);
        return this;
    }

    public ProRataFestivalBonus date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public ProRataFestivalBonus amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public ProRataFestivalBonus description(String description) {
        this.setDescription(description);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ProRataFestivalBonus employee(Employee employee) {
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
        if (!(o instanceof ProRataFestivalBonus)) {
            return false;
        }
        return id != null && id.equals(((ProRataFestivalBonus) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProRataFestivalBonus{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return this.amount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
