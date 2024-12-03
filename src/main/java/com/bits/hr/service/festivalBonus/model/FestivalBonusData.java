package com.bits.hr.service.festivalBonus.model;

import com.bits.hr.domain.Employee;
import lombok.Data;

@Data
public class FestivalBonusData {

    Employee employee;
    double effectiveGrossOnThatTime;
    boolean isHold = false;
}
