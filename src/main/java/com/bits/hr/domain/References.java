package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.RelationshipWithEmployee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A References.
 */
@Entity
@Table(name = "jhi_references")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class References implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "institute")
    private String institute;

    @Column(name = "designation")
    private String designation;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_with_employee")
    private RelationshipWithEmployee relationshipWithEmployee;

    @Column(name = "email")
    private String email;

    @Column(name = "contact_number")
    private String contactNumber;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public References id(Long id) {
        this.setId(id);
        return this;
    }

    public References name(String name) {
        this.setName(name);
        return this;
    }

    public References institute(String institute) {
        this.setInstitute(institute);
        return this;
    }

    public References designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public References relationshipWithEmployee(RelationshipWithEmployee relationshipWithEmployee) {
        this.setRelationshipWithEmployee(relationshipWithEmployee);
        return this;
    }

    public References email(String email) {
        this.setEmail(email);
        return this;
    }

    public References contactNumber(String contactNumber) {
        this.setContactNumber(contactNumber);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public References employee(Employee employee) {
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
        if (!(o instanceof References)) {
            return false;
        }
        return id != null && id.equals(((References) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "References{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", institute='" + getInstitute() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", relationshipWithEmployee='" + getRelationshipWithEmployee() + "'" +
            ", email='" + getEmail() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitute() {
        return this.institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getDesignation() {
        return this.designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public RelationshipWithEmployee getRelationshipWithEmployee() {
        return this.relationshipWithEmployee;
    }

    public void setRelationshipWithEmployee(RelationshipWithEmployee relationshipWithEmployee) {
        this.relationshipWithEmployee = relationshipWithEmployee;
    }

    public String getEmail() {
        return this.email;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
