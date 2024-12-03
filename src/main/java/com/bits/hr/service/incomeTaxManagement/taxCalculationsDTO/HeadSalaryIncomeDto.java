package com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeadSalaryIncomeDto {

    int grossSalary;
    int getExemption;
    int getTaxable;
}
