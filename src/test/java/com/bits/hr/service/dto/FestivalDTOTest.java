package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FestivalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FestivalDTO.class);
        FestivalDTO festivalDTO1 = new FestivalDTO();
        festivalDTO1.setId(1L);
        FestivalDTO festivalDTO2 = new FestivalDTO();
        assertThat(festivalDTO1).isNotEqualTo(festivalDTO2);
        festivalDTO2.setId(festivalDTO1.getId());
        assertThat(festivalDTO1).isEqualTo(festivalDTO2);
        festivalDTO2.setId(2L);
        assertThat(festivalDTO1).isNotEqualTo(festivalDTO2);
        festivalDTO1.setId(null);
        assertThat(festivalDTO1).isNotEqualTo(festivalDTO2);
    }
}
