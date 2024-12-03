package com.bits.hr.service.dto;

public class SalaryGenerationPreValidationDTO {

    private boolean aitConfigMissing;
    private boolean salaryLocked;

    public boolean isAitConfigMissing() {
        return aitConfigMissing;
    }

    public void setAitConfigMissing(boolean aitConfigMissing) {
        this.aitConfigMissing = aitConfigMissing;
    }

    public boolean isSalaryLocked() {
        return salaryLocked;
    }

    public void setSalaryLocked(boolean salaryLocked) {
        this.salaryLocked = salaryLocked;
    }
}
