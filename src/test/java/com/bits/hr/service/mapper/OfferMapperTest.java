package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OfferMapperTest {

    private OfferMapper offerMapper;

    @BeforeEach
    public void setUp() {
        offerMapper = new OfferMapperImpl();
    }
}
