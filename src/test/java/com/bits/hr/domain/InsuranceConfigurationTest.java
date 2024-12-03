package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceConfiguration.class);
        InsuranceConfiguration insuranceConfiguration1 = new InsuranceConfiguration();
        insuranceConfiguration1.setId(1L);
        InsuranceConfiguration insuranceConfiguration2 = new InsuranceConfiguration();
        insuranceConfiguration2.setId(insuranceConfiguration1.getId());
        assertThat(insuranceConfiguration1).isEqualTo(insuranceConfiguration2);
        insuranceConfiguration2.setId(2L);
        assertThat(insuranceConfiguration1).isNotEqualTo(insuranceConfiguration2);
        insuranceConfiguration1.setId(null);
        assertThat(insuranceConfiguration1).isNotEqualTo(insuranceConfiguration2);
    }
}
