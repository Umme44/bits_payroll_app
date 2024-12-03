package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeeStaticFile.
 */
@Entity
@Table(name = "employee_static_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeStaticFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Size(min = 0, max = 255)
    @Column(name = "file_path", length = 255)
    private String filePath;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmployeeStaticFile id(Long id) {
        this.setId(id);
        return this;
    }

    public EmployeeStaticFile filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeStaticFile employee(Employee employee) {
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
        if (!(o instanceof EmployeeStaticFile)) {
            return false;
        }
        return id != null && id.equals(((EmployeeStaticFile) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeStaticFile{" +
            "id=" + getId() +
            ", filePath='" + getFilePath() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
