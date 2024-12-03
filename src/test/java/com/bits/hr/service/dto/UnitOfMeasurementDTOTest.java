package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitOfMeasurementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitOfMeasurementDTO.class);
        UnitOfMeasurementDTO unitOfMeasurementDTO1 = new UnitOfMeasurementDTO();
        unitOfMeasurementDTO1.setId(1L);
        UnitOfMeasurementDTO unitOfMeasurementDTO2 = new UnitOfMeasurementDTO();
        assertThat(unitOfMeasurementDTO1).isNotEqualTo(unitOfMeasurementDTO2);
        unitOfMeasurementDTO2.setId(unitOfMeasurementDTO1.getId());
        assertThat(unitOfMeasurementDTO1).isEqualTo(unitOfMeasurementDTO2);
        unitOfMeasurementDTO2.setId(2L);
        assertThat(unitOfMeasurementDTO1).isNotEqualTo(unitOfMeasurementDTO2);
        unitOfMeasurementDTO1.setId(null);
        assertThat(unitOfMeasurementDTO1).isNotEqualTo(unitOfMeasurementDTO2);
    }
}
