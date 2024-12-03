package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.IdentityType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PfNominee.
 */
@Entity
@Table(name = "pf_nominee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PfNominee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nomination_date")
    private LocalDate nominationDate;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "present_address")
    private String presentAddress;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "relationship")
    private String relationship;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @Column(name = "share_percentage")
    private Double sharePercentage;

    @Column(name = "nid_number")
    private String nidNumber;

    @Column(name = "is_nid_verified")
    private Boolean isNidVerified;

    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "brn_number")
    private String brnNumber;

    @Column(name = "photo")
    private String photo;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "guardian_father_or_spouse_name")
    private String guardianFatherOrSpouseName;

    @Column(name = "guardian_date_of_birth")
    private LocalDate guardianDateOfBirth;

    @Column(name = "guardian_present_address")
    private String guardianPresentAddress;

    @Column(name = "guardian_permanent_address")
    private String guardianPermanentAddress;

    @Column(name = "guardian_proof_of_identity_of_legal_guardian")
    private String guardianProofOfIdentityOfLegalGuardian;

    @Column(name = "guardian_relationship_with_nominee")
    private String guardianRelationshipWithNominee;

    @Column(name = "guardian_nid_number")
    private String guardianNidNumber;

    @Column(name = "guardian_brn_number")
    private String guardianBrnNumber;

    @Column(name = "guardian_id_number")
    private String guardianIdNumber;

    @Column(name = "is_guardian_nid_verified")
    private Boolean isGuardianNidVerified;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "identity_type", nullable = false)
    private IdentityType identityType;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "id_number", length = 50, nullable = false)
    private String idNumber;

    @Size(min = 0, max = 200)
    @Column(name = "document_name", length = 200)
    private String documentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "guardian_identity_type")
    private IdentityType guardianIdentityType;

    @Size(min = 0, max = 200)
    @Column(name = "guardian_document_name", length = 200)
    private String guardianDocumentName;

    @ManyToOne
    private PfAccount pfAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee pfWitness;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee approvedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PfNominee id(Long id) {
        this.setId(id);
        return this;
    }

    public PfNominee nominationDate(LocalDate nominationDate) {
        this.setNominationDate(nominationDate);
        return this;
    }

    public PfNominee fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public PfNominee presentAddress(String presentAddress) {
        this.setPresentAddress(presentAddress);
        return this;
    }

    public PfNominee permanentAddress(String permanentAddress) {
        this.setPermanentAddress(permanentAddress);
        return this;
    }

    public PfNominee relationship(String relationship) {
        this.setRelationship(relationship);
        return this;
    }

    public PfNominee dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public PfNominee age(Integer age) {
        this.setAge(age);
        return this;
    }

    public PfNominee sharePercentage(Double sharePercentage) {
        this.setSharePercentage(sharePercentage);
        return this;
    }

    public PfNominee nidNumber(String nidNumber) {
        this.setNidNumber(nidNumber);
        return this;
    }

    public PfNominee isNidVerified(Boolean isNidVerified) {
        this.setIsNidVerified(isNidVerified);
        return this;
    }

    public PfNominee passportNumber(String passportNumber) {
        this.setPassportNumber(passportNumber);
        return this;
    }

    public PfNominee brnNumber(String brnNumber) {
        this.setBrnNumber(brnNumber);
        return this;
    }

    public PfNominee photo(String photo) {
        this.setPhoto(photo);
        return this;
    }

    public PfNominee guardianName(String guardianName) {
        this.setGuardianName(guardianName);
        return this;
    }

    public PfNominee guardianFatherOrSpouseName(String guardianFatherOrSpouseName) {
        this.setGuardianFatherOrSpouseName(guardianFatherOrSpouseName);
        return this;
    }

    public PfNominee guardianDateOfBirth(LocalDate guardianDateOfBirth) {
        this.setGuardianDateOfBirth(guardianDateOfBirth);
        return this;
    }

    public PfNominee guardianPresentAddress(String guardianPresentAddress) {
        this.setGuardianPresentAddress(guardianPresentAddress);
        return this;
    }

    public PfNominee guardianPermanentAddress(String guardianPermanentAddress) {
        this.setGuardianPermanentAddress(guardianPermanentAddress);
        return this;
    }

    public PfNominee guardianProofOfIdentityOfLegalGuardian(String guardianProofOfIdentityOfLegalGuardian) {
        this.setGuardianProofOfIdentityOfLegalGuardian(guardianProofOfIdentityOfLegalGuardian);
        return this;
    }

    public PfNominee guardianRelationshipWithNominee(String guardianRelationshipWithNominee) {
        this.setGuardianRelationshipWithNominee(guardianRelationshipWithNominee);
        return this;
    }

    public PfNominee guardianNidNumber(String guardianNidNumber) {
        this.setGuardianNidNumber(guardianNidNumber);
        return this;
    }

    public PfNominee guardianBrnNumber(String guardianBrnNumber) {
        this.setGuardianBrnNumber(guardianBrnNumber);
        return this;
    }

    public PfNominee guardianIdNumber(String guardianIdNumber) {
        this.setGuardianIdNumber(guardianIdNumber);
        return this;
    }

    public PfNominee isGuardianNidVerified(Boolean isGuardianNidVerified) {
        this.setIsGuardianNidVerified(isGuardianNidVerified);
        return this;
    }

    public PfNominee isApproved(Boolean isApproved) {
        this.setIsApproved(isApproved);
        return this;
    }

    public PfNominee identityType(IdentityType identityType) {
        this.setIdentityType(identityType);
        return this;
    }

    public PfNominee idNumber(String idNumber) {
        this.setIdNumber(idNumber);
        return this;
    }

    public PfNominee documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public PfNominee guardianIdentityType(IdentityType guardianIdentityType) {
        this.setGuardianIdentityType(guardianIdentityType);
        return this;
    }

    public PfNominee guardianDocumentName(String guardianDocumentName) {
        this.setGuardianDocumentName(guardianDocumentName);
        return this;
    }

    public PfAccount getPfAccount() {
        return this.pfAccount;
    }

    public void setPfAccount(PfAccount pfAccount) {
        this.pfAccount = pfAccount;
    }

    public PfNominee pfAccount(PfAccount pfAccount) {
        this.setPfAccount(pfAccount);
        return this;
    }

    public Employee getPfWitness() {
        return this.pfWitness;
    }

    public void setPfWitness(Employee employee) {
        this.pfWitness = employee;
    }

    public PfNominee pfWitness(Employee employee) {
        this.setPfWitness(employee);
        return this;
    }

    public Employee getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(Employee employee) {
        this.approvedBy = employee;
    }

    public PfNominee approvedBy(Employee employee) {
        this.setApprovedBy(employee);
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
        if (!(o instanceof PfNominee)) {
            return false;
        }
        return id != null && id.equals(((PfNominee) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfNominee{" +
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
            ", isNidVerified='" + getIsNidVerified() + "'" +
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
            ", isGuardianNidVerified='" + getIsGuardianNidVerified() + "'" +
            ", isApproved='" + getIsApproved() + "'" +
            ", identityType='" + getIdentityType() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", documentName='" + getDocumentName() + "'" +
            ", guardianIdentityType='" + getGuardianIdentityType() + "'" +
            ", guardianDocumentName='" + getGuardianDocumentName() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getNominationDate() {
        return this.nominationDate;
    }

    public void setNominationDate(LocalDate nominationDate) {
        this.nominationDate = nominationDate;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getRelationship() {
        return this.relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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

    public String getNidNumber() {
        return this.nidNumber;
    }

    public void setNidNumber(String nidNumber) {
        this.nidNumber = nidNumber;
    }

    public Boolean getIsNidVerified() {
        return this.isNidVerified;
    }

    public void setIsNidVerified(Boolean isNidVerified) {
        this.isNidVerified = isNidVerified;
    }

    public String getPassportNumber() {
        return this.passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getBrnNumber() {
        return this.brnNumber;
    }

    public void setBrnNumber(String brnNumber) {
        this.brnNumber = brnNumber;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGuardianName() {
        return this.guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianFatherOrSpouseName() {
        return this.guardianFatherOrSpouseName;
    }

    public void setGuardianFatherOrSpouseName(String guardianFatherOrSpouseName) {
        this.guardianFatherOrSpouseName = guardianFatherOrSpouseName;
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

    public String getGuardianPermanentAddress() {
        return this.guardianPermanentAddress;
    }

    public void setGuardianPermanentAddress(String guardianPermanentAddress) {
        this.guardianPermanentAddress = guardianPermanentAddress;
    }

    public String getGuardianProofOfIdentityOfLegalGuardian() {
        return this.guardianProofOfIdentityOfLegalGuardian;
    }

    public void setGuardianProofOfIdentityOfLegalGuardian(String guardianProofOfIdentityOfLegalGuardian) {
        this.guardianProofOfIdentityOfLegalGuardian = guardianProofOfIdentityOfLegalGuardian;
    }

    public String getGuardianRelationshipWithNominee() {
        return this.guardianRelationshipWithNominee;
    }

    public void setGuardianRelationshipWithNominee(String guardianRelationshipWithNominee) {
        this.guardianRelationshipWithNominee = guardianRelationshipWithNominee;
    }

    public String getGuardianNidNumber() {
        return this.guardianNidNumber;
    }

    public void setGuardianNidNumber(String guardianNidNumber) {
        this.guardianNidNumber = guardianNidNumber;
    }

    public String getGuardianBrnNumber() {
        return this.guardianBrnNumber;
    }

    public void setGuardianBrnNumber(String guardianBrnNumber) {
        this.guardianBrnNumber = guardianBrnNumber;
    }

    public String getGuardianIdNumber() {
        return this.guardianIdNumber;
    }

    public void setGuardianIdNumber(String guardianIdNumber) {
        this.guardianIdNumber = guardianIdNumber;
    }

    public Boolean getIsGuardianNidVerified() {
        return this.isGuardianNidVerified;
    }

    public void setIsGuardianNidVerified(Boolean isGuardianNidVerified) {
        this.isGuardianNidVerified = isGuardianNidVerified;
    }

    public Boolean getIsApproved() {
        return this.isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public IdentityType getIdentityType() {
        return this.identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public IdentityType getGuardianIdentityType() {
        return this.guardianIdentityType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setGuardianIdentityType(IdentityType guardianIdentityType) {
        this.guardianIdentityType = guardianIdentityType;
    }

    public String getGuardianDocumentName() {
        return this.guardianDocumentName;
    }

    public void setGuardianDocumentName(String guardianDocumentName) {
        this.guardianDocumentName = guardianDocumentName;
    }
}
