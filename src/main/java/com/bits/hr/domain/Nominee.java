package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.IdentityType;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Nominee.
 */
@Entity
@Table(name = "nominee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Nominee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 0, max = 255)
    @Column(name = "nominee_name", length = 255, nullable = false)
    private String nomineeName;

    @NotNull
    @Size(min = 0, max = 255)
    @Column(name = "present_address", length = 255, nullable = false)
    private String presentAddress;

    @Column(name = "relationship_with_employee")
    private String relationshipWithEmployee;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "100")
    @Column(name = "share_percentage", nullable = false)
    private Double sharePercentage;

    @Column(name = "image_path")
    private String imagePath;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Size(min = 0, max = 255)
    @Column(name = "guardian_name", length = 255)
    private String guardianName;

    @Column(name = "guardian_father_name")
    private String guardianFatherName;

    @Column(name = "guardian_spouse_name")
    private String guardianSpouseName;

    @Column(name = "guardian_date_of_birth")
    private LocalDate guardianDateOfBirth;

    @Size(min = 0, max = 255)
    @Column(name = "guardian_present_address", length = 255)
    private String guardianPresentAddress;

    @Column(name = "guardian_document_name")
    private String guardianDocumentName;

    @Column(name = "guardian_relationship_with")
    private String guardianRelationshipWith;

    @Column(name = "guardian_image_path")
    private String guardianImagePath;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "nomination_date")
    private LocalDate nominationDate;

    @NotNull
    @Size(min = 0, max = 255)
    @Column(name = "permanent_address", length = 255, nullable = false)
    private String permanentAddress;

    @Size(min = 0, max = 255)
    @Column(name = "guardian_permanent_address", length = 255)
    private String guardianPermanentAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "nominee_type")
    private NomineeType nomineeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "identity_type")
    private IdentityType identityType;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "is_nid_verified")
    private Boolean isNidVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "guardian_identity_type")
    private IdentityType guardianIdentityType;

    @Column(name = "guardian_id_number")
    private String guardianIdNumber;

    @Column(name = "is_guardian_nid_verified")
    private Boolean isGuardianNidVerified;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee approvedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee witness;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee member;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Nominee id(Long id) {
        this.setId(id);
        return this;
    }

    public Nominee nomineeName(String nomineeName) {
        this.setNomineeName(nomineeName);
        return this;
    }

    public Nominee presentAddress(String presentAddress) {
        this.setPresentAddress(presentAddress);
        return this;
    }

    public Nominee relationshipWithEmployee(String relationshipWithEmployee) {
        this.setRelationshipWithEmployee(relationshipWithEmployee);
        return this;
    }

    public Nominee dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public Nominee age(Integer age) {
        this.setAge(age);
        return this;
    }

    public Nominee sharePercentage(Double sharePercentage) {
        this.setSharePercentage(sharePercentage);
        return this;
    }

    public Nominee imagePath(String imagePath) {
        this.setImagePath(imagePath);
        return this;
    }

    public Nominee status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Nominee guardianName(String guardianName) {
        this.setGuardianName(guardianName);
        return this;
    }

    public Nominee guardianFatherName(String guardianFatherName) {
        this.setGuardianFatherName(guardianFatherName);
        return this;
    }

    public Nominee guardianSpouseName(String guardianSpouseName) {
        this.setGuardianSpouseName(guardianSpouseName);
        return this;
    }

    public Nominee guardianDateOfBirth(LocalDate guardianDateOfBirth) {
        this.setGuardianDateOfBirth(guardianDateOfBirth);
        return this;
    }

    public Nominee guardianPresentAddress(String guardianPresentAddress) {
        this.setGuardianPresentAddress(guardianPresentAddress);
        return this;
    }

    public Nominee guardianDocumentName(String guardianDocumentName) {
        this.setGuardianDocumentName(guardianDocumentName);
        return this;
    }

    public Nominee guardianRelationshipWith(String guardianRelationshipWith) {
        this.setGuardianRelationshipWith(guardianRelationshipWith);
        return this;
    }

    public Nominee guardianImagePath(String guardianImagePath) {
        this.setGuardianImagePath(guardianImagePath);
        return this;
    }

    public Nominee isLocked(Boolean isLocked) {
        this.setIsLocked(isLocked);
        return this;
    }

    public Nominee nominationDate(LocalDate nominationDate) {
        this.setNominationDate(nominationDate);
        return this;
    }

    public Nominee permanentAddress(String permanentAddress) {
        this.setPermanentAddress(permanentAddress);
        return this;
    }

    public Nominee guardianPermanentAddress(String guardianPermanentAddress) {
        this.setGuardianPermanentAddress(guardianPermanentAddress);
        return this;
    }

    public Nominee nomineeType(NomineeType nomineeType) {
        this.setNomineeType(nomineeType);
        return this;
    }

    public Nominee identityType(IdentityType identityType) {
        this.setIdentityType(identityType);
        return this;
    }

    public Nominee documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public Nominee idNumber(String idNumber) {
        this.setIdNumber(idNumber);
        return this;
    }

    public Nominee isNidVerified(Boolean isNidVerified) {
        this.setIsNidVerified(isNidVerified);
        return this;
    }

    public Nominee guardianIdentityType(IdentityType guardianIdentityType) {
        this.setGuardianIdentityType(guardianIdentityType);
        return this;
    }

    public Nominee guardianIdNumber(String guardianIdNumber) {
        this.setGuardianIdNumber(guardianIdNumber);
        return this;
    }

    public Nominee isGuardianNidVerified(Boolean isGuardianNidVerified) {
        this.setIsGuardianNidVerified(isGuardianNidVerified);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Nominee employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Employee getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(Employee employee) {
        this.approvedBy = employee;
    }

    public Nominee approvedBy(Employee employee) {
        this.setApprovedBy(employee);
        return this;
    }

    public Employee getWitness() {
        return this.witness;
    }

    public void setWitness(Employee employee) {
        this.witness = employee;
    }

    public Nominee witness(Employee employee) {
        this.setWitness(employee);
        return this;
    }

    public Employee getMember() {
        return this.member;
    }

    public void setMember(Employee employee) {
        this.member = employee;
    }

    public Nominee member(Employee employee) {
        this.setMember(employee);
        return this;
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
        if (!(o instanceof Nominee)) {
            return false;
        }
        return id != null && id.equals(((Nominee) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nominee{" +
            "id=" + getId() +
            ", nomineeName='" + getNomineeName() + "'" +
            ", presentAddress='" + getPresentAddress() + "'" +
            ", relationshipWithEmployee='" + getRelationshipWithEmployee() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", age=" + getAge() +
            ", sharePercentage=" + getSharePercentage() +
            ", imagePath='" + getImagePath() + "'" +
            ", status='" + getStatus() + "'" +
            ", guardianName='" + getGuardianName() + "'" +
            ", guardianFatherName='" + getGuardianFatherName() + "'" +
            ", guardianSpouseName='" + getGuardianSpouseName() + "'" +
            ", guardianDateOfBirth='" + getGuardianDateOfBirth() + "'" +
            ", guardianPresentAddress='" + getGuardianPresentAddress() + "'" +
            ", guardianDocumentName='" + getGuardianDocumentName() + "'" +
            ", guardianRelationshipWith='" + getGuardianRelationshipWith() + "'" +
            ", guardianImagePath='" + getGuardianImagePath() + "'" +
            ", isLocked='" + getIsLocked() + "'" +
            ", nominationDate='" + getNominationDate() + "'" +
            ", permanentAddress='" + getPermanentAddress() + "'" +
            ", guardianPermanentAddress='" + getGuardianPermanentAddress() + "'" +
            ", nomineeType='" + getNomineeType() + "'" +
            ", identityType='" + getIdentityType() + "'" +
            ", documentName='" + getDocumentName() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", isNidVerified='" + getIsNidVerified() + "'" +
            ", guardianIdentityType='" + getGuardianIdentityType() + "'" +
            ", guardianIdNumber='" + getGuardianIdNumber() + "'" +
            ", isGuardianNidVerified='" + getIsGuardianNidVerified() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomineeName() {
        return this.nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getPresentAddress() {
        return this.presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getRelationshipWithEmployee() {
        return this.relationshipWithEmployee;
    }

    public void setRelationshipWithEmployee(String relationshipWithEmployee) {
        this.relationshipWithEmployee = relationshipWithEmployee;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getSharePercentage() {
        return this.sharePercentage;
    }

    public void setSharePercentage(Double sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getGuardianName() {
        return this.guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianFatherName() {
        return this.guardianFatherName;
    }

    public void setGuardianFatherName(String guardianFatherName) {
        this.guardianFatherName = guardianFatherName;
    }

    public String getGuardianSpouseName() {
        return this.guardianSpouseName;
    }

    public void setGuardianSpouseName(String guardianSpouseName) {
        this.guardianSpouseName = guardianSpouseName;
    }

    public LocalDate getGuardianDateOfBirth() {
        return this.guardianDateOfBirth;
    }

    public void setGuardianDateOfBirth(LocalDate guardianDateOfBirth) {
        this.guardianDateOfBirth = guardianDateOfBirth;
    }

    public String getGuardianPresentAddress() {
        return this.guardianPresentAddress;
    }

    public void setGuardianPresentAddress(String guardianPresentAddress) {
        this.guardianPresentAddress = guardianPresentAddress;
    }

    public String getGuardianDocumentName() {
        return this.guardianDocumentName;
    }

    public void setGuardianDocumentName(String guardianDocumentName) {
        this.guardianDocumentName = guardianDocumentName;
    }

    public String getGuardianRelationshipWith() {
        return this.guardianRelationshipWith;
    }

    public void setGuardianRelationshipWith(String guardianRelationshipWith) {
        this.guardianRelationshipWith = guardianRelationshipWith;
    }

    public String getGuardianImagePath() {
        return this.guardianImagePath;
    }

    public void setGuardianImagePath(String guardianImagePath) {
        this.guardianImagePath = guardianImagePath;
    }

    public Boolean getIsLocked() {
        return this.isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public LocalDate getNominationDate() {
        return this.nominationDate;
    }

    public void setNominationDate(LocalDate nominationDate) {
        this.nominationDate = nominationDate;
    }

    public String getPermanentAddress() {
        return this.permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getGuardianPermanentAddress() {
        return this.guardianPermanentAddress;
    }

    public void setGuardianPermanentAddress(String guardianPermanentAddress) {
        this.guardianPermanentAddress = guardianPermanentAddress;
    }

    public NomineeType getNomineeType() {
        return this.nomineeType;
    }

    public void setNomineeType(NomineeType nomineeType) {
        this.nomineeType = nomineeType;
    }

    public IdentityType getIdentityType() {
        return this.identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Boolean getIsNidVerified() {
        return this.isNidVerified;
    }

    public void setIsNidVerified(Boolean isNidVerified) {
        this.isNidVerified = isNidVerified;
    }

    public IdentityType getGuardianIdentityType() {
        return this.guardianIdentityType;
    }

    public void setGuardianIdentityType(IdentityType guardianIdentityType) {
        this.guardianIdentityType = guardianIdentityType;
    }

    public String getGuardianIdNumber() {
        return this.guardianIdNumber;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setGuardianIdNumber(String guardianIdNumber) {
        this.guardianIdNumber = guardianIdNumber;
    }

    public Boolean getIsGuardianNidVerified() {
        return this.isGuardianNidVerified;
    }

    public void setIsGuardianNidVerified(Boolean isGuardianNidVerified) {
        this.isGuardianNidVerified = isGuardianNidVerified;
    }
}
