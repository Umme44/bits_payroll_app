package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfLoanApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfLoanApplication.class);
        PfLoanApplication pfLoanApplication1 = new PfLoanApplication();
        pfLoanApplication1.setId(1L);
        PfLoanApplication pfLoanApplication2 = new PfLoanApplication();
        pfLoanApplication2.setId(pfLoanApplication1.getId());
        assertThat(pfLoanApplication1).isEqualTo(pfLoanApplication2);
        pfLoanApplication2.setId(2L);
        assertThat(pfLoanApplication1).isNotEqualTo(pfLoanApplication2);
        pfLoanApplication1.setId(null);
        assertThat(pfLoanApplication1).isNotEqualTo(pfLoanApplication2);
    }
}
