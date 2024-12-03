package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FestivalBonusConfigMapperTest {

    private FestivalBonusConfigMapper festivalBonusConfigMapper;

    @BeforeEach
    public void setUp() {
        festivalBonusConfigMapper = new FestivalBonusConfigMapperImpl();
    }
}
