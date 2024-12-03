package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomRequisitionDTO.class);
        RoomRequisitionDTO roomRequisitionDTO1 = new RoomRequisitionDTO();
        roomRequisitionDTO1.setId(1L);
        RoomRequisitionDTO roomRequisitionDTO2 = new RoomRequisitionDTO();
        assertThat(roomRequisitionDTO1).isNotEqualTo(roomRequisitionDTO2);
        roomRequisitionDTO2.setId(roomRequisitionDTO1.getId());
        assertThat(roomRequisitionDTO1).isEqualTo(roomRequisitionDTO2);
        roomRequisitionDTO2.setId(2L);
        assertThat(roomRequisitionDTO1).isNotEqualTo(roomRequisitionDTO2);
        roomRequisitionDTO1.setId(null);
        assertThat(roomRequisitionDTO1).isNotEqualTo(roomRequisitionDTO2);
    }
}
