package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "model_name", nullable = false)
    private String modelName;

    @NotNull
    @Column(name = "chassis_number", nullable = false)
    private String chassisNumber;

    @NotNull
    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne
    private User approvedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee assignedDriver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public Vehicle modelName(String modelName) {
        this.setModelName(modelName);
        return this;
    }

    public Vehicle chassisNumber(String chassisNumber) {
        this.setChassisNumber(chassisNumber);
        return this;
    }

    public Vehicle registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public Vehicle status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Vehicle capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public Vehicle remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public Vehicle createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Vehicle updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Vehicle approvedAt(Instant approvedAt) {
        this.setApprovedAt(approvedAt);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Vehicle createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public Vehicle updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public User getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(User user) {
        this.approvedBy = user;
    }

    public Vehicle approvedBy(User user) {
        this.setApprovedBy(user);
        return this;
    }

    public Employee getAssignedDriver() {
        return this.assignedDriver;
    }

    public void setAssignedDriver(Employee employee) {
        this.assignedDriver = employee;
    }

    public Vehicle assignedDriver(Employee employee) {
        this.setAssignedDriver(employee);
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
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return id != null && id.equals(((Vehicle) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", modelName='" + getModelName() + "'" +
            ", chassisNumber='" + getChassisNumber() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", capacity=" + getCapacity() +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getChassisNumber() {
        return this.chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getApprovedAt() {
        return this.approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }
}
