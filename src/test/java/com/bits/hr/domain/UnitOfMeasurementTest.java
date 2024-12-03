package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitOfMeasurementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitOfMeasurement.class);
        UnitOfMeasurement unitOfMeasurement1 = new UnitOfMeasurement();
        unitOfMeasurement1.setId(1L);
        UnitOfMeasurement unitOfMeasurement2 = new UnitOfMeasurement();
        unitOfMeasurement2.setId(unitOfMeasurement1.getId());
        assertThat(unitOfMeasurement1).isEqualTo(unitOfMeasurement2);
        unitOfMeasurement2.setId(2L);
        assertThat(unitOfMeasurement1).isNotEqualTo(unitOfMeasurement2);
        unitOfMeasurement1.setId(null);
        assertThat(unitOfMeasurement1).isNotEqualTo(unitOfMeasurement2);
    }
}
