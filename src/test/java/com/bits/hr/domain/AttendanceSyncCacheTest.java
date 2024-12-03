package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendanceSyncCacheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttendanceSyncCache.class);
        AttendanceSyncCache attendanceSyncCache1 = new AttendanceSyncCache();
        attendanceSyncCache1.setId(1L);
        AttendanceSyncCache attendanceSyncCache2 = new AttendanceSyncCache();
        attendanceSyncCache2.setId(attendanceSyncCache1.getId());
        assertThat(attendanceSyncCache1).isEqualTo(attendanceSyncCache2);
        attendanceSyncCache2.setId(2L);
        assertThat(attendanceSyncCache1).isNotEqualTo(attendanceSyncCache2);
        attendanceSyncCache1.setId(null);
        assertThat(attendanceSyncCache1).isNotEqualTo(attendanceSyncCache2);
    }
}
