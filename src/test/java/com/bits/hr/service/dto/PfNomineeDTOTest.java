package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfNomineeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfNomineeDTO.class);
        PfNomineeDTO pfNomineeDTO1 = new PfNomineeDTO();
        pfNomineeDTO1.setId(1L);
        PfNomineeDTO pfNomineeDTO2 = new PfNomineeDTO();
        assertThat(pfNomineeDTO1).isNotEqualTo(pfNomineeDTO2);
        pfNomineeDTO2.setId(pfNomineeDTO1.getId());
        assertThat(pfNomineeDTO1).isEqualTo(pfNomineeDTO2);
        pfNomineeDTO2.setId(2L);
        assertThat(pfNomineeDTO1).isNotEqualTo(pfNomineeDTO2);
        pfNomineeDTO1.setId(null);
        assertThat(pfNomineeDTO1).isNotEqualTo(pfNomineeDTO2);
    }
}
