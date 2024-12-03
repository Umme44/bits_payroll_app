package com.bits.hr.service.dto;

import lombok.Data;

@Data
public class EmployeeDashboardAnalyticsDTO {

    private int totalEmployee;
    private int totalActiveEmployee;
    private int totalInActiveEmployee;
    private int activeRegularConfirmed;
    private int activeRegularProbation;
    private int activeByContract;
    private int activeIntern;
    private int totalResigned;
    private int pendingResigned;
    private int approvedResigned;
}
