package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcReqMapperTest {

    private ProcReqMapper procReqMapper;

    @BeforeEach
    public void setUp() {
        procReqMapper = new ProcReqMapperImpl();
    }
}
