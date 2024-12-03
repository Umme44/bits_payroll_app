package com.bits.hr.service.dto;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class HoldFbDisbursementApprovalDTO {

    private List<Long> listOfId;

    @NotNull
    private LocalDate disbursedAt;

    @Size(min = 0, max = 255)
    private String remarks;
}
