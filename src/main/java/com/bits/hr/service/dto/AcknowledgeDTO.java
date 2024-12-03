package com.bits.hr.service.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcknowledgeDTO {

    private Boolean successful;
    private List<String> errors;

    public AcknowledgeDTO() {
        this.successful = true;
        this.errors = new ArrayList<>();
    }
}
