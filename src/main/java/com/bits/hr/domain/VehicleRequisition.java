package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleRequisition.
 */
@Entity
@Table(name = "vehicle_requisition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleRequisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "purpose", nullable = false)
    private String purpose;

    @Column(name = "other_passengers_name")
    private String otherPassengersName;

    @NotNull
    @Min(value = 0L)
    @Max(value = 500L)
    @Column(name = "total_number_of_passengers", nullable = false)
    private Long totalNumberOfPassengers;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "sanction_at")
    private Instant sanctionAt;

    @Column(name = "transaction_number")
    private String transactionNumber;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    @Column(name = "start_time", nullable = false)
    private Double startTime;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    @Column(name = "end_time", nullable = false)
    private Double endTime;

    @NotNull
    @Column(name = "area", nullable = false)
    private String area;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne
    private User approvedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee requester;

    @ManyToOne
    @JsonIgnoreProperties(value = { "createdBy", "updatedBy", "approvedBy", "assignedDriver" }, allowSetters = true)
    private Vehicle vehicle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public VehicleRequisition id(Long id) {
        this.setId(id);
        return this;
    }

    public VehicleRequisition purpose(String purpose) {
        this.setPurpose(purpose);
        return this;
    }

    public VehicleRequisition otherPassengersName(String otherPassengersName) {
        this.setOtherPassengersName(otherPassengersName);
        return this;
    }

    public VehicleRequisition totalNumberOfPassengers(Long totalNumberOfPassengers) {
        this.setTotalNumberOfPassengers(totalNumberOfPassengers);
        return this;
    }

    public VehicleRequisition status(Status status) {
        this.setStatus(status);
        return this;
    }

    public VehicleRequisition remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public VehicleRequisition createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public VehicleRequisition updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public VehicleRequisition sanctionAt(Instant sanctionAt) {
        this.setSanctionAt(sanctionAt);
        return this;
    }

    public VehicleRequisition transactionNumber(String transactionNumber) {
        this.setTransactionNumber(transactionNumber);
        return this;
    }

    public VehicleRequisition startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public VehicleRequisition endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public VehicleRequisition startTime(Double startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public VehicleRequisition endTime(Double endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public VehicleRequisition area(String area) {
        this.setArea(area);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public VehicleRequisition createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public VehicleRequisition updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(User user) {
        this.approvedBy = user;
    }

    public VehicleRequisition approvedBy(User user) {
        this.setApprovedBy(user);
        return this;
    }

    public Employee getRequester() {
        return this.requester;
    }

    public void setRequester(Employee employee) {
        this.requester = employee;
    }

    public VehicleRequisition requester(Employee employee) {
        this.setRequester(employee);
        return this;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public VehicleRequisition vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
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
        if (!(o instanceof VehicleRequisition)) {
            return false;
        }
        return id != null && id.equals(((VehicleRequisition) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleRequisition{" +
            "id=" + getId() +
            ", purpose='" + getPurpose() + "'" +
            ", otherPassengersName='" + getOtherPassengersName() + "'" +
            ", totalNumberOfPassengers=" + getTotalNumberOfPassengers() +
            ", status='" + getStatus() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", sanctionAt='" + getSanctionAt() + "'" +
            ", transactionNumber='" + getTransactionNumber() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", startTime=" + getStartTime() +
            ", endTime=" + getEndTime() +
            ", area='" + getArea() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOtherPassengersName() {
        return this.otherPassengersName;
    }

    public void setOtherPassengersName(String otherPassengersName) {
        this.otherPassengersName = otherPassengersName;
    }

    public Long getTotalNumberOfPassengers() {
        return this.totalNumberOfPassengers;
    }

    public void setTotalNumberOfPassengers(Long totalNumberOfPassengers) {
        this.totalNumberOfPassengers = totalNumberOfPassengers;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public Instant getSanctionAt() {
        return this.sanctionAt;
    }

    public void setSanctionAt(Instant sanctionAt) {
        this.sanctionAt = sanctionAt;
    }

    public String getTransactionNumber() {
        return this.transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
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

    public Double getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return this.endTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
