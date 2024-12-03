import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployeeSalaryTempData, NewEmployeeSalaryTempData } from '../employee-salary-temp-data.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeSalaryTempData for edit and NewEmployeeSalaryTempDataFormGroupInput for create.
 */
type EmployeeSalaryTempDataFormGroupInput = IEmployeeSalaryTempData | PartialWithRequiredKeyOf<NewEmployeeSalaryTempData>;

type EmployeeSalaryTempDataFormDefaults = Pick<NewEmployeeSalaryTempData, 'id'>;

type EmployeeSalaryTempDataFormGroupContent = {
  id: FormControl<IEmployeeSalaryTempData['id'] | NewEmployeeSalaryTempData['id']>;
  month: FormControl<IEmployeeSalaryTempData['month']>;
  year: FormControl<IEmployeeSalaryTempData['year']>;
  mainGrossSalary: FormControl<IEmployeeSalaryTempData['mainGrossSalary']>;
  mainGrossBasicSalary: FormControl<IEmployeeSalaryTempData['mainGrossBasicSalary']>;
  mainGrossHouseRent: FormControl<IEmployeeSalaryTempData['mainGrossHouseRent']>;
  mainGrossMedicalAllowance: FormControl<IEmployeeSalaryTempData['mainGrossMedicalAllowance']>;
  mainGrossConveyanceAllowance: FormControl<IEmployeeSalaryTempData['mainGrossConveyanceAllowance']>;
  absentDays: FormControl<IEmployeeSalaryTempData['absentDays']>;
  fractionDays: FormControl<IEmployeeSalaryTempData['fractionDays']>;
  payableGrossSalary: FormControl<IEmployeeSalaryTempData['payableGrossSalary']>;
  payableGrossBasicSalary: FormControl<IEmployeeSalaryTempData['payableGrossBasicSalary']>;
  payableGrossHouseRent: FormControl<IEmployeeSalaryTempData['payableGrossHouseRent']>;
  payableGrossMedicalAllowance: FormControl<IEmployeeSalaryTempData['payableGrossMedicalAllowance']>;
  payableGrossConveyanceAllowance: FormControl<IEmployeeSalaryTempData['payableGrossConveyanceAllowance']>;
  arrearSalary: FormControl<IEmployeeSalaryTempData['arrearSalary']>;
  pfDeduction: FormControl<IEmployeeSalaryTempData['pfDeduction']>;
  taxDeduction: FormControl<IEmployeeSalaryTempData['taxDeduction']>;
  welfareFundDeduction: FormControl<IEmployeeSalaryTempData['welfareFundDeduction']>;
  mobileBillDeduction: FormControl<IEmployeeSalaryTempData['mobileBillDeduction']>;
  otherDeduction: FormControl<IEmployeeSalaryTempData['otherDeduction']>;
  totalDeduction: FormControl<IEmployeeSalaryTempData['totalDeduction']>;
  netPay: FormControl<IEmployeeSalaryTempData['netPay']>;
  remarks: FormControl<IEmployeeSalaryTempData['remarks']>;
  pfContribution: FormControl<IEmployeeSalaryTempData['pfContribution']>;
  gfContribution: FormControl<IEmployeeSalaryTempData['gfContribution']>;
  provisionForFestivalBonus: FormControl<IEmployeeSalaryTempData['provisionForFestivalBonus']>;
  provisionForLeaveEncashment: FormControl<IEmployeeSalaryTempData['provisionForLeaveEncashment']>;
  provishionForProjectBonus: FormControl<IEmployeeSalaryTempData['provishionForProjectBonus']>;
  livingAllowance: FormControl<IEmployeeSalaryTempData['livingAllowance']>;
  otherAddition: FormControl<IEmployeeSalaryTempData['otherAddition']>;
  salaryAdjustment: FormControl<IEmployeeSalaryTempData['salaryAdjustment']>;
  providentFundArrear: FormControl<IEmployeeSalaryTempData['providentFundArrear']>;
  entertainment: FormControl<IEmployeeSalaryTempData['entertainment']>;
  utility: FormControl<IEmployeeSalaryTempData['utility']>;
  employee: FormControl<IEmployeeSalaryTempData['employee']>;
};

