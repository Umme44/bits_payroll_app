package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovementEntryMapperTest {

    private MovementEntryMapper movementEntryMapper;

    @BeforeEach
    public void setUp() {
        movementEntryMapper = new MovementEntryMapperImpl();
    }
}
