package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpecialShiftTimingMapperTest {

    private SpecialShiftTimingMapper specialShiftTimingMapper;

    @BeforeEach
    public void setUp() {
        specialShiftTimingMapper = new SpecialShiftTimingMapperImpl();
    }
}
