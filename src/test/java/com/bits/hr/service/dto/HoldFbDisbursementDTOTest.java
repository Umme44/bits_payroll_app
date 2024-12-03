package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoldFbDisbursementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HoldFbDisbursementDTO.class);
        HoldFbDisbursementDTO holdFbDisbursementDTO1 = new HoldFbDisbursementDTO();
        holdFbDisbursementDTO1.setId(1L);
        HoldFbDisbursementDTO holdFbDisbursementDTO2 = new HoldFbDisbursementDTO();
        assertThat(holdFbDisbursementDTO1).isNotEqualTo(holdFbDisbursementDTO2);
        holdFbDisbursementDTO2.setId(holdFbDisbursementDTO1.getId());
        assertThat(holdFbDisbursementDTO1).isEqualTo(holdFbDisbursementDTO2);
        holdFbDisbursementDTO2.setId(2L);
        assertThat(holdFbDisbursementDTO1).isNotEqualTo(holdFbDisbursementDTO2);
        holdFbDisbursementDTO1.setId(null);
        assertThat(holdFbDisbursementDTO1).isNotEqualTo(holdFbDisbursementDTO2);
    }
}
