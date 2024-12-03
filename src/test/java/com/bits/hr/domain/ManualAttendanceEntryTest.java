package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualAttendanceEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualAttendanceEntry.class);
        ManualAttendanceEntry manualAttendanceEntry1 = new ManualAttendanceEntry();
        manualAttendanceEntry1.setId(1L);
        ManualAttendanceEntry manualAttendanceEntry2 = new ManualAttendanceEntry();
        manualAttendanceEntry2.setId(manualAttendanceEntry1.getId());
        assertThat(manualAttendanceEntry1).isEqualTo(manualAttendanceEntry2);
        manualAttendanceEntry2.setId(2L);
        assertThat(manualAttendanceEntry1).isNotEqualTo(manualAttendanceEntry2);
        manualAttendanceEntry1.setId(null);
        assertThat(manualAttendanceEntry1).isNotEqualTo(manualAttendanceEntry2);
    }
}
