import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISalaryGeneratorMaster, NewSalaryGeneratorMaster } from '../salary-generator-master.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISalaryGeneratorMaster for edit and NewSalaryGeneratorMasterFormGroupInput for create.
 */
type SalaryGeneratorMasterFormGroupInput = ISalaryGeneratorMaster | PartialWithRequiredKeyOf<NewSalaryGeneratorMaster>;

type SalaryGeneratorMasterFormDefaults = Pick<
  NewSalaryGeneratorMaster,
  | 'id'
  | 'isGenerated'
  | 'isMobileBillImported'
  | 'isPFLoanRepaymentImported'
  | 'isAttendanceImported'
  | 'isSalaryDeductionImported'
  | 'isFinalized'
>;

type SalaryGeneratorMasterFormGroupContent = {
  id: FormControl<ISalaryGeneratorMaster['id'] | NewSalaryGeneratorMaster['id']>;
  year: FormControl<ISalaryGeneratorMaster['year']>;
  month: FormControl<ISalaryGeneratorMaster['month']>;
  isGenerated: FormControl<ISalaryGeneratorMaster['isGenerated']>;
  isMobileBillImported: FormControl<ISalaryGeneratorMaster['isMobileBillImported']>;
  isPFLoanRepaymentImported: FormControl<ISalaryGeneratorMaster['isPFLoanRepaymentImported']>;
  isAttendanceImported: FormControl<ISalaryGeneratorMaster['isAttendanceImported']>;
  isSalaryDeductionImported: FormControl<ISalaryGeneratorMaster['isSalaryDeductionImported']>;
  isFinalized: FormControl<ISalaryGeneratorMaster['isFinalized']>;
  visibility: FormControl<ISalaryGeneratorMaster['visibility']>;
};

export type SalaryGeneratorMasterFormGroup = FormGroup<SalaryGeneratorMasterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SalaryGeneratorMasterFormService {
  createSalaryGeneratorMasterFormGroup(
    salaryGeneratorMaster: SalaryGeneratorMasterFormGroupInput = { id: null }
  ): SalaryGeneratorMasterFormGroup {
    const salaryGeneratorMasterRawValue = {
      ...this.getFormDefaults(),
      ...salaryGeneratorMaster,
    };
    return new FormGroup<SalaryGeneratorMasterFormGroupContent>({
      id: new FormControl(
        { value: salaryGeneratorMasterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      year: new FormControl(salaryGeneratorMasterRawValue.year),
      month: new FormControl(salaryGeneratorMasterRawValue.month),
      isGenerated: new FormControl(salaryGeneratorMasterRawValue.isGenerated),
      isMobileBillImported: new FormControl(salaryGeneratorMasterRawValue.isMobileBillImported),
      isPFLoanRepaymentImported: new FormControl(salaryGeneratorMasterRawValue.isPFLoanRepaymentImported),
      isAttendanceImported: new FormControl(salaryGeneratorMasterRawValue.isAttendanceImported),
      isSalaryDeductionImported: new FormControl(salaryGeneratorMasterRawValue.isSalaryDeductionImported),
      isFinalized: new FormControl(salaryGeneratorMasterRawValue.isFinalized),
      visibility: new FormControl(salaryGeneratorMasterRawValue.visibility),
    });
  }

  getSalaryGeneratorMaster(form: SalaryGeneratorMasterFormGroup): ISalaryGeneratorMaster | NewSalaryGeneratorMaster {
    return form.getRawValue() as ISalaryGeneratorMaster | NewSalaryGeneratorMaster;
  }

  resetForm(form: SalaryGeneratorMasterFormGroup, salaryGeneratorMaster: SalaryGeneratorMasterFormGroupInput): void {
    const salaryGeneratorMasterRawValue = { ...this.getFormDefaults(), ...salaryGeneratorMaster };
    form.reset(
      {
        ...salaryGeneratorMasterRawValue,
        id: { value: salaryGeneratorMasterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SalaryGeneratorMasterFormDefaults {
    return {
      id: null,
      isGenerated: false,
      isMobileBillImported: false,
      isPFLoanRepaymentImported: false,
      isAttendanceImported: false,
      isSalaryDeductionImported: false,
      isFinalized: false,
    };
  }
}
