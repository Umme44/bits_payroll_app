package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PfArrearMapperTest {

    private PfArrearMapper pfArrearMapper;

    @BeforeEach
    public void setUp() {
        pfArrearMapper = new PfArrearMapperImpl();
    }
}
