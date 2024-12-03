import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFinalSettlement, NewFinalSettlement } from '../final-settlement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFinalSettlement for edit and NewFinalSettlementFormGroupInput for create.
 */
type FinalSettlementFormGroupInput = IFinalSettlement | PartialWithRequiredKeyOf<NewFinalSettlement>;

type FinalSettlementFormDefaults = Pick<NewFinalSettlement, 'id' | 'isFinalized'>;

type FinalSettlementFormGroupContent = {
  id: FormControl<IFinalSettlement['id'] | NewFinalSettlement['id']>;
  dateOfResignation: FormControl<IFinalSettlement['dateOfResignation']>;
  noticePeriod: FormControl<IFinalSettlement['noticePeriod']>;
  lastWorkingDay: FormControl<IFinalSettlement['lastWorkingDay']>;
  dateOfRelease: FormControl<IFinalSettlement['dateOfRelease']>;
  serviceTenure: FormControl<IFinalSettlement['serviceTenure']>;
  mBasic: FormControl<IFinalSettlement['mBasic']>;
  mHouseRent: FormControl<IFinalSettlement['mHouseRent']>;
  mMedical: FormControl<IFinalSettlement['mMedical']>;
  mConveyance: FormControl<IFinalSettlement['mConveyance']>;
  salaryPayable: FormControl<IFinalSettlement['salaryPayable']>;
  salaryPayableRemarks: FormControl<IFinalSettlement['salaryPayableRemarks']>;
  totalDaysForLeaveEncashment: FormControl<IFinalSettlement['totalDaysForLeaveEncashment']>;
  totalLeaveEncashment: FormControl<IFinalSettlement['totalLeaveEncashment']>;
  mobileBillInCash: FormControl<IFinalSettlement['mobileBillInCash']>;
  allowance01Name: FormControl<IFinalSettlement['allowance01Name']>;
  allowance01Amount: FormControl<IFinalSettlement['allowance01Amount']>;
  allowance01Remarks: FormControl<IFinalSettlement['allowance01Remarks']>;
  allowance02Name: FormControl<IFinalSettlement['allowance02Name']>;
  allowance02Amount: FormControl<IFinalSettlement['allowance02Amount']>;
  allowance02Remarks: FormControl<IFinalSettlement['allowance02Remarks']>;
  allowance03Name: FormControl<IFinalSettlement['allowance03Name']>;
  allowance03Amount: FormControl<IFinalSettlement['allowance03Amount']>;
  allowance03Remarks: FormControl<IFinalSettlement['allowance03Remarks']>;
  allowance04Name: FormControl<IFinalSettlement['allowance04Name']>;
  allowance04Amount: FormControl<IFinalSettlement['allowance04Amount']>;
  allowance04Remarks: FormControl<IFinalSettlement['allowance04Remarks']>;
  deductionNoticePay: FormControl<IFinalSettlement['deductionNoticePay']>;
  deductionPf: FormControl<IFinalSettlement['deductionPf']>;
  deductionHaf: FormControl<IFinalSettlement['deductionHaf']>;
  deductionExcessCellBill: FormControl<IFinalSettlement['deductionExcessCellBill']>;
  deductionAbsentDaysAdjustment: FormControl<IFinalSettlement['deductionAbsentDaysAdjustment']>;
  totalSalaryPayable: FormControl<IFinalSettlement['totalSalaryPayable']>;
  deductionAnnualIncomeTax: FormControl<IFinalSettlement['deductionAnnualIncomeTax']>;
  netSalaryPayable: FormControl<IFinalSettlement['netSalaryPayable']>;
  totalPayablePf: FormControl<IFinalSettlement['totalPayablePf']>;
  totalPayableGf: FormControl<IFinalSettlement['totalPayableGf']>;
  totalFinalSettlementAmount: FormControl<IFinalSettlement['totalFinalSettlementAmount']>;
  createdAt: FormControl<IFinalSettlement['createdAt']>;
  updatedAt: FormControl<IFinalSettlement['updatedAt']>;
  deductionNoticePayDays: FormControl<IFinalSettlement['deductionNoticePayDays']>;
  deductionAbsentDaysAdjustmentDays: FormControl<IFinalSettlement['deductionAbsentDaysAdjustmentDays']>;
  deductionOther: FormControl<IFinalSettlement['deductionOther']>;
  totalSalary: FormControl<IFinalSettlement['totalSalary']>;
  totalGrossSalary: FormControl<IFinalSettlement['totalGrossSalary']>;
  totalDeduction: FormControl<IFinalSettlement['totalDeduction']>;
  finalSettlementDate: FormControl<IFinalSettlement['finalSettlementDate']>;
  isFinalized: FormControl<IFinalSettlement['isFinalized']>;
  salaryNumOfMonth: FormControl<IFinalSettlement['salaryNumOfMonth']>;
  remarks: FormControl<IFinalSettlement['remarks']>;
  employeeId: FormControl<IFinalSettlement['employeeId']>;
  createdById: FormControl<IFinalSettlement['createdById']>;
  updatedById: FormControl<IFinalSettlement['updatedById']>;
};

