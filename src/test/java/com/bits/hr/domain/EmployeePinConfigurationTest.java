package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeePinConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeePinConfiguration.class);
        EmployeePinConfiguration employeePinConfiguration1 = new EmployeePinConfiguration();
        employeePinConfiguration1.setId(1L);
        EmployeePinConfiguration employeePinConfiguration2 = new EmployeePinConfiguration();
        employeePinConfiguration2.setId(employeePinConfiguration1.getId());
        assertThat(employeePinConfiguration1).isEqualTo(employeePinConfiguration2);
        employeePinConfiguration2.setId(2L);
        assertThat(employeePinConfiguration1).isNotEqualTo(employeePinConfiguration2);
        employeePinConfiguration1.setId(null);
        assertThat(employeePinConfiguration1).isNotEqualTo(employeePinConfiguration2);
    }
}
