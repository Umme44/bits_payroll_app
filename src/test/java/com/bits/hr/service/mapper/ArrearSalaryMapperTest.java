package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrearSalaryMapperTest {

    private ArrearSalaryMapper arrearSalaryMapper;

    @BeforeEach
    public void setUp() {
        arrearSalaryMapper = new ArrearSalaryMapperImpl();
    }
}
