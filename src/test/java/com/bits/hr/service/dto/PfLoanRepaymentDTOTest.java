package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfLoanRepaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfLoanRepaymentDTO.class);
        PfLoanRepaymentDTO pfLoanRepaymentDTO1 = new PfLoanRepaymentDTO();
        pfLoanRepaymentDTO1.setId(1L);
        PfLoanRepaymentDTO pfLoanRepaymentDTO2 = new PfLoanRepaymentDTO();
        assertThat(pfLoanRepaymentDTO1).isNotEqualTo(pfLoanRepaymentDTO2);
        pfLoanRepaymentDTO2.setId(pfLoanRepaymentDTO1.getId());
        assertThat(pfLoanRepaymentDTO1).isEqualTo(pfLoanRepaymentDTO2);
        pfLoanRepaymentDTO2.setId(2L);
        assertThat(pfLoanRepaymentDTO1).isNotEqualTo(pfLoanRepaymentDTO2);
        pfLoanRepaymentDTO1.setId(null);
        assertThat(pfLoanRepaymentDTO1).isNotEqualTo(pfLoanRepaymentDTO2);
    }
}
