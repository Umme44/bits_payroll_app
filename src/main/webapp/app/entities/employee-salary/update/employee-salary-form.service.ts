import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployeeSalary, NewEmployeeSalary } from '../employee-salary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeSalary for edit and NewEmployeeSalaryFormGroupInput for create.
 */
type EmployeeSalaryFormGroupInput = IEmployeeSalary | PartialWithRequiredKeyOf<NewEmployeeSalary>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployeeSalary | NewEmployeeSalary> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type EmployeeSalaryFormRawValue = FormValueOf<IEmployeeSalary>;

type NewEmployeeSalaryFormRawValue = FormValueOf<NewEmployeeSalary>;

type EmployeeSalaryFormDefaults = Pick<
  NewEmployeeSalary,
  'id' | 'createdAt' | 'updatedAt' | 'isFinalized' | 'isDispatched' | 'isHold' | 'isVisibleToEmployee'
>;

type EmployeeSalaryFormGroupContent = {
  id: FormControl<EmployeeSalaryFormRawValue['id'] | NewEmployeeSalary['id']>;
  month: FormControl<EmployeeSalaryFormRawValue['month']>;
  year: FormControl<EmployeeSalaryFormRawValue['year']>;
  salaryGenerationDate: FormControl<EmployeeSalaryFormRawValue['salaryGenerationDate']>;
  createdBy: FormControl<EmployeeSalaryFormRawValue['createdBy']>;
  createdAt: FormControl<EmployeeSalaryFormRawValue['createdAt']>;
  updatedBy: FormControl<EmployeeSalaryFormRawValue['updatedBy']>;
  updatedAt: FormControl<EmployeeSalaryFormRawValue['updatedAt']>;
  refPin: FormControl<EmployeeSalaryFormRawValue['refPin']>;
  pin: FormControl<EmployeeSalaryFormRawValue['pin']>;
  joiningDate: FormControl<EmployeeSalaryFormRawValue['joiningDate']>;
  confirmationDate: FormControl<EmployeeSalaryFormRawValue['confirmationDate']>;
  employeeCategory: FormControl<EmployeeSalaryFormRawValue['employeeCategory']>;
  unit: FormControl<EmployeeSalaryFormRawValue['unit']>;
  department: FormControl<EmployeeSalaryFormRawValue['department']>;
  mainGrossSalary: FormControl<EmployeeSalaryFormRawValue['mainGrossSalary']>;
  mainGrossBasicSalary: FormControl<EmployeeSalaryFormRawValue['mainGrossBasicSalary']>;
  mainGrossHouseRent: FormControl<EmployeeSalaryFormRawValue['mainGrossHouseRent']>;
  mainGrossMedicalAllowance: FormControl<EmployeeSalaryFormRawValue['mainGrossMedicalAllowance']>;
  mainGrossConveyanceAllowance: FormControl<EmployeeSalaryFormRawValue['mainGrossConveyanceAllowance']>;
  absentDays: FormControl<EmployeeSalaryFormRawValue['absentDays']>;
  fractionDays: FormControl<EmployeeSalaryFormRawValue['fractionDays']>;
  payableGrossSalary: FormControl<EmployeeSalaryFormRawValue['payableGrossSalary']>;
  payableGrossBasicSalary: FormControl<EmployeeSalaryFormRawValue['payableGrossBasicSalary']>;
  payableGrossHouseRent: FormControl<EmployeeSalaryFormRawValue['payableGrossHouseRent']>;
  payableGrossMedicalAllowance: FormControl<EmployeeSalaryFormRawValue['payableGrossMedicalAllowance']>;
  payableGrossConveyanceAllowance: FormControl<EmployeeSalaryFormRawValue['payableGrossConveyanceAllowance']>;
  arrearSalary: FormControl<EmployeeSalaryFormRawValue['arrearSalary']>;
  pfDeduction: FormControl<EmployeeSalaryFormRawValue['pfDeduction']>;
  taxDeduction: FormControl<EmployeeSalaryFormRawValue['taxDeduction']>;
  welfareFundDeduction: FormControl<EmployeeSalaryFormRawValue['welfareFundDeduction']>;
  mobileBillDeduction: FormControl<EmployeeSalaryFormRawValue['mobileBillDeduction']>;
  otherDeduction: FormControl<EmployeeSalaryFormRawValue['otherDeduction']>;
  totalDeduction: FormControl<EmployeeSalaryFormRawValue['totalDeduction']>;
  netPay: FormControl<EmployeeSalaryFormRawValue['netPay']>;
  remarks: FormControl<EmployeeSalaryFormRawValue['remarks']>;
  pfContribution: FormControl<EmployeeSalaryFormRawValue['pfContribution']>;
  gfContribution: FormControl<EmployeeSalaryFormRawValue['gfContribution']>;
  provisionForFestivalBonus: FormControl<EmployeeSalaryFormRawValue['provisionForFestivalBonus']>;
  provisionForLeaveEncashment: FormControl<EmployeeSalaryFormRawValue['provisionForLeaveEncashment']>;
  isFinalized: FormControl<EmployeeSalaryFormRawValue['isFinalized']>;
  isDispatched: FormControl<EmployeeSalaryFormRawValue['isDispatched']>;
  entertainment: FormControl<EmployeeSalaryFormRawValue['entertainment']>;
  utility: FormControl<EmployeeSalaryFormRawValue['utility']>;
  otherAddition: FormControl<EmployeeSalaryFormRawValue['otherAddition']>;
  salaryAdjustment: FormControl<EmployeeSalaryFormRawValue['salaryAdjustment']>;
  providentFundArrear: FormControl<EmployeeSalaryFormRawValue['providentFundArrear']>;
  allowance01: FormControl<EmployeeSalaryFormRawValue['allowance01']>;
  allowance02: FormControl<EmployeeSalaryFormRawValue['allowance02']>;
  allowance03: FormControl<EmployeeSalaryFormRawValue['allowance03']>;
  allowance04: FormControl<EmployeeSalaryFormRawValue['allowance04']>;
  allowance05: FormControl<EmployeeSalaryFormRawValue['allowance05']>;
  allowance06: FormControl<EmployeeSalaryFormRawValue['allowance06']>;
  provisionForProjectBonus: FormControl<EmployeeSalaryFormRawValue['provisionForProjectBonus']>;
  isHold: FormControl<EmployeeSalaryFormRawValue['isHold']>;
  attendanceRegularisationStartDate: FormControl<EmployeeSalaryFormRawValue['attendanceRegularisationStartDate']>;
  attendanceRegularisationEndDate: FormControl<EmployeeSalaryFormRawValue['attendanceRegularisationEndDate']>;
  title: FormControl<EmployeeSalaryFormRawValue['title']>;
  isVisibleToEmployee: FormControl<EmployeeSalaryFormRawValue['isVisibleToEmployee']>;
  pfArrear: FormControl<EmployeeSalaryFormRawValue['pfArrear']>;
  taxCalculationSnapshot: FormControl<EmployeeSalaryFormRawValue['taxCalculationSnapshot']>;
  employeeId: FormControl<EmployeeSalaryFormRawValue['employeeId']>;
};

