package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalaryDeductionMapperTest {

    private SalaryDeductionMapper salaryDeductionMapper;

    @BeforeEach
    public void setUp() {
        salaryDeductionMapper = new SalaryDeductionMapperImpl();
    }
}
