package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HoldFbDisbursementMapperTest {

    private HoldFbDisbursementMapper holdFbDisbursementMapper;

    @BeforeEach
    public void setUp() {
        holdFbDisbursementMapper = new HoldFbDisbursementMapperImpl();
    }
}
