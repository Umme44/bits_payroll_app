package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferencesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferencesDTO.class);
        ReferencesDTO referencesDTO1 = new ReferencesDTO();
        referencesDTO1.setId(1L);
        ReferencesDTO referencesDTO2 = new ReferencesDTO();
        assertThat(referencesDTO1).isNotEqualTo(referencesDTO2);
        referencesDTO2.setId(referencesDTO1.getId());
        assertThat(referencesDTO1).isEqualTo(referencesDTO2);
        referencesDTO2.setId(2L);
        assertThat(referencesDTO1).isNotEqualTo(referencesDTO2);
        referencesDTO1.setId(null);
        assertThat(referencesDTO1).isNotEqualTo(referencesDTO2);
    }
}
