package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkingExperience.
 */
@Entity
@Table(name = "working_experience")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkingExperience implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "last_designation")
    private String lastDesignation;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "doj_of_last_organization")
    private LocalDate dojOfLastOrganization;

    @Column(name = "dor_of_last_organization")
    private LocalDate dorOfLastOrganization;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public WorkingExperience id(Long id) {
        this.setId(id);
        return this;
    }

    public WorkingExperience lastDesignation(String lastDesignation) {
        this.setLastDesignation(lastDesignation);
        return this;
    }

    public WorkingExperience organizationName(String organizationName) {
        this.setOrganizationName(organizationName);
        return this;
    }

    public WorkingExperience dojOfLastOrganization(LocalDate dojOfLastOrganization) {
        this.setDojOfLastOrganization(dojOfLastOrganization);
        return this;
    }

    public WorkingExperience dorOfLastOrganization(LocalDate dorOfLastOrganization) {
        this.setDorOfLastOrganization(dorOfLastOrganization);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public WorkingExperience employee(Employee employee) {
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
        if (!(o instanceof WorkingExperience)) {
            return false;
        }
        return id != null && id.equals(((WorkingExperience) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkingExperience{" +
            "id=" + getId() +
            ", lastDesignation='" + getLastDesignation() + "'" +
            ", organizationName='" + getOrganizationName() + "'" +
            ", dojOfLastOrganization='" + getDojOfLastOrganization() + "'" +
            ", dorOfLastOrganization='" + getDorOfLastOrganization() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastDesignation() {
        return this.lastDesignation;
    }

    public void setLastDesignation(String lastDesignation) {
        this.lastDesignation = lastDesignation;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public LocalDate getDojOfLastOrganization() {
        return this.dojOfLastOrganization;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setDojOfLastOrganization(LocalDate dojOfLastOrganization) {
        this.dojOfLastOrganization = dojOfLastOrganization;
    }

    public LocalDate getDorOfLastOrganization() {
        return this.dorOfLastOrganization;
    }

    public void setDorOfLastOrganization(LocalDate dorOfLastOrganization) {
        this.dorOfLastOrganization = dorOfLastOrganization;
    }
}
