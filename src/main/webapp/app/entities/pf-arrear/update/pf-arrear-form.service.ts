import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPfArrear, NewPfArrear } from '../pf-arrear.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPfArrear for edit and NewPfArrearFormGroupInput for create.
 */
type PfArrearFormGroupInput = IPfArrear | PartialWithRequiredKeyOf<NewPfArrear>;

type PfArrearFormDefaults = Pick<NewPfArrear, 'id'>;

type PfArrearFormGroupContent = {
  id: FormControl<IPfArrear['id'] | NewPfArrear['id']>;
  month: FormControl<IPfArrear['month']>;
  year: FormControl<IPfArrear['year']>;
  amount: FormControl<IPfArrear['amount']>;
  remarks: FormControl<IPfArrear['remarks']>;
  employee: FormControl<IPfArrear['employee']>;
  employeeId: FormControl<IPfArrear['employeeId']>;
};

export type PfArrearFormGroup = FormGroup<PfArrearFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PfArrearFormService {
  createPfArrearFormGroup(pfArrear: PfArrearFormGroupInput = { id: null }): PfArrearFormGroup {
    const pfArrearRawValue = {
      ...this.getFormDefaults(),
      ...pfArrear,
    };
    return new FormGroup<PfArrearFormGroupContent>({
      id: new FormControl(
        { value: pfArrearRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      month: new FormControl(pfArrearRawValue.month, {
        validators: [Validators.required],
      }),
      year: new FormControl(pfArrearRawValue.year, {
        validators: [Validators.required, Validators.min(1900), Validators.max(2100)],
      }),
      amount: new FormControl(pfArrearRawValue.amount, {
        validators: [Validators.required, Validators.min(0), Validators.max(10000000)],
      }),
      remarks: new FormControl(pfArrearRawValue.remarks, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      employee: new FormControl(pfArrearRawValue.employee),
      employeeId: new FormControl(pfArrearRawValue.employeeId),
    });
  }

  getPfArrear(form: PfArrearFormGroup): IPfArrear | NewPfArrear {
    return form.getRawValue() as IPfArrear | NewPfArrear;
  }

  resetForm(form: PfArrearFormGroup, pfArrear: PfArrearFormGroupInput): void {
    const pfArrearRawValue = { ...this.getFormDefaults(), ...pfArrear };
    form.reset(
      {
        ...pfArrearRawValue,
        id: { value: pfArrearRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PfArrearFormDefaults {
    return {
      id: null,
    };
  }
}
