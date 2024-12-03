package com.bits.hr.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "short_name", length = 255, nullable = false)
    private String shortName;

    @NotNull
    @Size(max = 255)
    @Column(name = "full_name", length = 255, nullable = false)
    private String fullName;

    @Size(max = 255)
    @Column(name = "slogan", length = 255)
    private String slogan;

    @NotNull
    @Size(max = 255)
    @Column(name = "domain_name", length = 255, nullable = false)
    private String domainName;

    @NotNull
    @Size(max = 255)
    @Column(name = "email_address", length = 255, nullable = false)
    private String emailAddress;

    @Size(max = 255)
    @Column(name = "hr_email_address", length = 255)
    private String hrEmailAddress;

    @Size(max = 255)
    @Column(name = "no_reply_email_address", length = 255)
    private String noReplyEmailAddress;

    @Size(max = 255)
    @Column(name = "contact_number", length = 255)
    private String contactNumber;

    @Size(max = 255)
    @Column(name = "finance_manager_pin", length = 255)
    private String financeManagerPIN;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "finance_manager_signature")
    private String financeManagerSignature;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "logo")
    private String logo;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "document_letter_head")
    private String documentLetterHead;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "pf_statement_letter_head")
    private String pfStatementLetterHead;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "tax_statement_letter_head")
    private String taxStatementLetterHead;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "nominee_letter_head")
    private String nomineeLetterHead;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "salary_payslip_letter_head")
    private String salaryPayslipLetterHead;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "festival_bonus_payslip_letter_head")
    private String festivalBonusPayslipLetterHead;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "recruitment_requisition_letter_head")
    private String recruitmentRequisitionLetterHead;

    @Column(name = "has_organization_stamp")
    private Boolean hasOrganizationStamp;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "organization_stamp")
    private String organizationStamp;

    @Size(max = 255)
    @Column(name = "linkedin", length = 255)
    private String linkedin;

    @Size(max = 255)
    @Column(name = "twitter", length = 255)
    private String twitter;

    @Size(max = 255)
    @Column(name = "facebook", length = 255)
    private String facebook;

    @Size(max = 255)
    @Column(name = "youtube", length = 255)
    private String youtube;

    @Size(max = 255)
    @Column(name = "instagram", length = 255)
    private String instagram;

    @Size(max = 255)
    @Column(name = "whatsapp", length = 255)
    private String whatsapp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Organization id(Long id) {
        this.setId(id);
        return this;
    }

    public Organization shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public Organization fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public Organization slogan(String slogan) {
        this.setSlogan(slogan);
        return this;
    }

    public Organization domainName(String domainName) {
        this.setDomainName(domainName);
        return this;
    }

    public Organization emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public Organization hrEmailAddress(String hrEmailAddress) {
        this.setHrEmailAddress(hrEmailAddress);
        return this;
    }

    public Organization noReplyEmailAddress(String noReplyEmailAddress) {
        this.setNoReplyEmailAddress(noReplyEmailAddress);
        return this;
    }

    public Organization contactNumber(String contactNumber) {
        this.setContactNumber(contactNumber);
        return this;
    }

    public Organization financeManagerPIN(String financeManagerPIN) {
        this.setFinanceManagerPIN(financeManagerPIN);
        return this;
    }

    public Organization financeManagerSignature(String financeManagerSignature) {
        this.setFinanceManagerSignature(financeManagerSignature);
        return this;
    }

    public Organization logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public Organization documentLetterHead(String documentLetterHead) {
        this.setDocumentLetterHead(documentLetterHead);
        return this;
    }

    public Organization pfStatementLetterHead(String pfStatementLetterHead) {
        this.setPfStatementLetterHead(pfStatementLetterHead);
        return this;
    }

    public Organization taxStatementLetterHead(String taxStatementLetterHead) {
        this.setTaxStatementLetterHead(taxStatementLetterHead);
        return this;
    }

    public Organization nomineeLetterHead(String nomineeLetterHead) {
        this.setNomineeLetterHead(nomineeLetterHead);
        return this;
    }

    public Organization salaryPayslipLetterHead(String salaryPayslipLetterHead) {
        this.setSalaryPayslipLetterHead(salaryPayslipLetterHead);
        return this;
    }

    public Organization festivalBonusPayslipLetterHead(String festivalBonusPayslipLetterHead) {
        this.setFestivalBonusPayslipLetterHead(festivalBonusPayslipLetterHead);
        return this;
    }

    public Organization recruitmentRequisitionLetterHead(String recruitmentRequisitionLetterHead) {
        this.setRecruitmentRequisitionLetterHead(recruitmentRequisitionLetterHead);
        return this;
    }

    public Organization hasOrganizationStamp(Boolean hasOrganizationStamp) {
        this.setHasOrganizationStamp(hasOrganizationStamp);
        return this;
    }

    public Organization organizationStamp(String organizationStamp) {
        this.setOrganizationStamp(organizationStamp);
        return this;
    }

    public Organization linkedin(String linkedin) {
        this.setLinkedin(linkedin);
        return this;
    }

    public Organization twitter(String twitter) {
        this.setTwitter(twitter);
        return this;
    }

    public Organization facebook(String facebook) {
        this.setFacebook(facebook);
        return this;
    }

    public Organization youtube(String youtube) {
        this.setYoutube(youtube);
        return this;
    }

    public Organization instagram(String instagram) {
        this.setInstagram(instagram);
        return this;
    }

    public Organization whatsapp(String whatsapp) {
        this.setWhatsapp(whatsapp);
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
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", shortName='" + getShortName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", slogan='" + getSlogan() + "'" +
            ", domainName='" + getDomainName() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", hrEmailAddress='" + getHrEmailAddress() + "'" +
            ", noReplyEmailAddress='" + getNoReplyEmailAddress() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", financeManagerPIN='" + getFinanceManagerPIN() + "'" +
            ", financeManagerSignature='" + getFinanceManagerSignature() + "'" +
            ", logo='" + getLogo() + "'" +
            ", documentLetterHead='" + getDocumentLetterHead() + "'" +
            ", pfStatementLetterHead='" + getPfStatementLetterHead() + "'" +
            ", taxStatementLetterHead='" + getTaxStatementLetterHead() + "'" +
            ", nomineeLetterHead='" + getNomineeLetterHead() + "'" +
            ", salaryPayslipLetterHead='" + getSalaryPayslipLetterHead() + "'" +
            ", festivalBonusPayslipLetterHead='" + getFestivalBonusPayslipLetterHead() + "'" +
            ", recruitmentRequisitionLetterHead='" + getRecruitmentRequisitionLetterHead() + "'" +
            ", hasOrganizationStamp='" + getHasOrganizationStamp() + "'" +
            ", organizationStamp='" + getOrganizationStamp() + "'" +
            ", linkedin='" + getLinkedin() + "'" +
            ", twitter='" + getTwitter() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", youtube='" + getYoutube() + "'" +
            ", instagram='" + getInstagram() + "'" +
            ", whatsapp='" + getWhatsapp() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSlogan() {
        return this.slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getHrEmailAddress() {
        return this.hrEmailAddress;
    }

    public void setHrEmailAddress(String hrEmailAddress) {
        this.hrEmailAddress = hrEmailAddress;
    }

    public String getNoReplyEmailAddress() {
        return this.noReplyEmailAddress;
    }

    public void setNoReplyEmailAddress(String noReplyEmailAddress) {
        this.noReplyEmailAddress = noReplyEmailAddress;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFinanceManagerPIN() {
        return this.financeManagerPIN;
    }

    public void setFinanceManagerPIN(String financeManagerPIN) {
        this.financeManagerPIN = financeManagerPIN;
    }

    public String getFinanceManagerSignature() {
        return this.financeManagerSignature;
    }

    public void setFinanceManagerSignature(String financeManagerSignature) {
        this.financeManagerSignature = financeManagerSignature;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDocumentLetterHead() {
        return this.documentLetterHead;
    }

    public void setDocumentLetterHead(String documentLetterHead) {
        this.documentLetterHead = documentLetterHead;
    }

    public String getPfStatementLetterHead() {
        return this.pfStatementLetterHead;
    }

    public void setPfStatementLetterHead(String pfStatementLetterHead) {
        this.pfStatementLetterHead = pfStatementLetterHead;
    }

    public String getTaxStatementLetterHead() {
        return this.taxStatementLetterHead;
    }

    public void setTaxStatementLetterHead(String taxStatementLetterHead) {
        this.taxStatementLetterHead = taxStatementLetterHead;
    }

    public String getNomineeLetterHead() {
        return this.nomineeLetterHead;
    }

    public void setNomineeLetterHead(String nomineeLetterHead) {
        this.nomineeLetterHead = nomineeLetterHead;
    }

    public String getSalaryPayslipLetterHead() {
        return this.salaryPayslipLetterHead;
    }

    public void setSalaryPayslipLetterHead(String salaryPayslipLetterHead) {
        this.salaryPayslipLetterHead = salaryPayslipLetterHead;
    }

    public String getFestivalBonusPayslipLetterHead() {
        return this.festivalBonusPayslipLetterHead;
    }

    public void setFestivalBonusPayslipLetterHead(String festivalBonusPayslipLetterHead) {
        this.festivalBonusPayslipLetterHead = festivalBonusPayslipLetterHead;
    }

    public String getRecruitmentRequisitionLetterHead() {
        return this.recruitmentRequisitionLetterHead;
    }

    public void setRecruitmentRequisitionLetterHead(String recruitmentRequisitionLetterHead) {
        this.recruitmentRequisitionLetterHead = recruitmentRequisitionLetterHead;
    }

    public Boolean getHasOrganizationStamp() {
        return this.hasOrganizationStamp;
    }

    public void setHasOrganizationStamp(Boolean hasOrganizationStamp) {
        this.hasOrganizationStamp = hasOrganizationStamp;
    }

    public String getOrganizationStamp() {
        return this.organizationStamp;
    }

    public void setOrganizationStamp(String organizationStamp) {
        this.organizationStamp = organizationStamp;
    }

    public String getLinkedin() {
        return this.linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getYoutube() {
        return this.youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getInstagram() {
        return this.instagram;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getWhatsapp() {
        return this.whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }
}
