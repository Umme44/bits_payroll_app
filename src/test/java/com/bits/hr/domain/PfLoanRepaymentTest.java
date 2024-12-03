package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfLoanRepaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfLoanRepayment.class);
        PfLoanRepayment pfLoanRepayment1 = new PfLoanRepayment();
        pfLoanRepayment1.setId(1L);
        PfLoanRepayment pfLoanRepayment2 = new PfLoanRepayment();
        pfLoanRepayment2.setId(pfLoanRepayment1.getId());
        assertThat(pfLoanRepayment1).isEqualTo(pfLoanRepayment2);
        pfLoanRepayment2.setId(2L);
        assertThat(pfLoanRepayment1).isNotEqualTo(pfLoanRepayment2);
        pfLoanRepayment1.setId(null);
        assertThat(pfLoanRepayment1).isNotEqualTo(pfLoanRepayment2);
    }
}
