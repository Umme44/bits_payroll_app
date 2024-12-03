package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfArrearDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfArrearDTO.class);
        PfArrearDTO pfArrearDTO1 = new PfArrearDTO();
        pfArrearDTO1.setId(1L);
        PfArrearDTO pfArrearDTO2 = new PfArrearDTO();
        assertThat(pfArrearDTO1).isNotEqualTo(pfArrearDTO2);
        pfArrearDTO2.setId(pfArrearDTO1.getId());
        assertThat(pfArrearDTO1).isEqualTo(pfArrearDTO2);
        pfArrearDTO2.setId(2L);
        assertThat(pfArrearDTO1).isNotEqualTo(pfArrearDTO2);
        pfArrearDTO1.setId(null);
        assertThat(pfArrearDTO1).isNotEqualTo(pfArrearDTO2);
    }
}
