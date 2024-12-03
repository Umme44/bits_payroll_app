package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankBranchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankBranchDTO.class);
        BankBranchDTO bankBranchDTO1 = new BankBranchDTO();
        bankBranchDTO1.setId(1L);
        BankBranchDTO bankBranchDTO2 = new BankBranchDTO();
        assertThat(bankBranchDTO1).isNotEqualTo(bankBranchDTO2);
        bankBranchDTO2.setId(bankBranchDTO1.getId());
        assertThat(bankBranchDTO1).isEqualTo(bankBranchDTO2);
        bankBranchDTO2.setId(2L);
        assertThat(bankBranchDTO1).isNotEqualTo(bankBranchDTO2);
        bankBranchDTO1.setId(null);
        assertThat(bankBranchDTO1).isNotEqualTo(bankBranchDTO2);
    }
}
