package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AitPaymentMapperTest {

    private AitPaymentMapper aitPaymentMapper;

    @BeforeEach
    public void setUp() {
        aitPaymentMapper = new AitPaymentMapperImpl();
    }
}
