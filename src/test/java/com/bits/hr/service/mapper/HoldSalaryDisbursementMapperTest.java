package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HoldSalaryDisbursementMapperTest {

    private HoldSalaryDisbursementMapper holdSalaryDisbursementMapper;

    @BeforeEach
    public void setUp() {
        holdSalaryDisbursementMapper = new HoldSalaryDisbursementMapperImpl();
    }
}
