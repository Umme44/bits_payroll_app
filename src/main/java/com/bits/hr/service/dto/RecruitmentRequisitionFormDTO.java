package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.RecruitmentNature;
import com.bits.hr.domain.enumeration.RequisitionResourceType;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.util.annotation.ValidateNaturalText;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.RecruitmentRequisitionForm} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionFormDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate expectedJoiningDate;

    @Size(min = 0, max = 250)
    @ValidateNaturalText
    @NotNull
    @Size(min = 1, max = 250)
    private String project;

    @NotNull
    @Min(value = 1)
    @Max(value = 1000)
    private Integer numberOfVacancies;

    @NotNull
    private EmployeeCategory employmentType;

    @NotNull
    private RequisitionResourceType resourceType;

    private String rrfNumber;

    @Size(min = 2, max = 250)
    @ValidateNaturalText
    private String preferredEducationType;

    private LocalDate dateOfRequisition;

    private LocalDate requestedDate;

    private LocalDate recommendationDate01;

    private LocalDate recommendationDate02;

    private LocalDate recommendationDate03;

    private LocalDate recommendationDate04;

    private LocalDate recommendationDate05;

    @NotNull
    private RequisitionStatus requisitionStatus;

    private LocalDate rejectedAt;

    private Instant createdAt;

    private Instant updatedAt;

    private Boolean isDeleted;

    @Size(min = 2, max = 250)
    @ValidateNaturalText
    private String preferredSkillType;

    private RecruitmentNature recruitmentNature;

    @Size(min = 2, max = 250)
    @ValidateNaturalText
    private String specialRequirement;

    private Long functionalDesignationId;

    private String functionalDesignationName;

    private Long bandId;
    private String bandName;

    private Long departmentId;
    private String departmentName;

    private Long unitId;
    private String unitName;

    private Long recommendedBy01Id;
    private String recommendedBy01FullName;

    private Long recommendedBy02Id;
    private String recommendedBy02FullName;

    private Long recommendedBy03Id;
    private String recommendedBy03FullName;

    private Long recommendedBy04Id;
    private String recommendedBy04FullName;

    private Long recommendedBy05Id;
    private String recommendedBy05FullName;

    private Long requesterId;
    private String requesterFullName;
    private String requesterPin;
    private String requesterDesignationName;

    private Long rejectedById;
    private String rejectedByFullName;

    private Long createdById;
    private String createdByFullName;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Long deletedById;

    private String deletedByLogin;

    private Integer totalOnboard;
    private Long employeeToBeReplacedId;
    private String employeeToBeReplacedFullName;
    private String employeeToBeReplacedPin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExpectedJoiningDate() {
        return expectedJoiningDate;
    }

    public void setExpectedJoiningDate(LocalDate expectedJoiningDate) {
        this.expectedJoiningDate = expectedJoiningDate;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Integer getNumberOfVacancies() {
        return numberOfVacancies;
    }

    public void setNumberOfVacancies(Integer numberOfVacancies) {
        this.numberOfVacancies = numberOfVacancies;
    }

    public EmployeeCategory getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmployeeCategory employmentType) {
        this.employmentType = employmentType;
    }

    public RequisitionResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(RequisitionResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getRrfNumber() {
        return rrfNumber;
    }

    public void setRrfNumber(String rrfNumber) {
        this.rrfNumber = rrfNumber;
    }

    public String getPreferredEducationType() {
        return preferredEducationType;
    }

    public void setPreferredEducationType(String preferredEducationType) {
        this.preferredEducationType = preferredEducationType;
    }

    public LocalDate getDateOfRequisition() {
        return dateOfRequisition;
    }

    public void setDateOfRequisition(LocalDate dateOfRequisition) {
        this.dateOfRequisition = dateOfRequisition;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDate getRecommendationDate01() {
        return recommendationDate01;
    }

    public void setRecommendationDate01(LocalDate recommendationDate01) {
        this.recommendationDate01 = recommendationDate01;
    }

    public LocalDate getRecommendationDate02() {
        return recommendationDate02;
    }

    public void setRecommendationDate02(LocalDate recommendationDate02) {
        this.recommendationDate02 = recommendationDate02;
    }

    public LocalDate getRecommendationDate03() {
        return recommendationDate03;
    }

    public void setRecommendationDate03(LocalDate recommendationDate03) {
        this.recommendationDate03 = recommendationDate03;
    }

    public LocalDate getRecommendationDate04() {
        return recommendationDate04;
    }

    public void setRecommendationDate04(LocalDate recommendationDate04) {
        this.recommendationDate04 = recommendationDate04;
    }

    public RequisitionStatus getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(RequisitionStatus requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public LocalDate getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(LocalDate rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getTotalOnboard() {
        return totalOnboard;
    }

    public void setTotalOnboard(Integer totalOnboard) {
        this.totalOnboard = totalOnboard;
    }

    public String getPreferredSkillType() {
        return preferredSkillType;
    }

    public void setPreferredSkillType(String preferredSkillType) {
        this.preferredSkillType = preferredSkillType;
    }

    public RecruitmentNature getRecruitmentNature() {
        return recruitmentNature;
    }

    public void setRecruitmentNature(RecruitmentNature recruitmentNature) {
        this.recruitmentNature = recruitmentNature;
    }

    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public void setSpecialRequirement(String specialRequirement) {
        this.specialRequirement = specialRequirement;
    }

    public LocalDate getRecommendationDate05() {
        return recommendationDate05;
    }

    public void setRecommendationDate05(LocalDate recommendationDate05) {
        this.recommendationDate05 = recommendationDate05;
    }

    public Long getFunctionalDesignationId() {
        return functionalDesignationId;
    }

    public void setFunctionalDesignationId(Long designationId) {
        this.functionalDesignationId = designationId;
    }

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getRecommendedBy01Id() {
        return recommendedBy01Id;
    }

    public void setRecommendedBy01Id(Long employeeId) {
        this.recommendedBy01Id = employeeId;
    }

    public Long getRecommendedBy02Id() {
        return recommendedBy02Id;
    }

    public void setRecommendedBy02Id(Long employeeId) {
        this.recommendedBy02Id = employeeId;
    }

    public Long getRecommendedBy03Id() {
        return recommendedBy03Id;
    }

    public void setRecommendedBy03Id(Long employeeId) {
        this.recommendedBy03Id = employeeId;
    }

    public Long getRecommendedBy04Id() {
        return recommendedBy04Id;
    }

    public void setRecommendedBy04Id(Long employeeId) {
        this.recommendedBy04Id = employeeId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long employeeId) {
        this.requesterId = employeeId;
    }

    public Long getRejectedById() {
        return rejectedById;
    }

    public void setRejectedById(Long employeeId) {
        this.rejectedById = employeeId;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long userId) {
        this.updatedById = userId;
    }

    public String getUpdatedByLogin() {
        return updatedByLogin;
    }

    public void setUpdatedByLogin(String userLogin) {
        this.updatedByLogin = userLogin;
    }

    public Long getDeletedById() {
        return deletedById;
    }

    public void setDeletedById(Long userId) {
        this.deletedById = userId;
    }

    public String getDeletedByLogin() {
        return deletedByLogin;
    }

    public void setDeletedByLogin(String userLogin) {
        this.deletedByLogin = userLogin;
    }

    public Long getRecommendedBy05Id() {
        return recommendedBy05Id;
    }

    public void setRecommendedBy05Id(Long employeeId) {
        this.recommendedBy05Id = employeeId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Long getEmployeeToBeReplacedId() {
        return employeeToBeReplacedId;
    }

    public void setEmployeeToBeReplacedId(Long employeeId) {
        this.employeeToBeReplacedId = employeeId;
    }

    public String getEmployeeToBeReplacedFullName() {
        return employeeToBeReplacedFullName;
    }

    public void setEmployeeToBeReplacedFullName(String employeeToBeReplacedFullName) {
        this.employeeToBeReplacedFullName = employeeToBeReplacedFullName;
    }

    public String getFunctionalDesignationName() {
        return functionalDesignationName;
    }

    public void setFunctionalDesignationName(String functionalDesignationName) {
        this.functionalDesignationName = functionalDesignationName;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
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

    public String getRecommendedBy01FullName() {
        return recommendedBy01FullName;
    }

    public void setRecommendedBy01FullName(String recommendedBy01FullName) {
        this.recommendedBy01FullName = recommendedBy01FullName;
    }

    public String getRecommendedBy02FullName() {
        return recommendedBy02FullName;
    }

    public void setRecommendedBy02FullName(String recommendedBy02FullName) {
        this.recommendedBy02FullName = recommendedBy02FullName;
    }

    public String getRecommendedBy03FullName() {
        return recommendedBy03FullName;
    }

    public void setRecommendedBy03FullName(String recommendedBy03FullName) {
        this.recommendedBy03FullName = recommendedBy03FullName;
    }

    public String getRecommendedBy04FullName() {
        return recommendedBy04FullName;
    }

    public void setRecommendedBy04FullName(String recommendedBy04FullName) {
        this.recommendedBy04FullName = recommendedBy04FullName;
    }

    public String getRecommendedBy05FullName() {
        return recommendedBy05FullName;
    }

    public void setRecommendedBy05FullName(String recommendedBy05FullName) {
        this.recommendedBy05FullName = recommendedBy05FullName;
    }

    public String getRequesterFullName() {
        return requesterFullName;
    }

    public void setRequesterFullName(String requesterFullName) {
        this.requesterFullName = requesterFullName;
    }

    public String getRequesterPin() {
        return requesterPin;
    }

    public void setRequesterPin(String requesterPin) {
        this.requesterPin = requesterPin;
    }

    public String getRequesterDesignationName() {
        return requesterDesignationName;
    }

    public void setRequesterDesignationName(String requesterDesignationName) {
        this.requesterDesignationName = requesterDesignationName;
    }

    public String getRejectedByFullName() {
        return rejectedByFullName;
    }

    public void setRejectedByFullName(String rejectedByFullName) {
        this.rejectedByFullName = rejectedByFullName;
    }

    public String getCreatedByFullName() {
        return createdByFullName;
    }

    public void setCreatedByFullName(String createdByFullName) {
        this.createdByFullName = createdByFullName;
    }

    public String getEmployeeToBeReplacedPin() {
        return employeeToBeReplacedPin;
    }

    public void setEmployeeToBeReplacedPin(String employeeToBeReplacedPin) {
        this.employeeToBeReplacedPin = employeeToBeReplacedPin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisitionFormDTO)) {
            return false;
        }

        return id != null && id.equals(((RecruitmentRequisitionFormDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RecruitmentRequisitionFormDTO{" +
            "id=" + id +
            ", expectedJoiningDate=" + expectedJoiningDate +
            ", project='" + project + '\'' +
            ", numberOfVacancies=" + numberOfVacancies +
            ", employmentType=" + employmentType +
            ", resourceType=" + resourceType +
            ", rrfNumber='" + rrfNumber + '\'' +
            ", preferredEducationType='" + preferredEducationType + '\'' +
            ", dateOfRequisition=" + dateOfRequisition +
            ", requestedDate=" + requestedDate +
            ", recommendationDate01=" + recommendationDate01 +
            ", recommendationDate02=" + recommendationDate02 +
            ", recommendationDate03=" + recommendationDate03 +
            ", recommendationDate04=" + recommendationDate04 +
            ", recommendationDate05=" + recommendationDate05 +
            ", requisitionStatus=" + requisitionStatus +
            ", rejectedAt=" + rejectedAt +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", isDeleted=" + isDeleted +
            ", preferredSkillType='" + preferredSkillType + '\'' +
            ", recruitmentNature=" + recruitmentNature +
            ", specialRequirement='" + specialRequirement + '\'' +
            ", functionalDesignationId=" + functionalDesignationId +
            ", functionalDesignationName='" + functionalDesignationName + '\'' +
            ", bandId=" + bandId +
            ", bandName='" + bandName + '\'' +
            ", departmentId=" + departmentId +
            ", departmentName='" + departmentName + '\'' +
            ", unitId=" + unitId +
            ", unitName='" + unitName + '\'' +
            ", recommendedBy01Id=" + recommendedBy01Id +
            ", recommendedBy01FullName='" + recommendedBy01FullName + '\'' +
            ", recommendedBy02Id=" + recommendedBy02Id +
            ", recommendedBy02FullName='" + recommendedBy02FullName + '\'' +
            ", recommendedBy03Id=" + recommendedBy03Id +
            ", recommendedBy03FullName='" + recommendedBy03FullName + '\'' +
            ", recommendedBy04Id=" + recommendedBy04Id +
            ", recommendedBy04FullName='" + recommendedBy04FullName + '\'' +
            ", recommendedBy05Id=" + recommendedBy05Id +
            ", recommendedBy05FullName='" + recommendedBy05FullName + '\'' +
            ", requesterId=" + requesterId +
            ", requesterFullName='" + requesterFullName + '\'' +
            ", requesterPin='" + requesterPin + '\'' +
            ", requesterDesignationName='" + requesterDesignationName + '\'' +
            ", rejectedById=" + rejectedById +
            ", rejectedByFullName='" + rejectedByFullName + '\'' +
            ", createdById=" + createdById +
            ", createdByFullName='" + createdByFullName + '\'' +
            ", createdByLogin='" + createdByLogin + '\'' +
            ", updatedById=" + updatedById +
            ", updatedByLogin='" + updatedByLogin + '\'' +
            ", deletedById=" + deletedById +
            ", deletedByLogin='" + deletedByLogin + '\'' +
            ", totalOnboard=" + totalOnboard +
            ", employeeToBeReplacedId=" + employeeToBeReplacedId +
            ", employeeToBeReplacedFullName='" + employeeToBeReplacedFullName + '\'' +
            ", employeeToBeReplacedPin='" + employeeToBeReplacedPin + '\'' +
            '}';
    }
}
