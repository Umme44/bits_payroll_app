package com.bits.hr.service.dto;

import lombok.Data;

/**
 * A DTO for the map {@link com.bits.hr.domain.BankBranch} with {@link com.bits.hr.domain.Employee} entity.
 */

@Data
public class EmployeeBankDetailsDTO {

    private String bankName;
    private String bankAccountNumber;
    private String bankBranch;
}
