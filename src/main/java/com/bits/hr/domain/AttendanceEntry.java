package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.AttendanceDeviceOrigin;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AttendanceEntry.
 */
@Entity
@Table(name = "attendance_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttendanceEntry implements Serializable {

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
    @ValidateNaturalText(allowNull = true)
    private String inNote;

    @Column(name = "out_time")
    private Instant outTime;

    @Column(name = "out_note")
    @ValidateNaturalText(allowNull = true)
    private String outNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Size(min = 0, max = 250)
    @Column(name = "note", length = 250)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "punch_in_device_origin")
    private AttendanceDeviceOrigin punchInDeviceOrigin = AttendanceDeviceOrigin.WEB;

    @Enumerated(EnumType.STRING)
    @Column(name = "punch_out_device_origin")
    private AttendanceDeviceOrigin punchOutDeviceOrigin = AttendanceDeviceOrigin.WEB;

    @Column(name = "is_auto_punch_out")
    private Boolean isAutoPunchOut = false;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public AttendanceEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public AttendanceEntry date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public AttendanceEntry inTime(Instant inTime) {
        this.setInTime(inTime);
        return this;
    }

    public AttendanceEntry inNote(String inNote) {
        this.setInNote(inNote);
        return this;
    }

    public AttendanceEntry outTime(Instant outTime) {
        this.setOutTime(outTime);
        return this;
    }

    public AttendanceEntry outNote(String outNote) {
        this.setOutNote(outNote);
        return this;
    }

    public AttendanceEntry status(Status status) {
        this.setStatus(status);
        return this;
    }

    public AttendanceEntry note(String note) {
        this.setNote(note);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public AttendanceEntry employee(Employee employee) {
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
        if (!(o instanceof AttendanceEntry)) {
            return false;
        }
        return id != null && id.equals(((AttendanceEntry) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttendanceEntry{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", inTime='" + getInTime() + "'" +
            ", inNote='" + getInNote() + "'" +
            ", outTime='" + getOutTime() + "'" +
            ", outNote='" + getOutNote() + "'" +
            ", status='" + getStatus() + "'" +
            ", note='" + getNote() + "'" +
            ", punchInDeviceOrigin='" + getPunchInDeviceOrigin() + "'" +
            ", punchOutDeviceOrigin='" + getPunchOutDeviceOrigin() + "'" +
            ", isAutoPunchOut='" + isIsAutoPunchOut() + "'" +
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

    public Status getStatus() {
        return this.status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AttendanceDeviceOrigin getPunchInDeviceOrigin() {
        return punchInDeviceOrigin;
    }

    public AttendanceEntry punchInDeviceOrigin(AttendanceDeviceOrigin punchInDeviceOrigin) {
        this.punchInDeviceOrigin = punchInDeviceOrigin;
        return this;
    }

    public void setPunchInDeviceOrigin(AttendanceDeviceOrigin punchInDeviceOrigin) {
        this.punchInDeviceOrigin = punchInDeviceOrigin;
    }

    public AttendanceDeviceOrigin getPunchOutDeviceOrigin() {
        return punchOutDeviceOrigin;
    }

    public AttendanceEntry punchOutDeviceOrigin(AttendanceDeviceOrigin punchOutDeviceOrigin) {
        this.punchOutDeviceOrigin = punchOutDeviceOrigin;
        return this;
    }

    public void setPunchOutDeviceOrigin(AttendanceDeviceOrigin punchOutDeviceOrigin) {
        this.punchOutDeviceOrigin = punchOutDeviceOrigin;
    }

    public Boolean isIsAutoPunchOut() {
        return isAutoPunchOut;
    }

    public AttendanceEntry isAutoPunchOut(Boolean isAutoPunchOut) {
        this.isAutoPunchOut = isAutoPunchOut;
        return this;
    }

    public void setIsAutoPunchOut(Boolean isAutoPunchOut) {
        this.isAutoPunchOut = isAutoPunchOut;
    }
}
