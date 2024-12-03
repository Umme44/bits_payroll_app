package com.bits.hr.service.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CertificateApprovalDto {

    private String referenceId;
    private long signatoryPersonId;
    private LocalDate issueDate;
    private String reason;
}
