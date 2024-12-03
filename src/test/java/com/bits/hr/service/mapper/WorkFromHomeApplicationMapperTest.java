package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkFromHomeApplicationMapperTest {

    private WorkFromHomeApplicationMapper workFromHomeApplicationMapper;

    @BeforeEach
    public void setUp() {
        workFromHomeApplicationMapper = new WorkFromHomeApplicationMapperImpl();
    }
}
