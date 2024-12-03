package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NationalityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NationalityDTO.class);
        NationalityDTO nationalityDTO1 = new NationalityDTO();
        nationalityDTO1.setId(1L);
        NationalityDTO nationalityDTO2 = new NationalityDTO();
        assertThat(nationalityDTO1).isNotEqualTo(nationalityDTO2);
        nationalityDTO2.setId(nationalityDTO1.getId());
        assertThat(nationalityDTO1).isEqualTo(nationalityDTO2);
        nationalityDTO2.setId(2L);
        assertThat(nationalityDTO1).isNotEqualTo(nationalityDTO2);
        nationalityDTO1.setId(null);
        assertThat(nationalityDTO1).isNotEqualTo(nationalityDTO2);
    }
}
