package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.InsuranceStatus;
import lombok.Data;

@Data
public class InsuranceRegistrationFilterDTO {

    private String searchText;
    private InsuranceStatus status;
    private Boolean isCancelled;
    private Boolean isSeperated;
    private Boolean isExcluded;
}
