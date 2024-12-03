package com.bits.hr.service.finalSettlement.dto.salary;

import lombok.Data;

@Data
public class SalaryStructure {

    private double basic;
    private double houseRent;
    private double medical;
    private double conveyance;
    private double entertainment;
    private double utility;
    private double TotalSalary;
}
