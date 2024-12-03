package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeePinConfigurationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeePinConfigurationDTO.class);
        EmployeePinConfigurationDTO employeePinConfigurationDTO1 = new EmployeePinConfigurationDTO();
        employeePinConfigurationDTO1.setId(1L);
        EmployeePinConfigurationDTO employeePinConfigurationDTO2 = new EmployeePinConfigurationDTO();
        assertThat(employeePinConfigurationDTO1).isNotEqualTo(employeePinConfigurationDTO2);
        employeePinConfigurationDTO2.setId(employeePinConfigurationDTO1.getId());
        assertThat(employeePinConfigurationDTO1).isEqualTo(employeePinConfigurationDTO2);
        employeePinConfigurationDTO2.setId(2L);
        assertThat(employeePinConfigurationDTO1).isNotEqualTo(employeePinConfigurationDTO2);
        employeePinConfigurationDTO1.setId(null);
        assertThat(employeePinConfigurationDTO1).isNotEqualTo(employeePinConfigurationDTO2);
    }
}
