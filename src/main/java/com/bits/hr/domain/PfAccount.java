package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.PfAccountStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PfAccount.
 */
@Entity
@Table(name = "pf_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PfAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "pf_code")
    private String pfCode;

    @Column(name = "membership_start_date")
    private LocalDate membershipStartDate;

    @Column(name = "membership_end_date")
    private LocalDate membershipEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PfAccountStatus status;

    @Column(name = "designation_name")
    private String designationName;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "acc_holder_name")
    private String accHolderName;

    @Column(name = "pin")
    private String pin;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Column(name = "date_of_confirmation")
    private LocalDate dateOfConfirmation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PfAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public PfAccount pfCode(String pfCode) {
        this.setPfCode(pfCode);
        return this;
    }

    public PfAccount membershipStartDate(LocalDate membershipStartDate) {
        this.setMembershipStartDate(membershipStartDate);
        return this;
    }

    public PfAccount membershipEndDate(LocalDate membershipEndDate) {
        this.setMembershipEndDate(membershipEndDate);
        return this;
    }

    public PfAccount status(PfAccountStatus status) {
        this.setStatus(status);
        return this;
    }

    public PfAccount designationName(String designationName) {
        this.setDesignationName(designationName);
        return this;
    }

    public PfAccount departmentName(String departmentName) {
        this.setDepartmentName(departmentName);
        return this;
    }

    public PfAccount unitName(String unitName) {
        this.setUnitName(unitName);
        return this;
    }

    public PfAccount accHolderName(String accHolderName) {
        this.setAccHolderName(accHolderName);
        return this;
    }

    public PfAccount pin(String pin) {
        this.setPin(pin);
        return this;
    }

    public PfAccount dateOfJoining(LocalDate dateOfJoining) {
        this.setDateOfJoining(dateOfJoining);
        return this;
    }

    public PfAccount dateOfConfirmation(LocalDate dateOfConfirmation) {
        this.setDateOfConfirmation(dateOfConfirmation);
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
        if (!(o instanceof PfAccount)) {
            return false;
        }
        return id != null && id.equals(((PfAccount) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfAccount{" +
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPfCode() {
        return this.pfCode;
    }

    public void setPfCode(String pfCode) {
        this.pfCode = pfCode;
    }

    public LocalDate getMembershipStartDate() {
        return this.membershipStartDate;
    }

    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public LocalDate getMembershipEndDate() {
        return this.membershipEndDate;
    }

    public void setMembershipEndDate(LocalDate membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }

    public PfAccountStatus getStatus() {
        return this.status;
    }

    public void setStatus(PfAccountStatus status) {
        this.status = status;
    }

    public String getDesignationName() {
        return this.designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAccHolderName() {
        return this.accHolderName;
    }

    public void setAccHolderName(String accHolderName) {
        this.accHolderName = accHolderName;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getDateOfJoining() {
        return this.dateOfJoining;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDateOfConfirmation() {
        return this.dateOfConfirmation;
    }

    public void setDateOfConfirmation(LocalDate dateOfConfirmation) {
        this.dateOfConfirmation = dateOfConfirmation;
    }
}
