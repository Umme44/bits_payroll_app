package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidateNumeric;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.PfAccount} entity.
 */
public class PfAccountDTO implements Serializable {

    private Long id;

    @ValidateNaturalText
    private String pfCode;

    private LocalDate membershipStartDate;

    private LocalDate membershipEndDate;

    private PfAccountStatus status;

    @ValidateNaturalText
    private String designationName;

    @ValidateNaturalText
    private String departmentName;

    @ValidateNaturalText
    private String unitName;

    @ValidateNaturalText
    private String accHolderName;

    @ValidateNumeric
    private String pin;

    private LocalDate dateOfJoining;

    private LocalDate dateOfConfirmation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPfCode() {
        return pfCode;
    }

    public void setPfCode(String pfCode) {
        this.pfCode = pfCode;
    }

    public LocalDate getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public LocalDate getMembershipEndDate() {
        return membershipEndDate;
    }

    public void setMembershipEndDate(LocalDate membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }

    public PfAccountStatus getStatus() {
        return status;
    }

    public void setStatus(PfAccountStatus status) {
        this.status = status;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAccHolderName() {
        return accHolderName;
    }

    public void setAccHolderName(String accHolderName) {
        this.accHolderName = accHolderName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDateOfConfirmation() {
        return dateOfConfirmation;
    }

    public void setDateOfConfirmation(LocalDate dateOfConfirmation) {
        this.dateOfConfirmation = dateOfConfirmation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PfAccountDTO)) {
            return false;
        }

        return id != null && id.equals(((PfAccountDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfAccountDTO{" +
            "id=" + getId() +
            ", pfCode='" + getPfCode() + "'" +
            ", membershipStartDate='" + getMembershipStartDate() + "'" +
            ", membershipEndDate='" + getMembershipEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", designationName='" + getDesignationName() + "'" +
            ", departmentName='" + getDepartmentName() + "'" +
            ", unitName='" + getUnitName() + "'" +
            ", accHolderName='" + getAccHolderName() + "'" +
            ", pin='" + getPin() + "'" +
            ", dateOfJoining='" + getDateOfJoining() + "'" +
            ", dateOfConfirmation='" + getDateOfConfirmation() + "'" +
            "}";
    }
}
