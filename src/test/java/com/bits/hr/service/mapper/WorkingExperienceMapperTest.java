package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkingExperienceMapperTest {

    private WorkingExperienceMapper workingExperienceMapper;

    @BeforeEach
    public void setUp() {
        workingExperienceMapper = new WorkingExperienceMapperImpl();
    }
}
