package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.bits.hr.domain.ProcReqMaster} entity.
 */
public class ProcReqMasterDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String requisitionNo;

    @NotNull
    private LocalDate requestedDate;

    private Boolean isCTOApprovalRequired;

    @NotNull
    private RequisitionStatus requisitionStatus;

    private LocalDate expectedReceivedDate;

    @Lob
    @ValidateNaturalText
    private String reasoning;

    @DecimalMin(value = "1")
    private Double totalApproximatePrice;

    private Instant recommendationAt01;

    private Instant recommendationAt02;

    private Instant recommendationAt03;

    private Instant recommendationAt04;

    private Instant recommendationAt05;

    private Integer nextRecommendationOrder;

    private LocalDate rejectedDate;

    @Lob
    private String rejectionReason;

    private Instant closedAt;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    private Long departmentId;

    private String departmentName;

    private Long requestedById;

    private String requestedByFullName;

    private String requestedByPIN;
    private String requestedByOfficialContactNo;
    private String requestedByDesignationName;
    private String requestedByDepartmentName;

    private Long recommendedBy01Id;

    private String recommendedBy01FullName;

    private String recommendedBy01Designation;

    private String recommendedBy01Department;

    private Long recommendedBy02Id;

    private String recommendedBy02FullName;

    private String recommendedBy02Designation;

    private String recommendedBy02Department;

    private Long recommendedBy03Id;

    private String recommendedBy03FullName;

    private String recommendedBy03Designation;

    private String recommendedBy03Department;

    private Long recommendedBy04Id;

    private String recommendedBy04FullName;

    private String recommendedBy04Designation;

    private String recommendedBy04Department;

    private Long recommendedBy05Id;

    private String recommendedBy05FullName;

    private String recommendedBy05Designation;

    private String recommendedBy05Department;

    private Long nextApprovalFromId;

    private String nextApprovalFromFullName;
    private String nextApprovalFromOfficialEmail;

    private Long rejectedById;

    private String rejectedByFullName;

    private Long closedById;

    private String closedByFullName;

    private Long updatedById;

    private String updatedByLogin;

    private Long createdById;

    private String createdByLogin;

    private Set<ProcReqDTO> procReqs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Boolean isIsCTOApprovalRequired() {
        return isCTOApprovalRequired;
    }

    public void setIsCTOApprovalRequired(Boolean isCTOApprovalRequired) {
        this.isCTOApprovalRequired = isCTOApprovalRequired;
    }

    public RequisitionStatus getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(RequisitionStatus requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public LocalDate getExpectedReceivedDate() {
        return expectedReceivedDate;
    }

    public void setExpectedReceivedDate(LocalDate expectedReceivedDate) {
        this.expectedReceivedDate = expectedReceivedDate;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public Double getTotalApproximatePrice() {
        return totalApproximatePrice;
    }

    public void setTotalApproximatePrice(Double totalApproximatePrice) {
        this.totalApproximatePrice = totalApproximatePrice;
    }

    public Instant getRecommendationAt01() {
        return recommendationAt01;
    }

    public void setRecommendationAt01(Instant recommendationAt01) {
        this.recommendationAt01 = recommendationAt01;
    }

    public Instant getRecommendationAt02() {
        return recommendationAt02;
    }

    public void setRecommendationAt02(Instant recommendationAt02) {
        this.recommendationAt02 = recommendationAt02;
    }

    public Instant getRecommendationAt03() {
        return recommendationAt03;
    }

    public void setRecommendationAt03(Instant recommendationAt03) {
        this.recommendationAt03 = recommendationAt03;
    }

    public Instant getRecommendationAt04() {
        return recommendationAt04;
    }

    public void setRecommendationAt04(Instant recommendationAt04) {
        this.recommendationAt04 = recommendationAt04;
    }

    public Instant getRecommendationAt05() {
        return recommendationAt05;
    }

    public void setRecommendationAt05(Instant recommendationAt05) {
        this.recommendationAt05 = recommendationAt05;
    }

    public Integer getNextRecommendationOrder() {
        return nextRecommendationOrder;
    }

    public void setNextRecommendationOrder(Integer nextRecommendationOrder) {
        this.nextRecommendationOrder = nextRecommendationOrder;
    }

    public LocalDate getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(LocalDate rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(Long employeeId) {
        this.requestedById = employeeId;
    }

    public String getRequestedByFullName() {
        return requestedByFullName;
    }

    public void setRequestedByFullName(String employeeFullName) {
        this.requestedByFullName = employeeFullName;
    }

    public String getRequestedByPIN() {
        return requestedByPIN;
    }

    public void setRequestedByPIN(String requestedByPIN) {
        this.requestedByPIN = requestedByPIN;
    }

    public String getRequestedByOfficialContactNo() {
        return requestedByOfficialContactNo;
    }

    public void setRequestedByOfficialContactNo(String requestedByOfficialContactNo) {
        this.requestedByOfficialContactNo = requestedByOfficialContactNo;
    }

    public String getRequestedByDesignationName() {
        return requestedByDesignationName;
    }

    public void setRequestedByDesignationName(String requestedByDesignationName) {
        this.requestedByDesignationName = requestedByDesignationName;
    }

    public String getRequestedByDepartmentName() {
        return requestedByDepartmentName;
    }

    public void setRequestedByDepartmentName(String requestedByDepartmentName) {
        this.requestedByDepartmentName = requestedByDepartmentName;
    }

    public Long getRecommendedBy01Id() {
        return recommendedBy01Id;
    }

    public void setRecommendedBy01Id(Long employeeId) {
        this.recommendedBy01Id = employeeId;
    }

    public String getRecommendedBy01FullName() {
        return recommendedBy01FullName;
    }

    public void setRecommendedBy01FullName(String employeeFullName) {
        this.recommendedBy01FullName = employeeFullName;
    }

    public Long getRecommendedBy02Id() {
        return recommendedBy02Id;
    }

    public void setRecommendedBy02Id(Long employeeId) {
        this.recommendedBy02Id = employeeId;
    }

    public String getRecommendedBy02FullName() {
        return recommendedBy02FullName;
    }

    public void setRecommendedBy02FullName(String employeeFullName) {
        this.recommendedBy02FullName = employeeFullName;
    }

    public Long getRecommendedBy03Id() {
        return recommendedBy03Id;
    }

    public void setRecommendedBy03Id(Long employeeId) {
        this.recommendedBy03Id = employeeId;
    }

    public String getRecommendedBy03FullName() {
        return recommendedBy03FullName;
    }

    public void setRecommendedBy03FullName(String employeeFullName) {
        this.recommendedBy03FullName = employeeFullName;
    }

    public Long getRecommendedBy04Id() {
        return recommendedBy04Id;
    }

    public void setRecommendedBy04Id(Long employeeId) {
        this.recommendedBy04Id = employeeId;
    }

    public String getRecommendedBy04FullName() {
        return recommendedBy04FullName;
    }

    public void setRecommendedBy04FullName(String employeeFullName) {
        this.recommendedBy04FullName = employeeFullName;
    }

    public Long getRecommendedBy05Id() {
        return recommendedBy05Id;
    }

    public void setRecommendedBy05Id(Long employeeId) {
        this.recommendedBy05Id = employeeId;
    }

    public String getRecommendedBy05FullName() {
        return recommendedBy05FullName;
    }

    public void setRecommendedBy05FullName(String employeeFullName) {
        this.recommendedBy05FullName = employeeFullName;
    }

    public Long getNextApprovalFromId() {
        return nextApprovalFromId;
    }

    public void setNextApprovalFromId(Long employeeId) {
        this.nextApprovalFromId = employeeId;
    }

    public String getNextApprovalFromFullName() {
        return nextApprovalFromFullName;
    }

    public void setNextApprovalFromFullName(String employeeFullName) {
        this.nextApprovalFromFullName = employeeFullName;
    }

    public String getNextApprovalFromOfficialEmail() {
        return nextApprovalFromOfficialEmail;
    }

    public void setNextApprovalFromOfficialEmail(String nextApprovalFromOfficialEmail) {
        this.nextApprovalFromOfficialEmail = nextApprovalFromOfficialEmail;
    }

    public Long getRejectedById() {
        return rejectedById;
    }

    public void setRejectedById(Long employeeId) {
        this.rejectedById = employeeId;
    }

    public String getRejectedByFullName() {
        return rejectedByFullName;
    }

    public void setRejectedByFullName(String employeeFullName) {
        this.rejectedByFullName = employeeFullName;
    }

    public Long getClosedById() {
        return closedById;
    }

    public void setClosedById(Long employeeId) {
        this.closedById = employeeId;
    }

    public String getClosedByFullName() {
        return closedByFullName;
    }

    public void setClosedByFullName(String employeeFullName) {
        this.closedByFullName = employeeFullName;
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

    public Set<ProcReqDTO> getProcReqs() {
        return procReqs;
    }

    public void setProcReqs(Set<ProcReqDTO> procReqs) {
        this.procReqs = procReqs;
    }

    public String getRecommendedBy01Designation() {
        return recommendedBy01Designation;
    }

    public void setRecommendedBy01Designation(String recommendedBy01Designation) {
        this.recommendedBy01Designation = recommendedBy01Designation;
    }

    public String getRecommendedBy01Department() {
        return recommendedBy01Department;
    }

    public void setRecommendedBy01Department(String recommendedBy01Department) {
        this.recommendedBy01Department = recommendedBy01Department;
    }

    public String getRecommendedBy02Designation() {
        return recommendedBy02Designation;
    }

    public void setRecommendedBy02Designation(String recommendedBy02Designation) {
        this.recommendedBy02Designation = recommendedBy02Designation;
    }

    public String getRecommendedBy02Department() {
        return recommendedBy02Department;
    }

    public void setRecommendedBy02Department(String recommendedBy02Department) {
        this.recommendedBy02Department = recommendedBy02Department;
    }

    public String getRecommendedBy03Designation() {
        return recommendedBy03Designation;
    }

    public void setRecommendedBy03Designation(String recommendedBy03Designation) {
        this.recommendedBy03Designation = recommendedBy03Designation;
    }

    public String getRecommendedBy03Department() {
        return recommendedBy03Department;
    }

    public void setRecommendedBy03Department(String recommendedBy03Department) {
        this.recommendedBy03Department = recommendedBy03Department;
    }

    public String getRecommendedBy04Designation() {
        return recommendedBy04Designation;
    }

    public void setRecommendedBy04Designation(String recommendedBy04Designation) {
        this.recommendedBy04Designation = recommendedBy04Designation;
    }

    public String getRecommendedBy04Department() {
        return recommendedBy04Department;
    }

    public void setRecommendedBy04Department(String recommendedBy04Department) {
        this.recommendedBy04Department = recommendedBy04Department;
    }

    public String getRecommendedBy05Designation() {
        return recommendedBy05Designation;
    }

    public void setRecommendedBy05Designation(String recommendedBy05Designation) {
        this.recommendedBy05Designation = recommendedBy05Designation;
    }

    public String getRecommendedBy05Department() {
        return recommendedBy05Department;
    }

    public void setRecommendedBy05Department(String recommendedBy05Department) {
        this.recommendedBy05Department = recommendedBy05Department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcReqMasterDTO)) {
            return false;
        }

        return id != null && id.equals(((ProcReqMasterDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcReqMasterDTO{" +
            "id=" + getId() +
            ", requisitionNo='" + getRequisitionNo() + "'" +
            ", requestedDate='" + getRequestedDate() + "'" +
            ", isCTOApprovalRequired='" + isIsCTOApprovalRequired() + "'" +
            ", requisitionStatus='" + getRequisitionStatus() + "'" +
            ", expectedReceivedDate='" + getExpectedReceivedDate() + "'" +
            ", reasoning='" + getReasoning() + "'" +
            ", totalApproximatePrice=" + getTotalApproximatePrice() +
            ", recommendationAt01='" + getRecommendationAt01() + "'" +
            ", recommendationAt02='" + getRecommendationAt02() + "'" +
            ", recommendationAt03='" + getRecommendationAt03() + "'" +
            ", recommendationAt04='" + getRecommendationAt04() + "'" +
            ", recommendationAt05='" + getRecommendationAt05() + "'" +
            ", nextRecommendationOrder=" + getNextRecommendationOrder() +
            ", rejectedDate='" + getRejectedDate() + "'" +
            ", rejectionReason='" + getRejectionReason() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", departmentId=" + getDepartmentId() +
            ", departmentName='" + getDepartmentName() + "'" +
            ", requestedById=" + getRequestedById() +
            ", requestedByFullName='" + getRequestedByFullName() + "'" +
            ", recommendedBy01Id=" + getRecommendedBy01Id() +
            ", recommendedBy01FullName='" + getRecommendedBy01FullName() + "'" +
            ", recommendedBy02Id=" + getRecommendedBy02Id() +
            ", recommendedBy02FullName='" + getRecommendedBy02FullName() + "'" +
            ", recommendedBy03Id=" + getRecommendedBy03Id() +
            ", recommendedBy03FullName='" + getRecommendedBy03FullName() + "'" +
            ", recommendedBy04Id=" + getRecommendedBy04Id() +
            ", recommendedBy04FullName='" + getRecommendedBy04FullName() + "'" +
            ", recommendedBy05Id=" + getRecommendedBy05Id() +
            ", recommendedBy05FullName='" + getRecommendedBy05FullName() + "'" +
            ", nextApprovalFromId=" + getNextApprovalFromId() +
            ", nextApprovalFromFullName='" + getNextApprovalFromFullName() + "'" +
            ", rejectedById=" + getRejectedById() +
            ", rejectedByFullName='" + getRejectedByFullName() + "'" +
            ", closedById=" + getClosedById() +
            ", closedByFullName='" + getClosedByFullName() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            "}";
    }
}
