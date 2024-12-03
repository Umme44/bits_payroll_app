package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovementEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovementEntry.class);
        MovementEntry movementEntry1 = new MovementEntry();
        movementEntry1.setId(1L);
        MovementEntry movementEntry2 = new MovementEntry();
        movementEntry2.setId(movementEntry1.getId());
        assertThat(movementEntry1).isEqualTo(movementEntry2);
        movementEntry2.setId(2L);
        assertThat(movementEntry1).isNotEqualTo(movementEntry2);
        movementEntry1.setId(null);
        assertThat(movementEntry1).isNotEqualTo(movementEntry2);
    }
}
