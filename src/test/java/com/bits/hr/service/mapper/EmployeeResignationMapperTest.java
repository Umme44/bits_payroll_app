package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeResignationMapperTest {

    private EmployeeResignationMapper employeeResignationMapper;

    @BeforeEach
    public void setUp() {
        employeeResignationMapper = new EmployeeResignationMapperImpl();
    }
}
