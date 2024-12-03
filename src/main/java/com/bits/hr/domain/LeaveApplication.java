package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidatePhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A LeaveApplication.
 */
@Entity
@Table(name = "leave_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type")
    private LeaveType leaveType;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_line_manager_approved")
    private Boolean isLineManagerApproved;

    @Column(name = "is_hr_approved")
    private Boolean isHRApproved;

    @Column(name = "is_rejected")
    private Boolean isRejected;

    @Column(name = "rejection_comment")
    private String rejectionComment;

    @Column(name = "is_half_day")
    private Boolean isHalfDay;

    @Min(value = 0)
    @Column(name = "duration_in_day")
    private Integer durationInDay;

    @Column(name = "phone_number_on_leave")
    @ValidatePhoneNumber
    private String phoneNumberOnLeave;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "address_on_leave")
    private String addressOnLeave;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @ValidateNaturalText
    @Column(name = "reason")
    private String reason;

    @Column(name = "sanctioned_at")
    private Instant sanctionedAt;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne
    private User sanctionedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public LeaveApplication id(Long id) {
        this.setId(id);
        return this;
    }

    public LeaveApplication applicationDate(LocalDate applicationDate) {
        this.setApplicationDate(applicationDate);
        return this;
    }

    public LeaveApplication leaveType(LeaveType leaveType) {
        this.setLeaveType(leaveType);
        return this;
    }

    public LeaveApplication description(String description) {
        this.setDescription(description);
        return this;
    }

    public LeaveApplication startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public LeaveApplication endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public LeaveApplication isLineManagerApproved(Boolean isLineManagerApproved) {
        this.setIsLineManagerApproved(isLineManagerApproved);
        return this;
    }

    public LeaveApplication isHRApproved(Boolean isHRApproved) {
        this.setIsHRApproved(isHRApproved);
        return this;
    }

    public LeaveApplication isRejected(Boolean isRejected) {
        this.setIsRejected(isRejected);
        return this;
    }

    public LeaveApplication rejectionComment(String rejectionComment) {
        this.setRejectionComment(rejectionComment);
        return this;
    }

    public LeaveApplication isHalfDay(Boolean isHalfDay) {
        this.setIsHalfDay(isHalfDay);
        return this;
    }

    public LeaveApplication durationInDay(Integer durationInDay) {
        this.setDurationInDay(durationInDay);
        return this;
    }

    public LeaveApplication phoneNumberOnLeave(String phoneNumberOnLeave) {
        this.setPhoneNumberOnLeave(phoneNumberOnLeave);
        return this;
    }

    public LeaveApplication addressOnLeave(String addressOnLeave) {
        this.setAddressOnLeave(addressOnLeave);
        return this;
    }

    public LeaveApplication reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public LeaveApplication sanctionedAt(Instant sanctionedAt) {
        this.setSanctionedAt(sanctionedAt);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveApplication employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public User getSanctionedBy() {
        return this.sanctionedBy;
    }

    public void setSanctionedBy(User user) {
        this.sanctionedBy = user;
    }

    public LeaveApplication sanctionedBy(User user) {
        this.setSanctionedBy(user);
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
        if (!(o instanceof LeaveApplication)) {
            return false;
        }
        return id != null && id.equals(((LeaveApplication) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveApplication{" +
            "id=" + getId() +
            ", applicationDate='" + getApplicationDate() + "'" +
            ", leaveType='" + getLeaveType() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isLineManagerApproved='" + getIsLineManagerApproved() + "'" +
            ", isHRApproved='" + getIsHRApproved() + "'" +
            ", isRejected='" + getIsRejected() + "'" +
            ", rejectionComment='" + getRejectionComment() + "'" +
            ", isHalfDay='" + getIsHalfDay() + "'" +
            ", durationInDay=" + getDurationInDay() +
            ", phoneNumberOnLeave='" + getPhoneNumberOnLeave() + "'" +
            ", addressOnLeave='" + getAddressOnLeave() + "'" +
            ", reason='" + getReason() + "'" +
            ", sanctionedAt='" + getSanctionedAt() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsLineManagerApproved() {
        return this.isLineManagerApproved;
    }

    public void setIsLineManagerApproved(Boolean isLineManagerApproved) {
        this.isLineManagerApproved = isLineManagerApproved;
    }

    public Boolean getIsHRApproved() {
        return this.isHRApproved;
    }

    public void setIsHRApproved(Boolean isHRApproved) {
        this.isHRApproved = isHRApproved;
    }

    public Boolean getIsRejected() {
        return this.isRejected;
    }

    public void setIsRejected(Boolean isRejected) {
        this.isRejected = isRejected;
    }

    public String getRejectionComment() {
        return this.rejectionComment;
    }

    public void setRejectionComment(String rejectionComment) {
        this.rejectionComment = rejectionComment;
    }

    public Boolean getIsHalfDay() {
        return this.isHalfDay;
    }

    public void setIsHalfDay(Boolean isHalfDay) {
        this.isHalfDay = isHalfDay;
    }

    public Integer getDurationInDay() {
        return this.durationInDay;
    }

    public void setDurationInDay(Integer durationInDay) {
        this.durationInDay = durationInDay;
    }

    public String getPhoneNumberOnLeave() {
        return this.phoneNumberOnLeave;
    }

    public void setPhoneNumberOnLeave(String phoneNumberOnLeave) {
        this.phoneNumberOnLeave = phoneNumberOnLeave;
    }

    public String getAddressOnLeave() {
        return this.addressOnLeave;
    }

    public void setAddressOnLeave(String addressOnLeave) {
        this.addressOnLeave = addressOnLeave;
    }

    public String getReason() {
        return this.reason;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getSanctionedAt() {
        return this.sanctionedAt;
    }

    public void setSanctionedAt(Instant sanctionedAt) {
        this.sanctionedAt = sanctionedAt;
    }
}
