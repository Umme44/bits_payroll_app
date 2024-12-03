package com.bits.hr.service.specializedSearch.dto;

import java.util.List;
import lombok.Data;

@Data
public class EmployeeSpecializedSearch {

    private long id;
    private String fullName;
    private String pin;
    private String officialEmail;
    private String officialContactNo;
    private String whatsappId;
    private String skypeId;
    private String designationName;
    private String departmentName;
    private String unitName;
    private Long reportingToId;
    private String reportingToName;

    private List<EmployeeSpecializedSearch> organizationalHierarchy;
    private List<EmployeeSpecializedSearch> peopleReportingTo;
}
