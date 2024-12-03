package com.bits.hr.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class NavBarItemAccessControlDTO {

    private boolean canManageTaxAcknowledgementReceipt = true;
}
