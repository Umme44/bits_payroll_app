package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualAttendanceEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualAttendanceEntryDTO.class);
        ManualAttendanceEntryDTO manualAttendanceEntryDTO1 = new ManualAttendanceEntryDTO();
        manualAttendanceEntryDTO1.setId(1L);
        ManualAttendanceEntryDTO manualAttendanceEntryDTO2 = new ManualAttendanceEntryDTO();
        assertThat(manualAttendanceEntryDTO1).isNotEqualTo(manualAttendanceEntryDTO2);
        manualAttendanceEntryDTO2.setId(manualAttendanceEntryDTO1.getId());
        assertThat(manualAttendanceEntryDTO1).isEqualTo(manualAttendanceEntryDTO2);
        manualAttendanceEntryDTO2.setId(2L);
        assertThat(manualAttendanceEntryDTO1).isNotEqualTo(manualAttendanceEntryDTO2);
        manualAttendanceEntryDTO1.setId(null);
        assertThat(manualAttendanceEntryDTO1).isNotEqualTo(manualAttendanceEntryDTO2);
    }
}
