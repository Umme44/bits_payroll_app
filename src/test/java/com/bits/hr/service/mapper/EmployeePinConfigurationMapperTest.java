package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeePinConfigurationMapperTest {

    private EmployeePinConfigurationMapper employeePinConfigurationMapper;

    @BeforeEach
    public void setUp() {
        employeePinConfigurationMapper = new EmployeePinConfigurationMapperImpl();
    }
}
