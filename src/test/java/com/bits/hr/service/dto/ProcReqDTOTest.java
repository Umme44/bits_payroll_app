package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcReqDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcReqDTO.class);
        ProcReqDTO procReqDTO1 = new ProcReqDTO();
        procReqDTO1.setId(1L);
        ProcReqDTO procReqDTO2 = new ProcReqDTO();
        assertThat(procReqDTO1).isNotEqualTo(procReqDTO2);
        procReqDTO2.setId(procReqDTO1.getId());
        assertThat(procReqDTO1).isEqualTo(procReqDTO2);
        procReqDTO2.setId(2L);
        assertThat(procReqDTO1).isNotEqualTo(procReqDTO2);
        procReqDTO1.setId(null);
        assertThat(procReqDTO1).isNotEqualTo(procReqDTO2);
    }
}
