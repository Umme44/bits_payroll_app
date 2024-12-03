package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ManualAttendanceEntry.
 */
@Entity
@Table(name = "manual_attendance_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualAttendanceEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "in_time")
    private Instant inTime;

    @Column(name = "in_note")
    private String inNote;

    @Column(name = "out_time")
    private Instant outTime;

    @Column(name = "out_note")
    private String outNote;

    @Column(name = "is_line_manager_approved")
    private Boolean isLineManagerApproved;

    @Column(name = "is_hr_approved")
    private Boolean isHRApproved;

    @Column(name = "is_rejected")
    private Boolean isRejected;

    @Column(name = "rejection_comment")
    private String rejectionComment;

    @Size(min = 0, max = 250)
    @Column(name = "note", length = 250)
    private String note;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ManualAttendanceEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public ManualAttendanceEntry date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public ManualAttendanceEntry inTime(Instant inTime) {
        this.setInTime(inTime);
        return this;
    }

    public ManualAttendanceEntry inNote(String inNote) {
        this.setInNote(inNote);
        return this;
    }

    public ManualAttendanceEntry outTime(Instant outTime) {
        this.setOutTime(outTime);
        return this;
    }

    public ManualAttendanceEntry outNote(String outNote) {
        this.setOutNote(outNote);
        return this;
    }

    public ManualAttendanceEntry isLineManagerApproved(Boolean isLineManagerApproved) {
        this.setIsLineManagerApproved(isLineManagerApproved);
        return this;
    }

    public ManualAttendanceEntry isHRApproved(Boolean isHRApproved) {
        this.setIsHRApproved(isHRApproved);
        return this;
    }

    public ManualAttendanceEntry isRejected(Boolean isRejected) {
        this.setIsRejected(isRejected);
        return this;
    }

    public ManualAttendanceEntry rejectionComment(String rejectionComment) {
        this.setRejectionComment(rejectionComment);
        return this;
    }

    public ManualAttendanceEntry note(String note) {
        this.setNote(note);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ManualAttendanceEntry employee(Employee employee) {
        this.setEmployee(employee);
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
        if (!(o instanceof ManualAttendanceEntry)) {
            return false;
        }
        return id != null && id.equals(((ManualAttendanceEntry) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualAttendanceEntry{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", inTime='" + getInTime() + "'" +
            ", inNote='" + getInNote() + "'" +
            ", outTime='" + getOutTime() + "'" +
            ", outNote='" + getOutNote() + "'" +
            ", isLineManagerApproved='" + getIsLineManagerApproved() + "'" +
            ", isHRApproved='" + getIsHRApproved() + "'" +
            ", isRejected='" + getIsRejected() + "'" +
            ", rejectionComment='" + getRejectionComment() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getInTime() {
        return this.inTime;
    }

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public String getInNote() {
        return this.inNote;
    }

    public void setInNote(String inNote) {
        this.inNote = inNote;
    }

    public Instant getOutTime() {
        return this.outTime;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }

    public String getOutNote() {
        return this.outNote;
    }

    public void setOutNote(String outNote) {
        this.outNote = outNote;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setRejectionComment(String rejectionComment) {
        this.rejectionComment = rejectionComment;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
