package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FestivalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Festival.class);
        Festival festival1 = new Festival();
        festival1.setId(1L);
        Festival festival2 = new Festival();
        festival2.setId(festival1.getId());
        assertThat(festival1).isEqualTo(festival2);
        festival2.setId(2L);
        assertThat(festival1).isNotEqualTo(festival2);
        festival1.setId(null);
        assertThat(festival1).isNotEqualTo(festival2);
    }
}
