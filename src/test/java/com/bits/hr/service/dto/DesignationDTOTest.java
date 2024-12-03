package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DesignationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DesignationDTO.class);
        DesignationDTO designationDTO1 = new DesignationDTO();
        designationDTO1.setId(1L);
        DesignationDTO designationDTO2 = new DesignationDTO();
        assertThat(designationDTO1).isNotEqualTo(designationDTO2);
        designationDTO2.setId(designationDTO1.getId());
        assertThat(designationDTO1).isEqualTo(designationDTO2);
        designationDTO2.setId(2L);
        assertThat(designationDTO1).isNotEqualTo(designationDTO2);
        designationDTO1.setId(null);
        assertThat(designationDTO1).isNotEqualTo(designationDTO2);
    }
}
