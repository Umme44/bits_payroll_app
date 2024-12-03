package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.NoticeStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A OfficeNotices.
 */
@Entity
@Table(name = "office_notices")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OfficeNotices implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 250)
    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NoticeStatus status;

    @Column(name = "publish_form")
    private LocalDate publishForm;

    @Column(name = "publish_to")
    private LocalDate publishTo;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public OfficeNotices id(Long id) {
        this.setId(id);
        return this;
    }

    public OfficeNotices title(String title) {
        this.setTitle(title);
        return this;
    }

    public OfficeNotices description(String description) {
        this.setDescription(description);
        return this;
    }

    public OfficeNotices status(NoticeStatus status) {
        this.setStatus(status);
        return this;
    }

    public OfficeNotices publishForm(LocalDate publishForm) {
        this.setPublishForm(publishForm);
        return this;
    }

    public OfficeNotices publishTo(LocalDate publishTo) {
        this.setPublishTo(publishTo);
        return this;
    }

    public OfficeNotices createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public OfficeNotices updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public OfficeNotices createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public OfficeNotices updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
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
        if (!(o instanceof OfficeNotices)) {
            return false;
        }
        return id != null && id.equals(((OfficeNotices) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OfficeNotices{" +
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NoticeStatus getStatus() {
        return this.status;
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

    public LocalDate getPublishForm() {
        return this.publishForm;
    }

    public void setPublishForm(LocalDate publishForm) {
        this.publishForm = publishForm;
    }

    public LocalDate getPublishTo() {
        return this.publishTo;
    }

    public void setPublishTo(LocalDate publishTo) {
        this.publishTo = publishTo;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
