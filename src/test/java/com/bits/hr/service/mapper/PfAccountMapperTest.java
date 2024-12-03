package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PfAccountMapperTest {

    private PfAccountMapper pfAccountMapper;

    @BeforeEach
    public void setUp() {
        pfAccountMapper = new PfAccountMapperImpl();
    }
}
