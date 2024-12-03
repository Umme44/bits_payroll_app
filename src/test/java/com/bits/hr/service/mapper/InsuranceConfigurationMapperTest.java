package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsuranceConfigurationMapperTest {

    private InsuranceConfigurationMapper insuranceConfigurationMapper;

    @BeforeEach
    public void setUp() {
        insuranceConfigurationMapper = new InsuranceConfigurationMapperImpl();
    }
}
