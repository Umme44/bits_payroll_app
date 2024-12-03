package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NomineeMapperTest {

    private NomineeMapper nomineeMapper;

    @BeforeEach
    public void setUp() {
        nomineeMapper = new NomineeMapperImpl();
    }
}
