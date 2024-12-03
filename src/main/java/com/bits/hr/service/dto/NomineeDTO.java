package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.IdentityType;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.mapstruct.Mapping;

/**
 * A DTO for the {@link com.bits.hr.domain.Nominee} entity.
 */
public class NomineeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 0, max = 255)
    private String nomineeName;

    @NotNull
    @Size(min = 0, max = 255)
    private String presentAddress;

    private String relationshipWithEmployee;

    private LocalDate dateOfBirth;

    private Integer age;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "100")
    private Double sharePercentage;

    @NotNull
    private Status status;

    private String imagePath;

    @Size(min = 0, max = 255)
    private String guardianName;

    private String guardianFatherName;

    private String guardianSpouseName;

    private LocalDate guardianDateOfBirth;

    @Size(min = 0, max = 255)
    private String guardianPresentAddress;

    private String guardianDocumentName;

    private String guardianRelationshipWith;

    private String guardianImagePath;

    private Boolean isLocked;

    private LocalDate nominationDate;

    @NotNull
    @Size(min = 0, max = 255)
    private String permanentAddress;

    @Size(min = 0, max = 255)
    private String guardianPermanentAddress;

    private NomineeType nomineeType;

    private IdentityType identityType;

    private String documentName;

    private String idNumber;

    private Boolean isNidVerified;

    private IdentityType guardianIdentityType;

    private String guardianIdNumber;

    private Boolean isGuardianNidVerified;

    private Long employeeId;

    private Long approvedById;

    private String approvedByFullName;

    private Long witnessId;

    private Long memberId;

    private String pin;
    private String fullName;
    private String designationName;
    private String departmentName;
    private String unitName;
    private EmployeeCategory employeeCategory;

    private LocalDate dateOfJoining;
    private LocalDate dateOfConfirmation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getRelationshipWithEmployee() {
        return relationshipWithEmployee;
    }

    public void setRelationshipWithEmployee(String relationshipWithEmployee) {
        this.relationshipWithEmployee = relationshipWithEmployee;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianFatherName() {
        return guardianFatherName;
    }

    public void setGuardianFatherName(String guardianFatherName) {
        this.guardianFatherName = guardianFatherName;
    }

    public String getGuardianSpouseName() {
        return guardianSpouseName;
    }

    public void setGuardianSpouseName(String guardianSpouseName) {
        this.guardianSpouseName = guardianSpouseName;
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

    public String getGuardianDocumentName() {
        return guardianDocumentName;
    }

    public void setGuardianDocumentName(String guardianDocumentName) {
        this.guardianDocumentName = guardianDocumentName;
    }

    public String getGuardianRelationshipWith() {
        return guardianRelationshipWith;
    }

    public void setGuardianRelationshipWith(String guardianRelationshipWith) {
        this.guardianRelationshipWith = guardianRelationshipWith;
    }

    public String getGuardianImagePath() {
        return guardianImagePath;
    }

    public void setGuardianImagePath(String guardianImagePath) {
        this.guardianImagePath = guardianImagePath;
    }

    public Boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public LocalDate getNominationDate() {
        return nominationDate;
    }

    public void setNominationDate(LocalDate nominationDate) {
        this.nominationDate = nominationDate;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getGuardianPermanentAddress() {
        return guardianPermanentAddress;
    }

    public void setGuardianPermanentAddress(String guardianPermanentAddress) {
        this.guardianPermanentAddress = guardianPermanentAddress;
    }

    public NomineeType getNomineeType() {
        return nomineeType;
    }

    public void setNomineeType(NomineeType nomineeType) {
        this.nomineeType = nomineeType;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Boolean isIsNidVerified() {
        return isNidVerified;
    }

    public void setIsNidVerified(Boolean isNidVerified) {
        this.isNidVerified = isNidVerified;
    }

    public IdentityType getGuardianIdentityType() {
        return guardianIdentityType;
    }

    public void setGuardianIdentityType(IdentityType guardianIdentityType) {
        this.guardianIdentityType = guardianIdentityType;
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

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long employeeId) {
        this.approvedById = employeeId;
    }

    public Long getWitnessId() {
        return witnessId;
    }

    public void setWitnessId(Long employeeId) {
        this.witnessId = employeeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long employeeId) {
        this.memberId = employeeId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDateOfConfirmation() {
        return dateOfConfirmation;
    }

    public void setDateOfConfirmation(LocalDate dateOfConfirmation) {
        this.dateOfConfirmation = dateOfConfirmation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NomineeDTO)) {
            return false;
        }

        return id != null && id.equals(((NomineeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NomineeDTO{" +
            "id=" + getId() +
            ", nomineeName='" + getNomineeName() + "'" +
            ", presentAddress='" + getPresentAddress() + "'" +
            ", relationshipWithEmployee='" + getRelationshipWithEmployee() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", age=" + getAge() +
            ", sharePercentage=" + getSharePercentage() +
            ", imagePath='" + getImagePath() + "'" +
            ", guardianName='" + getGuardianName() + "'" +
            ", guardianFatherName='" + getGuardianFatherName() + "'" +
            ", guardianSpouseName='" + getGuardianSpouseName() + "'" +
            ", guardianDateOfBirth='" + getGuardianDateOfBirth() + "'" +
            ", guardianPresentAddress='" + getGuardianPresentAddress() + "'" +
            ", guardianDocumentName='" + getGuardianDocumentName() + "'" +
            ", guardianRelationshipWith='" + getGuardianRelationshipWith() + "'" +
            ", guardianImagePath='" + getGuardianImagePath() + "'" +
            ", isLocked='" + isIsLocked() + "'" +
            ", nominationDate='" + getNominationDate() + "'" +
            ", permanentAddress='" + getPermanentAddress() + "'" +
            ", guardianPermanentAddress='" + getGuardianPermanentAddress() + "'" +
            ", nomineeType='" + getNomineeType() + "'" +
            ", identityType='" + getIdentityType() + "'" +
            ", documentName='" + getDocumentName() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", isNidVerified='" + isIsNidVerified() + "'" +
            ", guardianIdentityType='" + getGuardianIdentityType() + "'" +
            ", guardianIdNumber='" + getGuardianIdNumber() + "'" +
            ", isGuardianNidVerified='" + isIsGuardianNidVerified() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", approvedById=" + getApprovedById() +
            ", witnessId=" + getWitnessId() +
            ", memberId=" + getMemberId() +
            "}";
    }

    public String getApprovedByFullName() {
        return approvedByFullName;
    }

    public void setApprovedByFullName(String approvedByFullName) {
        this.approvedByFullName = approvedByFullName;
    }
}
