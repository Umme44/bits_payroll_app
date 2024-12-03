package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceRegistrationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceRegistrationDTO.class);
        InsuranceRegistrationDTO insuranceRegistrationDTO1 = new InsuranceRegistrationDTO();
        insuranceRegistrationDTO1.setId(1L);
        InsuranceRegistrationDTO insuranceRegistrationDTO2 = new InsuranceRegistrationDTO();
        assertThat(insuranceRegistrationDTO1).isNotEqualTo(insuranceRegistrationDTO2);
        insuranceRegistrationDTO2.setId(insuranceRegistrationDTO1.getId());
        assertThat(insuranceRegistrationDTO1).isEqualTo(insuranceRegistrationDTO2);
        insuranceRegistrationDTO2.setId(2L);
        assertThat(insuranceRegistrationDTO1).isNotEqualTo(insuranceRegistrationDTO2);
        insuranceRegistrationDTO1.setId(null);
        assertThat(insuranceRegistrationDTO1).isNotEqualTo(insuranceRegistrationDTO2);
    }
}
