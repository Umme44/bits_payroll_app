package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.IdentityType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.PfNominee} entity.
 */
public class PfNomineeDTO implements Serializable {

    private Long id;

    private LocalDate nominationDate;

    private String fullName;

    private String presentAddress;

    private String permanentAddress;

    private String relationship;

    private LocalDate dateOfBirth;

    private Integer age;

    private Double sharePercentage;

    private String nidNumber;

    private Boolean isNidVerified;

    private String passportNumber;

    private String brnNumber;

    private String photo;

    private String guardianName;

    private String guardianFatherOrSpouseName;

    private LocalDate guardianDateOfBirth;

    private String guardianPresentAddress;

    private String guardianPermanentAddress;

    private String guardianProofOfIdentityOfLegalGuardian;

    private String guardianRelationshipWithNominee;

    private String guardianNidNumber;

    private String guardianBrnNumber;

    private String guardianIdNumber;

    private Boolean isGuardianNidVerified;

    private Boolean isApproved;

    @NotNull
    private IdentityType identityType;

    @NotNull
    @Size(min = 0, max = 50)
    private String idNumber;

    @Size(min = 0, max = 200)
    private String documentName;

    private IdentityType guardianIdentityType;

    @Size(min = 0, max = 200)
    private String guardianDocumentName;

    private Long pfAccountId;

    private String pin;

    private String accHolderName;

    private Long pfWitnessId;

    private String pfWitnessFullName;

    private Long approvedById;

    private String approvedByFullName;

    private byte[] pfNomineeImage;

    private String designationName;

    private String departmentName;

    private String unitName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getNominationDate() {
        return nominationDate;
    }

