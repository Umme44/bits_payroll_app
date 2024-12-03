package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.BloodGroup;
import com.bits.hr.domain.enumeration.CardType;
import com.bits.hr.domain.enumeration.DisbursementMethod;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.MaritalStatus;
import com.bits.hr.domain.enumeration.PayType;
import com.bits.hr.domain.enumeration.Religion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "reference_id")
    private String referenceId;

    @NotNull
    @Column(name = "pin", nullable = false, unique = true)
    private String pin;

    @Column(name = "picture")
    private String picture;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "sur_name")
    private String surName;

    @Column(name = "national_id_no")
    private String nationalIdNo;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group")
    private BloodGroup bloodGroup;

    @Column(name = "present_address")
    private String presentAddress;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "personal_contact_no")
    private String personalContactNo;

    @Column(name = "personal_email")
    private String personalEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "religion")
    private Religion religion;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "date_of_marriage")
    private LocalDate dateOfMarriage;

    @Column(name = "spouse_name")
    private String spouseName;

    @Column(name = "official_email")
    private String officialEmail;

    @Column(name = "official_contact_no")
    private String officialContactNo;

    @Column(name = "office_phone_extension")
    private String officePhoneExtension;

    @Column(name = "whatsapp_id")
    private String whatsappId;

    @Column(name = "skype_id")
    private String skypeId;

    @Column(name = "emergency_contact_person_name")
    private String emergencyContactPersonName;

    @Column(name = "emergency_contact_person_relationship_with_employee")
    private String emergencyContactPersonRelationshipWithEmployee;

    @Column(name = "emergency_contact_person_contact_number")
    private String emergencyContactPersonContactNumber;

    @Column(name = "main_gross_salary")
    private Double mainGrossSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_category")
    private EmployeeCategory employeeCategory;

    @Column(name = "location")
    private String location;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Column(name = "date_of_confirmation")
    private LocalDate dateOfConfirmation;

    @Column(name = "is_probationary_period_extended")
    private Boolean isProbationaryPeriodExtended;

    @Column(name = "probation_period_extended_to")
    private LocalDate probationPeriodExtendedTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type")
    private PayType payType;

    @Enumerated(EnumType.STRING)
    @Column(name = "disbursement_method")
    private DisbursementMethod disbursementMethod;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_no")
    private String bankAccountNo;

    @Column(name = "mobile_celling")
    private Long mobileCelling;

    @Column(name = "bkash_number")
    private String bkashNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    private CardType cardType;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "tin_number")
    private String tinNumber;

    @Column(name = "passport_no")
    private String passportNo;

    @Column(name = "passport_place_of_issue")
    private String passportPlaceOfIssue;

    @Column(name = "passport_issued_date")
    private LocalDate passportIssuedDate;

    @Column(name = "passport_expiry_date")
    private LocalDate passportExpiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "welfare_fund_deduction")
    private Double welfareFundDeduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

    @Column(name = "has_disabled_child")
    private Boolean hasDisabledChild;

    @Column(name = "is_first_time_ait_giver")
    private Boolean isFirstTimeAitGiver;

    @Column(name = "is_salary_hold")
    private Boolean isSalaryHold;

    @Column(name = "is_festival_bonus_hold")
    private Boolean isFestivalBonusHold;

    @Column(name = "is_physically_disabled")
    private Boolean isPhysicallyDisabled;

    @Column(name = "is_freedom_fighter")
    private Boolean isFreedomFighter;

    @Column(name = "has_over_time")
    private Boolean hasOverTime;

    @Column(name = "probation_period_end_date")
    private LocalDate probationPeriodEndDate;

    @Column(name = "contract_period_extended_to")
    private LocalDate contractPeriodExtendedTo;

    @Column(name = "contract_period_end_date")
    private LocalDate contractPeriodEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type_02")
    private CardType cardType02;

    @Column(name = "card_number_02")
    private String cardNumber02;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type_03")
    private CardType cardType03;

    @Column(name = "card_number_03")
    private String cardNumber03;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_01")
    private Double allowance01;

    @Column(name = "allowance_01_effective_from")
    private LocalDate allowance01EffectiveFrom;

    @Column(name = "allowance_01_effective_to")
    private LocalDate allowance01EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_02")
    private Double allowance02;

    @Column(name = "allowance_02_effective_from")
    private LocalDate allowance02EffectiveFrom;

    @Column(name = "allowance_02_effective_to")
    private LocalDate allowance02EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_03")
    private Double allowance03;

    @Column(name = "allowance_03_effective_from")
    private LocalDate allowance03EffectiveFrom;

    @Column(name = "allowance_03_effective_to")
    private LocalDate allowance03EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_04")
    private Double allowance04;

    @Column(name = "allowance_04_effective_from")
    private LocalDate allowance04EffectiveFrom;

    @Column(name = "allowance_04_effective_to")
    private LocalDate allowance04EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_05")
    private Double allowance05;

    @Column(name = "allowance_05_effective_from")
    private LocalDate allowance05EffectiveFrom;

    @Column(name = "allowance_05_effective_to")
    private LocalDate allowance05EffectiveTo;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    @Column(name = "allowance_06")
    private Double allowance06;

    @Column(name = "allowance_06_effective_from")
    private LocalDate allowance06EffectiveFrom;

    @Column(name = "allowance_06_effective_to")
    private LocalDate allowance06EffectiveTo;

    @Column(name = "is_tax_paid_by_organisation")
    private Boolean isTaxPaidByOrganisation;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_allowed_to_give_online_attendance")
    private Boolean isAllowedToGiveOnlineAttendance;

    @Column(name = "notice_period_in_days")
    private Integer noticePeriodInDays;

    @Column(name = "is_fixed_term_contract")
    private Boolean isFixedTermContract;

    @Column(name = "current_in_time")
    private Instant currentInTime;

    @Column(name = "current_out_time")
    private Instant currentOutTime;

    @Column(name = "online_attendance_sanctioned_at")
    private Instant onlineAttendanceSanctionedAt;

    @Column(name = "is_nid_verified")
    private Boolean isNidVerified;

    @Column(name = "can_raise_rrf_on_behalf")
    private Boolean canRaiseRrfOnBehalf;

    @Size(max = 250)
    @Column(name = "taxes_circle", length = 250)
    private String taxesCircle;

    @Size(max = 250)
    @Column(name = "taxes_zone", length = 250)
    private String taxesZone;

    @Column(name = "can_manage_tax_acknowledgement_receipt")
    private Boolean canManageTaxAcknowledgementReceipt;

    @Column(name = "is_eligible_for_automated_attendance")
    private Boolean isEligibleForAutomatedAttendance;

    @Column(name = "is_festival_bonus_disabled")
    private Boolean isFestivalBonusDisabled;

    @Column(name = "is_currently_resigned")
    private Boolean isCurrentlyResigned;

    @Column(name = "floor")
    private String floor;

    @Column(name = "desk_location")
    private String deskLocation;

    @Column(name = "is_billable_resource")
    private Boolean isBillableResource;

    @Column(name = "is_augmented_resource")
    private Boolean isAugmentedResource;

    @Column(name = "last_working_day")
    private LocalDate lastWorkingDay;

    @ManyToOne
    @JsonIgnoreProperties(value = "employees", allowSetters = true)
    private Location officeLocation;

    @ManyToOne
    private Designation designation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "departmentHead" }, allowSetters = true)
    private Department department;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee reportingTo;

    @ManyToOne
    private Nationality nationality;

    @ManyToOne
    private BankBranch bankBranch;

    @ManyToOne
    @JsonIgnoreProperties(value = { "createdBy", "updatedBy" }, allowSetters = true)
    private Band band;

    @ManyToOne
    private Unit unit;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public Employee referenceId(String referenceId) {
        this.setReferenceId(referenceId);
        return this;
    }

    public Employee pin(String pin) {
        this.setPin(pin);
        return this;
    }

    public Employee picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public Employee fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public Employee surName(String surName) {
        this.setSurName(surName);
        return this;
    }

    public Employee nationalIdNo(String nationalIdNo) {
        this.setNationalIdNo(nationalIdNo);
        return this;
    }

    public Employee dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public Employee placeOfBirth(String placeOfBirth) {
        this.setPlaceOfBirth(placeOfBirth);
        return this;
    }

    public Employee fatherName(String fatherName) {
        this.setFatherName(fatherName);
        return this;
    }

    public Employee motherName(String motherName) {
        this.setMotherName(motherName);
        return this;
    }

    public Employee bloodGroup(BloodGroup bloodGroup) {
        this.setBloodGroup(bloodGroup);
        return this;
    }

    public Employee presentAddress(String presentAddress) {
        this.setPresentAddress(presentAddress);
        return this;
    }

    public Employee permanentAddress(String permanentAddress) {
        this.setPermanentAddress(permanentAddress);
        return this;
    }

    public Employee personalContactNo(String personalContactNo) {
        this.setPersonalContactNo(personalContactNo);
        return this;
    }

    public Employee personalEmail(String personalEmail) {
        this.setPersonalEmail(personalEmail);
        return this;
    }

    public Employee religion(Religion religion) {
        this.setReligion(religion);
        return this;
    }

    public Employee maritalStatus(MaritalStatus maritalStatus) {
        this.setMaritalStatus(maritalStatus);
        return this;
    }

    public Employee dateOfMarriage(LocalDate dateOfMarriage) {
        this.setDateOfMarriage(dateOfMarriage);
        return this;
    }

    public Employee spouseName(String spouseName) {
        this.setSpouseName(spouseName);
        return this;
    }

    public Employee officialEmail(String officialEmail) {
        this.setOfficialEmail(officialEmail);
        return this;
    }

    public Employee officialContactNo(String officialContactNo) {
        this.setOfficialContactNo(officialContactNo);
        return this;
    }

    public Employee officePhoneExtension(String officePhoneExtension) {
        this.setOfficePhoneExtension(officePhoneExtension);
        return this;
    }

    public Employee whatsappId(String whatsappId) {
        this.setWhatsappId(whatsappId);
        return this;
    }

    public Employee skypeId(String skypeId) {
        this.setSkypeId(skypeId);
        return this;
    }

    public Employee emergencyContactPersonName(String emergencyContactPersonName) {
        this.setEmergencyContactPersonName(emergencyContactPersonName);
        return this;
    }

    public Employee emergencyContactPersonRelationshipWithEmployee(String emergencyContactPersonRelationshipWithEmployee) {
        this.setEmergencyContactPersonRelationshipWithEmployee(emergencyContactPersonRelationshipWithEmployee);
        return this;
    }

    public Employee emergencyContactPersonContactNumber(String emergencyContactPersonContactNumber) {
        this.setEmergencyContactPersonContactNumber(emergencyContactPersonContactNumber);
        return this;
    }

    public Employee mainGrossSalary(Double mainGrossSalary) {
        this.setMainGrossSalary(mainGrossSalary);
        return this;
    }

    public Employee employeeCategory(EmployeeCategory employeeCategory) {
        this.setEmployeeCategory(employeeCategory);
        return this;
    }

    public Employee location(String location) {
        this.setLocation(location);
        return this;
    }

    public Employee dateOfJoining(LocalDate dateOfJoining) {
        this.setDateOfJoining(dateOfJoining);
        return this;
    }

    public Employee dateOfConfirmation(LocalDate dateOfConfirmation) {
        this.setDateOfConfirmation(dateOfConfirmation);
        return this;
    }

    public Employee isProbationaryPeriodExtended(Boolean isProbationaryPeriodExtended) {
        this.setIsProbationaryPeriodExtended(isProbationaryPeriodExtended);
        return this;
    }

    public Employee probationPeriodExtendedTo(LocalDate probationPeriodExtendedTo) {
        this.setProbationPeriodExtendedTo(probationPeriodExtendedTo);
        return this;
    }

    public Employee payType(PayType payType) {
        this.setPayType(payType);
        return this;
    }

    public Employee disbursementMethod(DisbursementMethod disbursementMethod) {
        this.setDisbursementMethod(disbursementMethod);
        return this;
    }

    public Employee bankName(String bankName) {
        this.setBankName(bankName);
        return this;
    }

    public Employee bankAccountNo(String bankAccountNo) {
        this.setBankAccountNo(bankAccountNo);
        return this;
    }

    public Employee mobileCelling(Long mobileCelling) {
        this.setMobileCelling(mobileCelling);
        return this;
    }

    public Employee bkashNumber(String bkashNumber) {
        this.setBkashNumber(bkashNumber);
        return this;
    }

    public Employee cardType(CardType cardType) {
        this.setCardType(cardType);
        return this;
    }

    public Employee cardNumber(String cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public Employee tinNumber(String tinNumber) {
        this.setTinNumber(tinNumber);
        return this;
    }

    public Employee passportNo(String passportNo) {
        this.setPassportNo(passportNo);
        return this;
    }

    public Employee passportPlaceOfIssue(String passportPlaceOfIssue) {
        this.setPassportPlaceOfIssue(passportPlaceOfIssue);
        return this;
    }

    public Employee passportIssuedDate(LocalDate passportIssuedDate) {
        this.setPassportIssuedDate(passportIssuedDate);
        return this;
    }

    public Employee passportExpiryDate(LocalDate passportExpiryDate) {
        this.setPassportExpiryDate(passportExpiryDate);
        return this;
    }

    public Employee gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public Employee welfareFundDeduction(Double welfareFundDeduction) {
        this.setWelfareFundDeduction(welfareFundDeduction);
        return this;
    }

    public Employee employmentStatus(EmploymentStatus employmentStatus) {
        this.setEmploymentStatus(employmentStatus);
        return this;
    }

    public Employee hasDisabledChild(Boolean hasDisabledChild) {
        this.setHasDisabledChild(hasDisabledChild);
        return this;
    }

    public Employee isFirstTimeAitGiver(Boolean isFirstTimeAitGiver) {
        this.setIsFirstTimeAitGiver(isFirstTimeAitGiver);
        return this;
    }

    public Employee isSalaryHold(Boolean isSalaryHold) {
        this.setIsSalaryHold(isSalaryHold);
        return this;
    }

    public Employee isFestivalBonusHold(Boolean isFestivalBonusHold) {
        this.setIsFestivalBonusHold(isFestivalBonusHold);
        return this;
    }

    public Employee isPhysicallyDisabled(Boolean isPhysicallyDisabled) {
        this.setIsPhysicallyDisabled(isPhysicallyDisabled);
        return this;
    }

    public Employee isFreedomFighter(Boolean isFreedomFighter) {
        this.setIsFreedomFighter(isFreedomFighter);
        return this;
    }

    public Employee hasOverTime(Boolean hasOverTime) {
        this.setHasOverTime(hasOverTime);
        return this;
    }

    public Employee probationPeriodEndDate(LocalDate probationPeriodEndDate) {
        this.setProbationPeriodEndDate(probationPeriodEndDate);
        return this;
    }

    public Employee contractPeriodExtendedTo(LocalDate contractPeriodExtendedTo) {
        this.setContractPeriodExtendedTo(contractPeriodExtendedTo);
        return this;
    }

    public Employee contractPeriodEndDate(LocalDate contractPeriodEndDate) {
        this.setContractPeriodEndDate(contractPeriodEndDate);
        return this;
    }

    public Employee cardType02(CardType cardType02) {
        this.setCardType02(cardType02);
        return this;
    }

    public Employee cardNumber02(String cardNumber02) {
        this.setCardNumber02(cardNumber02);
        return this;
    }

    public Employee cardType03(CardType cardType03) {
        this.setCardType03(cardType03);
        return this;
    }

    public Employee cardNumber03(String cardNumber03) {
        this.setCardNumber03(cardNumber03);
        return this;
    }

    public Employee allowance01(Double allowance01) {
        this.setAllowance01(allowance01);
        return this;
    }

    public Employee allowance01EffectiveFrom(LocalDate allowance01EffectiveFrom) {
        this.setAllowance01EffectiveFrom(allowance01EffectiveFrom);
        return this;
    }

    public Employee allowance01EffectiveTo(LocalDate allowance01EffectiveTo) {
        this.setAllowance01EffectiveTo(allowance01EffectiveTo);
        return this;
    }

    public Employee allowance02(Double allowance02) {
        this.setAllowance02(allowance02);
        return this;
    }

    public Employee allowance02EffectiveFrom(LocalDate allowance02EffectiveFrom) {
        this.setAllowance02EffectiveFrom(allowance02EffectiveFrom);
        return this;
    }

    public Employee allowance02EffectiveTo(LocalDate allowance02EffectiveTo) {
        this.setAllowance02EffectiveTo(allowance02EffectiveTo);
        return this;
    }

    public Employee allowance03(Double allowance03) {
        this.setAllowance03(allowance03);
        return this;
    }

    public Employee allowance03EffectiveFrom(LocalDate allowance03EffectiveFrom) {
        this.setAllowance03EffectiveFrom(allowance03EffectiveFrom);
        return this;
    }

    public Employee allowance03EffectiveTo(LocalDate allowance03EffectiveTo) {
        this.setAllowance03EffectiveTo(allowance03EffectiveTo);
        return this;
    }

    public Employee allowance04(Double allowance04) {
        this.setAllowance04(allowance04);
        return this;
    }

    public Employee allowance04EffectiveFrom(LocalDate allowance04EffectiveFrom) {
        this.setAllowance04EffectiveFrom(allowance04EffectiveFrom);
        return this;
    }

    public Employee allowance04EffectiveTo(LocalDate allowance04EffectiveTo) {
        this.setAllowance04EffectiveTo(allowance04EffectiveTo);
        return this;
    }

    public Employee allowance05(Double allowance05) {
        this.setAllowance05(allowance05);
        return this;
    }

    public Employee allowance05EffectiveFrom(LocalDate allowance05EffectiveFrom) {
        this.setAllowance05EffectiveFrom(allowance05EffectiveFrom);
        return this;
    }

    public Employee allowance05EffectiveTo(LocalDate allowance05EffectiveTo) {
        this.setAllowance05EffectiveTo(allowance05EffectiveTo);
        return this;
    }

    public Employee allowance06(Double allowance06) {
        this.setAllowance06(allowance06);
        return this;
    }

    public Employee allowance06EffectiveFrom(LocalDate allowance06EffectiveFrom) {
        this.setAllowance06EffectiveFrom(allowance06EffectiveFrom);
        return this;
    }

    public Employee allowance06EffectiveTo(LocalDate allowance06EffectiveTo) {
        this.setAllowance06EffectiveTo(allowance06EffectiveTo);
        return this;
    }

    public Employee isTaxPaidByOrganisation(Boolean isTaxPaidByOrganisation) {
        this.setIsTaxPaidByOrganisation(isTaxPaidByOrganisation);
        return this;
    }

    public Employee createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Employee createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Employee updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Employee updatedAt(LocalDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Employee isAllowedToGiveOnlineAttendance(Boolean isAllowedToGiveOnlineAttendance) {
        this.setIsAllowedToGiveOnlineAttendance(isAllowedToGiveOnlineAttendance);
        return this;
    }

    public Employee noticePeriodInDays(Integer noticePeriodInDays) {
        this.setNoticePeriodInDays(noticePeriodInDays);
        return this;
    }

    public Employee isFixedTermContract(Boolean isFixedTermContract) {
        this.setIsFixedTermContract(isFixedTermContract);
        return this;
    }

    public Employee currentInTime(Instant currentInTime) {
        this.setCurrentInTime(currentInTime);
        return this;
    }

    public Employee currentOutTime(Instant currentOutTime) {
        this.setCurrentOutTime(currentOutTime);
        return this;
    }

    public Employee onlineAttendanceSanctionedAt(Instant onlineAttendanceSanctionedAt) {
        this.setOnlineAttendanceSanctionedAt(onlineAttendanceSanctionedAt);
        return this;
    }

    public Employee isNidVerified(Boolean isNidVerified) {
        this.setIsNidVerified(isNidVerified);
        return this;
    }

    public Employee canRaiseRrfOnBehalf(Boolean canRaiseRrfOnBehalf) {
        this.setCanRaiseRrfOnBehalf(canRaiseRrfOnBehalf);
        return this;
    }

    public Employee taxesCircle(String taxesCircle) {
        this.setTaxesCircle(taxesCircle);
        return this;
    }

    public Employee taxesZone(String taxesZone) {
        this.setTaxesZone(taxesZone);
        return this;
    }

    public Employee canManageTaxAcknowledgementReceipt(Boolean canManageTaxAcknowledgementReceipt) {
        this.setCanManageTaxAcknowledgementReceipt(canManageTaxAcknowledgementReceipt);
        return this;
    }

    public Employee isEligibleForAutomatedAttendance(Boolean isEligibleForAutomatedAttendance) {
        this.setIsEligibleForAutomatedAttendance(isEligibleForAutomatedAttendance);
        return this;
    }

    public Designation getDesignation() {
        return this.designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Employee designation(Designation designation) {
        this.setDesignation(designation);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Employee getReportingTo() {
        return this.reportingTo;
    }

    public void setReportingTo(Employee employee) {
        this.reportingTo = employee;
    }

    public Employee reportingTo(Employee employee) {
        this.setReportingTo(employee);
        return this;
    }

    public Nationality getNationality() {
        return this.nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Employee nationality(Nationality nationality) {
        this.setNationality(nationality);
        return this;
    }

    public BankBranch getBankBranch() {
        return this.bankBranch;
    }

    public void setBankBranch(BankBranch bankBranch) {
        this.bankBranch = bankBranch;
    }

    public Employee bankBranch(BankBranch bankBranch) {
        this.setBankBranch(bankBranch);
        return this;
    }

    public Band getBand() {
        return this.band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public Employee band(Band band) {
        this.setBand(band);
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Employee unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee user(User user) {
        this.setUser(user);
        return this;
    }

    public Boolean getIsCurrentlyResigned() {
        return this.isCurrentlyResigned;
    }

    public Employee isCurrentlyResigned(Boolean isCurrentlyResigned) {
        this.setIsCurrentlyResigned(isCurrentlyResigned);
        return this;
    }

    public void setIsCurrentlyResigned(Boolean isCurrentlyResigned) {
        this.isCurrentlyResigned = isCurrentlyResigned;
    }

    public Boolean getIsBillableResource() {
        return this.isBillableResource;
    }

    public Employee isBillableResource(Boolean isBillableResource) {
        this.setIsBillableResource(isBillableResource);
        return this;
    }

    public void setIsBillableResource(Boolean isBillableResource) {
        this.isBillableResource = isBillableResource;
    }

    public Boolean getIsAugmentedResource() {
        return this.isAugmentedResource;
    }

    public Employee isAugmentedResource(Boolean isAugmentedResource) {
        this.setIsAugmentedResource(isAugmentedResource);
        return this;
    }

    public void setIsAugmentedResource(Boolean isAugmentedResource) {
        this.isAugmentedResource = isAugmentedResource;
    }

    public LocalDate getLastWorkingDay() {
        return this.lastWorkingDay;
    }

    public Employee lastWorkingDay(LocalDate lastWorkingDay) {
        this.setLastWorkingDay(lastWorkingDay);
        return this;
    }

    public void setLastWorkingDay(LocalDate lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }


    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", referenceId='" + getReferenceId() + "'" +
            ", pin='" + getPin() + "'" +
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
            ", isProbationaryPeriodExtended='" + getIsProbationaryPeriodExtended() + "'" +
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
            ", passportNo='" + getPassportNo() + "'" +
            ", passportPlaceOfIssue='" + getPassportPlaceOfIssue() + "'" +
            ", passportIssuedDate='" + getPassportIssuedDate() + "'" +
            ", passportExpiryDate='" + getPassportExpiryDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", welfareFundDeduction=" + getWelfareFundDeduction() +
            ", employmentStatus='" + getEmploymentStatus() + "'" +
            ", hasDisabledChild='" + getHasDisabledChild() + "'" +
            ", isFirstTimeAitGiver='" + getIsFirstTimeAitGiver() + "'" +
            ", isSalaryHold='" + getIsSalaryHold() + "'" +
            ", isFestivalBonusHold='" + getIsFestivalBonusHold() + "'" +
            ", isPhysicallyDisabled='" + getIsPhysicallyDisabled() + "'" +
            ", isFreedomFighter='" + getIsFreedomFighter() + "'" +
            ", hasOverTime='" + getHasOverTime() + "'" +
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
            ", isTaxPaidByOrganisation='" + getIsTaxPaidByOrganisation() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isAllowedToGiveOnlineAttendance='" + getIsAllowedToGiveOnlineAttendance() + "'" +
            ", noticePeriodInDays=" + getNoticePeriodInDays() +
            ", isFixedTermContract='" + getIsFixedTermContract() + "'" +
            ", currentInTime='" + getCurrentInTime() + "'" +
            ", currentOutTime='" + getCurrentOutTime() + "'" +
            ", onlineAttendanceSanctionedAt='" + getOnlineAttendanceSanctionedAt() + "'" +
            ", isNidVerified='" + getIsNidVerified() + "'" +
            ", canRaiseRrfOnBehalf='" + getCanRaiseRrfOnBehalf() + "'" +
            ", taxesCircle='" + getTaxesCircle() + "'" +
            ", taxesZone='" + getTaxesZone() + "'" +
            ", canManageTaxAcknowledgementReceipt='" + getCanManageTaxAcknowledgementReceipt() + "'" +
            ", isEligibleForAutomatedAttendance='" + getIsEligibleForAutomatedAttendance() + "'" +
            ", isFestivalBonusDisabled='" + isIsFestivalBonusDisabled() + "'" +
            ", isCurrentlyResigned='" + getIsCurrentlyResigned() + "'" +
            ", floor='" + getFloor() + "'" +
            ", deskLocation='" + getDeskLocation() + "'" +
            ", isBillableResource='" + getIsBillableResource() + "'" +
            ", isAugmentedResource='" + getIsAugmentedResource() + "'" +
            ", lastWorkingDay='" + getLastWorkingDay() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceId() {
        return this.referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSurName() {
        return this.surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getNationalIdNo() {
        return this.nationalIdNo;
    }

    public void setNationalIdNo(String nationalIdNo) {
        this.nationalIdNo = nationalIdNo;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return this.motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public BloodGroup getBloodGroup() {
        return this.bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPresentAddress() {
        return this.presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPermanentAddress() {
        return this.permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPersonalContactNo() {
        return this.personalContactNo;
    }

    public void setPersonalContactNo(String personalContactNo) {
        this.personalContactNo = personalContactNo;
    }

    public String getPersonalEmail() {
        return this.personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public Religion getReligion() {
        return this.religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public LocalDate getDateOfMarriage() {
        return this.dateOfMarriage;
    }

    public void setDateOfMarriage(LocalDate dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public String getSpouseName() {
        return this.spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getOfficialEmail() {
        return this.officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getOfficialContactNo() {
        return this.officialContactNo;
    }

    public void setOfficialContactNo(String officialContactNo) {
        this.officialContactNo = officialContactNo;
    }

    public String getOfficePhoneExtension() {
        return this.officePhoneExtension;
    }

    public void setOfficePhoneExtension(String officePhoneExtension) {
        this.officePhoneExtension = officePhoneExtension;
    }

    public String getWhatsappId() {
        return this.whatsappId;
    }

    public void setWhatsappId(String whatsappId) {
        this.whatsappId = whatsappId;
    }

    public String getSkypeId() {
        return this.skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public String getEmergencyContactPersonName() {
        return this.emergencyContactPersonName;
    }

    public void setEmergencyContactPersonName(String emergencyContactPersonName) {
        this.emergencyContactPersonName = emergencyContactPersonName;
    }

    public String getEmergencyContactPersonRelationshipWithEmployee() {
        return this.emergencyContactPersonRelationshipWithEmployee;
    }

    public void setEmergencyContactPersonRelationshipWithEmployee(String emergencyContactPersonRelationshipWithEmployee) {
        this.emergencyContactPersonRelationshipWithEmployee = emergencyContactPersonRelationshipWithEmployee;
    }

    public String getEmergencyContactPersonContactNumber() {
        return this.emergencyContactPersonContactNumber;
    }

    public void setEmergencyContactPersonContactNumber(String emergencyContactPersonContactNumber) {
        this.emergencyContactPersonContactNumber = emergencyContactPersonContactNumber;
    }

    public Double getMainGrossSalary() {
        return this.mainGrossSalary;
    }

    public void setMainGrossSalary(Double mainGrossSalary) {
        this.mainGrossSalary = mainGrossSalary;
    }

    public EmployeeCategory getEmployeeCategory() {
        return this.employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDateOfJoining() {
        return this.dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDateOfConfirmation() {
        return this.dateOfConfirmation;
    }

    public void setDateOfConfirmation(LocalDate dateOfConfirmation) {
        this.dateOfConfirmation = dateOfConfirmation;
    }

    public Boolean getIsProbationaryPeriodExtended() {
        return this.isProbationaryPeriodExtended;
    }

    public void setIsProbationaryPeriodExtended(Boolean isProbationaryPeriodExtended) {
        this.isProbationaryPeriodExtended = isProbationaryPeriodExtended;
    }

    public LocalDate getProbationPeriodExtendedTo() {
        return this.probationPeriodExtendedTo;
    }

    public void setProbationPeriodExtendedTo(LocalDate probationPeriodExtendedTo) {
        this.probationPeriodExtendedTo = probationPeriodExtendedTo;
    }

    public PayType getPayType() {
        return this.payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public DisbursementMethod getDisbursementMethod() {
        return this.disbursementMethod;
    }

    public void setDisbursementMethod(DisbursementMethod disbursementMethod) {
        this.disbursementMethod = disbursementMethod;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return this.bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public Long getMobileCelling() {
        return this.mobileCelling;
    }

    public void setMobileCelling(Long mobileCelling) {
        this.mobileCelling = mobileCelling;
    }

    public String getBkashNumber() {
        return this.bkashNumber;
    }

    public void setBkashNumber(String bkashNumber) {
        this.bkashNumber = bkashNumber;
    }

    public CardType getCardType() {
        return this.cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTinNumber() {
        return this.tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getPassportNo() {
        return this.passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportPlaceOfIssue() {
        return this.passportPlaceOfIssue;
    }

    public void setPassportPlaceOfIssue(String passportPlaceOfIssue) {
        this.passportPlaceOfIssue = passportPlaceOfIssue;
    }

    public LocalDate getPassportIssuedDate() {
        return this.passportIssuedDate;
    }

    public void setPassportIssuedDate(LocalDate passportIssuedDate) {
        this.passportIssuedDate = passportIssuedDate;
    }

    public LocalDate getPassportExpiryDate() {
        return this.passportExpiryDate;
    }

    public void setPassportExpiryDate(LocalDate passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getWelfareFundDeduction() {
        return this.welfareFundDeduction;
    }

    public void setWelfareFundDeduction(Double welfareFundDeduction) {
        this.welfareFundDeduction = welfareFundDeduction;
    }

    public EmploymentStatus getEmploymentStatus() {
        return this.employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Boolean getHasDisabledChild() {
        return this.hasDisabledChild;
    }

    public void setHasDisabledChild(Boolean hasDisabledChild) {
        this.hasDisabledChild = hasDisabledChild;
    }

    public Boolean getIsFirstTimeAitGiver() {
        return this.isFirstTimeAitGiver;
    }

    public void setIsFirstTimeAitGiver(Boolean isFirstTimeAitGiver) {
        this.isFirstTimeAitGiver = isFirstTimeAitGiver;
    }

    public Boolean getIsSalaryHold() {
        return this.isSalaryHold;
    }

    public void setIsSalaryHold(Boolean isSalaryHold) {
        this.isSalaryHold = isSalaryHold;
    }

    public Boolean getIsFestivalBonusHold() {
        return this.isFestivalBonusHold;
    }

    public void setIsFestivalBonusHold(Boolean isFestivalBonusHold) {
        this.isFestivalBonusHold = isFestivalBonusHold;
    }

    public Boolean getIsPhysicallyDisabled() {
        return this.isPhysicallyDisabled;
    }

    public void setIsPhysicallyDisabled(Boolean isPhysicallyDisabled) {
        this.isPhysicallyDisabled = isPhysicallyDisabled;
    }

    public Boolean getIsFreedomFighter() {
        return this.isFreedomFighter;
    }

    public void setIsFreedomFighter(Boolean isFreedomFighter) {
        this.isFreedomFighter = isFreedomFighter;
    }

    public Boolean getHasOverTime() {
        return this.hasOverTime;
    }

    public void setHasOverTime(Boolean hasOverTime) {
        this.hasOverTime = hasOverTime;
    }

    public LocalDate getProbationPeriodEndDate() {
        return this.probationPeriodEndDate;
    }

    public void setProbationPeriodEndDate(LocalDate probationPeriodEndDate) {
        this.probationPeriodEndDate = probationPeriodEndDate;
    }

    public LocalDate getContractPeriodExtendedTo() {
        return this.contractPeriodExtendedTo;
    }

    public void setContractPeriodExtendedTo(LocalDate contractPeriodExtendedTo) {
        this.contractPeriodExtendedTo = contractPeriodExtendedTo;
    }

    public LocalDate getContractPeriodEndDate() {
        return this.contractPeriodEndDate;
    }

    public void setContractPeriodEndDate(LocalDate contractPeriodEndDate) {
        this.contractPeriodEndDate = contractPeriodEndDate;
    }

    public CardType getCardType02() {
        return this.cardType02;
    }

    public void setCardType02(CardType cardType02) {
        this.cardType02 = cardType02;
    }

    public String getCardNumber02() {
        return this.cardNumber02;
    }

    public void setCardNumber02(String cardNumber02) {
        this.cardNumber02 = cardNumber02;
    }

    public CardType getCardType03() {
        return this.cardType03;
    }

    public void setCardType03(CardType cardType03) {
        this.cardType03 = cardType03;
    }

    public String getCardNumber03() {
        return this.cardNumber03;
    }

    public void setCardNumber03(String cardNumber03) {
        this.cardNumber03 = cardNumber03;
    }

    public Double getAllowance01() {
        return this.allowance01;
    }

    public void setAllowance01(Double allowance01) {
        this.allowance01 = allowance01;
    }

    public LocalDate getAllowance01EffectiveFrom() {
        return this.allowance01EffectiveFrom;
    }

    public void setAllowance01EffectiveFrom(LocalDate allowance01EffectiveFrom) {
        this.allowance01EffectiveFrom = allowance01EffectiveFrom;
    }

    public LocalDate getAllowance01EffectiveTo() {
        return this.allowance01EffectiveTo;
    }

    public void setAllowance01EffectiveTo(LocalDate allowance01EffectiveTo) {
        this.allowance01EffectiveTo = allowance01EffectiveTo;
    }

    public Double getAllowance02() {
        return this.allowance02;
    }

    public void setAllowance02(Double allowance02) {
        this.allowance02 = allowance02;
    }

    public LocalDate getAllowance02EffectiveFrom() {
        return this.allowance02EffectiveFrom;
    }

    public void setAllowance02EffectiveFrom(LocalDate allowance02EffectiveFrom) {
        this.allowance02EffectiveFrom = allowance02EffectiveFrom;
    }

    public LocalDate getAllowance02EffectiveTo() {
        return this.allowance02EffectiveTo;
    }

    public void setAllowance02EffectiveTo(LocalDate allowance02EffectiveTo) {
        this.allowance02EffectiveTo = allowance02EffectiveTo;
    }

    public Double getAllowance03() {
        return this.allowance03;
    }

    public void setAllowance03(Double allowance03) {
        this.allowance03 = allowance03;
    }

    public LocalDate getAllowance03EffectiveFrom() {
        return this.allowance03EffectiveFrom;
    }

    public void setAllowance03EffectiveFrom(LocalDate allowance03EffectiveFrom) {
        this.allowance03EffectiveFrom = allowance03EffectiveFrom;
    }

    public LocalDate getAllowance03EffectiveTo() {
        return this.allowance03EffectiveTo;
    }

    public void setAllowance03EffectiveTo(LocalDate allowance03EffectiveTo) {
        this.allowance03EffectiveTo = allowance03EffectiveTo;
    }

    public Double getAllowance04() {
        return this.allowance04;
    }

    public void setAllowance04(Double allowance04) {
        this.allowance04 = allowance04;
    }

    public LocalDate getAllowance04EffectiveFrom() {
        return this.allowance04EffectiveFrom;
    }

    public void setAllowance04EffectiveFrom(LocalDate allowance04EffectiveFrom) {
        this.allowance04EffectiveFrom = allowance04EffectiveFrom;
    }

    public LocalDate getAllowance04EffectiveTo() {
        return this.allowance04EffectiveTo;
    }

    public void setAllowance04EffectiveTo(LocalDate allowance04EffectiveTo) {
        this.allowance04EffectiveTo = allowance04EffectiveTo;
    }

    public Double getAllowance05() {
        return this.allowance05;
    }

    public void setAllowance05(Double allowance05) {
        this.allowance05 = allowance05;
    }

    public LocalDate getAllowance05EffectiveFrom() {
        return this.allowance05EffectiveFrom;
    }

    public void setAllowance05EffectiveFrom(LocalDate allowance05EffectiveFrom) {
        this.allowance05EffectiveFrom = allowance05EffectiveFrom;
    }

    public LocalDate getAllowance05EffectiveTo() {
        return this.allowance05EffectiveTo;
    }

    public void setAllowance05EffectiveTo(LocalDate allowance05EffectiveTo) {
        this.allowance05EffectiveTo = allowance05EffectiveTo;
    }

    public Double getAllowance06() {
        return this.allowance06;
    }

    public void setAllowance06(Double allowance06) {
        this.allowance06 = allowance06;
    }

    public LocalDate getAllowance06EffectiveFrom() {
        return this.allowance06EffectiveFrom;
    }

    public void setAllowance06EffectiveFrom(LocalDate allowance06EffectiveFrom) {
        this.allowance06EffectiveFrom = allowance06EffectiveFrom;
    }

    public LocalDate getAllowance06EffectiveTo() {
        return this.allowance06EffectiveTo;
    }

    public void setAllowance06EffectiveTo(LocalDate allowance06EffectiveTo) {
        this.allowance06EffectiveTo = allowance06EffectiveTo;
    }

    public Boolean getIsTaxPaidByOrganisation() {
        return this.isTaxPaidByOrganisation;
    }

    public void setIsTaxPaidByOrganisation(Boolean isTaxPaidByOrganisation) {
        this.isTaxPaidByOrganisation = isTaxPaidByOrganisation;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsAllowedToGiveOnlineAttendance() {
        return this.isAllowedToGiveOnlineAttendance;
    }

    public void setIsAllowedToGiveOnlineAttendance(Boolean isAllowedToGiveOnlineAttendance) {
        this.isAllowedToGiveOnlineAttendance = isAllowedToGiveOnlineAttendance;
    }

    public Integer getNoticePeriodInDays() {
        return this.noticePeriodInDays;
    }

    public void setNoticePeriodInDays(Integer noticePeriodInDays) {
        this.noticePeriodInDays = noticePeriodInDays;
    }

    public Boolean getIsFixedTermContract() {
        return this.isFixedTermContract;
    }

    public void setIsFixedTermContract(Boolean isFixedTermContract) {
        this.isFixedTermContract = isFixedTermContract;
    }

    public Instant getCurrentInTime() {
        return this.currentInTime;
    }

    public void setCurrentInTime(Instant currentInTime) {
        this.currentInTime = currentInTime;
    }

    public Instant getCurrentOutTime() {
        return this.currentOutTime;
    }

    public void setCurrentOutTime(Instant currentOutTime) {
        this.currentOutTime = currentOutTime;
    }

    public Instant getOnlineAttendanceSanctionedAt() {
        return this.onlineAttendanceSanctionedAt;
    }

    public void setOnlineAttendanceSanctionedAt(Instant onlineAttendanceSanctionedAt) {
        this.onlineAttendanceSanctionedAt = onlineAttendanceSanctionedAt;
    }

    public Boolean getIsNidVerified() {
        return this.isNidVerified;
    }

    public void setIsNidVerified(Boolean isNidVerified) {
        this.isNidVerified = isNidVerified;
    }

    public Boolean getCanRaiseRrfOnBehalf() {
        return this.canRaiseRrfOnBehalf;
    }

    public void setCanRaiseRrfOnBehalf(Boolean canRaiseRrfOnBehalf) {
        this.canRaiseRrfOnBehalf = canRaiseRrfOnBehalf;
    }

    public String getTaxesCircle() {
        return this.taxesCircle;
    }

    public void setTaxesCircle(String taxesCircle) {
        this.taxesCircle = taxesCircle;
    }

    public String getTaxesZone() {
        return this.taxesZone;
    }

    public void setTaxesZone(String taxesZone) {
        this.taxesZone = taxesZone;
    }

    public Boolean getCanManageTaxAcknowledgementReceipt() {
        return this.canManageTaxAcknowledgementReceipt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCanManageTaxAcknowledgementReceipt(Boolean canManageTaxAcknowledgementReceipt) {
        this.canManageTaxAcknowledgementReceipt = canManageTaxAcknowledgementReceipt;
    }

    public Boolean getIsEligibleForAutomatedAttendance() {
        return this.isEligibleForAutomatedAttendance;
    }

    public void setIsEligibleForAutomatedAttendance(Boolean isEligibleForAutomatedAttendance) {
        this.isEligibleForAutomatedAttendance = isEligibleForAutomatedAttendance;
    }

    public Boolean isIsFestivalBonusDisabled() {
        return isFestivalBonusDisabled;
    }

    public Employee isFestivalBonusDisabled(Boolean isFestivalBonusDisabled) {
        this.isFestivalBonusDisabled = isFestivalBonusDisabled;
        return this;
    }

    public void setIsFestivalBonusDisabled(Boolean isFestivalBonusDisabled) {
        this.isFestivalBonusDisabled = isFestivalBonusDisabled;
    }

    public Location getOfficeLocation() {
        return officeLocation;
    }

    public Employee officeLocation(Location location) {
        this.officeLocation = location;
        return this;
    }

    public void setOfficeLocation(Location location) {
        this.officeLocation = location;
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
}
