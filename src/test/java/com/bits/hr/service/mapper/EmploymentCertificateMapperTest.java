package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmploymentCertificateMapperTest {

    private EmploymentCertificateMapper employmentCertificateMapper;

    @BeforeEach
    public void setUp() {
        employmentCertificateMapper = new EmploymentCertificateMapperImpl();
    }
}
