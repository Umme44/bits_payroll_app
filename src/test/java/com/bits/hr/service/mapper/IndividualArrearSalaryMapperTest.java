package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndividualArrearSalaryMapperTest {

    private IndividualArrearSalaryMapper individualArrearSalaryMapper;

    @BeforeEach
    public void setUp() {
        individualArrearSalaryMapper = new IndividualArrearSalaryMapperImpl();
    }
}
