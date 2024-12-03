package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EducationDetails.
 */
@Entity
@Table(name = "education_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EducationDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name_of_degree")
    private String nameOfDegree;

    @Column(name = "subject")
    private String subject;

    @Column(name = "institute")
    private String institute;

    @Column(name = "year_of_degree_completion")
    private String yearOfDegreeCompletion;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EducationDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public EducationDetails nameOfDegree(String nameOfDegree) {
        this.setNameOfDegree(nameOfDegree);
        return this;
    }

    public EducationDetails subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public EducationDetails institute(String institute) {
        this.setInstitute(institute);
        return this;
    }

    public EducationDetails yearOfDegreeCompletion(String yearOfDegreeCompletion) {
        this.setYearOfDegreeCompletion(yearOfDegreeCompletion);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EducationDetails employee(Employee employee) {
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
        if (!(o instanceof EducationDetails)) {
            return false;
        }
        return id != null && id.equals(((EducationDetails) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EducationDetails{" +
            "id=" + getId() +
            ", nameOfDegree='" + getNameOfDegree() + "'" +
            ", subject='" + getSubject() + "'" +
            ", institute='" + getInstitute() + "'" +
            ", yearOfDegreeCompletion='" + getYearOfDegreeCompletion() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameOfDegree() {
        return this.nameOfDegree;
    }

    public void setNameOfDegree(String nameOfDegree) {
        this.nameOfDegree = nameOfDegree;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInstitute() {
        return this.institute;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getYearOfDegreeCompletion() {
        return this.yearOfDegreeCompletion;
    }

    public void setYearOfDegreeCompletion(String yearOfDegreeCompletion) {
        this.yearOfDegreeCompletion = yearOfDegreeCompletion;
    }
}
