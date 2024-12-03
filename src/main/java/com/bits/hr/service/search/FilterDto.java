package com.bits.hr.service.search;

import com.bits.hr.util.annotation.ValidateSearchText;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

public class FilterDto {

    private Long destinationId;
    private Long departmentId;
    private Long unitId;

    @ValidateSearchText
    private String searchText;
    private String bloodGroup;
    private String gender;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer year;
    private Month month;

    public FilterDto() {}

    public FilterDto(Long destinationId, Long departmentId, Long unitId, String searchText, String bloodGroup, String gender) {
        this.destinationId = destinationId;
        this.departmentId = departmentId;
        this.unitId = unitId;
        this.searchText = searchText.trim() + "";
        this.bloodGroup = bloodGroup;
        this.gender = gender;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText.trim();
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterDto)) return false;
        FilterDto filterDto = (FilterDto) o;
        return (
            getDestinationId().equals(filterDto.getDestinationId()) &&
            getDepartmentId().equals(filterDto.getDepartmentId()) &&
            getUnitId().equals(filterDto.getUnitId()) &&
            getSearchText().equals(filterDto.getSearchText()) &&
            getBloodGroup().equals(filterDto.getBloodGroup()) &&
            getGender().equals(filterDto.getGender())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDestinationId(), getDepartmentId(), getUnitId(), getSearchText(), getBloodGroup(), getGender());
    }
}
