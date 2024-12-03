package com.bits.hr.service.dto;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.EmployeeStaticFile} entity.
 */
public class EmployeeStaticFileDTO implements Serializable {

    private Long id;

    @Size(min = 0, max = 255)
    private String filePath;

    private Long employeeId;

    private String pin;

    private String fullName;

    private byte[] getByteStreamFromFilePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public byte[] getGetByteStreamFromFilePath() {
        return getByteStreamFromFilePath;
    }

    public void setGetByteStreamFromFilePath(byte[] getByteStreamFromFilePath) {
        this.getByteStreamFromFilePath = getByteStreamFromFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeStaticFileDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeStaticFileDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeStaticFileDTO{" +
            "id=" + getId() +
            ", filePath='" + getFilePath() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
