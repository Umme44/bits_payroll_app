package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuildingMapperTest {

    private BuildingMapper buildingMapper;

    @BeforeEach
    public void setUp() {
        buildingMapper = new BuildingMapperImpl();
    }
}
