package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnitOfMeasurementMapperTest {

    private UnitOfMeasurementMapper unitOfMeasurementMapper;

    @BeforeEach
    public void setUp() {
        unitOfMeasurementMapper = new UnitOfMeasurementMapperImpl();
    }
}
