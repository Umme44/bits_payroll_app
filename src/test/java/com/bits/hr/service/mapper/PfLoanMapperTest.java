package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PfLoanMapperTest {

    private PfLoanMapper pfLoanMapper;

    @BeforeEach
    public void setUp() {
        pfLoanMapper = new PfLoanMapperImpl();
    }
}
