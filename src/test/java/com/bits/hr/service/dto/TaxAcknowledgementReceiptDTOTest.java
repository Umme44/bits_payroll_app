package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxAcknowledgementReceiptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxAcknowledgementReceiptDTO.class);
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO1 = new TaxAcknowledgementReceiptDTO();
        taxAcknowledgementReceiptDTO1.setId(1L);
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO2 = new TaxAcknowledgementReceiptDTO();
        assertThat(taxAcknowledgementReceiptDTO1).isNotEqualTo(taxAcknowledgementReceiptDTO2);
        taxAcknowledgementReceiptDTO2.setId(taxAcknowledgementReceiptDTO1.getId());
        assertThat(taxAcknowledgementReceiptDTO1).isEqualTo(taxAcknowledgementReceiptDTO2);
        taxAcknowledgementReceiptDTO2.setId(2L);
        assertThat(taxAcknowledgementReceiptDTO1).isNotEqualTo(taxAcknowledgementReceiptDTO2);
        taxAcknowledgementReceiptDTO1.setId(null);
        assertThat(taxAcknowledgementReceiptDTO1).isNotEqualTo(taxAcknowledgementReceiptDTO2);
    }
}
