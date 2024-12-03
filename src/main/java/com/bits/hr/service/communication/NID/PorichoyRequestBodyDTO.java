package com.bits.hr.service.communication.NID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PorichoyRequestBodyDTO {

    String dateOfBirth;
    boolean englishTranslation = true;
    String nidNumber;

    public PorichoyRequestBodyDTO() {}

    public PorichoyRequestBodyDTO(String dateOfBirth, boolean englishTranslation, String nidNumber) {
        this.dateOfBirth = dateOfBirth;
        this.englishTranslation = englishTranslation;
        this.nidNumber = nidNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isEnglishTranslation() {
        return englishTranslation;
    }

    public void setEnglishTranslation(boolean englishTranslation) {
        this.englishTranslation = englishTranslation;
    }

    public String getNidNumber() {
        return nidNumber;
    }

    public void setNidNumber(String nidNumber) {
        this.nidNumber = nidNumber;
    }

    @Override
    public String toString() {
        return (
            "PorichoyRequestBodyDTO{" +
            "dateOfBirth='" +
            dateOfBirth +
            '\'' +
            ", englishTranslation=" +
            englishTranslation +
            ", nidNumber='" +
            nidNumber +
            '\'' +
            '}'
        );
    }
}
