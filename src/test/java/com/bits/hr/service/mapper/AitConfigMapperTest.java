package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AitConfigMapperTest {

    private AitConfigMapper aitConfigMapper;

    @BeforeEach
    public void setUp() {
        aitConfigMapper = new AitConfigMapperImpl();
    }
}
