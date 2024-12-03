package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfLoanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfLoan.class);
        PfLoan pfLoan1 = new PfLoan();
        pfLoan1.setId(1L);
        PfLoan pfLoan2 = new PfLoan();
        pfLoan2.setId(pfLoan1.getId());
        assertThat(pfLoan1).isEqualTo(pfLoan2);
        pfLoan2.setId(2L);
        assertThat(pfLoan1).isNotEqualTo(pfLoan2);
        pfLoan1.setId(null);
        assertThat(pfLoan1).isNotEqualTo(pfLoan2);
    }
}
