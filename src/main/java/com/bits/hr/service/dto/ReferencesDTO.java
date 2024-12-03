package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.RelationshipWithEmployee;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidatePhoneNumber;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.References} entity.
 */
public class ReferencesDTO implements Serializable {

    private Long id;

    @NotBlank
    @ValidateNaturalText
    private String name;

    @ValidateNaturalText
    private String institute;

    @ValidateNaturalText
    private String designation;

    private RelationshipWithEmployee relationshipWithEmployee;

    @Email
    private String email;

    @NotBlank
    @Size(min = 11, max = 17)
    @ValidatePhoneNumber
    private String contactNumber;

    private Long employeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public RelationshipWithEmployee getRelationshipWithEmployee() {
        return relationshipWithEmployee;
    }

    public void setRelationshipWithEmployee(RelationshipWithEmployee relationshipWithEmployee) {
        this.relationshipWithEmployee = relationshipWithEmployee;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReferencesDTO)) {
            return false;
        }

        return id != null && id.equals(((ReferencesDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReferencesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", institute='" + getInstitute() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", relationshipWithEmployee='" + getRelationshipWithEmployee() + "'" +
            ", email='" + getEmail() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
