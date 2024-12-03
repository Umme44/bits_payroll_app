package com.bits.hr.service.dto;

import java.util.List;
import lombok.Data;

@Data
public class InsuranceRegistrationAdminDTO {

    private String employeePin;
    private List<InsuranceRegistrationDTO> insuranceRegistrationDTOList;
}
