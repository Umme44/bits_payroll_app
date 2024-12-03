package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FloorMapperTest {

    private FloorMapper floorMapper;

    @BeforeEach
    public void setUp() {
        floorMapper = new FloorMapperImpl();
    }
}
