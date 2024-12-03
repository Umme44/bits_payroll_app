package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PfLoanRepaymentMapperTest {

    private PfLoanRepaymentMapper pfLoanRepaymentMapper;

    @BeforeEach
    public void setUp() {
        pfLoanRepaymentMapper = new PfLoanRepaymentMapperImpl();
    }
}
