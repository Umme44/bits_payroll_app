package com.bits.hr.service.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualArrearSalaryGroupDataDTO {

    private String title;
    private LocalDate effectiveDate;
    private LocalDate effectiveFrom;
}
