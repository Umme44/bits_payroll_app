package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleRequisition.class);
        VehicleRequisition vehicleRequisition1 = new VehicleRequisition();
        vehicleRequisition1.setId(1L);
        VehicleRequisition vehicleRequisition2 = new VehicleRequisition();
        vehicleRequisition2.setId(vehicleRequisition1.getId());
        assertThat(vehicleRequisition1).isEqualTo(vehicleRequisition2);
        vehicleRequisition2.setId(2L);
        assertThat(vehicleRequisition1).isNotEqualTo(vehicleRequisition2);
        vehicleRequisition1.setId(null);
        assertThat(vehicleRequisition1).isNotEqualTo(vehicleRequisition2);
    }
}
