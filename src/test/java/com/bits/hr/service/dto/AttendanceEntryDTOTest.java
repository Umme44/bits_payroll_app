package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendanceEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttendanceEntryDTO.class);
        AttendanceEntryDTO attendanceEntryDTO1 = new AttendanceEntryDTO();
        attendanceEntryDTO1.setId(1L);
        AttendanceEntryDTO attendanceEntryDTO2 = new AttendanceEntryDTO();
        assertThat(attendanceEntryDTO1).isNotEqualTo(attendanceEntryDTO2);
        attendanceEntryDTO2.setId(attendanceEntryDTO1.getId());
        assertThat(attendanceEntryDTO1).isEqualTo(attendanceEntryDTO2);
        attendanceEntryDTO2.setId(2L);
        assertThat(attendanceEntryDTO1).isNotEqualTo(attendanceEntryDTO2);
        attendanceEntryDTO1.setId(null);
        assertThat(attendanceEntryDTO1).isNotEqualTo(attendanceEntryDTO2);
    }
}
