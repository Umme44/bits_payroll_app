import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDeductionType, NewDeductionType } from '../deduction-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeductionType for edit and NewDeductionTypeFormGroupInput for create.
 */
type DeductionTypeFormGroupInput = IDeductionType | PartialWithRequiredKeyOf<NewDeductionType>;

type DeductionTypeFormDefaults = Pick<NewDeductionType, 'id'>;

type DeductionTypeFormGroupContent = {
  id: FormControl<IDeductionType['id'] | NewDeductionType['id']>;
  name: FormControl<IDeductionType['name']>;
};

export type DeductionTypeFormGroup = FormGroup<DeductionTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeductionTypeFormService {
  createDeductionTypeFormGroup(deductionType: DeductionTypeFormGroupInput = { id: null }): DeductionTypeFormGroup {
    const deductionTypeRawValue = {
      ...this.getFormDefaults(),
      ...deductionType,
    };
    return new FormGroup<DeductionTypeFormGroupContent>({
      id: new FormControl(
        { value: deductionTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(deductionTypeRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(250)],
      }),
    });
  }

  getDeductionType(form: DeductionTypeFormGroup): IDeductionType | NewDeductionType {
    return form.getRawValue() as IDeductionType | NewDeductionType;
  }

  resetForm(form: DeductionTypeFormGroup, deductionType: DeductionTypeFormGroupInput): void {
    const deductionTypeRawValue = { ...this.getFormDefaults(), ...deductionType };
    form.reset(
      {
        ...deductionTypeRawValue,
        id: { value: deductionTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DeductionTypeFormDefaults {
    return {
      id: null,
    };
  }
}
