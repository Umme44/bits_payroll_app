package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalenderYearTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalenderYear.class);
        CalenderYear calenderYear1 = new CalenderYear();
        calenderYear1.setId(1L);
        CalenderYear calenderYear2 = new CalenderYear();
        calenderYear2.setId(calenderYear1.getId());
        assertThat(calenderYear1).isEqualTo(calenderYear2);
        calenderYear2.setId(2L);
        assertThat(calenderYear1).isNotEqualTo(calenderYear2);
        calenderYear1.setId(null);
        assertThat(calenderYear1).isNotEqualTo(calenderYear2);
    }
}
