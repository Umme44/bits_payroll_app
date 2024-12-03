package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FestivalBonusConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FestivalBonusConfigDTO.class);
        FestivalBonusConfigDTO festivalBonusConfigDTO1 = new FestivalBonusConfigDTO();
        festivalBonusConfigDTO1.setId(1L);
        FestivalBonusConfigDTO festivalBonusConfigDTO2 = new FestivalBonusConfigDTO();
        assertThat(festivalBonusConfigDTO1).isNotEqualTo(festivalBonusConfigDTO2);
        festivalBonusConfigDTO2.setId(festivalBonusConfigDTO1.getId());
        assertThat(festivalBonusConfigDTO1).isEqualTo(festivalBonusConfigDTO2);
        festivalBonusConfigDTO2.setId(2L);
        assertThat(festivalBonusConfigDTO1).isNotEqualTo(festivalBonusConfigDTO2);
        festivalBonusConfigDTO1.setId(null);
        assertThat(festivalBonusConfigDTO1).isNotEqualTo(festivalBonusConfigDTO2);
    }
}
