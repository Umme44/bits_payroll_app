package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.RecruitmentNature;
import com.bits.hr.domain.enumeration.RequisitionResourceType;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RecruitmentRequisitionForm.
 */
@Entity
@Table(name = "recruitment_requisition_form")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RecruitmentRequisitionForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "expected_joining_date", nullable = false)
    private LocalDate expectedJoiningDate;

    @ValidateNaturalText
    @Size(min = 1, max = 250)
    @Column(name = "project", length = 250)
    private String project;

    @NotNull
    @Min(value = 1)
    @Max(value = 1000)
    @Column(name = "number_of_vacancies", nullable = false)
    private Integer numberOfVacancies;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmployeeCategory employmentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private RequisitionResourceType resourceType;

    @Column(name = "rrf_number")
    private String rrfNumber;

    @Size(min = 2, max = 250)
    @Column(name = "preferred_education_type", length = 250)
    @ValidateNaturalText
    private String preferredEducationType;

    @Column(name = "date_of_requisition")
    private LocalDate dateOfRequisition;

    @Column(name = "requested_date")
    private LocalDate requestedDate;

    @Column(name = "recommendation_date_01")
    private LocalDate recommendationDate01;

    @Column(name = "recommendation_date_02")
    private LocalDate recommendationDate02;

    @Column(name = "recommendation_date_03")
    private LocalDate recommendationDate03;

    @Column(name = "recommendation_date_04")
    private LocalDate recommendationDate04;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "requisition_status", nullable = false)
    private RequisitionStatus requisitionStatus;

    @Column(name = "rejected_at")
    private LocalDate rejectedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "total_onboard")
    private Integer totalOnboard;

    @Size(min = 2, max = 250)
    @Column(name = "preferred_skill_type", length = 250)
    private String preferredSkillType;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_nature")
    private RecruitmentNature recruitmentNature;

    @Size(min = 2, max = 250)
    @Column(name = "special_requirement", length = 250)
    private String specialRequirement;

    @Column(name = "recommendation_date_05")
    private LocalDate recommendationDate05;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Designation functionalDesignation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Band band;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Department department;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Unit unit;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Employee recommendedBy01;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Employee recommendedBy02;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Employee recommendedBy03;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Employee recommendedBy04;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Employee requester;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Employee rejectedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private User createdBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private User updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private User deletedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Employee recommendedBy05;

    @ManyToOne
    @JsonIgnoreProperties(value = "recruitmentRequisitionForms", allowSetters = true)
    private Employee employeeToBeReplaced;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExpectedJoiningDate() {
        return expectedJoiningDate;
    }

    public RecruitmentRequisitionForm expectedJoiningDate(LocalDate expectedJoiningDate) {
        this.expectedJoiningDate = expectedJoiningDate;
        return this;
    }

    public void setExpectedJoiningDate(LocalDate expectedJoiningDate) {
        this.expectedJoiningDate = expectedJoiningDate;
    }

    public String getProject() {
        return project;
    }

    public RecruitmentRequisitionForm project(String project) {
        this.project = project;
        return this;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Integer getNumberOfVacancies() {
        return numberOfVacancies;
    }

    public RecruitmentRequisitionForm numberOfVacancies(Integer numberOfVacancies) {
        this.numberOfVacancies = numberOfVacancies;
        return this;
    }

    public void setNumberOfVacancies(Integer numberOfVacancies) {
        this.numberOfVacancies = numberOfVacancies;
    }

    public EmployeeCategory getEmploymentType() {
        return employmentType;
    }

    public RecruitmentRequisitionForm employmentType(EmployeeCategory employmentType) {
        this.employmentType = employmentType;
        return this;
    }

    public void setEmploymentType(EmployeeCategory employmentType) {
        this.employmentType = employmentType;
    }

    public RequisitionResourceType getResourceType() {
        return resourceType;
    }

    public RecruitmentRequisitionForm resourceType(RequisitionResourceType resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public void setResourceType(RequisitionResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getRrfNumber() {
        return rrfNumber;
    }

    public RecruitmentRequisitionForm rrfNumber(String rrfNumber) {
        this.rrfNumber = rrfNumber;
        return this;
    }

    public void setRrfNumber(String rrfNumber) {
        this.rrfNumber = rrfNumber;
    }

    public String getPreferredEducationType() {
        return preferredEducationType;
    }

    public RecruitmentRequisitionForm preferredEducationType(String preferredEducationType) {
        this.preferredEducationType = preferredEducationType;
        return this;
    }

    public void setPreferredEducationType(String preferredEducationType) {
        this.preferredEducationType = preferredEducationType;
    }

    public LocalDate getDateOfRequisition() {
        return dateOfRequisition;
    }

    public RecruitmentRequisitionForm dateOfRequisition(LocalDate dateOfRequisition) {
        this.dateOfRequisition = dateOfRequisition;
        return this;
    }

    public void setDateOfRequisition(LocalDate dateOfRequisition) {
        this.dateOfRequisition = dateOfRequisition;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public RecruitmentRequisitionForm requestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
        return this;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDate getRecommendationDate01() {
        return recommendationDate01;
    }

    public RecruitmentRequisitionForm recommendationDate01(LocalDate recommendationDate01) {
        this.recommendationDate01 = recommendationDate01;
        return this;
    }

    public void setRecommendationDate01(LocalDate recommendationDate01) {
        this.recommendationDate01 = recommendationDate01;
    }

    public LocalDate getRecommendationDate02() {
        return recommendationDate02;
    }

    public RecruitmentRequisitionForm recommendationDate02(LocalDate recommendationDate02) {
        this.recommendationDate02 = recommendationDate02;
        return this;
    }

    public void setRecommendationDate02(LocalDate recommendationDate02) {
        this.recommendationDate02 = recommendationDate02;
    }

    public LocalDate getRecommendationDate03() {
        return recommendationDate03;
    }

    public RecruitmentRequisitionForm recommendationDate03(LocalDate recommendationDate03) {
        this.recommendationDate03 = recommendationDate03;
        return this;
    }

    public void setRecommendationDate03(LocalDate recommendationDate03) {
        this.recommendationDate03 = recommendationDate03;
    }

    public LocalDate getRecommendationDate04() {
        return recommendationDate04;
    }

    public RecruitmentRequisitionForm recommendationDate04(LocalDate recommendationDate04) {
        this.recommendationDate04 = recommendationDate04;
        return this;
    }

    public void setRecommendationDate04(LocalDate recommendationDate04) {
        this.recommendationDate04 = recommendationDate04;
    }

    public RequisitionStatus getRequisitionStatus() {
        return requisitionStatus;
    }

    public RecruitmentRequisitionForm requisitionStatus(RequisitionStatus requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
        return this;
    }

    public void setRequisitionStatus(RequisitionStatus requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public LocalDate getRejectedAt() {
        return rejectedAt;
    }

    public RecruitmentRequisitionForm rejectedAt(LocalDate rejectedAt) {
        this.rejectedAt = rejectedAt;
        return this;
    }

    public void setRejectedAt(LocalDate rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public RecruitmentRequisitionForm createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public RecruitmentRequisitionForm updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public RecruitmentRequisitionForm isDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getTotalOnboard() {
        return totalOnboard;
    }

    public RecruitmentRequisitionForm totalOnboard(Integer totalOnboard) {
        this.totalOnboard = totalOnboard;
        return this;
    }

    public void setTotalOnboard(Integer totalOnboard) {
        this.totalOnboard = totalOnboard;
    }

    public String getPreferredSkillType() {
        return preferredSkillType;
    }

    public RecruitmentRequisitionForm preferredSkillType(String preferredSkillType) {
        this.preferredSkillType = preferredSkillType;
        return this;
    }

    public void setPreferredSkillType(String preferredSkillType) {
        this.preferredSkillType = preferredSkillType;
    }

    public RecruitmentNature getRecruitmentNature() {
        return recruitmentNature;
    }

    public RecruitmentRequisitionForm recruitmentNature(RecruitmentNature recruitmentNature) {
        this.recruitmentNature = recruitmentNature;
        return this;
    }

    public void setRecruitmentNature(RecruitmentNature recruitmentNature) {
        this.recruitmentNature = recruitmentNature;
    }

    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public RecruitmentRequisitionForm specialRequirement(String specialRequirement) {
        this.specialRequirement = specialRequirement;
        return this;
    }

    public void setSpecialRequirement(String specialRequirement) {
        this.specialRequirement = specialRequirement;
    }

    public LocalDate getRecommendationDate05() {
        return recommendationDate05;
    }

    public RecruitmentRequisitionForm recommendationDate05(LocalDate recommendationDate05) {
        this.recommendationDate05 = recommendationDate05;
        return this;
    }

    public void setRecommendationDate05(LocalDate recommendationDate05) {
        this.recommendationDate05 = recommendationDate05;
    }

    public Designation getFunctionalDesignation() {
        return functionalDesignation;
    }

    public RecruitmentRequisitionForm functionalDesignation(Designation designation) {
        this.functionalDesignation = designation;
        return this;
    }

    public void setFunctionalDesignation(Designation designation) {
        this.functionalDesignation = designation;
    }

    public Band getBand() {
        return band;
    }

    public RecruitmentRequisitionForm band(Band band) {
        this.band = band;
        return this;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public Department getDepartment() {
        return department;
    }

    public RecruitmentRequisitionForm department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Unit getUnit() {
        return unit;
    }

    public RecruitmentRequisitionForm unit(Unit unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Employee getRecommendedBy01() {
        return recommendedBy01;
    }

    public RecruitmentRequisitionForm recommendedBy01(Employee employee) {
        this.recommendedBy01 = employee;
        return this;
    }

    public void setRecommendedBy01(Employee employee) {
        this.recommendedBy01 = employee;
    }

    public Employee getRecommendedBy02() {
        return recommendedBy02;
    }

    public RecruitmentRequisitionForm recommendedBy02(Employee employee) {
        this.recommendedBy02 = employee;
        return this;
    }

    public void setRecommendedBy02(Employee employee) {
        this.recommendedBy02 = employee;
    }

    public Employee getRecommendedBy03() {
        return recommendedBy03;
    }

    public RecruitmentRequisitionForm recommendedBy03(Employee employee) {
        this.recommendedBy03 = employee;
        return this;
    }

    public void setRecommendedBy03(Employee employee) {
        this.recommendedBy03 = employee;
    }

    public Employee getRecommendedBy04() {
        return recommendedBy04;
    }

    public RecruitmentRequisitionForm recommendedBy04(Employee employee) {
        this.recommendedBy04 = employee;
        return this;
    }

    public void setRecommendedBy04(Employee employee) {
        this.recommendedBy04 = employee;
    }

    public Employee getRequester() {
        return requester;
    }

    public RecruitmentRequisitionForm requester(Employee employee) {
        this.requester = employee;
        return this;
    }

    public void setRequester(Employee employee) {
        this.requester = employee;
    }

    public Employee getRejectedBy() {
        return rejectedBy;
    }

    public RecruitmentRequisitionForm rejectedBy(Employee employee) {
        this.rejectedBy = employee;
        return this;
    }

    public void setRejectedBy(Employee employee) {
        this.rejectedBy = employee;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public RecruitmentRequisitionForm createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public RecruitmentRequisitionForm updatedBy(User user) {
        this.updatedBy = user;
        return this;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public User getDeletedBy() {
        return deletedBy;
    }

    public RecruitmentRequisitionForm deletedBy(User user) {
        this.deletedBy = user;
        return this;
    }

    public void setDeletedBy(User user) {
        this.deletedBy = user;
    }

    public Employee getRecommendedBy05() {
        return recommendedBy05;
    }

    public RecruitmentRequisitionForm recommendedBy05(Employee employee) {
        this.recommendedBy05 = employee;
        return this;
    }

    public void setRecommendedBy05(Employee employee) {
        this.recommendedBy05 = employee;
    }

    public Employee getEmployeeToBeReplaced() {
        return employeeToBeReplaced;
    }

    public RecruitmentRequisitionForm employeeToBeReplaced(Employee employee) {
        this.employeeToBeReplaced = employee;
        return this;
    }

    public void setEmployeeToBeReplaced(Employee employee) {
        this.employeeToBeReplaced = employee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisitionForm)) {
            return false;
        }
        return id != null && id.equals(((RecruitmentRequisitionForm) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionForm{" +
            "id=" + getId() +
            ", expectedJoiningDate='" + getExpectedJoiningDate() + "'" +
            ", project='" + getProject() + "'" +
            ", numberOfVacancies=" + getNumberOfVacancies() +
            ", employmentType='" + getEmploymentType() + "'" +
            ", resourceType='" + getResourceType() + "'" +
            ", rrfNumber='" + getRrfNumber() + "'" +
            ", preferredEducationType='" + getPreferredEducationType() + "'" +
            ", dateOfRequisition='" + getDateOfRequisition() + "'" +
            ", requestedDate='" + getRequestedDate() + "'" +
            ", recommendationDate01='" + getRecommendationDate01() + "'" +
            ", recommendationDate02='" + getRecommendationDate02() + "'" +
            ", recommendationDate03='" + getRecommendationDate03() + "'" +
            ", recommendationDate04='" + getRecommendationDate04() + "'" +
            ", requisitionStatus='" + getRequisitionStatus() + "'" +
            ", rejectedAt='" + getRejectedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isDeleted='" + isIsDeleted() + "'" +
            ", totalOnboard=" + getTotalOnboard() +
            ", preferredSkillType='" + getPreferredSkillType() + "'" +
            ", recruitmentNature='" + getRecruitmentNature() + "'" +
            ", specialRequirement='" + getSpecialRequirement() + "'" +
            ", recommendationDate05='" + getRecommendationDate05() + "'" +
            "}";
    }
}
