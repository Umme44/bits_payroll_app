package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaveBalanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveBalanceDTO.class);
        LeaveBalanceDTO leaveBalanceDTO1 = new LeaveBalanceDTO();
        leaveBalanceDTO1.setId(1L);
        LeaveBalanceDTO leaveBalanceDTO2 = new LeaveBalanceDTO();
        assertThat(leaveBalanceDTO1).isNotEqualTo(leaveBalanceDTO2);
        leaveBalanceDTO2.setId(leaveBalanceDTO1.getId());
        assertThat(leaveBalanceDTO1).isEqualTo(leaveBalanceDTO2);
        leaveBalanceDTO2.setId(2L);
        assertThat(leaveBalanceDTO1).isNotEqualTo(leaveBalanceDTO2);
        leaveBalanceDTO1.setId(null);
        assertThat(leaveBalanceDTO1).isNotEqualTo(leaveBalanceDTO2);
    }
}
