package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncomeTaxChallanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomeTaxChallanDTO.class);
        IncomeTaxChallanDTO incomeTaxChallanDTO1 = new IncomeTaxChallanDTO();
        incomeTaxChallanDTO1.setId(1L);
        IncomeTaxChallanDTO incomeTaxChallanDTO2 = new IncomeTaxChallanDTO();
        assertThat(incomeTaxChallanDTO1).isNotEqualTo(incomeTaxChallanDTO2);
        incomeTaxChallanDTO2.setId(incomeTaxChallanDTO1.getId());
        assertThat(incomeTaxChallanDTO1).isEqualTo(incomeTaxChallanDTO2);
        incomeTaxChallanDTO2.setId(2L);
        assertThat(incomeTaxChallanDTO1).isNotEqualTo(incomeTaxChallanDTO2);
        incomeTaxChallanDTO1.setId(null);
        assertThat(incomeTaxChallanDTO1).isNotEqualTo(incomeTaxChallanDTO2);
    }
}
