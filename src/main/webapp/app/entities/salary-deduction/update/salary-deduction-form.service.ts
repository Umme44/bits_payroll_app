import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISalaryDeduction, NewSalaryDeduction } from '../salary-deduction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISalaryDeduction for edit and NewSalaryDeductionFormGroupInput for create.
 */
type SalaryDeductionFormGroupInput = ISalaryDeduction | PartialWithRequiredKeyOf<NewSalaryDeduction>;

type SalaryDeductionFormDefaults = Pick<NewSalaryDeduction, 'id'>;

type SalaryDeductionFormGroupContent = {
  id: FormControl<ISalaryDeduction['id'] | NewSalaryDeduction['id']>;
  deductionAmount: FormControl<ISalaryDeduction['deductionAmount']>;
  deductionYear: FormControl<ISalaryDeduction['deductionYear']>;
  deductionMonth: FormControl<ISalaryDeduction['deductionMonth']>;
  deductionTypeId: FormControl<ISalaryDeduction['deductionTypeId']>;
  employeeId: FormControl<ISalaryDeduction['employeeId']>;
};

export type SalaryDeductionFormGroup = FormGroup<SalaryDeductionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SalaryDeductionFormService {
  createSalaryDeductionFormGroup(salaryDeduction: SalaryDeductionFormGroupInput = { id: null }): SalaryDeductionFormGroup {
    const salaryDeductionRawValue = {
      ...this.getFormDefaults(),
      ...salaryDeduction,
    };
    return new FormGroup<SalaryDeductionFormGroupContent>({
      id: new FormControl(
        { value: salaryDeductionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      deductionAmount: new FormControl(salaryDeductionRawValue.deductionAmount, {
        validators: [Validators.required, Validators.min(0), Validators.max(1000000)],
      }),
      deductionYear: new FormControl(salaryDeductionRawValue.deductionYear, {
        validators: [Validators.required, Validators.min(1990), Validators.max(2100)],
      }),
      deductionMonth: new FormControl(salaryDeductionRawValue.deductionMonth, {
        validators: [Validators.required, Validators.min(1), Validators.max(12)],
      }),
      deductionTypeId: new FormControl(salaryDeductionRawValue.deductionTypeId, {
        validators: [Validators.required],
      }),
      employeeId: new FormControl(salaryDeductionRawValue.employeeId, {
        validators: [Validators.required],
      }),
    });
  }

  getSalaryDeduction(form: SalaryDeductionFormGroup): ISalaryDeduction | NewSalaryDeduction {
    return form.getRawValue() as ISalaryDeduction | NewSalaryDeduction;
  }

  resetForm(form: SalaryDeductionFormGroup, salaryDeduction: SalaryDeductionFormGroupInput): void {
    const salaryDeductionRawValue = { ...this.getFormDefaults(), ...salaryDeduction };
    form.reset(
      {
        ...salaryDeductionRawValue,
        id: { value: salaryDeductionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SalaryDeductionFormDefaults {
    return {
      id: null,
    };
  }
}
