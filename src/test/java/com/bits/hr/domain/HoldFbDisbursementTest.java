package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoldFbDisbursementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HoldFbDisbursement.class);
        HoldFbDisbursement holdFbDisbursement1 = new HoldFbDisbursement();
        holdFbDisbursement1.setId(1L);
        HoldFbDisbursement holdFbDisbursement2 = new HoldFbDisbursement();
        holdFbDisbursement2.setId(holdFbDisbursement1.getId());
        assertThat(holdFbDisbursement1).isEqualTo(holdFbDisbursement2);
        holdFbDisbursement2.setId(2L);
        assertThat(holdFbDisbursement1).isNotEqualTo(holdFbDisbursement2);
        holdFbDisbursement1.setId(null);
        assertThat(holdFbDisbursement1).isNotEqualTo(holdFbDisbursement2);
    }
}
