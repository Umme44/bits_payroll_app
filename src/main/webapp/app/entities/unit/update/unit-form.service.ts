import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUnit, NewUnit } from '../unit.model';
import { CustomValidator } from '../../../validators/custom-validator';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUnit for edit and NewUnitFormGroupInput for create.
 */
type UnitFormGroupInput = IUnit | PartialWithRequiredKeyOf<NewUnit>;

type UnitFormDefaults = Pick<NewUnit, 'id'>;

type UnitFormGroupContent = {
  id: FormControl<IUnit['id'] | NewUnit['id']>;
  unitName: FormControl<IUnit['unitName']>;
};

export type UnitFormGroup = FormGroup<UnitFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UnitFormService {
  createUnitFormGroup(unit: UnitFormGroupInput = { id: null }): UnitFormGroup {
    const unitRawValue = {
      ...this.getFormDefaults(),
      ...unit,
    };
    return new FormGroup<UnitFormGroupContent>({
      id: new FormControl(
        { value: unitRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      unitName: new FormControl(unitRawValue.unitName, {
        validators: [Validators.required, CustomValidator.naturalTextValidator()],
      }),
    });
  }

  getUnit(form: UnitFormGroup): IUnit | NewUnit {
    return form.getRawValue() as IUnit | NewUnit;
  }

  resetForm(form: UnitFormGroup, unit: UnitFormGroupInput): void {
    const unitRawValue = { ...this.getFormDefaults(), ...unit };
    form.reset(
      {
        ...unitRawValue,
        id: { value: unitRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UnitFormDefaults {
    return {
      id: null,
    };
  }
}
