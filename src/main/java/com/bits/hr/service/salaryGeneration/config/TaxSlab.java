package com.bits.hr.service.salaryGeneration.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lemon
 */

@Getter
@Setter
@NoArgsConstructor
public class TaxSlab {

    private double limit;
    private double rate;
    private String head;
    private String subHead;
}
