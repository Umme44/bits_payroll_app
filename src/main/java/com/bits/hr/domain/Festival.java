package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Religion;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Festival.
 */
@Entity
@Table(name = "festival")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Festival implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "title", length = 255, nullable = false, unique = true)
    private String title;

    @Size(min = 0, max = 255)
    @Column(name = "festival_name", length = 255)
    private String festivalName;

    @Column(name = "festival_date")
    private LocalDate festivalDate;

    @NotNull
    @Column(name = "bonus_disbursement_date", nullable = false)
    private LocalDate bonusDisbursementDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "religion", nullable = false)
    private Religion religion;

    @NotNull
    @Column(name = "is_pro_rata", nullable = false)
    private Boolean isProRata;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Festival id(Long id) {
        this.setId(id);
        return this;
    }

    public Festival title(String title) {
        this.setTitle(title);
        return this;
    }

    public Festival festivalName(String festivalName) {
        this.setFestivalName(festivalName);
        return this;
    }

    public Festival festivalDate(LocalDate festivalDate) {
        this.setFestivalDate(festivalDate);
        return this;
    }

    public Festival bonusDisbursementDate(LocalDate bonusDisbursementDate) {
        this.setBonusDisbursementDate(bonusDisbursementDate);
        return this;
    }

    public Festival religion(Religion religion) {
        this.setReligion(religion);
        return this;
    }

    public Festival isProRata(Boolean isProRata) {
        this.setIsProRata(isProRata);
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
        if (!(o instanceof Festival)) {
            return false;
        }
        return id != null && id.equals(((Festival) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Festival{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", festivalName='" + getFestivalName() + "'" +
            ", festivalDate='" + getFestivalDate() + "'" +
            ", bonusDisbursementDate='" + getBonusDisbursementDate() + "'" +
            ", religion='" + getReligion() + "'" +
            ", isProRata='" + getIsProRata() + "'" +
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

    public String getFestivalName() {
        return this.festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public LocalDate getFestivalDate() {
        return this.festivalDate;
    }

    public void setFestivalDate(LocalDate festivalDate) {
        this.festivalDate = festivalDate;
    }

    public LocalDate getBonusDisbursementDate() {
        return this.bonusDisbursementDate;
    }

    public void setBonusDisbursementDate(LocalDate bonusDisbursementDate) {
        this.bonusDisbursementDate = bonusDisbursementDate;
    }

    public Religion getReligion() {
        return this.religion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Boolean getIsProRata() {
        return this.isProRata;
    }

    public void setIsProRata(Boolean isProRata) {
        this.isProRata = isProRata;
    }
}
