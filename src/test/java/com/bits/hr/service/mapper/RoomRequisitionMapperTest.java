package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomRequisitionMapperTest {

    private RoomRequisitionMapper roomRequisitionMapper;

    @BeforeEach
    public void setUp() {
        roomRequisitionMapper = new RoomRequisitionMapperImpl();
    }
}
