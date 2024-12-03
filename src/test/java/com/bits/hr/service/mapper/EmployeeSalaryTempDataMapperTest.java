package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeSalaryTempDataMapperTest {

    private EmployeeSalaryTempDataMapper employeeSalaryTempDataMapper;

    @BeforeEach
    public void setUp() {
        employeeSalaryTempDataMapper = new EmployeeSalaryTempDataMapperImpl();
    }
}
