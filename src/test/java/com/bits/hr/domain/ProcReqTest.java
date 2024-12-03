package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcReqTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcReq.class);
        ProcReq procReq1 = new ProcReq();
        procReq1.setId(1L);
        ProcReq procReq2 = new ProcReq();
        procReq2.setId(procReq1.getId());
        assertThat(procReq1).isEqualTo(procReq2);
        procReq2.setId(2L);
        assertThat(procReq1).isNotEqualTo(procReq2);
        procReq1.setId(null);
        assertThat(procReq1).isNotEqualTo(procReq2);
    }
}
