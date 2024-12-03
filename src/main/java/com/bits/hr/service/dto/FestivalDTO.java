package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Religion;
import com.bits.hr.util.annotation.ValidateNaturalText;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.Festival} entity.
 */
public class FestivalDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    @ValidateNaturalText
    private String title;

    @Size(min = 0, max = 255)
    private String festivalName;

    private LocalDate festivalDate;

    @NotNull
    private LocalDate bonusDisbursementDate;

    @NotNull
    private Religion religion;

    @NotNull
    private Boolean isProRata;

    private int numberOfBonus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public LocalDate getFestivalDate() {
        return festivalDate;
    }

    public void setFestivalDate(LocalDate festivalDate) {
        this.festivalDate = festivalDate;
    }

    public LocalDate getBonusDisbursementDate() {
        return bonusDisbursementDate;
    }

    public void setBonusDisbursementDate(LocalDate bonusDisbursementDate) {
        this.bonusDisbursementDate = bonusDisbursementDate;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Boolean isIsProRata() {
        return isProRata;
    }

    public void setIsProRata(Boolean isProRata) {
        this.isProRata = isProRata;
    }

    public int getNumberOfBonus() {
        return numberOfBonus;
    }

    public void setNumberOfBonus(int numberOfBonus) {
        this.numberOfBonus = numberOfBonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FestivalDTO)) {
            return false;
        }

        return id != null && id.equals(((FestivalDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FestivalDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", festivalName='" + getFestivalName() + "'" +
            ", festivalDate='" + getFestivalDate() + "'" +
            ", bonusDisbursementDate='" + getBonusDisbursementDate() + "'" +
            ", religion='" + getReligion() + "'" +
            ", isProRata='" + isIsProRata() + "'" +
            "}";
    }
}
