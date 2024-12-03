package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NomineeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NomineeDTO.class);
        NomineeDTO nomineeDTO1 = new NomineeDTO();
        nomineeDTO1.setId(1L);
        NomineeDTO nomineeDTO2 = new NomineeDTO();
        assertThat(nomineeDTO1).isNotEqualTo(nomineeDTO2);
        nomineeDTO2.setId(nomineeDTO1.getId());
        assertThat(nomineeDTO1).isEqualTo(nomineeDTO2);
        nomineeDTO2.setId(2L);
        assertThat(nomineeDTO1).isNotEqualTo(nomineeDTO2);
        nomineeDTO1.setId(null);
        assertThat(nomineeDTO1).isNotEqualTo(nomineeDTO2);
    }
}
