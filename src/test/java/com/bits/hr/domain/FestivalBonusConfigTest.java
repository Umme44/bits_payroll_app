package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FestivalBonusConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FestivalBonusConfig.class);
        FestivalBonusConfig festivalBonusConfig1 = new FestivalBonusConfig();
        festivalBonusConfig1.setId(1L);
        FestivalBonusConfig festivalBonusConfig2 = new FestivalBonusConfig();
        festivalBonusConfig2.setId(festivalBonusConfig1.getId());
        assertThat(festivalBonusConfig1).isEqualTo(festivalBonusConfig2);
        festivalBonusConfig2.setId(2L);
        assertThat(festivalBonusConfig1).isNotEqualTo(festivalBonusConfig2);
        festivalBonusConfig1.setId(null);
        assertThat(festivalBonusConfig1).isNotEqualTo(festivalBonusConfig2);
    }
}
