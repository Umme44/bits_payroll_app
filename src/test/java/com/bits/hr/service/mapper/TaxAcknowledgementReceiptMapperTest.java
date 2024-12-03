package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaxAcknowledgementReceiptMapperTest {

    private TaxAcknowledgementReceiptMapper taxAcknowledgementReceiptMapper;

    @BeforeEach
    public void setUp() {
        taxAcknowledgementReceiptMapper = new TaxAcknowledgementReceiptMapperImpl();
    }
}
