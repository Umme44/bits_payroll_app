package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcReqMasterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcReqMaster.class);
        ProcReqMaster procReqMaster1 = new ProcReqMaster();
        procReqMaster1.setId(1L);
        ProcReqMaster procReqMaster2 = new ProcReqMaster();
        procReqMaster2.setId(procReqMaster1.getId());
        assertThat(procReqMaster1).isEqualTo(procReqMaster2);
        procReqMaster2.setId(2L);
        assertThat(procReqMaster1).isNotEqualTo(procReqMaster2);
        procReqMaster1.setId(null);
        assertThat(procReqMaster1).isNotEqualTo(procReqMaster2);
    }
}
