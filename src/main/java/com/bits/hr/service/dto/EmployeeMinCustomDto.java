package com.bits.hr.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeMinCustomDto {

    private Long id;
    private String name;
    private String pin;
    private String departmentName;
    private String designationName;
    private String unitName;
    private String officialEmail;
    private String officialPhone;
    private EmployeeMinCustomDto reportingTo;
}
