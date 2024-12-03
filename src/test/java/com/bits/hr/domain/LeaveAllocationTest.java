package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaveAllocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveAllocation.class);
        LeaveAllocation leaveAllocation1 = new LeaveAllocation();
        leaveAllocation1.setId(1L);
        LeaveAllocation leaveAllocation2 = new LeaveAllocation();
        leaveAllocation2.setId(leaveAllocation1.getId());
        assertThat(leaveAllocation1).isEqualTo(leaveAllocation2);
        leaveAllocation2.setId(2L);
        assertThat(leaveAllocation1).isNotEqualTo(leaveAllocation2);
        leaveAllocation1.setId(null);
        assertThat(leaveAllocation1).isNotEqualTo(leaveAllocation2);
    }
}
