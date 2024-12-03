package com.bits.hr.service.dto;

public class UserTaxAcknowledgementValidationDTO {

    private boolean isValid;
    private String validationMessage;

    public UserTaxAcknowledgementValidationDTO(boolean isValid, String validationMessage) {
        this.isValid = isValid;
        this.validationMessage = validationMessage;
    }

    public boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        isValid = isValid;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
}
