package com.bits.hr.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Offer.
 */
@Entity
@Table(name = "offer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Offer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @NotNull
    @Size(min = 3, max = 250)
    @Column(name = "description", length = 250, nullable = false)
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "created_at")
    private LocalDate createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Offer id(Long id) {
        this.setId(id);
        return this;
    }

    public Offer title(String title) {
        this.setTitle(title);
        return this;
    }

    public Offer description(String description) {
        this.setDescription(description);
        return this;
    }

    public Offer imagePath(String imagePath) {
        this.setImagePath(imagePath);
        return this;
    }

    public Offer createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
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
        if (!(o instanceof Offer)) {
            return false;
        }
        return id != null && id.equals(((Offer) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Offer{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
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

    public String getImagePath() {
        return this.imagePath;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
