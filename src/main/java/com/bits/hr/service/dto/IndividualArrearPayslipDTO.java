package com.bits.hr.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualArrearPayslipDTO implements Serializable {

    private Long id;
    private LocalDate effectiveDate;
    private String title;
    private String titleEffectiveFrom;
    private String arrearRemarks;

    private String pin;
    private String fullName;
    private String designationName;
    private String departmentName;
    private String unitName;
    private LocalDate joiningDate;
    private String bankName;
    private String bankAccountNo;

    private Double basic;
    private Double houseRent;
    private Double medical;
    private Double conveyance;

    private Double grossPay;
    private Double festivalBonus;
    private Double livingAllowance;
    private Double otherAddition;
    private Double salaryAdjustment;

    private Double taxDeduction;
    private Double arrearPfDeduction;

    private Double totalAddition;
    private Double totalDeduction;

    private Double netPayable;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndividualArrearPayslipDTO)) {
            return false;
        }

        return id != null && id.equals(((IndividualArrearPayslipDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "IndividualArrearPayslipDTO{" +
            "id=" + id +
            ", effectiveDate=" + effectiveDate +
            ", title='" + title + '\'' +
            ", titleEffectiveFrom='" + titleEffectiveFrom + '\'' +
            ", arrearRemarks='" + arrearRemarks + '\'' +
            ", pin='" + pin + '\'' +
            ", fullName='" + fullName + '\'' +
            ", designationName='" + designationName + '\'' +
            ", departmentName='" + departmentName + '\'' +
            ", unitName='" + unitName + '\'' +
            ", joiningDate=" + joiningDate +
            ", bankName='" + bankName + '\'' +
            ", bankAccountNo='" + bankAccountNo + '\'' +
            ", basic=" + basic +
            ", houseRent=" + houseRent +
            ", medical=" + medical +
            ", conveyance=" + conveyance +
            ", grossPay=" + grossPay +
            ", festivalBonus=" + festivalBonus +
            ", livingAllowance=" + livingAllowance +
            ", otherAddition=" + otherAddition +
            ", salaryAdjustment=" + salaryAdjustment +
            ", taxDeduction=" + taxDeduction +
            ", arrearPfDeduction=" + arrearPfDeduction +
            ", totalAddition=" + totalAddition +
            ", totalDeduction=" + totalDeduction +
            ", netPayable=" + netPayable +
            '}';
    }
}
