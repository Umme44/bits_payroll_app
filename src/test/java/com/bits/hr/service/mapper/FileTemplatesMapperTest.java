package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileTemplatesMapperTest {

    private FileTemplatesMapper fileTemplatesMapper;

    @BeforeEach
    public void setUp() {
        fileTemplatesMapper = new FileTemplatesMapperImpl();
    }
}
