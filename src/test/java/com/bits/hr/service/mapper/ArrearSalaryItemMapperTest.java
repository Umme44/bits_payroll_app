package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrearSalaryItemMapperTest {

    private ArrearSalaryItemMapper arrearSalaryItemMapper;

    @BeforeEach
    public void setUp() {
        arrearSalaryItemMapper = new ArrearSalaryItemMapperImpl();
    }
}
