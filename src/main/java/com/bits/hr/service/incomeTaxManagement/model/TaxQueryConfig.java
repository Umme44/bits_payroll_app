package com.bits.hr.service.incomeTaxManagement.model;

import com.bits.hr.domain.enumeration.Month;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class TaxQueryConfig {

    // 1-12
    int salaryGenMonth;

    LocalDate incomeYearStartDate;
    LocalDate incomeYearEndDate;

    int incomeYearStart;
    int incomeYearEnd;

    // custom enum type , not from java util
    List<Month> startYearMonths;
    List<Month> endYearMonths;
}
