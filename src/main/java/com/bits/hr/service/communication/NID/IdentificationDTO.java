package com.bits.hr.service.communication.NID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentificationDTO {

    private String dateOfBirth;
    private String fatherNameBn;
    private String fatherNameEn;
    private String fullNameBn;
    private String fullNameEn;
    private String gender;
    private String motherNameBn;
    private String motherNameEn;
    private String nationalIdNumber;
    private String oldNationalIdNumber;
    private String permanentAddressEn;
    private String photoUrl;
    private String presentAddressBn;
    private String presentAddressEn;
    private String profession;
    private String spouseNameBn;
    private String spouseNameEn;

    public IdentificationDTO() {}

    public IdentificationDTO(
        String dateOfBirth,
        String fatherNameBn,
        String fatherNameEn,
        String fullNameBn,
        String fullNameEn,
        String gender,
        String motherNameBn,
        String motherNameEn,
        String nationalIdNumber,
        String oldNationalIdNumber,
        String permanentAddressEn,
        String photoUrl,
        String presentAddressBn,
        String presentAddressEn,
        String profession,
        String spouseNameBn,
        String spouseNameEn
    ) {
        this.dateOfBirth = dateOfBirth;
        this.fatherNameBn = fatherNameBn;
        this.fatherNameEn = fatherNameEn;
        this.fullNameBn = fullNameBn;
        this.fullNameEn = fullNameEn;
        this.gender = gender;
        this.motherNameBn = motherNameBn;
        this.motherNameEn = motherNameEn;
        this.nationalIdNumber = nationalIdNumber;
        this.oldNationalIdNumber = oldNationalIdNumber;
        this.permanentAddressEn = permanentAddressEn;
        this.photoUrl = photoUrl;
        this.presentAddressBn = presentAddressBn;
        this.presentAddressEn = presentAddressEn;
        this.profession = profession;
        this.spouseNameBn = spouseNameBn;
        this.spouseNameEn = spouseNameEn;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFatherNameBn() {
        return fatherNameBn;
    }

    public void setFatherNameBn(String fatherNameBn) {
        this.fatherNameBn = fatherNameBn;
    }

    public String getFatherNameEn() {
        return fatherNameEn;
    }

    public void setFatherNameEn(String fatherNameEn) {
        this.fatherNameEn = fatherNameEn;
    }

    public String getFullNameBn() {
        return fullNameBn;
    }

    public void setFullNameBn(String fullNameBn) {
        this.fullNameBn = fullNameBn;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMotherNameBn() {
        return motherNameBn;
    }

    public void setMotherNameBn(String motherNameBn) {
        this.motherNameBn = motherNameBn;
    }

    public String getMotherNameEn() {
        return motherNameEn;
    }

    public void setMotherNameEn(String motherNameEn) {
        this.motherNameEn = motherNameEn;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    public String getOldNationalIdNumber() {
        return oldNationalIdNumber;
    }

    public void setOldNationalIdNumber(String oldNationalIdNumber) {
        this.oldNationalIdNumber = oldNationalIdNumber;
    }

    public String getPermanentAddressEn() {
        return permanentAddressEn;
    }

    public void setPermanentAddressEn(String permanentAddressEn) {
        this.permanentAddressEn = permanentAddressEn;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPresentAddressBn() {
        return presentAddressBn;
    }

    public void setPresentAddressBn(String presentAddressBn) {
        this.presentAddressBn = presentAddressBn;
    }

    public String getPresentAddressEn() {
        return presentAddressEn;
    }

    public void setPresentAddressEn(String presentAddressEn) {
        this.presentAddressEn = presentAddressEn;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSpouseNameBn() {
        return spouseNameBn;
    }

    public void setSpouseNameBn(String spouseNameBn) {
        this.spouseNameBn = spouseNameBn;
    }

    public String getSpouseNameEn() {
        return spouseNameEn;
    }

    public void setSpouseNameEn(String spouseNameEn) {
        this.spouseNameEn = spouseNameEn;
    }

    @Override
    public String toString() {
        return (
            "IdentificationDTO{" +
            "dateOfBirth='" +
            dateOfBirth +
            '\'' +
            ", fatherNameBn='" +
            fatherNameBn +
            '\'' +
            ", fatherNameEn='" +
            fatherNameEn +
            '\'' +
            ", fullNameBn='" +
            fullNameBn +
            '\'' +
            ", fullNameEn='" +
            fullNameEn +
            '\'' +
            ", gender='" +
            gender +
            '\'' +
            ", motherNameBn='" +
            motherNameBn +
            '\'' +
            ", motherNameEn='" +
            motherNameEn +
            '\'' +
            ", nationalIdNumber='" +
            nationalIdNumber +
            '\'' +
            ", oldNationalIdNumber='" +
            oldNationalIdNumber +
            '\'' +
            ", permanentAddressEn='" +
            permanentAddressEn +
            '\'' +
            ", photoUrl='" +
            photoUrl +
            '\'' +
            ", presentAddressBn='" +
            presentAddressBn +
            '\'' +
            ", presentAddressEn='" +
            presentAddressEn +
            '\'' +
            ", profession='" +
            profession +
            '\'' +
            ", spouseNameBn='" +
            spouseNameBn +
            '\'' +
            ", spouseNameEn='" +
            spouseNameEn +
            '\'' +
            '}'
        );
    }
}
