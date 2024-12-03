package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaveAllocationMapperTest {

    private LeaveAllocationMapper leaveAllocationMapper;

    @BeforeEach
    public void setUp() {
        leaveAllocationMapper = new LeaveAllocationMapperImpl();
    }
}
