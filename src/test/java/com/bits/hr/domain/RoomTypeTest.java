package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomType.class);
        RoomType roomType1 = new RoomType();
        roomType1.setId(1L);
        RoomType roomType2 = new RoomType();
        roomType2.setId(roomType1.getId());
        assertThat(roomType1).isEqualTo(roomType2);
        roomType2.setId(2L);
        assertThat(roomType1).isNotEqualTo(roomType2);
        roomType1.setId(null);
        assertThat(roomType1).isNotEqualTo(roomType2);
    }
}
