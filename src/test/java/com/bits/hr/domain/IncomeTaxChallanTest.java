package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncomeTaxChallanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomeTaxChallan.class);
        IncomeTaxChallan incomeTaxChallan1 = new IncomeTaxChallan();
        incomeTaxChallan1.setId(1L);
        IncomeTaxChallan incomeTaxChallan2 = new IncomeTaxChallan();
        incomeTaxChallan2.setId(incomeTaxChallan1.getId());
        assertThat(incomeTaxChallan1).isEqualTo(incomeTaxChallan2);
        incomeTaxChallan2.setId(2L);
        assertThat(incomeTaxChallan1).isNotEqualTo(incomeTaxChallan2);
        incomeTaxChallan1.setId(null);
        assertThat(incomeTaxChallan1).isNotEqualTo(incomeTaxChallan2);
    }
}
