package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AitConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AitConfigDTO.class);
        AitConfigDTO aitConfigDTO1 = new AitConfigDTO();
        aitConfigDTO1.setId(1L);
        AitConfigDTO aitConfigDTO2 = new AitConfigDTO();
        assertThat(aitConfigDTO1).isNotEqualTo(aitConfigDTO2);
        aitConfigDTO2.setId(aitConfigDTO1.getId());
        assertThat(aitConfigDTO1).isEqualTo(aitConfigDTO2);
        aitConfigDTO2.setId(2L);
        assertThat(aitConfigDTO1).isNotEqualTo(aitConfigDTO2);
        aitConfigDTO1.setId(null);
        assertThat(aitConfigDTO1).isNotEqualTo(aitConfigDTO2);
    }
}
