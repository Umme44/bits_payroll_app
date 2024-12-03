package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NationalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nationality.class);
        Nationality nationality1 = new Nationality();
        nationality1.setId(1L);
        Nationality nationality2 = new Nationality();
        nationality2.setId(nationality1.getId());
        assertThat(nationality1).isEqualTo(nationality2);
        nationality2.setId(2L);
        assertThat(nationality1).isNotEqualTo(nationality2);
        nationality1.setId(null);
        assertThat(nationality1).isNotEqualTo(nationality2);
    }
}
