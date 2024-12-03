package com.bits.hr.service.dto;

import lombok.Data;

@Data
public class NomineeEligibilityDTO {

    boolean eligibleForGeneral = false;
    boolean eligibleForPf = false;
    boolean eligibleForGf = false;
}
