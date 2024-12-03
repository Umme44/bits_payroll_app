package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendanceEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttendanceEntry.class);
        AttendanceEntry attendanceEntry1 = new AttendanceEntry();
        attendanceEntry1.setId(1L);
        AttendanceEntry attendanceEntry2 = new AttendanceEntry();
        attendanceEntry2.setId(attendanceEntry1.getId());
        assertThat(attendanceEntry1).isEqualTo(attendanceEntry2);
        attendanceEntry2.setId(2L);
        assertThat(attendanceEntry1).isNotEqualTo(attendanceEntry2);
        attendanceEntry1.setId(null);
        assertThat(attendanceEntry1).isNotEqualTo(attendanceEntry2);
    }
}
