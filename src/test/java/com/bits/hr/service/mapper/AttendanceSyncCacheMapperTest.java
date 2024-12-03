package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttendanceSyncCacheMapperTest {

    private AttendanceSyncCacheMapper attendanceSyncCacheMapper;

    @BeforeEach
    public void setUp() {
        attendanceSyncCacheMapper = new AttendanceSyncCacheMapperImpl();
    }
}
