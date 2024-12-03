package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.HolidayType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Holidays.
 */
@Entity
@Table(name = "holidays")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Holidays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "holiday_type")
    private HolidayType holidayType;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_moon_dependent")
    private Boolean isMoonDependent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Holidays id(Long id) {
        this.setId(id);
        return this;
    }

    public Holidays holidayType(HolidayType holidayType) {
        this.setHolidayType(holidayType);
        return this;
    }

    public Holidays description(String description) {
        this.setDescription(description);
        return this;
    }

    public Holidays startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public Holidays endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public Holidays isMoonDependent(Boolean isMoonDependent) {
        this.setIsMoonDependent(isMoonDependent);
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
        if (!(o instanceof Holidays)) {
            return false;
        }
        return id != null && id.equals(((Holidays) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Holidays{" +
            "id=" + getId() +
            ", holidayType='" + getHolidayType() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isMoonDependent='" + getIsMoonDependent() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HolidayType getHolidayType() {
        return this.holidayType;
    }

    public void setHolidayType(HolidayType holidayType) {
        this.holidayType = holidayType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getIsMoonDependent() {
        return this.isMoonDependent;
    }

    public void setIsMoonDependent(Boolean isMoonDependent) {
        this.isMoonDependent = isMoonDependent;
    }
}
