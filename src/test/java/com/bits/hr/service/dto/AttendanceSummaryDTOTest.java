package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendanceSummaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttendanceSummaryDTO.class);
        AttendanceSummaryDTO attendanceSummaryDTO1 = new AttendanceSummaryDTO();
        attendanceSummaryDTO1.setId(1L);
        AttendanceSummaryDTO attendanceSummaryDTO2 = new AttendanceSummaryDTO();
        assertThat(attendanceSummaryDTO1).isNotEqualTo(attendanceSummaryDTO2);
        attendanceSummaryDTO2.setId(attendanceSummaryDTO1.getId());
        assertThat(attendanceSummaryDTO1).isEqualTo(attendanceSummaryDTO2);
        attendanceSummaryDTO2.setId(2L);
        assertThat(attendanceSummaryDTO1).isNotEqualTo(attendanceSummaryDTO2);
        attendanceSummaryDTO1.setId(null);
        assertThat(attendanceSummaryDTO1).isNotEqualTo(attendanceSummaryDTO2);
    }
}
