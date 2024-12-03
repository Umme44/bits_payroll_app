package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProRataFestivalBonusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProRataFestivalBonus.class);
        ProRataFestivalBonus proRataFestivalBonus1 = new ProRataFestivalBonus();
        proRataFestivalBonus1.setId(1L);
        ProRataFestivalBonus proRataFestivalBonus2 = new ProRataFestivalBonus();
        proRataFestivalBonus2.setId(proRataFestivalBonus1.getId());
        assertThat(proRataFestivalBonus1).isEqualTo(proRataFestivalBonus2);
        proRataFestivalBonus2.setId(2L);
        assertThat(proRataFestivalBonus1).isNotEqualTo(proRataFestivalBonus2);
        proRataFestivalBonus1.setId(null);
        assertThat(proRataFestivalBonus1).isNotEqualTo(proRataFestivalBonus2);
    }
}
