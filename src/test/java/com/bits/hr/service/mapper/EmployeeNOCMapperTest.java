package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeNOCMapperTest {

    private EmployeeNOCMapper employeeNOCMapper;

    @BeforeEach
    public void setUp() {
        employeeNOCMapper = new EmployeeNOCMapperImpl();
    }
}
