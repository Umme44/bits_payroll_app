package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManualAttendanceEntryMapperTest {

    private ManualAttendanceEntryMapper manualAttendanceEntryMapper;

    @BeforeEach
    public void setUp() {
        manualAttendanceEntryMapper = new ManualAttendanceEntryMapperImpl();
    }
}
