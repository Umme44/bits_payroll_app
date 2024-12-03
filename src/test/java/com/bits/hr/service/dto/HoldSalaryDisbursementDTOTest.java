package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoldSalaryDisbursementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HoldSalaryDisbursementDTO.class);
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO1 = new HoldSalaryDisbursementDTO();
        holdSalaryDisbursementDTO1.setId(1L);
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO2 = new HoldSalaryDisbursementDTO();
        assertThat(holdSalaryDisbursementDTO1).isNotEqualTo(holdSalaryDisbursementDTO2);
        holdSalaryDisbursementDTO2.setId(holdSalaryDisbursementDTO1.getId());
        assertThat(holdSalaryDisbursementDTO1).isEqualTo(holdSalaryDisbursementDTO2);
        holdSalaryDisbursementDTO2.setId(2L);
        assertThat(holdSalaryDisbursementDTO1).isNotEqualTo(holdSalaryDisbursementDTO2);
        holdSalaryDisbursementDTO1.setId(null);
        assertThat(holdSalaryDisbursementDTO1).isNotEqualTo(holdSalaryDisbursementDTO2);
    }
}
