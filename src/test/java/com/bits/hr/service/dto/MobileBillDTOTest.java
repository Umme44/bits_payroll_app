package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MobileBillDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileBillDTO.class);
        MobileBillDTO mobileBillDTO1 = new MobileBillDTO();
        mobileBillDTO1.setId(1L);
        MobileBillDTO mobileBillDTO2 = new MobileBillDTO();
        assertThat(mobileBillDTO1).isNotEqualTo(mobileBillDTO2);
        mobileBillDTO2.setId(mobileBillDTO1.getId());
        assertThat(mobileBillDTO1).isEqualTo(mobileBillDTO2);
        mobileBillDTO2.setId(2L);
        assertThat(mobileBillDTO1).isNotEqualTo(mobileBillDTO2);
        mobileBillDTO1.setId(null);
        assertThat(mobileBillDTO1).isNotEqualTo(mobileBillDTO2);
    }
}
