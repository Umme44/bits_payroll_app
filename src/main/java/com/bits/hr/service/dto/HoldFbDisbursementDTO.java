package com.bits.hr.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.HoldFbDisbursement} entity.
 */
public class HoldFbDisbursementDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate disbursedAt;

    @Size(min = 0, max = 255)
    private String remarks;

    private Long disbursedById;

    private String disbursedByLogin;

    private Long festivalBonusDetailId;

    private String employeeName;

    private String pin;

    private Double bonusAmount;

    private String festivalTitle;

    private String festivalName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDisbursedAt() {
        return disbursedAt;
    }

    public void setDisbursedAt(LocalDate disbursedAt) {
        this.disbursedAt = disbursedAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getDisbursedById() {
        return disbursedById;
    }

    public void setDisbursedById(Long userId) {
        this.disbursedById = userId;
    }

    public String getDisbursedByLogin() {
        return disbursedByLogin;
    }

    public void setDisbursedByLogin(String userLogin) {
        this.disbursedByLogin = userLogin;
    }

    public Long getFestivalBonusDetailId() {
        return festivalBonusDetailId;
    }

    public void setFestivalBonusDetailId(Long festivalBonusDetailsId) {
        this.festivalBonusDetailId = festivalBonusDetailsId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getFestivalTitle() {
        return festivalTitle;
    }

    public void setFestivalTitle(String festivalTitle) {
        this.festivalTitle = festivalTitle;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HoldFbDisbursementDTO)) {
            return false;
        }

        return id != null && id.equals(((HoldFbDisbursementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HoldFbDisbursementDTO{" +
            "id=" + id +
            ", disbursedAt=" + disbursedAt +
            ", remarks='" + remarks + '\'' +
            ", disbursedById=" + disbursedById +
            ", disbursedByLogin='" + disbursedByLogin + '\'' +
            ", festivalBonusDetailId=" + festivalBonusDetailId +
            ", employeeName='" + employeeName + '\'' +
            ", pin='" + pin + '\'' +
            ", bonusAmount=" + bonusAmount +
            ", festivalTitle='" + festivalTitle + '\'' +
            ", festivalName='" + festivalName + '\'' +
            '}';
    }
}
