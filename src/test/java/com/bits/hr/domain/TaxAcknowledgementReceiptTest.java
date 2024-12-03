package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxAcknowledgementReceiptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxAcknowledgementReceipt.class);
        TaxAcknowledgementReceipt taxAcknowledgementReceipt1 = new TaxAcknowledgementReceipt();
        taxAcknowledgementReceipt1.setId(1L);
        TaxAcknowledgementReceipt taxAcknowledgementReceipt2 = new TaxAcknowledgementReceipt();
        taxAcknowledgementReceipt2.setId(taxAcknowledgementReceipt1.getId());
        assertThat(taxAcknowledgementReceipt1).isEqualTo(taxAcknowledgementReceipt2);
        taxAcknowledgementReceipt2.setId(2L);
        assertThat(taxAcknowledgementReceipt1).isNotEqualTo(taxAcknowledgementReceipt2);
        taxAcknowledgementReceipt1.setId(null);
        assertThat(taxAcknowledgementReceipt1).isNotEqualTo(taxAcknowledgementReceipt2);
    }
}
