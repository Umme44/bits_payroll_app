package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AitPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AitPayment.class);
        AitPayment aitPayment1 = new AitPayment();
        aitPayment1.setId(1L);
        AitPayment aitPayment2 = new AitPayment();
        aitPayment2.setId(aitPayment1.getId());
        assertThat(aitPayment1).isEqualTo(aitPayment2);
        aitPayment2.setId(2L);
        assertThat(aitPayment1).isNotEqualTo(aitPayment2);
        aitPayment1.setId(null);
        assertThat(aitPayment1).isNotEqualTo(aitPayment2);
    }
}
