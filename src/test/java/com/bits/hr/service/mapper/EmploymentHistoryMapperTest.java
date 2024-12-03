package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmploymentHistoryMapperTest {

    private EmploymentHistoryMapper employmentHistoryMapper;

    @BeforeEach
    public void setUp() {
        employmentHistoryMapper = new EmploymentHistoryMapperImpl();
    }
}
