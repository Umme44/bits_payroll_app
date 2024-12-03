package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FestivalBonusDetailsMapperTest {

    private FestivalBonusDetailsMapper festivalBonusDetailsMapper;

    @BeforeEach
    public void setUp() {
        festivalBonusDetailsMapper = new FestivalBonusDetailsMapperImpl();
    }
}
