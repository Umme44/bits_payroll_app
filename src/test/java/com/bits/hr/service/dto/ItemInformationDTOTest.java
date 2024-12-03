package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemInformationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemInformationDTO.class);
        ItemInformationDTO itemInformationDTO1 = new ItemInformationDTO();
        itemInformationDTO1.setId(1L);
        ItemInformationDTO itemInformationDTO2 = new ItemInformationDTO();
        assertThat(itemInformationDTO1).isNotEqualTo(itemInformationDTO2);
        itemInformationDTO2.setId(itemInformationDTO1.getId());
        assertThat(itemInformationDTO1).isEqualTo(itemInformationDTO2);
        itemInformationDTO2.setId(2L);
        assertThat(itemInformationDTO1).isNotEqualTo(itemInformationDTO2);
        itemInformationDTO1.setId(null);
        assertThat(itemInformationDTO1).isNotEqualTo(itemInformationDTO2);
    }
}
