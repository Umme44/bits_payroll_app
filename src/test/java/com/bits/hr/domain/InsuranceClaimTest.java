package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceClaimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceClaim.class);
        InsuranceClaim insuranceClaim1 = new InsuranceClaim();
        insuranceClaim1.setId(1L);
        InsuranceClaim insuranceClaim2 = new InsuranceClaim();
        insuranceClaim2.setId(insuranceClaim1.getId());
        assertThat(insuranceClaim1).isEqualTo(insuranceClaim2);
        insuranceClaim2.setId(2L);
        assertThat(insuranceClaim1).isNotEqualTo(insuranceClaim2);
        insuranceClaim1.setId(null);
        assertThat(insuranceClaim1).isNotEqualTo(insuranceClaim2);
    }
}
