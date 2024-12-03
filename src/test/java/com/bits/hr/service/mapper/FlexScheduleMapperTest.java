package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlexScheduleMapperTest {

    private FlexScheduleMapper flexScheduleMapper;

    @BeforeEach
    public void setUp() {
        flexScheduleMapper = new FlexScheduleMapperImpl();
    }
}
