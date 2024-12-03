package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.EventType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmploymentHistory.
 */
@Entity
@Table(name = "employment_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmploymentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "pin")
    private String pin;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "previous_main_gross_salary")
    private Double previousMainGrossSalary;

    @Column(name = "current_main_gross_salary")
    private Double currentMainGrossSalary;

    @Column(name = "previous_working_hour")
    private String previousWorkingHour;

    @Column(name = "changed_working_hour")
    private String changedWorkingHour;

    @Column(name = "is_modifiable")
    private Boolean isModifiable;

    @ManyToOne
    private Designation previousDesignation;

    @ManyToOne
    private Designation changedDesignation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "departmentHead" }, allowSetters = true)
    private Department previousDepartment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "departmentHead" }, allowSetters = true)
    private Department changedDepartment;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee previousReportingTo;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee changedReportingTo;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne
    private Unit previousUnit;

    @ManyToOne
    private Unit changedUnit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "createdBy", "updatedBy" }, allowSetters = true)
    private Band previousBand;

    @ManyToOne
    @JsonIgnoreProperties(value = { "createdBy", "updatedBy" }, allowSetters = true)
    private Band changedBand;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmploymentHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public EmploymentHistory referenceId(String referenceId) {
        this.setReferenceId(referenceId);
        return this;
    }

    public EmploymentHistory pin(String pin) {
        this.setPin(pin);
        return this;
    }

    public EmploymentHistory eventType(EventType eventType) {
        this.setEventType(eventType);
        return this;
    }

    public EmploymentHistory effectiveDate(LocalDate effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public EmploymentHistory previousMainGrossSalary(Double previousMainGrossSalary) {
        this.setPreviousMainGrossSalary(previousMainGrossSalary);
        return this;
    }

    public EmploymentHistory currentMainGrossSalary(Double currentMainGrossSalary) {
        this.setCurrentMainGrossSalary(currentMainGrossSalary);
        return this;
    }

    public EmploymentHistory previousWorkingHour(String previousWorkingHour) {
        this.setPreviousWorkingHour(previousWorkingHour);
        return this;
    }

    public EmploymentHistory changedWorkingHour(String changedWorkingHour) {
        this.setChangedWorkingHour(changedWorkingHour);
        return this;
    }

    public EmploymentHistory isModifiable(Boolean isModifiable) {
        this.setIsModifiable(isModifiable);
        return this;
    }

    public Designation getPreviousDesignation() {
        return this.previousDesignation;
    }

    public void setPreviousDesignation(Designation designation) {
        this.previousDesignation = designation;
    }

    public EmploymentHistory previousDesignation(Designation designation) {
        this.setPreviousDesignation(designation);
        return this;
    }

    public Designation getChangedDesignation() {
        return this.changedDesignation;
    }

    public void setChangedDesignation(Designation designation) {
        this.changedDesignation = designation;
    }

    public EmploymentHistory changedDesignation(Designation designation) {
        this.setChangedDesignation(designation);
        return this;
    }

    public Department getPreviousDepartment() {
        return this.previousDepartment;
    }

    public void setPreviousDepartment(Department department) {
        this.previousDepartment = department;
    }

    public EmploymentHistory previousDepartment(Department department) {
        this.setPreviousDepartment(department);
        return this;
    }

    public Department getChangedDepartment() {
        return this.changedDepartment;
    }

    public void setChangedDepartment(Department department) {
        this.changedDepartment = department;
    }

    public EmploymentHistory changedDepartment(Department department) {
        this.setChangedDepartment(department);
        return this;
    }

    public Employee getPreviousReportingTo() {
        return this.previousReportingTo;
    }

    public void setPreviousReportingTo(Employee employee) {
        this.previousReportingTo = employee;
    }

    public EmploymentHistory previousReportingTo(Employee employee) {
        this.setPreviousReportingTo(employee);
        return this;
    }

    public Employee getChangedReportingTo() {
        return this.changedReportingTo;
    }

    public void setChangedReportingTo(Employee employee) {
        this.changedReportingTo = employee;
    }

    public EmploymentHistory changedReportingTo(Employee employee) {
        this.setChangedReportingTo(employee);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmploymentHistory employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Unit getPreviousUnit() {
        return this.previousUnit;
    }

    public void setPreviousUnit(Unit unit) {
        this.previousUnit = unit;
    }

    public EmploymentHistory previousUnit(Unit unit) {
        this.setPreviousUnit(unit);
        return this;
    }

    public Unit getChangedUnit() {
        return this.changedUnit;
    }

    public void setChangedUnit(Unit unit) {
        this.changedUnit = unit;
    }

    public EmploymentHistory changedUnit(Unit unit) {
        this.setChangedUnit(unit);
        return this;
    }

    public Band getPreviousBand() {
        return this.previousBand;
    }

    public void setPreviousBand(Band band) {
        this.previousBand = band;
    }

    public EmploymentHistory previousBand(Band band) {
        this.setPreviousBand(band);
        return this;
    }

    public Band getChangedBand() {
        return this.changedBand;
    }

    public void setChangedBand(Band band) {
        this.changedBand = band;
    }

    public EmploymentHistory changedBand(Band band) {
        this.setChangedBand(band);
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
        if (!(o instanceof EmploymentHistory)) {
            return false;
        }
        return id != null && id.equals(((EmploymentHistory) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmploymentHistory{" +
            "id=" + getId() +
            ", referenceId='" + getReferenceId() + "'" +
            ", pin='" + getPin() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", previousMainGrossSalary=" + getPreviousMainGrossSalary() +
            ", currentMainGrossSalary=" + getCurrentMainGrossSalary() +
            ", previousWorkingHour='" + getPreviousWorkingHour() + "'" +
            ", changedWorkingHour='" + getChangedWorkingHour() + "'" +
            ", isModifiable='" + getIsModifiable() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceId() {
        return this.referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Double getPreviousMainGrossSalary() {
        return this.previousMainGrossSalary;
    }

    public void setPreviousMainGrossSalary(Double previousMainGrossSalary) {
        this.previousMainGrossSalary = previousMainGrossSalary;
    }

    public Double getCurrentMainGrossSalary() {
        return this.currentMainGrossSalary;
    }

    public void setCurrentMainGrossSalary(Double currentMainGrossSalary) {
        this.currentMainGrossSalary = currentMainGrossSalary;
    }

    public String getPreviousWorkingHour() {
        return this.previousWorkingHour;
    }

    public void setPreviousWorkingHour(String previousWorkingHour) {
        this.previousWorkingHour = previousWorkingHour;
    }

    public String getChangedWorkingHour() {
        return this.changedWorkingHour;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setChangedWorkingHour(String changedWorkingHour) {
        this.changedWorkingHour = changedWorkingHour;
    }

    public Boolean getIsModifiable() {
        return this.isModifiable;
    }

    public void setIsModifiable(Boolean isModifiable) {
        this.isModifiable = isModifiable;
    }
}
