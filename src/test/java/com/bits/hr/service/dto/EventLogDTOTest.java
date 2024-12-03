package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventLogDTO.class);
        EventLogDTO eventLogDTO1 = new EventLogDTO();
        eventLogDTO1.setId(1L);
        EventLogDTO eventLogDTO2 = new EventLogDTO();
        assertThat(eventLogDTO1).isNotEqualTo(eventLogDTO2);
        eventLogDTO2.setId(eventLogDTO1.getId());
        assertThat(eventLogDTO1).isEqualTo(eventLogDTO2);
        eventLogDTO2.setId(2L);
        assertThat(eventLogDTO1).isNotEqualTo(eventLogDTO2);
        eventLogDTO1.setId(null);
        assertThat(eventLogDTO1).isNotEqualTo(eventLogDTO2);
    }
}
