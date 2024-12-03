package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PfLoanApplicationMapperTest {

    private PfLoanApplicationMapper pfLoanApplicationMapper;

    @BeforeEach
    public void setUp() {
        pfLoanApplicationMapper = new PfLoanApplicationMapperImpl();
    }
}
