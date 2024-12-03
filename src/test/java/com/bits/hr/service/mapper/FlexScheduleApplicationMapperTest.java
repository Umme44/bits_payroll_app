package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlexScheduleApplicationMapperTest {

    private FlexScheduleApplicationMapper flexScheduleApplicationMapper;

    @BeforeEach
    public void setUp() {
        flexScheduleApplicationMapper = new FlexScheduleApplicationMapperImpl();
    }
}
