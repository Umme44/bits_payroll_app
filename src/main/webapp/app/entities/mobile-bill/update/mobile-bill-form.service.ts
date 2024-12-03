import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMobileBill, NewMobileBill } from '../mobile-bill.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMobileBill for edit and NewMobileBillFormGroupInput for create.
 */
type MobileBillFormGroupInput = IMobileBill | PartialWithRequiredKeyOf<NewMobileBill>;

type MobileBillFormDefaults = Pick<NewMobileBill, 'id'>;

type MobileBillFormGroupContent = {
  id: FormControl<IMobileBill['id'] | NewMobileBill['id']>;
  month: FormControl<IMobileBill['month']>;
  amount: FormControl<IMobileBill['amount']>;
  year: FormControl<IMobileBill['year']>;
  employeeId: FormControl<IMobileBill['employeeId']>;
};

export type MobileBillFormGroup = FormGroup<MobileBillFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MobileBillFormService {
  createMobileBillFormGroup(mobileBill: MobileBillFormGroupInput = { id: null }): MobileBillFormGroup {
    const mobileBillRawValue = {
      ...this.getFormDefaults(),
      ...mobileBill,
    };
    return new FormGroup<MobileBillFormGroupContent>({
      id: new FormControl(
        { value: mobileBillRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      month: new FormControl(mobileBillRawValue.month),
      amount: new FormControl(mobileBillRawValue.amount),
      year: new FormControl(mobileBillRawValue.year),
      employeeId: new FormControl(mobileBillRawValue.employeeId),
    });
  }

  getMobileBill(form: MobileBillFormGroup): IMobileBill | NewMobileBill {
    return form.getRawValue() as IMobileBill | NewMobileBill;
  }

  resetForm(form: MobileBillFormGroup, mobileBill: MobileBillFormGroupInput): void {
    const mobileBillRawValue = { ...this.getFormDefaults(), ...mobileBill };
    form.reset(
      {
        ...mobileBillRawValue,
        id: { value: mobileBillRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MobileBillFormDefaults {
    return {
      id: null,
    };
  }
}
