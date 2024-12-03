package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialShiftTimingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialShiftTiming.class);
        SpecialShiftTiming specialShiftTiming1 = new SpecialShiftTiming();
        specialShiftTiming1.setId(1L);
        SpecialShiftTiming specialShiftTiming2 = new SpecialShiftTiming();
        specialShiftTiming2.setId(specialShiftTiming1.getId());
        assertThat(specialShiftTiming1).isEqualTo(specialShiftTiming2);
        specialShiftTiming2.setId(2L);
        assertThat(specialShiftTiming1).isNotEqualTo(specialShiftTiming2);
        specialShiftTiming1.setId(null);
        assertThat(specialShiftTiming1).isNotEqualTo(specialShiftTiming2);
    }
}
