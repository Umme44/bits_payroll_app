package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArrearPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArrearPayment.class);
        ArrearPayment arrearPayment1 = new ArrearPayment();
        arrearPayment1.setId(1L);
        ArrearPayment arrearPayment2 = new ArrearPayment();
        arrearPayment2.setId(arrearPayment1.getId());
        assertThat(arrearPayment1).isEqualTo(arrearPayment2);
        arrearPayment2.setId(2L);
        assertThat(arrearPayment1).isNotEqualTo(arrearPayment2);
        arrearPayment1.setId(null);
        assertThat(arrearPayment1).isNotEqualTo(arrearPayment2);
    }
}
