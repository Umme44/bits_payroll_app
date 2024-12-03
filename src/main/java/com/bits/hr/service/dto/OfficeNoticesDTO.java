package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.NoticeStatus;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.OfficeNotices} entity.
 */
public class OfficeNoticesDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 250)
    @ValidateNaturalText
    private String title;

    @Lob
    @ValidateNaturalText
    private String description;

    private NoticeStatus status;

    private LocalDate publishForm;

    private LocalDate publishTo;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private String createdBy;

    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NoticeStatus getStatus() {
        return status;
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

    public LocalDate getPublishForm() {
        return publishForm;
    }

    public void setPublishForm(LocalDate publishForm) {
        this.publishForm = publishForm;
    }

    public LocalDate getPublishTo() {
        return publishTo;
    }

    public void setPublishTo(LocalDate publishTo) {
        this.publishTo = publishTo;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OfficeNoticesDTO)) {
            return false;
        }

        return id != null && id.equals(((OfficeNoticesDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OfficeNoticesDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", publishForm='" + getPublishForm() + "'" +
            ", publishTo='" + getPublishTo() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
