package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceClaimDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceClaimDTO.class);
        InsuranceClaimDTO insuranceClaimDTO1 = new InsuranceClaimDTO();
        insuranceClaimDTO1.setId(1L);
        InsuranceClaimDTO insuranceClaimDTO2 = new InsuranceClaimDTO();
        assertThat(insuranceClaimDTO1).isNotEqualTo(insuranceClaimDTO2);
        insuranceClaimDTO2.setId(insuranceClaimDTO1.getId());
        assertThat(insuranceClaimDTO1).isEqualTo(insuranceClaimDTO2);
        insuranceClaimDTO2.setId(2L);
        assertThat(insuranceClaimDTO1).isNotEqualTo(insuranceClaimDTO2);
        insuranceClaimDTO1.setId(null);
        assertThat(insuranceClaimDTO1).isNotEqualTo(insuranceClaimDTO2);
    }
}
