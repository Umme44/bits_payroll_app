package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalenderYearMapperTest {

    private CalenderYearMapper calenderYearMapper;

    @BeforeEach
    public void setUp() {
        calenderYearMapper = new CalenderYearMapperImpl();
    }
}
