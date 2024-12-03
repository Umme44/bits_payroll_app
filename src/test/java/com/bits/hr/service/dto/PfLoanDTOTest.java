package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfLoanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfLoanDTO.class);
        PfLoanDTO pfLoanDTO1 = new PfLoanDTO();
        pfLoanDTO1.setId(1L);
        PfLoanDTO pfLoanDTO2 = new PfLoanDTO();
        assertThat(pfLoanDTO1).isNotEqualTo(pfLoanDTO2);
        pfLoanDTO2.setId(pfLoanDTO1.getId());
        assertThat(pfLoanDTO1).isEqualTo(pfLoanDTO2);
        pfLoanDTO2.setId(2L);
        assertThat(pfLoanDTO1).isNotEqualTo(pfLoanDTO2);
        pfLoanDTO1.setId(null);
        assertThat(pfLoanDTO1).isNotEqualTo(pfLoanDTO2);
    }
}