export type EmployeeSalaryTempDataFormGroup = FormGroup<EmployeeSalaryTempDataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryTempDataFormService {
  createEmployeeSalaryTempDataFormGroup(
    employeeSalaryTempData: EmployeeSalaryTempDataFormGroupInput = { id: null }
  ): EmployeeSalaryTempDataFormGroup {
    const employeeSalaryTempDataRawValue = {
      ...this.getFormDefaults(),
      ...employeeSalaryTempData,
    };
    return new FormGroup<EmployeeSalaryTempDataFormGroupContent>({
      id: new FormControl(
        { value: employeeSalaryTempDataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      month: new FormControl(employeeSalaryTempDataRawValue.month),
      year: new FormControl(employeeSalaryTempDataRawValue.year),
      mainGrossSalary: new FormControl(employeeSalaryTempDataRawValue.mainGrossSalary),
      mainGrossBasicSalary: new FormControl(employeeSalaryTempDataRawValue.mainGrossBasicSalary),
      mainGrossHouseRent: new FormControl(employeeSalaryTempDataRawValue.mainGrossHouseRent),
      mainGrossMedicalAllowance: new FormControl(employeeSalaryTempDataRawValue.mainGrossMedicalAllowance),
      mainGrossConveyanceAllowance: new FormControl(employeeSalaryTempDataRawValue.mainGrossConveyanceAllowance),
      absentDays: new FormControl(employeeSalaryTempDataRawValue.absentDays),
      fractionDays: new FormControl(employeeSalaryTempDataRawValue.fractionDays),
      payableGrossSalary: new FormControl(employeeSalaryTempDataRawValue.payableGrossSalary),
      payableGrossBasicSalary: new FormControl(employeeSalaryTempDataRawValue.payableGrossBasicSalary),
      payableGrossHouseRent: new FormControl(employeeSalaryTempDataRawValue.payableGrossHouseRent),
      payableGrossMedicalAllowance: new FormControl(employeeSalaryTempDataRawValue.payableGrossMedicalAllowance),
      payableGrossConveyanceAllowance: new FormControl(employeeSalaryTempDataRawValue.payableGrossConveyanceAllowance),
      arrearSalary: new FormControl(employeeSalaryTempDataRawValue.arrearSalary),
      pfDeduction: new FormControl(employeeSalaryTempDataRawValue.pfDeduction),
      taxDeduction: new FormControl(employeeSalaryTempDataRawValue.taxDeduction),
      welfareFundDeduction: new FormControl(employeeSalaryTempDataRawValue.welfareFundDeduction),
      mobileBillDeduction: new FormControl(employeeSalaryTempDataRawValue.mobileBillDeduction),
      otherDeduction: new FormControl(employeeSalaryTempDataRawValue.otherDeduction),
      totalDeduction: new FormControl(employeeSalaryTempDataRawValue.totalDeduction),
      netPay: new FormControl(employeeSalaryTempDataRawValue.netPay),
      remarks: new FormControl(employeeSalaryTempDataRawValue.remarks),
      pfContribution: new FormControl(employeeSalaryTempDataRawValue.pfContribution),
      gfContribution: new FormControl(employeeSalaryTempDataRawValue.gfContribution),
      provisionForFestivalBonus: new FormControl(employeeSalaryTempDataRawValue.provisionForFestivalBonus),
      provisionForLeaveEncashment: new FormControl(employeeSalaryTempDataRawValue.provisionForLeaveEncashment),
      provishionForProjectBonus: new FormControl(employeeSalaryTempDataRawValue.provishionForProjectBonus),
      livingAllowance: new FormControl(employeeSalaryTempDataRawValue.livingAllowance),
      otherAddition: new FormControl(employeeSalaryTempDataRawValue.otherAddition),
      salaryAdjustment: new FormControl(employeeSalaryTempDataRawValue.salaryAdjustment),
      providentFundArrear: new FormControl(employeeSalaryTempDataRawValue.providentFundArrear),
      entertainment: new FormControl(employeeSalaryTempDataRawValue.entertainment),
      utility: new FormControl(employeeSalaryTempDataRawValue.utility),
      employee: new FormControl(employeeSalaryTempDataRawValue.employee),
    });
  }

  getEmployeeSalaryTempData(form: EmployeeSalaryTempDataFormGroup): IEmployeeSalaryTempData | NewEmployeeSalaryTempData {
    return form.getRawValue() as IEmployeeSalaryTempData | NewEmployeeSalaryTempData;
  }

  resetForm(form: EmployeeSalaryTempDataFormGroup, employeeSalaryTempData: EmployeeSalaryTempDataFormGroupInput): void {
    const employeeSalaryTempDataRawValue = { ...this.getFormDefaults(), ...employeeSalaryTempData };
    form.reset(
      {
        ...employeeSalaryTempDataRawValue,
        id: { value: employeeSalaryTempDataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeSalaryTempDataFormDefaults {
    return {
      id: null,
    };
  }
}
