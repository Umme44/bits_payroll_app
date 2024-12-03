package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AitConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AitConfig.class);
        AitConfig aitConfig1 = new AitConfig();
        aitConfig1.setId(1L);
        AitConfig aitConfig2 = new AitConfig();
        aitConfig2.setId(aitConfig1.getId());
        assertThat(aitConfig1).isEqualTo(aitConfig2);
        aitConfig2.setId(2L);
        assertThat(aitConfig1).isNotEqualTo(aitConfig2);
        aitConfig1.setId(null);
        assertThat(aitConfig1).isNotEqualTo(aitConfig2);
    }
}
