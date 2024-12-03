package com.bits.hr.service.config.DTO;

import com.bits.hr.domain.Employee;
import lombok.Data;

@Data
public class RRFApprovalDTO {

    private Employee requester;
    private Employee recommendedByLM;
    private Employee recommendedByHoD;
    private Employee recommendedByCTO;
    private Employee recommendedByHoHR;
    private Employee recommendedByCEO;

}
