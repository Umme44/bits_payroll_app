package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeePinMapperTest {

    private EmployeePinMapper employeePinMapper;

    @BeforeEach
    public void setUp() {
        employeePinMapper = new EmployeePinMapperImpl();
    }
}
