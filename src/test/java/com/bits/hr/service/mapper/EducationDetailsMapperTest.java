package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EducationDetailsMapperTest {

    private EducationDetailsMapper educationDetailsMapper;

    @BeforeEach
    public void setUp() {
        educationDetailsMapper = new EducationDetailsMapperImpl();
    }
}
