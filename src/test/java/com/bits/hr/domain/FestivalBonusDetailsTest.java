package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FestivalBonusDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FestivalBonusDetails.class);
        FestivalBonusDetails festivalBonusDetails1 = new FestivalBonusDetails();
        festivalBonusDetails1.setId(1L);
        FestivalBonusDetails festivalBonusDetails2 = new FestivalBonusDetails();
        festivalBonusDetails2.setId(festivalBonusDetails1.getId());
        assertThat(festivalBonusDetails1).isEqualTo(festivalBonusDetails2);
        festivalBonusDetails2.setId(2L);
        assertThat(festivalBonusDetails1).isNotEqualTo(festivalBonusDetails2);
        festivalBonusDetails1.setId(null);
        assertThat(festivalBonusDetails1).isNotEqualTo(festivalBonusDetails2);
    }
}
