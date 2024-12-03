package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PfCollectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PfCollectionDTO.class);
        PfCollectionDTO pfCollectionDTO1 = new PfCollectionDTO();
        pfCollectionDTO1.setId(1L);
        PfCollectionDTO pfCollectionDTO2 = new PfCollectionDTO();
        assertThat(pfCollectionDTO1).isNotEqualTo(pfCollectionDTO2);
        pfCollectionDTO2.setId(pfCollectionDTO1.getId());
        assertThat(pfCollectionDTO1).isEqualTo(pfCollectionDTO2);
        pfCollectionDTO2.setId(2L);
        assertThat(pfCollectionDTO1).isNotEqualTo(pfCollectionDTO2);
        pfCollectionDTO1.setId(null);
        assertThat(pfCollectionDTO1).isNotEqualTo(pfCollectionDTO2);
    }
}
