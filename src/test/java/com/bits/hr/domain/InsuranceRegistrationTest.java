package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceRegistrationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceRegistration.class);
        InsuranceRegistration insuranceRegistration1 = new InsuranceRegistration();
        insuranceRegistration1.setId(1L);
        InsuranceRegistration insuranceRegistration2 = new InsuranceRegistration();
        insuranceRegistration2.setId(insuranceRegistration1.getId());
        assertThat(insuranceRegistration1).isEqualTo(insuranceRegistration2);
        insuranceRegistration2.setId(2L);
        assertThat(insuranceRegistration1).isNotEqualTo(insuranceRegistration2);
        insuranceRegistration1.setId(null);
        assertThat(insuranceRegistration1).isNotEqualTo(insuranceRegistration2);
    }
}
