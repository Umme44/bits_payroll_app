package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncomeTaxChallanMapperTest {

    private IncomeTaxChallanMapper incomeTaxChallanMapper;

    @BeforeEach
    public void setUp() {
        incomeTaxChallanMapper = new IncomeTaxChallanMapperImpl();
    }
}
