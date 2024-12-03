package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MobileBillMapperTest {

    private MobileBillMapper mobileBillMapper;

    @BeforeEach
    public void setUp() {
        mobileBillMapper = new MobileBillMapperImpl();
    }
}
