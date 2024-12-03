package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaveAllocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveAllocationDTO.class);
        LeaveAllocationDTO leaveAllocationDTO1 = new LeaveAllocationDTO();
        leaveAllocationDTO1.setId(1L);
        LeaveAllocationDTO leaveAllocationDTO2 = new LeaveAllocationDTO();
        assertThat(leaveAllocationDTO1).isNotEqualTo(leaveAllocationDTO2);
        leaveAllocationDTO2.setId(leaveAllocationDTO1.getId());
        assertThat(leaveAllocationDTO1).isEqualTo(leaveAllocationDTO2);
        leaveAllocationDTO2.setId(2L);
        assertThat(leaveAllocationDTO1).isNotEqualTo(leaveAllocationDTO2);
        leaveAllocationDTO1.setId(null);
        assertThat(leaveAllocationDTO1).isNotEqualTo(leaveAllocationDTO2);
    }
}