export type EmployeeSalaryFormGroup = FormGroup<EmployeeSalaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryFormService {
  createEmployeeSalaryFormGroup(employeeSalary: EmployeeSalaryFormGroupInput = { id: null }): EmployeeSalaryFormGroup {
    const employeeSalaryRawValue = this.convertEmployeeSalaryToEmployeeSalaryRawValue({
      ...this.getFormDefaults(),
      ...employeeSalary,
    });
    return new FormGroup<EmployeeSalaryFormGroupContent>({
      id: new FormControl(
        { value: employeeSalaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      month: new FormControl(employeeSalaryRawValue.month),
      year: new FormControl(employeeSalaryRawValue.year),
      salaryGenerationDate: new FormControl(employeeSalaryRawValue.salaryGenerationDate),
      createdBy: new FormControl(employeeSalaryRawValue.createdBy),
      createdAt: new FormControl(employeeSalaryRawValue.createdAt),
      updatedBy: new FormControl(employeeSalaryRawValue.updatedBy),
      updatedAt: new FormControl(employeeSalaryRawValue.updatedAt),
      refPin: new FormControl(employeeSalaryRawValue.refPin),
      pin: new FormControl(employeeSalaryRawValue.pin),
      joiningDate: new FormControl(employeeSalaryRawValue.joiningDate),
      confirmationDate: new FormControl(employeeSalaryRawValue.confirmationDate),
      employeeCategory: new FormControl(employeeSalaryRawValue.employeeCategory),
      unit: new FormControl(employeeSalaryRawValue.unit),
      department: new FormControl(employeeSalaryRawValue.department),
      mainGrossSalary: new FormControl(employeeSalaryRawValue.mainGrossSalary),
      mainGrossBasicSalary: new FormControl(employeeSalaryRawValue.mainGrossBasicSalary),
      mainGrossHouseRent: new FormControl(employeeSalaryRawValue.mainGrossHouseRent),
      mainGrossMedicalAllowance: new FormControl(employeeSalaryRawValue.mainGrossMedicalAllowance),
      mainGrossConveyanceAllowance: new FormControl(employeeSalaryRawValue.mainGrossConveyanceAllowance),
      absentDays: new FormControl(employeeSalaryRawValue.absentDays),
      fractionDays: new FormControl(employeeSalaryRawValue.fractionDays),
      payableGrossSalary: new FormControl(employeeSalaryRawValue.payableGrossSalary),
      payableGrossBasicSalary: new FormControl(employeeSalaryRawValue.payableGrossBasicSalary),
      payableGrossHouseRent: new FormControl(employeeSalaryRawValue.payableGrossHouseRent),
      payableGrossMedicalAllowance: new FormControl(employeeSalaryRawValue.payableGrossMedicalAllowance),
      payableGrossConveyanceAllowance: new FormControl(employeeSalaryRawValue.payableGrossConveyanceAllowance),
      arrearSalary: new FormControl(employeeSalaryRawValue.arrearSalary),
      pfDeduction: new FormControl(employeeSalaryRawValue.pfDeduction),
      taxDeduction: new FormControl(employeeSalaryRawValue.taxDeduction),
      welfareFundDeduction: new FormControl(employeeSalaryRawValue.welfareFundDeduction),
      mobileBillDeduction: new FormControl(employeeSalaryRawValue.mobileBillDeduction),
      otherDeduction: new FormControl(employeeSalaryRawValue.otherDeduction),
      totalDeduction: new FormControl(employeeSalaryRawValue.totalDeduction),
      netPay: new FormControl(employeeSalaryRawValue.netPay),
      remarks: new FormControl(employeeSalaryRawValue.remarks),
      pfContribution: new FormControl(employeeSalaryRawValue.pfContribution),
      gfContribution: new FormControl(employeeSalaryRawValue.gfContribution),
      provisionForFestivalBonus: new FormControl(employeeSalaryRawValue.provisionForFestivalBonus),
      provisionForLeaveEncashment: new FormControl(employeeSalaryRawValue.provisionForLeaveEncashment),
      isFinalized: new FormControl(employeeSalaryRawValue.isFinalized),
      isDispatched: new FormControl(employeeSalaryRawValue.isDispatched),
      entertainment: new FormControl(employeeSalaryRawValue.entertainment, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      utility: new FormControl(employeeSalaryRawValue.utility, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      otherAddition: new FormControl(employeeSalaryRawValue.otherAddition),
      salaryAdjustment: new FormControl(employeeSalaryRawValue.salaryAdjustment, {
        validators: [Validators.min(-10000000), Validators.max(10000000)],
      }),
      providentFundArrear: new FormControl(employeeSalaryRawValue.providentFundArrear, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance01: new FormControl(employeeSalaryRawValue.allowance01, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance02: new FormControl(employeeSalaryRawValue.allowance02, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance03: new FormControl(employeeSalaryRawValue.allowance03, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance04: new FormControl(employeeSalaryRawValue.allowance04, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance05: new FormControl(employeeSalaryRawValue.allowance05, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      allowance06: new FormControl(employeeSalaryRawValue.allowance06, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      provisionForProjectBonus: new FormControl(employeeSalaryRawValue.provisionForProjectBonus, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      isHold: new FormControl(employeeSalaryRawValue.isHold),
      attendanceRegularisationStartDate: new FormControl(employeeSalaryRawValue.attendanceRegularisationStartDate),
      attendanceRegularisationEndDate: new FormControl(employeeSalaryRawValue.attendanceRegularisationEndDate),
      title: new FormControl(employeeSalaryRawValue.title),
      isVisibleToEmployee: new FormControl(employeeSalaryRawValue.isVisibleToEmployee),
      pfArrear: new FormControl(employeeSalaryRawValue.pfArrear),
      taxCalculationSnapshot: new FormControl(employeeSalaryRawValue.taxCalculationSnapshot),
      employeeId: new FormControl(employeeSalaryRawValue.employeeId),
    });
  }

  getEmployeeSalary(form: EmployeeSalaryFormGroup): IEmployeeSalary | NewEmployeeSalary {
    return this.convertEmployeeSalaryRawValueToEmployeeSalary(
      form.getRawValue() as EmployeeSalaryFormRawValue | NewEmployeeSalaryFormRawValue
    );
  }

  resetForm(form: EmployeeSalaryFormGroup, employeeSalary: EmployeeSalaryFormGroupInput): void {
    const employeeSalaryRawValue = this.convertEmployeeSalaryToEmployeeSalaryRawValue({ ...this.getFormDefaults(), ...employeeSalary });
    form.reset(
      {
        ...employeeSalaryRawValue,
        id: { value: employeeSalaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeSalaryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
      isFinalized: false,
      isDispatched: false,
      isHold: false,
      isVisibleToEmployee: false,
    };
  }

  private convertEmployeeSalaryRawValueToEmployeeSalary(
    rawEmployeeSalary: EmployeeSalaryFormRawValue | NewEmployeeSalaryFormRawValue
  ): IEmployeeSalary | NewEmployeeSalary {
    return {
      ...rawEmployeeSalary,
      createdAt: dayjs(rawEmployeeSalary.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawEmployeeSalary.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeeSalaryToEmployeeSalaryRawValue(
    employeeSalary: IEmployeeSalary | (Partial<NewEmployeeSalary> & EmployeeSalaryFormDefaults)
  ): EmployeeSalaryFormRawValue | PartialWithRequiredKeyOf<NewEmployeeSalaryFormRawValue> {
    return {
      ...employeeSalary,
      createdAt: employeeSalary.createdAt ? employeeSalary.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: employeeSalary.updatedAt ? employeeSalary.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
