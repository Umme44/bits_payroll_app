package com.bits.hr.service.dto;

import com.bits.hr.domain.EmployeeDocument;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link EmployeeDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String pin;

    @NotNull
    @ValidateNaturalText
    private String fileName;

    @Lob
    private String filePath;

    private Boolean hasEmployeeVisibility;

    @ValidateNaturalText
    private String remarks;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    private String fileExtension;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getHasEmployeeVisibility() {
        return hasEmployeeVisibility;
    }

    public void setHasEmployeeVisibility(Boolean hasEmployeeVisibility) {
        this.hasEmployeeVisibility = hasEmployeeVisibility;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDocumentDTO)) {
            return false;
        }

        EmployeeDocumentDTO employeeDocumentDTO = (EmployeeDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDocumentDTO{" +
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
