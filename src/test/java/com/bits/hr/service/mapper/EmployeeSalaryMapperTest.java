package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeSalaryMapperTest {

    private EmployeeSalaryMapper employeeSalaryMapper;

    @BeforeEach
    public void setUp() {
        employeeSalaryMapper = new EmployeeSalaryMapperImpl();
    }
}
