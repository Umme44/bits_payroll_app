package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcReqMasterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcReqMasterDTO.class);
        ProcReqMasterDTO procReqMasterDTO1 = new ProcReqMasterDTO();
        procReqMasterDTO1.setId(1L);
        ProcReqMasterDTO procReqMasterDTO2 = new ProcReqMasterDTO();
        assertThat(procReqMasterDTO1).isNotEqualTo(procReqMasterDTO2);
        procReqMasterDTO2.setId(procReqMasterDTO1.getId());
        assertThat(procReqMasterDTO1).isEqualTo(procReqMasterDTO2);
        procReqMasterDTO2.setId(2L);
        assertThat(procReqMasterDTO1).isNotEqualTo(procReqMasterDTO2);
        procReqMasterDTO1.setId(null);
        assertThat(procReqMasterDTO1).isNotEqualTo(procReqMasterDTO2);
    }
}
