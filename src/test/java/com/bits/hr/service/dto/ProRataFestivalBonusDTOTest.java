package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProRataFestivalBonusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProRataFestivalBonusDTO.class);
        ProRataFestivalBonusDTO proRataFestivalBonusDTO1 = new ProRataFestivalBonusDTO();
        proRataFestivalBonusDTO1.setId(1L);
        ProRataFestivalBonusDTO proRataFestivalBonusDTO2 = new ProRataFestivalBonusDTO();
        assertThat(proRataFestivalBonusDTO1).isNotEqualTo(proRataFestivalBonusDTO2);
        proRataFestivalBonusDTO2.setId(proRataFestivalBonusDTO1.getId());
        assertThat(proRataFestivalBonusDTO1).isEqualTo(proRataFestivalBonusDTO2);
        proRataFestivalBonusDTO2.setId(2L);
        assertThat(proRataFestivalBonusDTO1).isNotEqualTo(proRataFestivalBonusDTO2);
        proRataFestivalBonusDTO1.setId(null);
        assertThat(proRataFestivalBonusDTO1).isNotEqualTo(proRataFestivalBonusDTO2);
    }
}
