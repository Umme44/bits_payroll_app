package com.bits.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleRequisitionDTO.class);
        VehicleRequisitionDTO vehicleRequisitionDTO1 = new VehicleRequisitionDTO();
        vehicleRequisitionDTO1.setId(1L);
        VehicleRequisitionDTO vehicleRequisitionDTO2 = new VehicleRequisitionDTO();
        assertThat(vehicleRequisitionDTO1).isNotEqualTo(vehicleRequisitionDTO2);
        vehicleRequisitionDTO2.setId(vehicleRequisitionDTO1.getId());
        assertThat(vehicleRequisitionDTO1).isEqualTo(vehicleRequisitionDTO2);
        vehicleRequisitionDTO2.setId(2L);
        assertThat(vehicleRequisitionDTO1).isNotEqualTo(vehicleRequisitionDTO2);
        vehicleRequisitionDTO1.setId(null);
        assertThat(vehicleRequisitionDTO1).isNotEqualTo(vehicleRequisitionDTO2);
    }
}
