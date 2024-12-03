package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.Visibility;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SalaryGeneratorMaster.
 */
@Entity
@Table(name = "salary_generator_master")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalaryGeneratorMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "year")
    private String year;

    @Column(name = "month")
    private String month;

    @Column(name = "is_generated")
    private Boolean isGenerated;

    @Column(name = "is_mobile_bill_imported")
    private Boolean isMobileBillImported;

    @Column(name = "is_pf_loan_repayment_imported")
    private Boolean isPFLoanRepaymentImported;

    @Column(name = "is_attendance_imported")
    private Boolean isAttendanceImported;

    @Column(name = "is_salary_deduction_imported")
    private Boolean isSalaryDeductionImported;

    @Column(name = "is_finalized")
    private Boolean isFinalized;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility visibility;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SalaryGeneratorMaster id(Long id) {
        this.setId(id);
        return this;
    }

    public SalaryGeneratorMaster year(String year) {
        this.setYear(year);
        return this;
    }

    public SalaryGeneratorMaster month(String month) {
        this.setMonth(month);
        return this;
    }

    public SalaryGeneratorMaster isGenerated(Boolean isGenerated) {
        this.setIsGenerated(isGenerated);
        return this;
    }

    public SalaryGeneratorMaster isMobileBillImported(Boolean isMobileBillImported) {
        this.setIsMobileBillImported(isMobileBillImported);
        return this;
    }

    public SalaryGeneratorMaster isPFLoanRepaymentImported(Boolean isPFLoanRepaymentImported) {
        this.setIsPFLoanRepaymentImported(isPFLoanRepaymentImported);
        return this;
    }

    public SalaryGeneratorMaster isAttendanceImported(Boolean isAttendanceImported) {
        this.setIsAttendanceImported(isAttendanceImported);
        return this;
    }

    public SalaryGeneratorMaster isSalaryDeductionImported(Boolean isSalaryDeductionImported) {
        this.setIsSalaryDeductionImported(isSalaryDeductionImported);
        return this;
    }

    public SalaryGeneratorMaster isFinalized(Boolean isFinalized) {
        this.setIsFinalized(isFinalized);
        return this;
    }

    public SalaryGeneratorMaster visibility(Visibility visibility) {
        this.setVisibility(visibility);
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
        if (!(o instanceof SalaryGeneratorMaster)) {
            return false;
        }
        return id != null && id.equals(((SalaryGeneratorMaster) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryGeneratorMaster{" +
            "id=" + getId() +
            ", year='" + getYear() + "'" +
            ", month='" + getMonth() + "'" +
            ", isGenerated='" + getIsGenerated() + "'" +
            ", isMobileBillImported='" + getIsMobileBillImported() + "'" +
            ", isPFLoanRepaymentImported='" + getIsPFLoanRepaymentImported() + "'" +
            ", isAttendanceImported='" + getIsAttendanceImported() + "'" +
            ", isSalaryDeductionImported='" + getIsSalaryDeductionImported() + "'" +
            ", isFinalized='" + getIsFinalized() + "'" +
            ", visibility='" + getVisibility() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return this.month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Boolean getIsGenerated() {
        return this.isGenerated;
    }

    public void setIsGenerated(Boolean isGenerated) {
        this.isGenerated = isGenerated;
    }

    public Boolean getIsMobileBillImported() {
        return this.isMobileBillImported;
    }

    public void setIsMobileBillImported(Boolean isMobileBillImported) {
        this.isMobileBillImported = isMobileBillImported;
    }

    public Boolean getIsPFLoanRepaymentImported() {
        return this.isPFLoanRepaymentImported;
    }

    public void setIsPFLoanRepaymentImported(Boolean isPFLoanRepaymentImported) {
        this.isPFLoanRepaymentImported = isPFLoanRepaymentImported;
    }

    public Boolean getIsAttendanceImported() {
        return this.isAttendanceImported;
    }

    public void setIsAttendanceImported(Boolean isAttendanceImported) {
        this.isAttendanceImported = isAttendanceImported;
    }

    public Boolean getIsSalaryDeductionImported() {
        return this.isSalaryDeductionImported;
    }

    public void setIsSalaryDeductionImported(Boolean isSalaryDeductionImported) {
        this.isSalaryDeductionImported = isSalaryDeductionImported;
    }

    public Boolean getIsFinalized() {
        return this.isFinalized;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setIsFinalized(Boolean isFinalized) {
        this.isFinalized = isFinalized;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public int yearAsInt() {
        return (int) Double.parseDouble(year);
    }

    public int monthAsInt() {
        if (month != null) {
            return (int) Double.parseDouble(this.month);
        }
        return 0;
    }
}
