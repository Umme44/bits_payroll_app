package com.bits.hr.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A EmployeeDocument.
 */
@Entity
@Table(name = "employee_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "pin", nullable = false)
    private String pin;

    @Size(min = 4, max = 100)
    @Column(name = "file_name", nullable = false, length = 100)
    private String fileName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "has_employee_visibility")
    private Boolean hasEmployeeVisibility;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "file_extension")
    private String fileExtension;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmployeeDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return this.pin;
    }

    public EmployeeDocument pin(String pin) {
        this.setPin(pin);
        return this;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFileName() {
        return this.fileName;
    }

    public EmployeeDocument fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public EmployeeDocument filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getHasEmployeeVisibility() {
        return this.hasEmployeeVisibility;
    }

    public EmployeeDocument hasEmployeeVisibility(Boolean hasEmployeeVisibility) {
        this.setHasEmployeeVisibility(hasEmployeeVisibility);
        return this;
    }

    public void setHasEmployeeVisibility(Boolean hasEmployeeVisibility) {
        this.hasEmployeeVisibility = hasEmployeeVisibility;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public EmployeeDocument remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public EmployeeDocument createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public EmployeeDocument createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public EmployeeDocument updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public EmployeeDocument updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }

    public EmployeeDocument fileExtension(String fileExtension) {
        this.setFileExtension(fileExtension);
        return this;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDocument)) {
            return false;
        }
        return id != null && id.equals(((EmployeeDocument) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDocument{" +
            "id=" + getId() +
            ", pin='" + getPin() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", hasEmployeeVisibility='" + getHasEmployeeVisibility() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", fileExtension='" + getFileExtension() + "'" +
            "}";
    }
}
