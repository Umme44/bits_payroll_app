package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoldSalaryDisbursementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HoldSalaryDisbursement.class);
        HoldSalaryDisbursement holdSalaryDisbursement1 = new HoldSalaryDisbursement();
        holdSalaryDisbursement1.setId(1L);
        HoldSalaryDisbursement holdSalaryDisbursement2 = new HoldSalaryDisbursement();
        holdSalaryDisbursement2.setId(holdSalaryDisbursement1.getId());
        assertThat(holdSalaryDisbursement1).isEqualTo(holdSalaryDisbursement2);
        holdSalaryDisbursement2.setId(2L);
        assertThat(holdSalaryDisbursement1).isNotEqualTo(holdSalaryDisbursement2);
        holdSalaryDisbursement1.setId(null);
        assertThat(holdSalaryDisbursement1).isNotEqualTo(holdSalaryDisbursement2);
    }
}
