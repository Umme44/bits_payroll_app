package com.bits.hr.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CalenderYear.
 */
@Entity
@Table(name = "calender_year")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CalenderYear implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    @Column(name = "year", nullable = false, unique = true)
    private Integer year;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public CalenderYear id(Long id) {
        this.setId(id);
        return this;
    }

    public CalenderYear year(Integer year) {
        this.setYear(year);
        return this;
    }

    public CalenderYear startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public CalenderYear endDate(LocalDate endDate) {
        this.setEndDate(endDate);
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
        if (!(o instanceof CalenderYear)) {
            return false;
        }
        return id != null && id.equals(((CalenderYear) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CalenderYear{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
