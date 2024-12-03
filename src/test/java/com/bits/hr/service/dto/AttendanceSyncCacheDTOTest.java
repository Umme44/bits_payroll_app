package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendanceSyncCacheDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttendanceSyncCacheDTO.class);
        AttendanceSyncCacheDTO attendanceSyncCacheDTO1 = new AttendanceSyncCacheDTO();
        attendanceSyncCacheDTO1.setId(1L);
        AttendanceSyncCacheDTO attendanceSyncCacheDTO2 = new AttendanceSyncCacheDTO();
        assertThat(attendanceSyncCacheDTO1).isNotEqualTo(attendanceSyncCacheDTO2);
        attendanceSyncCacheDTO2.setId(attendanceSyncCacheDTO1.getId());
        assertThat(attendanceSyncCacheDTO1).isEqualTo(attendanceSyncCacheDTO2);
        attendanceSyncCacheDTO2.setId(2L);
        assertThat(attendanceSyncCacheDTO1).isNotEqualTo(attendanceSyncCacheDTO2);
        attendanceSyncCacheDTO1.setId(null);
        assertThat(attendanceSyncCacheDTO1).isNotEqualTo(attendanceSyncCacheDTO2);
    }
}
