package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexScheduleApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexScheduleApplication.class);
        FlexScheduleApplication flexScheduleApplication1 = new FlexScheduleApplication();
        flexScheduleApplication1.setId(1L);
        FlexScheduleApplication flexScheduleApplication2 = new FlexScheduleApplication();
        flexScheduleApplication2.setId(flexScheduleApplication1.getId());
        assertThat(flexScheduleApplication1).isEqualTo(flexScheduleApplication2);
        flexScheduleApplication2.setId(2L);
        assertThat(flexScheduleApplication1).isNotEqualTo(flexScheduleApplication2);
        flexScheduleApplication1.setId(null);
        assertThat(flexScheduleApplication1).isNotEqualTo(flexScheduleApplication2);
    }
}
