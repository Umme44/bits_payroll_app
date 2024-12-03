package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.FileAccessPrevilage;
import com.bits.hr.domain.enumeration.FileTemplatesType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A FileTemplates.
 */
@Entity
@Table(name = "file_templates")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileTemplates implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 250)
    @Column(name = "title", length = 250, nullable = false, unique = true)
    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "file_path")
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FileTemplatesType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_privilege")
    private FileAccessPrevilage accessPrivilege;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FileTemplates id(Long id) {
        this.setId(id);
        return this;
    }

    public FileTemplates title(String title) {
        this.setTitle(title);
        return this;
    }

    public FileTemplates filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public FileTemplates type(FileTemplatesType type) {
        this.setType(type);
        return this;
    }

    public FileTemplates accessPrivilege(FileAccessPrevilage accessPrivilege) {
        this.setAccessPrivilege(accessPrivilege);
        return this;
    }

    public FileTemplates isActive(Boolean isActive) {
        this.setIsActive(isActive);
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
        if (!(o instanceof FileTemplates)) {
            return false;
        }
        return id != null && id.equals(((FileTemplates) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileTemplates{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", type='" + getType() + "'" +
            ", accessPrivilege='" + getAccessPrivilege() + "'" +
            ", isActive='" + getIsActive() + "'" +
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

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileTemplatesType getType() {
        return this.type;
    }

    public void setType(FileTemplatesType type) {
        this.type = type;
    }

    public FileAccessPrevilage getAccessPrivilege() {
        return this.accessPrivilege;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setAccessPrivilege(FileAccessPrevilage accessPrivilege) {
        this.accessPrivilege = accessPrivilege;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
