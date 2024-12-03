package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A RoomRequisition.
 */
@Entity
@Table(name = "room_requisition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomRequisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Size(min = 0, max = 250)
    @Column(name = "booking_trn", length = 250, unique = true)
    private String bookingTrn;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "sanctioned_at")
    private Instant sanctionedAt;

    @Size(min = 0, max = 500)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "participant_list", length = 500)
    private String participantList;

    @Size(min = 0, max = 250)
    @Column(name = "rejected_reason", length = 250)
    private String rejectedReason;

    @NotNull
    @Column(name = "booking_start_date", nullable = false)
    private LocalDate bookingStartDate;

    @NotNull
    @Column(name = "booking_end_date", nullable = false)
    private LocalDate bookingEndDate;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "start_time", nullable = false)
    private Double startTime;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "end_time", nullable = false)
    private Double endTime;

    @NotNull
    @Size(min = 0, max = 250)
    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @Size(min = 0, max = 250)
    @Column(name = "agenda", length = 250)
    private String agenda;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "optional_participant_list")
    private String optionalParticipantList;

    @Column(name = "is_full_day")
    private Boolean isFullDay;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne
    private User sanctionedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee requester;

    @ManyToOne
    @JsonIgnoreProperties(value = { "createdBy", "updatedBy", "building", "floor", "roomType" }, allowSetters = true)
    private Room room;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public RoomRequisition id(Long id) {
        this.setId(id);
        return this;
    }

    public RoomRequisition status(Status status) {
        this.setStatus(status);
        return this;
    }

    public RoomRequisition bookingTrn(String bookingTrn) {
        this.setBookingTrn(bookingTrn);
        return this;
    }

    public RoomRequisition createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public RoomRequisition updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public RoomRequisition sanctionedAt(Instant sanctionedAt) {
        this.setSanctionedAt(sanctionedAt);
        return this;
    }

    public RoomRequisition participantList(String participantList) {
        this.setParticipantList(participantList);
        return this;
    }

    public RoomRequisition rejectedReason(String rejectedReason) {
        this.setRejectedReason(rejectedReason);
        return this;
    }

    public RoomRequisition bookingStartDate(LocalDate bookingStartDate) {
        this.setBookingStartDate(bookingStartDate);
        return this;
    }

    public RoomRequisition bookingEndDate(LocalDate bookingEndDate) {
        this.setBookingEndDate(bookingEndDate);
        return this;
    }

    public RoomRequisition startTime(Double startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public RoomRequisition endTime(Double endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public RoomRequisition title(String title) {
        this.setTitle(title);
        return this;
    }

    public RoomRequisition agenda(String agenda) {
        this.setAgenda(agenda);
        return this;
    }

    public RoomRequisition optionalParticipantList(String optionalParticipantList) {
        this.setOptionalParticipantList(optionalParticipantList);
        return this;
    }

    public RoomRequisition isFullDay(Boolean isFullDay) {
        this.setIsFullDay(isFullDay);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public RoomRequisition createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public RoomRequisition updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getSanctionedBy() {
        return this.sanctionedBy;
    }

    public void setSanctionedBy(User user) {
        this.sanctionedBy = user;
    }

    public RoomRequisition sanctionedBy(User user) {
        this.setSanctionedBy(user);
        return this;
    }

    public Employee getRequester() {
        return this.requester;
    }

    public void setRequester(Employee employee) {
        this.requester = employee;
    }

    public RoomRequisition requester(Employee employee) {
        this.setRequester(employee);
        return this;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public RoomRequisition room(Room room) {
        this.setRoom(room);
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
        if (!(o instanceof RoomRequisition)) {
            return false;
        }
        return id != null && id.equals(((RoomRequisition) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomRequisition{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", bookingTrn='" + getBookingTrn() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", sanctionedAt='" + getSanctionedAt() + "'" +
            ", participantList='" + getParticipantList() + "'" +
            ", rejectedReason='" + getRejectedReason() + "'" +
            ", bookingStartDate='" + getBookingStartDate() + "'" +
            ", bookingEndDate='" + getBookingEndDate() + "'" +
            ", startTime=" + getStartTime() +
            ", endTime=" + getEndTime() +
            ", title='" + getTitle() + "'" +
            ", agenda='" + getAgenda() + "'" +
            ", optionalParticipantList='" + getOptionalParticipantList() + "'" +
            ", isFullDay='" + getIsFullDay() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBookingTrn() {
        return this.bookingTrn;
    }

    public void setBookingTrn(String bookingTrn) {
        this.bookingTrn = bookingTrn;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getSanctionedAt() {
        return this.sanctionedAt;
    }

    public void setSanctionedAt(Instant sanctionedAt) {
        this.sanctionedAt = sanctionedAt;
    }

    public String getParticipantList() {
        return this.participantList;
    }

    public void setParticipantList(String participantList) {
        this.participantList = participantList;
    }

    public String getRejectedReason() {
        return this.rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public LocalDate getBookingStartDate() {
        return this.bookingStartDate;
    }

    public void setBookingStartDate(LocalDate bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public LocalDate getBookingEndDate() {
        return this.bookingEndDate;
    }

    public void setBookingEndDate(LocalDate bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    public Double getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAgenda() {
        return this.agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getOptionalParticipantList() {
        return this.optionalParticipantList;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setOptionalParticipantList(String optionalParticipantList) {
        this.optionalParticipantList = optionalParticipantList;
    }

    public Boolean getIsFullDay() {
        return this.isFullDay;
    }

    public void setIsFullDay(Boolean isFullDay) {
        this.isFullDay = isFullDay;
    }
}
