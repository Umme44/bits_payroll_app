package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.bits.hr.domain.Band} entity.
 */
public class BandDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 250)
    @ValidateNaturalText
    private String bandName;

    @NotNull
    private Double minSalary;

    @NotNull
    private Double maxSalary;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100000")
    private Double welfareFund;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100000")
    private Double mobileCelling;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Double getWelfareFund() {
        return welfareFund;
    }

    public void setWelfareFund(Double welfareFund) {
        this.welfareFund = welfareFund;
    }

    public Double getMobileCelling() {
        return mobileCelling;
    }

    public void setMobileCelling(Double mobileCelling) {
        this.mobileCelling = mobileCelling;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long userId) {
        this.updatedById = userId;
    }

    public String getUpdatedByLogin() {
        return updatedByLogin;
    }

    public void setUpdatedByLogin(String userLogin) {
        this.updatedByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BandDTO)) {
            return false;
        }

        return id != null && id.equals(((BandDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BandDTO{" +
            "id=" + getId() +
            ", bandName='" + getBandName() + "'" +
            ", minSalary=" + getMinSalary() +
            ", maxSalary=" + getMaxSalary() +
            ", welfareFund=" + getWelfareFund() +
            ", mobileCelling=" + getMobileCelling() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            "}";
    }
}
