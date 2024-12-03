package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttendanceMapperTest {

    private AttendanceMapper attendanceMapper;

    @BeforeEach
    public void setUp() {
        attendanceMapper = new AttendanceMapperImpl();
    }
}
