package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrearPaymentMapperTest {

    private ArrearPaymentMapper arrearPaymentMapper;

    @BeforeEach
    public void setUp() {
        arrearPaymentMapper = new ArrearPaymentMapperImpl();
    }
}
