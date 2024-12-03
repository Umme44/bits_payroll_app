package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomRequisition.class);
        RoomRequisition roomRequisition1 = new RoomRequisition();
        roomRequisition1.setId(1L);
        RoomRequisition roomRequisition2 = new RoomRequisition();
        roomRequisition2.setId(roomRequisition1.getId());
        assertThat(roomRequisition1).isEqualTo(roomRequisition2);
        roomRequisition2.setId(2L);
        assertThat(roomRequisition1).isNotEqualTo(roomRequisition2);
        roomRequisition1.setId(null);
        assertThat(roomRequisition1).isNotEqualTo(roomRequisition2);
    }
}
