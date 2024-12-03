package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcReqMasterMapperTest {

    private ProcReqMasterMapper procReqMasterMapper;

    @BeforeEach
    public void setUp() {
        procReqMasterMapper = new ProcReqMasterMapperImpl();
    }
}