export type FinalSettlementFormGroup = FormGroup<FinalSettlementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FinalSettlementFormService {
  createFinalSettlementFormGroup(finalSettlement: FinalSettlementFormGroupInput = { id: null }): FinalSettlementFormGroup {
    const finalSettlementRawValue = {
      ...this.getFormDefaults(),
      ...finalSettlement,
    };
    return new FormGroup<FinalSettlementFormGroupContent>({
      id: new FormControl(
        { value: finalSettlementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dateOfResignation: new FormControl(finalSettlementRawValue.dateOfResignation),
      noticePeriod: new FormControl(finalSettlementRawValue.noticePeriod),
      lastWorkingDay: new FormControl(finalSettlementRawValue.lastWorkingDay),
      dateOfRelease: new FormControl(finalSettlementRawValue.dateOfRelease),
      serviceTenure: new FormControl(finalSettlementRawValue.serviceTenure),
      mBasic: new FormControl(finalSettlementRawValue.mBasic),
      mHouseRent: new FormControl(finalSettlementRawValue.mHouseRent),
      mMedical: new FormControl(finalSettlementRawValue.mMedical),
      mConveyance: new FormControl(finalSettlementRawValue.mConveyance),
      salaryPayable: new FormControl(finalSettlementRawValue.salaryPayable),
      salaryPayableRemarks: new FormControl(finalSettlementRawValue.salaryPayableRemarks),
      totalDaysForLeaveEncashment: new FormControl(finalSettlementRawValue.totalDaysForLeaveEncashment),
      totalLeaveEncashment: new FormControl(finalSettlementRawValue.totalLeaveEncashment),
      mobileBillInCash: new FormControl(finalSettlementRawValue.mobileBillInCash),
      allowance01Name: new FormControl(finalSettlementRawValue.allowance01Name),
      allowance01Amount: new FormControl(finalSettlementRawValue.allowance01Amount),
      allowance01Remarks: new FormControl(finalSettlementRawValue.allowance01Remarks),
      allowance02Name: new FormControl(finalSettlementRawValue.allowance02Name),
      allowance02Amount: new FormControl(finalSettlementRawValue.allowance02Amount),
      allowance02Remarks: new FormControl(finalSettlementRawValue.allowance02Remarks),
      allowance03Name: new FormControl(finalSettlementRawValue.allowance03Name),
      allowance03Amount: new FormControl(finalSettlementRawValue.allowance03Amount),
      allowance03Remarks: new FormControl(finalSettlementRawValue.allowance03Remarks),
      allowance04Name: new FormControl(finalSettlementRawValue.allowance04Name),
      allowance04Amount: new FormControl(finalSettlementRawValue.allowance04Amount),
      allowance04Remarks: new FormControl(finalSettlementRawValue.allowance04Remarks),
      deductionNoticePay: new FormControl(finalSettlementRawValue.deductionNoticePay),
      deductionPf: new FormControl(finalSettlementRawValue.deductionPf),
      deductionHaf: new FormControl(finalSettlementRawValue.deductionHaf),
      deductionExcessCellBill: new FormControl(finalSettlementRawValue.deductionExcessCellBill),
      deductionAbsentDaysAdjustment: new FormControl(finalSettlementRawValue.deductionAbsentDaysAdjustment),
      totalSalaryPayable: new FormControl(finalSettlementRawValue.totalSalaryPayable),
      deductionAnnualIncomeTax: new FormControl(finalSettlementRawValue.deductionAnnualIncomeTax),
      netSalaryPayable: new FormControl(finalSettlementRawValue.netSalaryPayable),
      totalPayablePf: new FormControl(finalSettlementRawValue.totalPayablePf),
      totalPayableGf: new FormControl(finalSettlementRawValue.totalPayableGf),
      totalFinalSettlementAmount: new FormControl(finalSettlementRawValue.totalFinalSettlementAmount),
      createdAt: new FormControl(finalSettlementRawValue.createdAt),
      updatedAt: new FormControl(finalSettlementRawValue.updatedAt),
      deductionNoticePayDays: new FormControl(finalSettlementRawValue.deductionNoticePayDays),
      deductionAbsentDaysAdjustmentDays: new FormControl(finalSettlementRawValue.deductionAbsentDaysAdjustmentDays),
      deductionOther: new FormControl(finalSettlementRawValue.deductionOther),
      totalSalary: new FormControl(finalSettlementRawValue.totalSalary),
      totalGrossSalary: new FormControl(finalSettlementRawValue.totalGrossSalary),
      totalDeduction: new FormControl(finalSettlementRawValue.totalDeduction),
      finalSettlementDate: new FormControl(finalSettlementRawValue.finalSettlementDate),
      isFinalized: new FormControl(finalSettlementRawValue.isFinalized),
      salaryNumOfMonth: new FormControl(finalSettlementRawValue.salaryNumOfMonth),
      remarks: new FormControl(finalSettlementRawValue.remarks),
      employeeId: new FormControl(finalSettlementRawValue.employeeId, {
        validators: [Validators.required],
      }),
      createdById: new FormControl(finalSettlementRawValue.createdById),
      updatedById: new FormControl(finalSettlementRawValue.updatedById),
    });
  }

  getFinalSettlement(form: FinalSettlementFormGroup): IFinalSettlement | NewFinalSettlement {
    return form.getRawValue() as IFinalSettlement | NewFinalSettlement;
  }

  resetForm(form: FinalSettlementFormGroup, finalSettlement: FinalSettlementFormGroupInput): void {
    const finalSettlementRawValue = { ...this.getFormDefaults(), ...finalSettlement };
    form.reset(
      {
        ...finalSettlementRawValue,
        id: { value: finalSettlementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FinalSettlementFormDefaults {
    return {
      id: null,
      isFinalized: false,
    };
  }
}
