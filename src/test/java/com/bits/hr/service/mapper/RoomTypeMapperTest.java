package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTypeMapperTest {

    private RoomTypeMapper roomTypeMapper;

    @BeforeEach
    public void setUp() {
        roomTypeMapper = new RoomTypeMapperImpl();
    }
}
