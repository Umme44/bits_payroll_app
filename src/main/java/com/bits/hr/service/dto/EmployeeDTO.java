package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.BloodGroup;
import com.bits.hr.domain.enumeration.CardType;
import com.bits.hr.domain.enumeration.DisbursementMethod;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.MaritalStatus;
import com.bits.hr.domain.enumeration.PayType;
import com.bits.hr.domain.enumeration.Religion;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.Employee} entity.
 */
public class EmployeeDTO implements Serializable {

    private Long id;

    private String referenceId;

    @NotNull
    private String pin;

    private String picture;

    private String fullName;

    private String surName;

    private String nationalIdNo;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String fatherName;

    private String motherName;

    private BloodGroup bloodGroup;

    private String presentAddress;

    private String permanentAddress;

    private String personalContactNo;

    private String personalEmail;

    private Religion religion;

    private MaritalStatus maritalStatus;

    private LocalDate dateOfMarriage;

    private String spouseName;

    private String officialEmail;

    private String officialContactNo;

    private String officePhoneExtension;

    private String whatsappId;

    private String skypeId;

    private String emergencyContactPersonName;

    private String emergencyContactPersonRelationshipWithEmployee;

    private String emergencyContactPersonContactNumber;

    private Double mainGrossSalary;

    private EmployeeCategory employeeCategory;

    private String location;

    private LocalDate dateOfJoining;

    private LocalDate dateOfConfirmation;

    private Boolean isProbationaryPeriodExtended;

    private LocalDate probationPeriodExtendedTo;

    private PayType payType;

    private DisbursementMethod disbursementMethod;

    private String bankName;

    private String bankAccountNo;

    private Long mobileCelling;

    private String bkashNumber;

    private CardType cardType;

    private String cardNumber;

    private String tinNumber;

    private String passportNo;

    private String passportPlaceOfIssue;

    private LocalDate passportIssuedDate;

    private LocalDate passportExpiryDate;

    private Gender gender;

    private Double welfareFundDeduction;

    private EmploymentStatus employmentStatus;

    private Boolean hasDisabledChild;

    private Boolean isFirstTimeAitGiver;

    private Boolean isSalaryHold;

    private Boolean isFestivalBonusHold;

    private Boolean isPhysicallyDisabled;

    private Boolean isFreedomFighter;

    private Boolean hasOverTime;

    private LocalDate probationPeriodEndDate;

    private LocalDate contractPeriodExtendedTo;

    private LocalDate contractPeriodEndDate;

    private CardType cardType02;

    private String cardNumber02;

    private CardType cardType03;

    private String cardNumber03;

    private String taxesCircle;

    private String taxesZone;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance01;

    private LocalDate allowance01EffectiveFrom;

    private LocalDate allowance01EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance02;

    private LocalDate allowance02EffectiveFrom;

    private LocalDate allowance02EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance03;

    private LocalDate allowance03EffectiveFrom;

    private LocalDate allowance03EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance04;

    private LocalDate allowance04EffectiveFrom;

    private LocalDate allowance04EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance05;

    private LocalDate allowance05EffectiveFrom;

    private LocalDate allowance05EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double allowance06;

    private LocalDate allowance06EffectiveFrom;

    private LocalDate allowance06EffectiveTo;

    private Boolean isTaxPaidByOrganisation;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private LocalDate updatedAt;

    private Boolean isAllowedToGiveOnlineAttendance;

    private Integer noticePeriodInDays;

    private Boolean isFixedTermContract;

    private Instant currentInTime;

    private Instant currentOutTime;

    private Instant onlineAttendanceSanctionedAt;

    private Boolean isNidVerified;

    private Boolean canRaiseRrfOnBehalf;

    private Boolean canManageTaxAcknowledgementReceipt;

    private Boolean isEligibleForAutomatedAttendance;

    private Boolean isFestivalBonusDisabled;

    private Long officeLocationId;

    private Long designationId;

    private Long departmentId;

    private Long reportingToId;

    private Long nationalityId;

    private String nationalityNationalityName;

    private Long bankBranchId;

    private Long bandId;

    private Long unitId;

    private Long userId;

    private String userLogin;

    private String designationName;
    private String departmentName;
    private String reportingToName;
    private String reportingToPin;
    private String bankBranchName;
    private String bandName;
    private String unitName;

    private Boolean isCurrentlyResigned;

