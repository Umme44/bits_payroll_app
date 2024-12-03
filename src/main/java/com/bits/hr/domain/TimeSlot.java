package com.bits.hr.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A TimeSlot.
 */
@Entity
@Table(name = "time_slot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 0, max = 250)
    @Column(name = "title", length = 250, nullable = false, unique = true)
    private String title;

    @NotNull
    @Column(name = "in_time", nullable = false)
    private Instant inTime;

    @NotNull
    @Column(name = "out_time", nullable = false)
    private Instant outTime;

    @Column(name = "is_applicable_by_employee")
    private Boolean isApplicableByEmployee;

    @Column(name = "is_default_shift")
    private Boolean isDefaultShift;

    @Size(min = 0, max = 50)
    @Column(name = "code", length = 50, unique = true)
    private String code;

    @Size(min = 0, max = 500)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "week_ends", length = 500)
    private String weekEnds;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public TimeSlot id(Long id) {
        this.setId(id);
        return this;
    }

    public TimeSlot title(String title) {
        this.setTitle(title);
        return this;
    }

    public TimeSlot inTime(Instant inTime) {
        this.setInTime(inTime);
        return this;
    }

    public TimeSlot outTime(Instant outTime) {
        this.setOutTime(outTime);
        return this;
    }

    public TimeSlot isApplicableByEmployee(Boolean isApplicableByEmployee) {
        this.setIsApplicableByEmployee(isApplicableByEmployee);
        return this;
    }

    public TimeSlot isDefaultShift(Boolean isDefaultShift) {
        this.setIsDefaultShift(isDefaultShift);
        return this;
    }

    public TimeSlot code(String code) {
        this.setCode(code);
        return this;
    }

    public TimeSlot weekEnds(String weekEnds) {
        this.setWeekEnds(weekEnds);
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
        if (!(o instanceof TimeSlot)) {
            return false;
        }
        return id != null && id.equals(((TimeSlot) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSlot{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", inTime='" + getInTime() + "'" +
            ", outTime='" + getOutTime() + "'" +
            ", isApplicableByEmployee='" + getIsApplicableByEmployee() + "'" +
            ", isDefaultShift='" + getIsDefaultShift() + "'" +
            ", code='" + getCode() + "'" +
            ", weekEnds='" + getWeekEnds() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getInTime() {
        return this.inTime;
    }

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public Instant getOutTime() {
        return this.outTime;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }

    public Boolean getIsApplicableByEmployee() {
        return this.isApplicableByEmployee;
    }

    public void setIsApplicableByEmployee(Boolean isApplicableByEmployee) {
        this.isApplicableByEmployee = isApplicableByEmployee;
    }

    public Boolean getIsDefaultShift() {
        return this.isDefaultShift;
    }

    public void setIsDefaultShift(Boolean isDefaultShift) {
        this.isDefaultShift = isDefaultShift;
    }

    public String getCode() {
        return this.code;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCode(String code) {
        this.code = code;
    }

    public String getWeekEnds() {
        return this.weekEnds;
    }

    public void setWeekEnds(String weekEnds) {
        this.weekEnds = weekEnds;
    }
}
