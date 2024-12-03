package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.FileAccessPrevilage;
import com.bits.hr.domain.enumeration.FileTemplatesType;
import java.io.Serializable;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.FileTemplates} entity.
 */
public class FileTemplatesDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 250)
    private String title;

    @Lob
    private String filePath;

    private FileTemplatesType type;

    private FileAccessPrevilage accessPrivilege;

    @NotNull
    private Boolean isActive;

    @Lob
    private byte[] file;

    private String fileContentType;

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileTemplatesType getType() {
        return type;
    }

    public void setType(FileTemplatesType type) {
        this.type = type;
    }

    public FileAccessPrevilage getAccessPrivilege() {
        return accessPrivilege;
    }

    public void setAccessPrivilege(FileAccessPrevilage accessPrivilege) {
        this.accessPrivilege = accessPrivilege;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileTemplatesDTO)) {
            return false;
        }

        return id != null && id.equals(((FileTemplatesDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileTemplatesDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", type='" + getType() + "'" +
            ", accessPrivilege='" + getAccessPrivilege() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