    private String floor;

    private String deskLocation;

    private Boolean isBillableResource;

    private Boolean isAugmentedResource;

    private LocalDate lastWorkingDay;

    public String getRrfNumber() {
        return rrfNumber;
    }

    public void setRrfNumber(String rrfNumber) {
        this.rrfNumber = rrfNumber;
    }

    private String rrfNumber;


    public Boolean getIsCurrentlyResigned() {
        return isCurrentlyResigned;
    }

    public void setIsCurrentlyResigned(Boolean isCurrentlyResigned) {
        this.isCurrentlyResigned = isCurrentlyResigned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceId() {
        if (referenceId == null) return "";
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getNationalIdNo() {
        return nationalIdNo;
    }

    public void setNationalIdNo(String nationalIdNo) {
        this.nationalIdNo = nationalIdNo;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
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

    public String getPersonalContactNo() {
        return personalContactNo;
    }

    public void setPersonalContactNo(String personalContactNo) {
        this.personalContactNo = personalContactNo;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public LocalDate getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(LocalDate dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getOfficialContactNo() {
        return officialContactNo;
    }

    public void setOfficialContactNo(String officialContactNo) {
        this.officialContactNo = officialContactNo;
    }

    public String getOfficePhoneExtension() {
        return officePhoneExtension;
    }

    public void setOfficePhoneExtension(String officePhoneExtension) {
        this.officePhoneExtension = officePhoneExtension;
    }

    public String getWhatsappId() {
        return whatsappId;
    }

    public void setWhatsappId(String whatsappId) {
        this.whatsappId = whatsappId;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public String getEmergencyContactPersonName() {
        return emergencyContactPersonName;
    }

    public void setEmergencyContactPersonName(String emergencyContactPersonName) {
        this.emergencyContactPersonName = emergencyContactPersonName;
    }

    public String getEmergencyContactPersonRelationshipWithEmployee() {
        return emergencyContactPersonRelationshipWithEmployee;
    }

    public void setEmergencyContactPersonRelationshipWithEmployee(String emergencyContactPersonRelationshipWithEmployee) {
        this.emergencyContactPersonRelationshipWithEmployee = emergencyContactPersonRelationshipWithEmployee;
    }

    public String getEmergencyContactPersonContactNumber() {
        return emergencyContactPersonContactNumber;
    }

    public void setEmergencyContactPersonContactNumber(String emergencyContactPersonContactNumber) {
        this.emergencyContactPersonContactNumber = emergencyContactPersonContactNumber;
    }

    public Double getMainGrossSalary() {
        if (mainGrossSalary == null) return 0d;
        return mainGrossSalary;
    }

    public void setMainGrossSalary(Double mainGrossSalary) {
        this.mainGrossSalary = mainGrossSalary;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Boolean isIsProbationaryPeriodExtended() {
        return isProbationaryPeriodExtended;
    }

    public void setIsProbationaryPeriodExtended(Boolean isProbationaryPeriodExtended) {
        this.isProbationaryPeriodExtended = isProbationaryPeriodExtended;
    }

    public LocalDate getProbationPeriodExtendedTo() {
        return probationPeriodExtendedTo;
    }

    public void setProbationPeriodExtendedTo(LocalDate probationPeriodExtendedTo) {
        this.probationPeriodExtendedTo = probationPeriodExtendedTo;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public DisbursementMethod getDisbursementMethod() {
        return disbursementMethod;
    }

    public void setDisbursementMethod(DisbursementMethod disbursementMethod) {
        this.disbursementMethod = disbursementMethod;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public Long getMobileCelling() {
        return mobileCelling;
    }

    public void setMobileCelling(Long mobileCelling) {
        this.mobileCelling = mobileCelling;
    }

    public String getBkashNumber() {
        return bkashNumber;
    }

    public void setBkashNumber(String bkashNumber) {
        this.bkashNumber = bkashNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportPlaceOfIssue() {
        return passportPlaceOfIssue;
    }

    public void setPassportPlaceOfIssue(String passportPlaceOfIssue) {
        this.passportPlaceOfIssue = passportPlaceOfIssue;
    }

    public LocalDate getPassportIssuedDate() {
        return passportIssuedDate;
    }

    public void setPassportIssuedDate(LocalDate passportIssuedDate) {
        this.passportIssuedDate = passportIssuedDate;
    }

    public LocalDate getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setPassportExpiryDate(LocalDate passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getWelfareFundDeduction() {
        return welfareFundDeduction;
    }

    public void setWelfareFundDeduction(Double welfareFundDeduction) {
        this.welfareFundDeduction = welfareFundDeduction;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Boolean isHasDisabledChild() {
        return hasDisabledChild;
    }

    public void setHasDisabledChild(Boolean hasDisabledChild) {
        this.hasDisabledChild = hasDisabledChild;
    }

    public Boolean isIsFirstTimeAitGiver() {
        return isFirstTimeAitGiver;
    }

    public void setIsFirstTimeAitGiver(Boolean isFirstTimeAitGiver) {
        this.isFirstTimeAitGiver = isFirstTimeAitGiver;
    }

    public Boolean isIsSalaryHold() {
        return isSalaryHold;
    }

    public void setIsSalaryHold(Boolean isSalaryHold) {
        this.isSalaryHold = isSalaryHold;
    }

    public Boolean isIsFestivalBonusHold() {
        return isFestivalBonusHold;
    }

    public void setIsFestivalBonusHold(Boolean isFestivalBonusHold) {
        this.isFestivalBonusHold = isFestivalBonusHold;
    }

    public Boolean isIsPhysicallyDisabled() {
        return isPhysicallyDisabled;
    }

    public void setIsPhysicallyDisabled(Boolean isPhysicallyDisabled) {
        this.isPhysicallyDisabled = isPhysicallyDisabled;
    }

    public Boolean isIsFreedomFighter() {
        return isFreedomFighter;
    }

    public void setIsFreedomFighter(Boolean isFreedomFighter) {
        this.isFreedomFighter = isFreedomFighter;
    }

    public Boolean isHasOverTime() {
        return hasOverTime;
    }

    public void setHasOverTime(Boolean hasOverTime) {
        this.hasOverTime = hasOverTime;
    }

    public LocalDate getProbationPeriodEndDate() {
        return probationPeriodEndDate;
    }

    public void setProbationPeriodEndDate(LocalDate probationPeriodEndDate) {
        this.probationPeriodEndDate = probationPeriodEndDate;
    }

    public LocalDate getContractPeriodExtendedTo() {
        return contractPeriodExtendedTo;
    }

    public void setContractPeriodExtendedTo(LocalDate contractPeriodExtendedTo) {
        this.contractPeriodExtendedTo = contractPeriodExtendedTo;
    }

    public LocalDate getContractPeriodEndDate() {
        return contractPeriodEndDate;
    }

    public void setContractPeriodEndDate(LocalDate contractPeriodEndDate) {
        this.contractPeriodEndDate = contractPeriodEndDate;
    }

    public CardType getCardType02() {
        return cardType02;
    }

    public void setCardType02(CardType cardType02) {
        this.cardType02 = cardType02;
    }

    public String getCardNumber02() {
        return cardNumber02;
    }

    public void setCardNumber02(String cardNumber02) {
        this.cardNumber02 = cardNumber02;
    }

    public CardType getCardType03() {
        return cardType03;
    }

    public void setCardType03(CardType cardType03) {
        this.cardType03 = cardType03;
    }

    public String getCardNumber03() {
        return cardNumber03;
    }

    public void setCardNumber03(String cardNumber03) {
        this.cardNumber03 = cardNumber03;
    }

    public Double getAllowance01() {
        return allowance01;
    }

    public void setAllowance01(Double allowance01) {
        this.allowance01 = allowance01;
    }

    public LocalDate getAllowance01EffectiveFrom() {
        return allowance01EffectiveFrom;
    }

    public void setAllowance01EffectiveFrom(LocalDate allowance01EffectiveFrom) {
        this.allowance01EffectiveFrom = allowance01EffectiveFrom;
    }

    public LocalDate getAllowance01EffectiveTo() {
        return allowance01EffectiveTo;
    }

    public void setAllowance01EffectiveTo(LocalDate allowance01EffectiveTo) {
        this.allowance01EffectiveTo = allowance01EffectiveTo;
    }

    public Double getAllowance02() {
        return allowance02;
    }

    public void setAllowance02(Double allowance02) {
        this.allowance02 = allowance02;
    }

    public LocalDate getAllowance02EffectiveFrom() {
        return allowance02EffectiveFrom;
    }

    public void setAllowance02EffectiveFrom(LocalDate allowance02EffectiveFrom) {
        this.allowance02EffectiveFrom = allowance02EffectiveFrom;
    }

    public LocalDate getAllowance02EffectiveTo() {
        return allowance02EffectiveTo;
    }

    public void setAllowance02EffectiveTo(LocalDate allowance02EffectiveTo) {
        this.allowance02EffectiveTo = allowance02EffectiveTo;
    }

    public Double getAllowance03() {
        return allowance03;
    }

    public void setAllowance03(Double allowance03) {
        this.allowance03 = allowance03;
    }

    public LocalDate getAllowance03EffectiveFrom() {
        return allowance03EffectiveFrom;
    }

    public void setAllowance03EffectiveFrom(LocalDate allowance03EffectiveFrom) {
        this.allowance03EffectiveFrom = allowance03EffectiveFrom;
    }

    public LocalDate getAllowance03EffectiveTo() {
        return allowance03EffectiveTo;
    }

    public void setAllowance03EffectiveTo(LocalDate allowance03EffectiveTo) {
        this.allowance03EffectiveTo = allowance03EffectiveTo;
    }

    public Double getAllowance04() {
        return allowance04;
    }

    public void setAllowance04(Double allowance04) {
        this.allowance04 = allowance04;
    }

    public LocalDate getAllowance04EffectiveFrom() {
        return allowance04EffectiveFrom;
    }

    public void setAllowance04EffectiveFrom(LocalDate allowance04EffectiveFrom) {
        this.allowance04EffectiveFrom = allowance04EffectiveFrom;
    }

    public LocalDate getAllowance04EffectiveTo() {
        return allowance04EffectiveTo;
    }

    public void setAllowance04EffectiveTo(LocalDate allowance04EffectiveTo) {
        this.allowance04EffectiveTo = allowance04EffectiveTo;
    }

    public Double getAllowance05() {
        return allowance05;
    }

    public void setAllowance05(Double allowance05) {
        this.allowance05 = allowance05;
    }

    public LocalDate getAllowance05EffectiveFrom() {
        return allowance05EffectiveFrom;
    }

    public void setAllowance05EffectiveFrom(LocalDate allowance05EffectiveFrom) {
        this.allowance05EffectiveFrom = allowance05EffectiveFrom;
    }

    public LocalDate getAllowance05EffectiveTo() {
        return allowance05EffectiveTo;
    }

    public void setAllowance05EffectiveTo(LocalDate allowance05EffectiveTo) {
        this.allowance05EffectiveTo = allowance05EffectiveTo;
    }

    public Double getAllowance06() {
        return allowance06;
    }

    public void setAllowance06(Double allowance06) {
        this.allowance06 = allowance06;
    }

    public LocalDate getAllowance06EffectiveFrom() {
        return allowance06EffectiveFrom;
    }

    public void setAllowance06EffectiveFrom(LocalDate allowance06EffectiveFrom) {
        this.allowance06EffectiveFrom = allowance06EffectiveFrom;
    }

    public LocalDate getAllowance06EffectiveTo() {
        return allowance06EffectiveTo;
    }

    public void setAllowance06EffectiveTo(LocalDate allowance06EffectiveTo) {
        this.allowance06EffectiveTo = allowance06EffectiveTo;
    }

    public Boolean isIsTaxPaidByOrganisation() {
        return isTaxPaidByOrganisation;
    }

    public void setIsTaxPaidByOrganisation(Boolean isTaxPaidByOrganisation) {
        this.isTaxPaidByOrganisation = isTaxPaidByOrganisation;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean isIsAllowedToGiveOnlineAttendance() {
        return isAllowedToGiveOnlineAttendance;
    }

    public void setIsAllowedToGiveOnlineAttendance(Boolean isAllowedToGiveOnlineAttendance) {
        this.isAllowedToGiveOnlineAttendance = isAllowedToGiveOnlineAttendance;
    }

    public Integer getNoticePeriodInDays() {
        return noticePeriodInDays;
    }

    public void setNoticePeriodInDays(Integer noticePeriodInDays) {
        this.noticePeriodInDays = noticePeriodInDays;
    }

    public Boolean isIsFixedTermContract() {
        return isFixedTermContract;
    }

    public void setIsFixedTermContract(Boolean isFixedTermContract) {
        this.isFixedTermContract = isFixedTermContract;
    }

    public Instant getCurrentInTime() {
        return currentInTime;
    }

    public void setCurrentInTime(Instant currentInTime) {
        this.currentInTime = currentInTime;
    }

    public Instant getCurrentOutTime() {
        return currentOutTime;
    }

    public void setCurrentOutTime(Instant currentOutTime) {
        this.currentOutTime = currentOutTime;
    }

    public Instant getOnlineAttendanceSanctionedAt() {
        return onlineAttendanceSanctionedAt;
    }

    public void setOnlineAttendanceSanctionedAt(Instant onlineAttendanceSanctionedAt) {
        this.onlineAttendanceSanctionedAt = onlineAttendanceSanctionedAt;
    }

    public Boolean isIsNidVerified() {
        return isNidVerified;
    }

    public void setIsNidVerified(Boolean isNidVerified) {
        this.isNidVerified = isNidVerified;
    }

    public Boolean isCanRaiseRrfOnBehalf() {
        return canRaiseRrfOnBehalf;
    }

    public void setCanRaiseRrfOnBehalf(Boolean canRaiseRrfOnBehalf) {
        this.canRaiseRrfOnBehalf = canRaiseRrfOnBehalf;
    }

    public Boolean isCanManageTaxAcknowledgementReceipt() {
        return canManageTaxAcknowledgementReceipt;
    }

    public void setCanManageTaxAcknowledgementReceipt(Boolean canManageTaxAcknowledgementReceipt) {
        this.canManageTaxAcknowledgementReceipt = canManageTaxAcknowledgementReceipt;
    }

    public Boolean isIsEligibleForAutomatedAttendance() {
        return isEligibleForAutomatedAttendance;
    }

    public void setIsEligibleForAutomatedAttendance(Boolean isEligibleForAutomatedAttendance) {
        this.isEligibleForAutomatedAttendance = isEligibleForAutomatedAttendance;
    }

    public Boolean isIsFestivalBonusDisabled() {
        return isFestivalBonusDisabled;
    }

    public void setIsFestivalBonusDisabled(Boolean isFestivalBonusDisabled) {
        this.isFestivalBonusDisabled = isFestivalBonusDisabled;
    }

    public Long getOfficeLocationId() {
        return officeLocationId;
    }

    public void setOfficeLocationId(Long locationId) {
        this.officeLocationId = locationId;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getReportingToId() {
        return reportingToId;
    }

    public void setReportingToId(Long employeeId) {
        this.reportingToId = employeeId;
    }

    public Long getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Long nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getNationalityNationalityName() {
        return nationalityNationalityName;
    }

    public void setNationalityNationalityName(String nationalityNationalityName) {
        this.nationalityNationalityName = nationalityNationalityName;
    }

    public Long getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Long bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Boolean getProbationaryPeriodExtended() {
        return isProbationaryPeriodExtended;
    }

    public void setProbationaryPeriodExtended(Boolean probationaryPeriodExtended) {
        isProbationaryPeriodExtended = probationaryPeriodExtended;
    }

    public Boolean getHasDisabledChild() {
        return hasDisabledChild;
    }

    public Boolean getFirstTimeAitGiver() {
        return isFirstTimeAitGiver;
    }

    public void setFirstTimeAitGiver(Boolean firstTimeAitGiver) {
        isFirstTimeAitGiver = firstTimeAitGiver;
    }

    public Boolean getSalaryHold() {
        return isSalaryHold;
    }

    public void setSalaryHold(Boolean salaryHold) {
        isSalaryHold = salaryHold;
    }

    public Boolean getFestivalBonusHold() {
        return isFestivalBonusHold;
    }

    public void setFestivalBonusHold(Boolean festivalBonusHold) {
        isFestivalBonusHold = festivalBonusHold;
    }

    public Boolean getPhysicallyDisabled() {
        return isPhysicallyDisabled;
    }

    public void setPhysicallyDisabled(Boolean physicallyDisabled) {
        isPhysicallyDisabled = physicallyDisabled;
    }

    public Boolean getFreedomFighter() {
        return isFreedomFighter;
    }

    public void setFreedomFighter(Boolean freedomFighter) {
        isFreedomFighter = freedomFighter;
    }

    public Boolean getHasOverTime() {
        return hasOverTime;
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

    public String getReportingToName() {
        return reportingToName;
    }

    public void setReportingToName(String reportingToName) {
        this.reportingToName = reportingToName;
    }

    public String getReportingToPin() {
        return reportingToPin;
    }

    public void setReportingToPin(String reportingToPin) {
        this.reportingToPin = reportingToPin;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getTaxesCircle() {
        return taxesCircle;
    }

    public void setTaxesCircle(String taxesCircle) {
        this.taxesCircle = taxesCircle;
    }

    public String getTaxesZone() {
        return taxesZone;
    }

    public void setTaxesZone(String taxesZone) {
        this.taxesZone = taxesZone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDeskLocation() {
        return deskLocation;
    }

    public void setDeskLocation(String deskLocation) {
        this.deskLocation = deskLocation;
    }

    public Boolean getIsBillableResource() {
        return isBillableResource;
    }

    public void setIsBillableResource(Boolean isBillableResource) {
        this.isBillableResource = isBillableResource;
    }

    public Boolean getIsAugmentedResource() {
        return isAugmentedResource;
    }

    public void setIsAugmentedResource(Boolean isAugmentedResource) {
        this.isAugmentedResource = isAugmentedResource;
    }

    public LocalDate getLastWorkingDay() {
        return lastWorkingDay;
    }

    public void setLastWorkingDay(LocalDate lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }


    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + getId() +
            ", referenceId='" + getReferenceId() + "'" +
            ", pin='" + getPin() + "'" +
            ", rrfNumber='" + getRrfNumber() + "'" +
            ", picture='" + getPicture() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", surName='" + getSurName() + "'" +
            ", nationalIdNo='" + getNationalIdNo() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", motherName='" + getMotherName() + "'" +
            ", bloodGroup='" + getBloodGroup() + "'" +
            ", presentAddress='" + getPresentAddress() + "'" +
            ", permanentAddress='" + getPermanentAddress() + "'" +
            ", personalContactNo='" + getPersonalContactNo() + "'" +
            ", personalEmail='" + getPersonalEmail() + "'" +
            ", religion='" + getReligion() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", dateOfMarriage='" + getDateOfMarriage() + "'" +
            ", spouseName='" + getSpouseName() + "'" +
            ", officialEmail='" + getOfficialEmail() + "'" +
            ", officialContactNo='" + getOfficialContactNo() + "'" +
            ", officePhoneExtension='" + getOfficePhoneExtension() + "'" +
            ", whatsappId='" + getWhatsappId() + "'" +
            ", skypeId='" + getSkypeId() + "'" +
            ", emergencyContactPersonName='" + getEmergencyContactPersonName() + "'" +
            ", emergencyContactPersonRelationshipWithEmployee='" + getEmergencyContactPersonRelationshipWithEmployee() + "'" +
            ", emergencyContactPersonContactNumber='" + getEmergencyContactPersonContactNumber() + "'" +
            ", mainGrossSalary=" + getMainGrossSalary() +
            ", employeeCategory='" + getEmployeeCategory() + "'" +
            ", location='" + getLocation() + "'" +
            ", dateOfJoining='" + getDateOfJoining() + "'" +
            ", dateOfConfirmation='" + getDateOfConfirmation() + "'" +
            ", isProbationaryPeriodExtended='" + isIsProbationaryPeriodExtended() + "'" +
            ", probationPeriodExtendedTo='" + getProbationPeriodExtendedTo() + "'" +
            ", payType='" + getPayType() + "'" +
            ", disbursementMethod='" + getDisbursementMethod() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", bankAccountNo='" + getBankAccountNo() + "'" +
            ", mobileCelling=" + getMobileCelling() +
            ", bkashNumber='" + getBkashNumber() + "'" +
            ", cardType='" + getCardType() + "'" +
            ", cardNumber='" + getCardNumber() + "'" +
            ", tinNumber='" + getTinNumber() + "'" +
            ", taxesCircle='" + getTaxesCircle() + "'" +
            ", taxesZone='" + getTaxesZone() + "'" +
            ", passportNo='" + getPassportNo() + "'" +
            ", passportPlaceOfIssue='" + getPassportPlaceOfIssue() + "'" +
            ", passportIssuedDate='" + getPassportIssuedDate() + "'" +
            ", passportExpiryDate='" + getPassportExpiryDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", welfareFundDeduction=" + getWelfareFundDeduction() +
            ", employmentStatus='" + getEmploymentStatus() + "'" +
            ", hasDisabledChild='" + isHasDisabledChild() + "'" +
            ", isFirstTimeAitGiver='" + isIsFirstTimeAitGiver() + "'" +
            ", isSalaryHold='" + isIsSalaryHold() + "'" +
            ", isFestivalBonusHold='" + isIsFestivalBonusHold() + "'" +
            ", isPhysicallyDisabled='" + isIsPhysicallyDisabled() + "'" +
            ", isFreedomFighter='" + isIsFreedomFighter() + "'" +
            ", hasOverTime='" + isHasOverTime() + "'" +
            ", probationPeriodEndDate='" + getProbationPeriodEndDate() + "'" +
            ", contractPeriodExtendedTo='" + getContractPeriodExtendedTo() + "'" +
            ", contractPeriodEndDate='" + getContractPeriodEndDate() + "'" +
            ", cardType02='" + getCardType02() + "'" +
            ", cardNumber02='" + getCardNumber02() + "'" +
            ", cardType03='" + getCardType03() + "'" +
            ", cardNumber03='" + getCardNumber03() + "'" +
            ", allowance01=" + getAllowance01() +
            ", allowance01EffectiveFrom='" + getAllowance01EffectiveFrom() + "'" +
            ", allowance01EffectiveTo='" + getAllowance01EffectiveTo() + "'" +
            ", allowance02=" + getAllowance02() +
            ", allowance02EffectiveFrom='" + getAllowance02EffectiveFrom() + "'" +
            ", allowance02EffectiveTo='" + getAllowance02EffectiveTo() + "'" +
            ", allowance03=" + getAllowance03() +
            ", allowance03EffectiveFrom='" + getAllowance03EffectiveFrom() + "'" +
            ", allowance03EffectiveTo='" + getAllowance03EffectiveTo() + "'" +
            ", allowance04=" + getAllowance04() +
            ", allowance04EffectiveFrom='" + getAllowance04EffectiveFrom() + "'" +
            ", allowance04EffectiveTo='" + getAllowance04EffectiveTo() + "'" +
            ", allowance05=" + getAllowance05() +
            ", allowance05EffectiveFrom='" + getAllowance05EffectiveFrom() + "'" +
            ", allowance05EffectiveTo='" + getAllowance05EffectiveTo() + "'" +
            ", allowance06=" + getAllowance06() +
            ", allowance06EffectiveFrom='" + getAllowance06EffectiveFrom() + "'" +
            ", allowance06EffectiveTo='" + getAllowance06EffectiveTo() + "'" +
            ", isTaxPaidByOrganisation='" + isIsTaxPaidByOrganisation() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isAllowedToGiveOnlineAttendance='" + isIsAllowedToGiveOnlineAttendance() + "'" +
            ", noticePeriodInDays=" + getNoticePeriodInDays() +
            ", isFixedTermContract='" + isIsFixedTermContract() + "'" +
            ", currentInTime='" + getCurrentInTime() + "'" +
            ", currentOutTime='" + getCurrentOutTime() + "'" +
            ", onlineAttendanceSanctionedAt='" + getOnlineAttendanceSanctionedAt() + "'" +
            ", isNidVerified='" + isIsNidVerified() + "'" +
            ", canRaiseRrfOnBehalf='" + isCanRaiseRrfOnBehalf() + "'" +
            ", canManageTaxAcknowledgementReceipt='" + isCanManageTaxAcknowledgementReceipt() + "'" +
            ", isEligibleForAutomatedAttendance='" + isIsEligibleForAutomatedAttendance() + "'" +
            ", officeLocationId=" + getOfficeLocationId() +
            ", designationId=" + getDesignationId() +
            ", departmentId=" + getDepartmentId() +
            ", reportingToId=" + getReportingToId() +
            ", nationalityId=" + getNationalityId() +
            ", nationalityNationalityName='" + getNationalityNationalityName() + "'" +
            ", bankBranchId=" + getBankBranchId() +
            ", bandId=" + getBandId() +
            ", unitId=" + getUnitId() +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", isCurrentlyResigned='" + getIsCurrentlyResigned() + "'" +
            ", floor='" + getFloor() + "'" +
            ", deskLocation='" + getDeskLocation() + "'" +
            ", isBillableResource='" + getIsBillableResource() + "'" +
            ", isAugmentedResource='" + getIsAugmentedResource() + "'" +
            ", lastWorkingDay='" + getLastWorkingDay() + "'" +
            "}";
    }
}
