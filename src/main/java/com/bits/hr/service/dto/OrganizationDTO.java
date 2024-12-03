package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.OrganizationFileType;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidateNumeric;
import com.bits.hr.util.annotation.ValidatePhoneNumber;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.Organization} entity.
 */
public class OrganizationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    @ValidateNaturalText(allowNull = true)
    private String shortName;

    @NotNull
    @Size(max = 255)
    @ValidateNaturalText(allowNull = true)
    private String fullName;

    @Size(max = 255)
    @ValidateNaturalText(allowNull = true)
    private String slogan;

    @NotNull
    @Size(max = 255)
    @URL
    private String domainName;

    @NotNull
    @Size(max = 255)
    @Email
    private String emailAddress;

    @Size(max = 255)
    @Email
    private String hrEmailAddress;

    @Size(max = 255)
    @Email
    private String noReplyEmailAddress;

    @Size(max = 255)
    @ValidatePhoneNumber(allowNull = true)
    private String contactNumber;

    @Size(max = 255)
    @ValidateNumeric(allowNull = true)
    private String financeManagerPIN;

    @Size(max = 255)
    @ValidateNaturalText(allowNull = true)
    private String financeManagerName;

    @Size(max = 255)
    @ValidateNaturalText(allowNull = true)
    private String financeManagerDesignation;

    @Lob
    private String financeManagerSignature;

    @Lob
    private String logo;

    @Lob
    private String documentLetterHead;

    @Lob
    private String pfStatementLetterHead;

    @Lob
    private String taxStatementLetterHead;

    @Lob
    private String nomineeLetterHead;

    @Lob
    private String salaryPayslipLetterHead;

    @Lob
    private String festivalBonusPayslipLetterHead;

    @Lob
    private String recruitmentRequisitionLetterHead;

    private Boolean hasOrganizationStamp;

    @Lob
    private String organizationStamp;

    @Size(max = 255)
    @URL
    private String linkedin;

    @Size(max = 255)
    @URL
    private String twitter;

    @Size(max = 255)
    @URL
    private String facebook;

    @Size(max = 255)
    @URL
    private String youtube;

    @URL
    private String instagram;

    @Size(max = 255)
    @ValidateNaturalText(allowNull = true)
    private String whatsapp;

    @Size(max = 255)
    private String financeManagerUnit;

    private List<OrganizationFileType> organizationFileTypeList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getHrEmailAddress() {
        return hrEmailAddress;
    }

    public void setHrEmailAddress(String hrEmailAddress) {
        this.hrEmailAddress = hrEmailAddress;
    }

    public String getNoReplyEmailAddress() {
        return noReplyEmailAddress;
    }

    public void setNoReplyEmailAddress(String noReplyEmailAddress) {
        this.noReplyEmailAddress = noReplyEmailAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFinanceManagerPIN() {
        return financeManagerPIN;
    }

    public void setFinanceManagerPIN(String financeManagerPIN) {
        this.financeManagerPIN = financeManagerPIN;
    }

    public String getFinanceManagerSignature() {
        return financeManagerSignature;
    }

    public void setFinanceManagerSignature(String financeManagerSignature) {
        this.financeManagerSignature = financeManagerSignature;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDocumentLetterHead() {
        return documentLetterHead;
    }

    public void setDocumentLetterHead(String documentLetterHead) {
        this.documentLetterHead = documentLetterHead;
    }

    public String getPfStatementLetterHead() {
        return pfStatementLetterHead;
    }

    public void setPfStatementLetterHead(String pfStatementLetterHead) {
        this.pfStatementLetterHead = pfStatementLetterHead;
    }

    public String getTaxStatementLetterHead() {
        return taxStatementLetterHead;
    }

    public void setTaxStatementLetterHead(String taxStatementLetterHead) {
        this.taxStatementLetterHead = taxStatementLetterHead;
    }

    public String getNomineeLetterHead() {
        return nomineeLetterHead;
    }

    public void setNomineeLetterHead(String nomineeLetterHead) {
        this.nomineeLetterHead = nomineeLetterHead;
    }

    public String getSalaryPayslipLetterHead() {
        return salaryPayslipLetterHead;
    }

    public void setSalaryPayslipLetterHead(String salaryPayslipLetterHead) {
        this.salaryPayslipLetterHead = salaryPayslipLetterHead;
    }

    public String getFestivalBonusPayslipLetterHead() {
        return festivalBonusPayslipLetterHead;
    }

    public void setFestivalBonusPayslipLetterHead(String festivalBonusPayslipLetterHead) {
        this.festivalBonusPayslipLetterHead = festivalBonusPayslipLetterHead;
    }

    public String getRecruitmentRequisitionLetterHead() {
        return recruitmentRequisitionLetterHead;
    }

    public void setRecruitmentRequisitionLetterHead(String recruitmentRequisitionLetterHead) {
        this.recruitmentRequisitionLetterHead = recruitmentRequisitionLetterHead;
    }

    public Boolean isHasOrganizationStamp() {
        return hasOrganizationStamp;
    }

    public void setHasOrganizationStamp(Boolean hasOrganizationStamp) {
        this.hasOrganizationStamp = hasOrganizationStamp;
    }

    public String getOrganizationStamp() {
        return organizationStamp;
    }

    public void setOrganizationStamp(String organizationStamp) {
        this.organizationStamp = organizationStamp;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public List<OrganizationFileType> getOrganizationFileTypeList() {
        return organizationFileTypeList;
    }

    public void setOrganizationFileTypeList(List<OrganizationFileType> organizationFileTypeList) {
        this.organizationFileTypeList = organizationFileTypeList;
    }

    public String getFinanceManagerName() {
        return financeManagerName;
    }

    public void setFinanceManagerName(String financeManagerName) {
        this.financeManagerName = financeManagerName;
    }

    public String getFinanceManagerDesignation() {
        return financeManagerDesignation;
    }

    public void setFinanceManagerDesignation(String financeManagerDesignation) {
        this.financeManagerDesignation = financeManagerDesignation;
    }

    public Boolean getHasOrganizationStamp() {
        return hasOrganizationStamp;
    }

    public @Size(max = 255) String getFinanceManagerUnit() {
        return financeManagerUnit;
    }

    public void setFinanceManagerUnit(@Size(max = 255) String financeManagerUnit) {
        this.financeManagerUnit = financeManagerUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationDTO{" +
            "id=" + id +
            ", shortName='" + shortName + '\'' +
            ", fullName='" + fullName + '\'' +
            ", slogan='" + slogan + '\'' +
            ", domainName='" + domainName + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", hrEmailAddress='" + hrEmailAddress + '\'' +
            ", noReplyEmailAddress='" + noReplyEmailAddress + '\'' +
            ", contactNumber='" + contactNumber + '\'' +
            ", financeManagerPIN='" + financeManagerPIN + '\'' +
            ", financeManagerName='" + financeManagerName + '\'' +
            ", financeManagerDesignation='" + financeManagerDesignation + '\'' +
            ", financeManagerUnit='" + financeManagerUnit + '\'' +
            ", financeManagerSignature='" + financeManagerSignature + '\'' +
            ", logo='" + logo + '\'' +
            ", documentLetterHead='" + documentLetterHead + '\'' +
            ", pfStatementLetterHead='" + pfStatementLetterHead + '\'' +
            ", taxStatementLetterHead='" + taxStatementLetterHead + '\'' +
            ", nomineeLetterHead='" + nomineeLetterHead + '\'' +
            ", salaryPayslipLetterHead='" + salaryPayslipLetterHead + '\'' +
            ", festivalBonusPayslipLetterHead='" + festivalBonusPayslipLetterHead + '\'' +
            ", recruitmentRequisitionLetterHead='" + recruitmentRequisitionLetterHead + '\'' +
            ", hasOrganizationStamp=" + hasOrganizationStamp +
            ", organizationStamp='" + organizationStamp + '\'' +
            ", linkedin='" + linkedin + '\'' +
            ", twitter='" + twitter + '\'' +
            ", facebook='" + facebook + '\'' +
            ", youtube='" + youtube + '\'' +
            ", instagram='" + instagram + '\'' +
            ", whatsapp='" + whatsapp + '\'' +
            ", organizationFileTypeList=" + organizationFileTypeList +
            '}';
    }
}
