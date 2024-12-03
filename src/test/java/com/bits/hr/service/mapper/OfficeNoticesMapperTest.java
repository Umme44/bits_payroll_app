package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OfficeNoticesMapperTest {

    private OfficeNoticesMapper officeNoticesMapper;

    @BeforeEach
    public void setUp() {
        officeNoticesMapper = new OfficeNoticesMapperImpl();
    }
}
