package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReferencesMapperTest {

    private ReferencesMapper referencesMapper;

    @BeforeEach
    public void setUp() {
        referencesMapper = new ReferencesMapperImpl();
    }
}
