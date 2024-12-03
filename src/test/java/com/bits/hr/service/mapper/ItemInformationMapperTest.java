package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemInformationMapperTest {

    private ItemInformationMapper itemInformationMapper;

    @BeforeEach
    public void setUp() {
        itemInformationMapper = new ItemInformationMapperImpl();
    }
}
