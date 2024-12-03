package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexScheduleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexSchedule.class);
        FlexSchedule flexSchedule1 = new FlexSchedule();
        flexSchedule1.setId(1L);
        FlexSchedule flexSchedule2 = new FlexSchedule();
        flexSchedule2.setId(flexSchedule1.getId());
        assertThat(flexSchedule1).isEqualTo(flexSchedule2);
        flexSchedule2.setId(2L);
        assertThat(flexSchedule1).isNotEqualTo(flexSchedule2);
        flexSchedule1.setId(null);
        assertThat(flexSchedule1).isNotEqualTo(flexSchedule2);
    }
}
