package com.bits.hr.service.dto;

import javax.validation.constraints.NotNull;

public class SalaryGenerationDTO {

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;

    @NotNull
    private Long designationId;

    @NotNull
    private Long departmentId;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return (
            "SalaryGenerationDTO{" +
            "year=" +
            year +
            ", month='" +
            month +
            '\'' +
            ", designationId=" +
            designationId +
            ", departmentId=" +
            departmentId +
            '}'
        );
    }
}
