package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrainingHistory.
 */
@Entity
@Table(name = "training_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "training_name")
    private String trainingName;

    @Column(name = "coordinated_by")
    private String coordinatedBy;

    @Column(name = "date_of_completion")
    private LocalDate dateOfCompletion;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public TrainingHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public TrainingHistory trainingName(String trainingName) {
        this.setTrainingName(trainingName);
        return this;
    }

    public TrainingHistory coordinatedBy(String coordinatedBy) {
        this.setCoordinatedBy(coordinatedBy);
        return this;
    }

    public TrainingHistory dateOfCompletion(LocalDate dateOfCompletion) {
        this.setDateOfCompletion(dateOfCompletion);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public TrainingHistory employee(Employee employee) {
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
        if (!(o instanceof TrainingHistory)) {
            return false;
        }
        return id != null && id.equals(((TrainingHistory) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingHistory{" +
            "id=" + getId() +
            ", trainingName='" + getTrainingName() + "'" +
            ", coordinatedBy='" + getCoordinatedBy() + "'" +
            ", dateOfCompletion='" + getDateOfCompletion() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingName() {
        return this.trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getCoordinatedBy() {
        return this.coordinatedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCoordinatedBy(String coordinatedBy) {
        this.coordinatedBy = coordinatedBy;
    }

    public LocalDate getDateOfCompletion() {
        return this.dateOfCompletion;
    }

    public void setDateOfCompletion(LocalDate dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }
}
