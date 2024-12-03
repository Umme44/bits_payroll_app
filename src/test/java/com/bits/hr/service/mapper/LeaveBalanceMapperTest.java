package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaveBalanceMapperTest {

    private LeaveBalanceMapper leaveBalanceMapper;

    @BeforeEach
    public void setUp() {
        leaveBalanceMapper = new LeaveBalanceMapperImpl();
    }
}
