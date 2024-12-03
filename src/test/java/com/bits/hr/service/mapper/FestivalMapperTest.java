package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FestivalMapperTest {

    private FestivalMapper festivalMapper;

    @BeforeEach
    public void setUp() {
        festivalMapper = new FestivalMapperImpl();
    }
}
