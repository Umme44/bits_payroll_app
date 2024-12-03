package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MobileBillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileBill.class);
        MobileBill mobileBill1 = new MobileBill();
        mobileBill1.setId(1L);
        MobileBill mobileBill2 = new MobileBill();
        mobileBill2.setId(mobileBill1.getId());
        assertThat(mobileBill1).isEqualTo(mobileBill2);
        mobileBill2.setId(2L);
        assertThat(mobileBill1).isNotEqualTo(mobileBill2);
        mobileBill1.setId(null);
        assertThat(mobileBill1).isNotEqualTo(mobileBill2);
    }
}
