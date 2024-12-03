import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IArrearSalary, NewArrearSalary } from '../arrear-salary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArrearSalary for edit and NewArrearSalaryFormGroupInput for create.
 */
type ArrearSalaryFormGroupInput = IArrearSalary | PartialWithRequiredKeyOf<NewArrearSalary>;

type ArrearSalaryFormDefaults = Pick<NewArrearSalary, 'id'>;

type ArrearSalaryFormGroupContent = {
  id: FormControl<IArrearSalary['id'] | NewArrearSalary['id']>;
  month: FormControl<IArrearSalary['month']>;
  year: FormControl<IArrearSalary['year']>;
  amount: FormControl<IArrearSalary['amount']>;
  arrearType: FormControl<IArrearSalary['arrearType']>;
  employee: FormControl<IArrearSalary['employee']>;
};

export type ArrearSalaryFormGroup = FormGroup<ArrearSalaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArrearSalaryFormService {
  createArrearSalaryFormGroup(arrearSalary: ArrearSalaryFormGroupInput = { id: null }): ArrearSalaryFormGroup {
    const arrearSalaryRawValue = {
      ...this.getFormDefaults(),
      ...arrearSalary,
    };
    return new FormGroup<ArrearSalaryFormGroupContent>({
      id: new FormControl(
        { value: arrearSalaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      month: new FormControl(arrearSalaryRawValue.month, {
        validators: [Validators.required],
      }),
      year: new FormControl(arrearSalaryRawValue.year, {
        validators: [Validators.required, Validators.min(1990), Validators.max(2099)],
      }),
      amount: new FormControl(arrearSalaryRawValue.amount, {
        validators: [Validators.required, Validators.min(1), Validators.max(9999999)],
      }),
      arrearType: new FormControl(arrearSalaryRawValue.arrearType),
      employee: new FormControl(arrearSalaryRawValue.employee, {
        validators: [Validators.required],
      }),
    });
  }

  getArrearSalary(form: ArrearSalaryFormGroup): IArrearSalary | NewArrearSalary {
    return form.getRawValue() as IArrearSalary | NewArrearSalary;
  }

  resetForm(form: ArrearSalaryFormGroup, arrearSalary: ArrearSalaryFormGroupInput): void {
    const arrearSalaryRawValue = { ...this.getFormDefaults(), ...arrearSalary };
    form.reset(
      {
        ...arrearSalaryRawValue,
        id: { value: arrearSalaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ArrearSalaryFormDefaults {
    return {
      id: null,
    };
  }
}
