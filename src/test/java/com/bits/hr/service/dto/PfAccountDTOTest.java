package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfAccountDTO.class);
        PfAccountDTO pfAccountDTO1 = new PfAccountDTO();
        pfAccountDTO1.setId(1L);
        PfAccountDTO pfAccountDTO2 = new PfAccountDTO();
        assertThat(pfAccountDTO1).isNotEqualTo(pfAccountDTO2);
        pfAccountDTO2.setId(pfAccountDTO1.getId());
        assertThat(pfAccountDTO1).isEqualTo(pfAccountDTO2);
        pfAccountDTO2.setId(2L);
        assertThat(pfAccountDTO1).isNotEqualTo(pfAccountDTO2);
        pfAccountDTO1.setId(null);
        assertThat(pfAccountDTO1).isNotEqualTo(pfAccountDTO2);
    }
}
