package com.bits.hr.service.dto;

import lombok.Data;

@Data
public class RrfRaiseValidityDTO {

    private boolean canRaiseRRFOwn = false;
    private boolean canRaiseRRFOnBehalf = false;
}
