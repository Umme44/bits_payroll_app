package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlexScheduleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlexScheduleDTO.class);
        FlexScheduleDTO flexScheduleDTO1 = new FlexScheduleDTO();
        flexScheduleDTO1.setId(1L);
        FlexScheduleDTO flexScheduleDTO2 = new FlexScheduleDTO();
        assertThat(flexScheduleDTO1).isNotEqualTo(flexScheduleDTO2);
        flexScheduleDTO2.setId(flexScheduleDTO1.getId());
        assertThat(flexScheduleDTO1).isEqualTo(flexScheduleDTO2);
        flexScheduleDTO2.setId(2L);
        assertThat(flexScheduleDTO1).isNotEqualTo(flexScheduleDTO2);
        flexScheduleDTO1.setId(null);
        assertThat(flexScheduleDTO1).isNotEqualTo(flexScheduleDTO2);
    }
}