    public void setNominationDate(LocalDate nominationDate) {
        this.nominationDate = nominationDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(Double sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public String getNidNumber() {
        return nidNumber;
    }

    public void setNidNumber(String nidNumber) {
        this.nidNumber = nidNumber;
    }

    public Boolean isIsNidVerified() {
        return isNidVerified;
    }

    public void setIsNidVerified(Boolean isNidVerified) {
        this.isNidVerified = isNidVerified;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getBrnNumber() {
        return brnNumber;
    }

    public void setBrnNumber(String brnNumber) {
        this.brnNumber = brnNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianFatherOrSpouseName() {
        return guardianFatherOrSpouseName;
    }

    public void setGuardianFatherOrSpouseName(String guardianFatherOrSpouseName) {
        this.guardianFatherOrSpouseName = guardianFatherOrSpouseName;
    }

    public LocalDate getGuardianDateOfBirth() {
        return guardianDateOfBirth;
    }

    public void setGuardianDateOfBirth(LocalDate guardianDateOfBirth) {
        this.guardianDateOfBirth = guardianDateOfBirth;
    }

    public String getGuardianPresentAddress() {
        return guardianPresentAddress;
    }

    public void setGuardianPresentAddress(String guardianPresentAddress) {
        this.guardianPresentAddress = guardianPresentAddress;
    }

    public String getGuardianPermanentAddress() {
        return guardianPermanentAddress;
    }

    public void setGuardianPermanentAddress(String guardianPermanentAddress) {
        this.guardianPermanentAddress = guardianPermanentAddress;
    }

    public String getGuardianProofOfIdentityOfLegalGuardian() {
        return guardianProofOfIdentityOfLegalGuardian;
    }

    public void setGuardianProofOfIdentityOfLegalGuardian(String guardianProofOfIdentityOfLegalGuardian) {
        this.guardianProofOfIdentityOfLegalGuardian = guardianProofOfIdentityOfLegalGuardian;
    }

    public String getGuardianRelationshipWithNominee() {
        return guardianRelationshipWithNominee;
    }

    public void setGuardianRelationshipWithNominee(String guardianRelationshipWithNominee) {
        this.guardianRelationshipWithNominee = guardianRelationshipWithNominee;
    }

    public String getGuardianNidNumber() {
        return guardianNidNumber;
    }

    public void setGuardianNidNumber(String guardianNidNumber) {
        this.guardianNidNumber = guardianNidNumber;
    }

    public String getGuardianBrnNumber() {
        return guardianBrnNumber;
    }

    public void setGuardianBrnNumber(String guardianBrnNumber) {
        this.guardianBrnNumber = guardianBrnNumber;
    }

    public String getGuardianIdNumber() {
        return guardianIdNumber;
    }

    public void setGuardianIdNumber(String guardianIdNumber) {
        this.guardianIdNumber = guardianIdNumber;
    }

    public Boolean isIsGuardianNidVerified() {
        return isGuardianNidVerified;
    }

    public void setIsGuardianNidVerified(Boolean isGuardianNidVerified) {
        this.isGuardianNidVerified = isGuardianNidVerified;
    }

    public Boolean isIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public IdentityType getGuardianIdentityType() {
        return guardianIdentityType;
    }

    public void setGuardianIdentityType(IdentityType guardianIdentityType) {
        this.guardianIdentityType = guardianIdentityType;
    }

    public String getGuardianDocumentName() {
        return guardianDocumentName;
    }

    public void setGuardianDocumentName(String guardianDocumentName) {
        this.guardianDocumentName = guardianDocumentName;
    }

    public Long getPfAccountId() {
        return pfAccountId;
    }

    public void setPfAccountId(Long pfAccountId) {
        this.pfAccountId = pfAccountId;
    }

    public Long getPfWitnessId() {
        return pfWitnessId;
    }

    public void setPfWitnessId(Long employeeId) {
        this.pfWitnessId = employeeId;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long employeeId) {
        this.approvedById = employeeId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAccHolderName() {
        return accHolderName;
    }

    public void setAccHolderName(String accHolderName) {
        this.accHolderName = accHolderName;
    }

    public String getPfWitnessFullName() {
        return pfWitnessFullName;
    }

    public void setPfWitnessFullName(String pfWitnessFullName) {
        this.pfWitnessFullName = pfWitnessFullName;
    }

    public String getApprovedByFullName() {
        return approvedByFullName;
    }

    public void setApprovedByFullName(String approvedByFullName) {
        this.approvedByFullName = approvedByFullName;
    }

    public byte[] getPfNomineeImage() {
        return pfNomineeImage;
    }

    public void setPfNomineeImage(byte[] pfNomineeImage) {
        this.pfNomineeImage = pfNomineeImage;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PfNomineeDTO)) {
            return false;
        }

        return id != null && id.equals(((PfNomineeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfNomineeDTO{" +
            "id=" + getId() +
            ", nominationDate='" + getNominationDate() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", presentAddress='" + getPresentAddress() + "'" +
            ", permanentAddress='" + getPermanentAddress() + "'" +
            ", relationship='" + getRelationship() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", age=" + getAge() +
            ", sharePercentage=" + getSharePercentage() +
            ", nidNumber='" + getNidNumber() + "'" +
            ", isNidVerified='" + isIsNidVerified() + "'" +
            ", passportNumber='" + getPassportNumber() + "'" +
            ", brnNumber='" + getBrnNumber() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", guardianName='" + getGuardianName() + "'" +
            ", guardianFatherOrSpouseName='" + getGuardianFatherOrSpouseName() + "'" +
            ", guardianDateOfBirth='" + getGuardianDateOfBirth() + "'" +
            ", guardianPresentAddress='" + getGuardianPresentAddress() + "'" +
            ", guardianPermanentAddress='" + getGuardianPermanentAddress() + "'" +
            ", guardianProofOfIdentityOfLegalGuardian='" + getGuardianProofOfIdentityOfLegalGuardian() + "'" +
            ", guardianRelationshipWithNominee='" + getGuardianRelationshipWithNominee() + "'" +
            ", guardianNidNumber='" + getGuardianNidNumber() + "'" +
            ", guardianBrnNumber='" + getGuardianBrnNumber() + "'" +
            ", guardianIdNumber='" + getGuardianIdNumber() + "'" +
            ", isGuardianNidVerified='" + isIsGuardianNidVerified() + "'" +
            ", isApproved='" + isIsApproved() + "'" +
            ", identityType='" + getIdentityType() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", documentName='" + getDocumentName() + "'" +
            ", guardianIdentityType='" + getGuardianIdentityType() + "'" +
            ", guardianDocumentName='" + getGuardianDocumentName() + "'" +
            ", pfAccountId=" + getPfAccountId() +
            ", pfWitnessId=" + getPfWitnessId() +
            ", approvedById=" + getApprovedById() +
            "}";
    }
}
