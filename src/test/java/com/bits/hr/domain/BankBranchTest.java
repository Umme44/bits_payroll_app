package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankBranchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankBranch.class);
        BankBranch bankBranch1 = new BankBranch();
        bankBranch1.setId(1L);
        BankBranch bankBranch2 = new BankBranch();
        bankBranch2.setId(bankBranch1.getId());
        assertThat(bankBranch1).isEqualTo(bankBranch2);
        bankBranch2.setId(2L);
        assertThat(bankBranch1).isNotEqualTo(bankBranch2);
        bankBranch1.setId(null);
        assertThat(bankBranch1).isNotEqualTo(bankBranch2);
    }
}
