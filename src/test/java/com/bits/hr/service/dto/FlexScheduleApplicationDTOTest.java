package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexScheduleApplicationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexScheduleApplicationDTO.class);
        FlexScheduleApplicationDTO flexScheduleApplicationDTO1 = new FlexScheduleApplicationDTO();
        flexScheduleApplicationDTO1.setId(1L);
        FlexScheduleApplicationDTO flexScheduleApplicationDTO2 = new FlexScheduleApplicationDTO();
        assertThat(flexScheduleApplicationDTO1).isNotEqualTo(flexScheduleApplicationDTO2);
        flexScheduleApplicationDTO2.setId(flexScheduleApplicationDTO1.getId());
        assertThat(flexScheduleApplicationDTO1).isEqualTo(flexScheduleApplicationDTO2);
        flexScheduleApplicationDTO2.setId(2L);
        assertThat(flexScheduleApplicationDTO1).isNotEqualTo(flexScheduleApplicationDTO2);
        flexScheduleApplicationDTO1.setId(null);
        assertThat(flexScheduleApplicationDTO1).isNotEqualTo(flexScheduleApplicationDTO2);
    }
}
