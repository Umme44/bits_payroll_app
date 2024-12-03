package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttendanceEntryMapperTest {

    private AttendanceEntryMapper attendanceEntryMapper;

    @BeforeEach
    public void setUp() {
        attendanceEntryMapper = new AttendanceEntryMapperImpl();
    }
}
