package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceConfigurationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceConfigurationDTO.class);
        InsuranceConfigurationDTO insuranceConfigurationDTO1 = new InsuranceConfigurationDTO();
        insuranceConfigurationDTO1.setId(1L);
        InsuranceConfigurationDTO insuranceConfigurationDTO2 = new InsuranceConfigurationDTO();
        assertThat(insuranceConfigurationDTO1).isNotEqualTo(insuranceConfigurationDTO2);
        insuranceConfigurationDTO2.setId(insuranceConfigurationDTO1.getId());
        assertThat(insuranceConfigurationDTO1).isEqualTo(insuranceConfigurationDTO2);
        insuranceConfigurationDTO2.setId(2L);
        assertThat(insuranceConfigurationDTO1).isNotEqualTo(insuranceConfigurationDTO2);
        insuranceConfigurationDTO1.setId(null);
        assertThat(insuranceConfigurationDTO1).isNotEqualTo(insuranceConfigurationDTO2);
    }
}
