package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsuranceClaimMapperTest {

    private InsuranceClaimMapper insuranceClaimMapper;

    @BeforeEach
    public void setUp() {
        insuranceClaimMapper = new InsuranceClaimMapperImpl();
    }
}
