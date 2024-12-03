package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ProcReqMaster.
 */
@Entity
@Table(name = "proc_req_master")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcReqMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "requisition_no", length = 255, nullable = false, unique = true)
    private String requisitionNo;

    @NotNull
    @Column(name = "requested_date", nullable = false)
    private LocalDate requestedDate;

    @Column(name = "is_cto_approval_required")
    private Boolean isCTOApprovalRequired;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "requisition_status", nullable = false)
    private RequisitionStatus requisitionStatus;

    @Column(name = "expected_received_date")
    private LocalDate expectedReceivedDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "reasoning")
    @ValidateNaturalText
    private String reasoning;

    @DecimalMin(value = "1")
    @Column(name = "total_approximate_price")
    private Double totalApproximatePrice;

    @Column(name = "recommendation_at_01")
    private Instant recommendationAt01;

    @Column(name = "recommendation_at_02")
    private Instant recommendationAt02;

    @Column(name = "recommendation_at_03")
    private Instant recommendationAt03;

    @Column(name = "recommendation_at_04")
    private Instant recommendationAt04;

    @Column(name = "recommendation_at_05")
    private Instant recommendationAt05;

    @Column(name = "next_recommendation_order")
    private Integer nextRecommendationOrder;

    @Column(name = "rejected_date")
    private LocalDate rejectedDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "closed_at")
    private Instant closedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "departmentHead" }, allowSetters = true)
    private Department department;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee requestedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee recommendedBy01;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee recommendedBy02;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee recommendedBy03;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee recommendedBy04;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee recommendedBy05;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee nextApprovalFrom;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee rejectedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee closedBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @OneToMany(mappedBy = "procReqMaster")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "itemInformation", "procReqMaster" }, allowSetters = true)
    private Set<ProcReq> procReqs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ProcReqMaster id(Long id) {
        this.setId(id);
        return this;
    }

    public ProcReqMaster requisitionNo(String requisitionNo) {
        this.setRequisitionNo(requisitionNo);
        return this;
    }

    public ProcReqMaster requestedDate(LocalDate requestedDate) {
        this.setRequestedDate(requestedDate);
        return this;
    }

    public ProcReqMaster isCTOApprovalRequired(Boolean isCTOApprovalRequired) {
        this.setIsCTOApprovalRequired(isCTOApprovalRequired);
        return this;
    }

    public ProcReqMaster requisitionStatus(RequisitionStatus requisitionStatus) {
        this.setRequisitionStatus(requisitionStatus);
        return this;
    }

    public ProcReqMaster expectedReceivedDate(LocalDate expectedReceivedDate) {
        this.setExpectedReceivedDate(expectedReceivedDate);
        return this;
    }

    public ProcReqMaster reasoning(String reasoning) {
        this.setReasoning(reasoning);
        return this;
    }

    public ProcReqMaster totalApproximatePrice(Double totalApproximatePrice) {
        this.setTotalApproximatePrice(totalApproximatePrice);
        return this;
    }

    public ProcReqMaster recommendationAt01(Instant recommendationAt01) {
        this.setRecommendationAt01(recommendationAt01);
        return this;
    }

    public ProcReqMaster recommendationAt02(Instant recommendationAt02) {
        this.setRecommendationAt02(recommendationAt02);
        return this;
    }

    public ProcReqMaster recommendationAt03(Instant recommendationAt03) {
        this.setRecommendationAt03(recommendationAt03);
        return this;
    }

    public ProcReqMaster recommendationAt04(Instant recommendationAt04) {
        this.setRecommendationAt04(recommendationAt04);
        return this;
    }

    public ProcReqMaster recommendationAt05(Instant recommendationAt05) {
        this.setRecommendationAt05(recommendationAt05);
        return this;
    }

    public ProcReqMaster nextRecommendationOrder(Integer nextRecommendationOrder) {
        this.setNextRecommendationOrder(nextRecommendationOrder);
        return this;
    }

    public ProcReqMaster rejectedDate(LocalDate rejectedDate) {
        this.setRejectedDate(rejectedDate);
        return this;
    }

    public ProcReqMaster rejectionReason(String rejectionReason) {
        this.setRejectionReason(rejectionReason);
        return this;
    }

    public ProcReqMaster closedAt(Instant closedAt) {
        this.setClosedAt(closedAt);
        return this;
    }

    public ProcReqMaster createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ProcReqMaster updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public ProcReqMaster department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Employee getRequestedBy() {
        return this.requestedBy;
    }

    public void setRequestedBy(Employee employee) {
        this.requestedBy = employee;
    }

    public ProcReqMaster requestedBy(Employee employee) {
        this.setRequestedBy(employee);
        return this;
    }

    public Employee getRecommendedBy01() {
        return this.recommendedBy01;
    }

    public void setRecommendedBy01(Employee employee) {
        this.recommendedBy01 = employee;
    }

    public ProcReqMaster recommendedBy01(Employee employee) {
        this.setRecommendedBy01(employee);
        return this;
    }

    public Employee getRecommendedBy02() {
        return this.recommendedBy02;
    }

    public void setRecommendedBy02(Employee employee) {
        this.recommendedBy02 = employee;
    }

    public ProcReqMaster recommendedBy02(Employee employee) {
        this.setRecommendedBy02(employee);
        return this;
    }

    public Employee getRecommendedBy03() {
        return this.recommendedBy03;
    }

    public void setRecommendedBy03(Employee employee) {
        this.recommendedBy03 = employee;
    }

    public ProcReqMaster recommendedBy03(Employee employee) {
        this.setRecommendedBy03(employee);
        return this;
    }

    public Employee getRecommendedBy04() {
        return this.recommendedBy04;
    }

    public void setRecommendedBy04(Employee employee) {
        this.recommendedBy04 = employee;
    }

    public ProcReqMaster recommendedBy04(Employee employee) {
        this.setRecommendedBy04(employee);
        return this;
    }

    public Employee getRecommendedBy05() {
        return this.recommendedBy05;
    }

    public void setRecommendedBy05(Employee employee) {
        this.recommendedBy05 = employee;
    }

    public ProcReqMaster recommendedBy05(Employee employee) {
        this.setRecommendedBy05(employee);
        return this;
    }

    public Employee getNextApprovalFrom() {
        return this.nextApprovalFrom;
    }

    public void setNextApprovalFrom(Employee employee) {
        this.nextApprovalFrom = employee;
    }

    public ProcReqMaster nextApprovalFrom(Employee employee) {
        this.setNextApprovalFrom(employee);
        return this;
    }

    public Employee getRejectedBy() {
        return this.rejectedBy;
    }

    public void setRejectedBy(Employee employee) {
        this.rejectedBy = employee;
    }

    public ProcReqMaster rejectedBy(Employee employee) {
        this.setRejectedBy(employee);
        return this;
    }

    public Employee getClosedBy() {
        return this.closedBy;
    }

    public void setClosedBy(Employee employee) {
        this.closedBy = employee;
    }

    public ProcReqMaster closedBy(Employee employee) {
        this.setClosedBy(employee);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public ProcReqMaster updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public ProcReqMaster createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public Set<ProcReq> getProcReqs() {
        return this.procReqs;
    }

    public void setProcReqs(Set<ProcReq> procReqs) {
        if (this.procReqs != null) {
            this.procReqs.forEach(i -> i.setProcReqMaster(null));
        }
        if (procReqs != null) {
            procReqs.forEach(i -> i.setProcReqMaster(this));
        }
        this.procReqs = procReqs;
    }

    public ProcReqMaster procReqs(Set<ProcReq> procReqs) {
        this.setProcReqs(procReqs);
        return this;
    }

    public ProcReqMaster addProcReq(ProcReq procReq) {
        this.procReqs.add(procReq);
        procReq.setProcReqMaster(this);
        return this;
    }

    public ProcReqMaster removeProcReq(ProcReq procReq) {
        this.procReqs.remove(procReq);
        procReq.setProcReqMaster(null);
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
        if (!(o instanceof ProcReqMaster)) {
            return false;
        }
        return id != null && id.equals(((ProcReqMaster) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcReqMaster{" +
            "id=" + getId() +
            ", requisitionNo='" + getRequisitionNo() + "'" +
            ", requestedDate='" + getRequestedDate() + "'" +
            ", isCTOApprovalRequired='" + getIsCTOApprovalRequired() + "'" +
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
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequisitionNo() {
        return this.requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public LocalDate getRequestedDate() {
        return this.requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Boolean getIsCTOApprovalRequired() {
        return this.isCTOApprovalRequired;
    }

    public void setIsCTOApprovalRequired(Boolean isCTOApprovalRequired) {
        this.isCTOApprovalRequired = isCTOApprovalRequired;
    }

    public RequisitionStatus getRequisitionStatus() {
        return this.requisitionStatus;
    }

    public void setRequisitionStatus(RequisitionStatus requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public LocalDate getExpectedReceivedDate() {
        return this.expectedReceivedDate;
    }

    public void setExpectedReceivedDate(LocalDate expectedReceivedDate) {
        this.expectedReceivedDate = expectedReceivedDate;
    }

    public String getReasoning() {
        return this.reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public Double getTotalApproximatePrice() {
        return this.totalApproximatePrice;
    }

    public void setTotalApproximatePrice(Double totalApproximatePrice) {
        this.totalApproximatePrice = totalApproximatePrice;
    }

    public Instant getRecommendationAt01() {
        return this.recommendationAt01;
    }

    public void setRecommendationAt01(Instant recommendationAt01) {
        this.recommendationAt01 = recommendationAt01;
    }

    public Instant getRecommendationAt02() {
        return this.recommendationAt02;
    }

    public void setRecommendationAt02(Instant recommendationAt02) {
        this.recommendationAt02 = recommendationAt02;
    }

    public Instant getRecommendationAt03() {
        return this.recommendationAt03;
    }

    public void setRecommendationAt03(Instant recommendationAt03) {
        this.recommendationAt03 = recommendationAt03;
    }

    public Instant getRecommendationAt04() {
        return this.recommendationAt04;
    }

    public void setRecommendationAt04(Instant recommendationAt04) {
        this.recommendationAt04 = recommendationAt04;
    }

    public Instant getRecommendationAt05() {
        return this.recommendationAt05;
    }

    public void setRecommendationAt05(Instant recommendationAt05) {
        this.recommendationAt05 = recommendationAt05;
    }

    public Integer getNextRecommendationOrder() {
        return this.nextRecommendationOrder;
    }

    public void setNextRecommendationOrder(Integer nextRecommendationOrder) {
        this.nextRecommendationOrder = nextRecommendationOrder;
    }

    public LocalDate getRejectedDate() {
        return this.rejectedDate;
    }

    public void setRejectedDate(LocalDate rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public String getRejectionReason() {
        return this.rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Instant getClosedAt() {
        return this.closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
