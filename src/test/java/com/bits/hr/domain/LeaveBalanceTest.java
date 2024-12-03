package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaveBalanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveBalance.class);
        LeaveBalance leaveBalance1 = new LeaveBalance();
        leaveBalance1.setId(1L);
        LeaveBalance leaveBalance2 = new LeaveBalance();
        leaveBalance2.setId(leaveBalance1.getId());
        assertThat(leaveBalance1).isEqualTo(leaveBalance2);
        leaveBalance2.setId(2L);
        assertThat(leaveBalance1).isNotEqualTo(leaveBalance2);
        leaveBalance1.setId(null);
        assertThat(leaveBalance1).isNotEqualTo(leaveBalance2);
    }
}
