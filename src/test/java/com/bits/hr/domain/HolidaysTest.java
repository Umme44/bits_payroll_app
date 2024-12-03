package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HolidaysTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Holidays.class);
        Holidays holidays1 = new Holidays();
        holidays1.setId(1L);
        Holidays holidays2 = new Holidays();
        holidays2.setId(holidays1.getId());
        assertThat(holidays1).isEqualTo(holidays2);
        holidays2.setId(2L);
        assertThat(holidays1).isNotEqualTo(holidays2);
        holidays1.setId(null);
        assertThat(holidays1).isNotEqualTo(holidays2);
    }
}
