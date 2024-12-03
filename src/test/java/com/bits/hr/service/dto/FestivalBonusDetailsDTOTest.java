package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FestivalBonusDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FestivalBonusDetailsDTO.class);
        FestivalBonusDetailsDTO festivalBonusDetailsDTO1 = new FestivalBonusDetailsDTO();
        festivalBonusDetailsDTO1.setId(1L);
        FestivalBonusDetailsDTO festivalBonusDetailsDTO2 = new FestivalBonusDetailsDTO();
        assertThat(festivalBonusDetailsDTO1).isNotEqualTo(festivalBonusDetailsDTO2);
        festivalBonusDetailsDTO2.setId(festivalBonusDetailsDTO1.getId());
        assertThat(festivalBonusDetailsDTO1).isEqualTo(festivalBonusDetailsDTO2);
        festivalBonusDetailsDTO2.setId(2L);
        assertThat(festivalBonusDetailsDTO1).isNotEqualTo(festivalBonusDetailsDTO2);
        festivalBonusDetailsDTO1.setId(null);
        assertThat(festivalBonusDetailsDTO1).isNotEqualTo(festivalBonusDetailsDTO2);
    }
}
