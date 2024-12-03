package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinalSettlementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinalSettlement.class);
        FinalSettlement finalSettlement1 = new FinalSettlement();
        finalSettlement1.setId(1L);
        FinalSettlement finalSettlement2 = new FinalSettlement();
        finalSettlement2.setId(finalSettlement1.getId());
        assertThat(finalSettlement1).isEqualTo(finalSettlement2);
        finalSettlement2.setId(2L);
        assertThat(finalSettlement1).isNotEqualTo(finalSettlement2);
        finalSettlement1.setId(null);
        assertThat(finalSettlement1).isNotEqualTo(finalSettlement2);
    }
}
