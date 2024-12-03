package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovementEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovementEntryDTO.class);
        MovementEntryDTO movementEntryDTO1 = new MovementEntryDTO();
        movementEntryDTO1.setId(1L);
        MovementEntryDTO movementEntryDTO2 = new MovementEntryDTO();
        assertThat(movementEntryDTO1).isNotEqualTo(movementEntryDTO2);
        movementEntryDTO2.setId(movementEntryDTO1.getId());
        assertThat(movementEntryDTO1).isEqualTo(movementEntryDTO2);
        movementEntryDTO2.setId(2L);
        assertThat(movementEntryDTO1).isNotEqualTo(movementEntryDTO2);
        movementEntryDTO1.setId(null);
        assertThat(movementEntryDTO1).isNotEqualTo(movementEntryDTO2);
    }
}
