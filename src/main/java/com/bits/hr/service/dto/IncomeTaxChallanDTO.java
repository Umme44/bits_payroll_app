package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.util.annotation.ValidateAlphaNumeric;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.IncomeTaxChallan} entity.
 */
public class IncomeTaxChallanDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    @ValidateAlphaNumeric
    private String challanNo;

    @NotNull
    private LocalDate challanDate;

    @NotNull
    private Double amount;

    @NotNull
    private Month month;

    @NotNull
    @Min(value = 1990)
    @Max(value = 2199)
    private Integer year;

    @Size(min = 0, max = 250)
    @ValidateNaturalText
    private String remarks;

    private Long aitConfigId;

    private LocalDate startDate;
    private LocalDate endDate;

    private int startYear;

    private int endYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public LocalDate getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(LocalDate challanDate) {
        this.challanDate = challanDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getAitConfigId() {
        return aitConfigId;
    }

    public void setAitConfigId(Long aitConfigId) {
        this.aitConfigId = aitConfigId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncomeTaxChallanDTO)) {
            return false;
        }

        return id != null && id.equals(((IncomeTaxChallanDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncomeTaxChallanDTO{" +
            "id=" + getId() +
            ", challanNo='" + getChallanNo() + "'" +
            ", challanDate='" + getChallanDate() + "'" +
            ", amount=" + getAmount() +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", remarks='" + getRemarks() + "'" +
            ", aitConfigId=" + getAitConfigId() +
            "}";
    }
}
