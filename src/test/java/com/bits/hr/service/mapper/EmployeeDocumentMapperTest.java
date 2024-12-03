package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeDocumentMapperTest {

    private EmployeeDocumentMapper employeeDocumentMapper;

    @BeforeEach
    public void setUp() {
        employeeDocumentMapper = new EmployeeDocumentMapperImpl();
    }
}
