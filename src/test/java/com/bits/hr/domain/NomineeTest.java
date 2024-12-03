package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NomineeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nominee.class);
        Nominee nominee1 = new Nominee();
        nominee1.setId(1L);
        Nominee nominee2 = new Nominee();
        nominee2.setId(nominee1.getId());
        assertThat(nominee1).isEqualTo(nominee2);
        nominee2.setId(2L);
        assertThat(nominee1).isNotEqualTo(nominee2);
        nominee1.setId(null);
        assertThat(nominee1).isNotEqualTo(nominee2);
    }
}
