package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalaryCertificateMapperTest {

    private SalaryCertificateMapper salaryCertificateMapper;

    @BeforeEach
    public void setUp() {
        salaryCertificateMapper = new SalaryCertificateMapperImpl();
    }
}
