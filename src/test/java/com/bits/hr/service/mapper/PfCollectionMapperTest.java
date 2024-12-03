package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PfCollectionMapperTest {

    private PfCollectionMapper pfCollectionMapper;

    @BeforeEach
    public void setUp() {
        pfCollectionMapper = new PfCollectionMapperImpl();
    }
}
