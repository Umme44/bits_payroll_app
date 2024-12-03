import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDesignation, NewDesignation } from '../designation.model';
import { CustomValidator } from '../../../validators/custom-validator';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDesignation for edit and NewDesignationFormGroupInput for create.
 */
type DesignationFormGroupInput = IDesignation | PartialWithRequiredKeyOf<NewDesignation>;

type DesignationFormDefaults = Pick<NewDesignation, 'id'>;

type DesignationFormGroupContent = {
  id: FormControl<IDesignation['id'] | NewDesignation['id']>;
  designationName: FormControl<IDesignation['designationName']>;
};

export type DesignationFormGroup = FormGroup<DesignationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DesignationFormService {
  createDesignationFormGroup(designation: DesignationFormGroupInput = { id: null }): DesignationFormGroup {
    const designationRawValue = {
      ...this.getFormDefaults(),
      ...designation,
    };
    return new FormGroup<DesignationFormGroupContent>({
      id: new FormControl(
        { value: designationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      designationName: new FormControl(designationRawValue.designationName, {
        validators: [CustomValidator.naturalTextValidator(),Validators.required, Validators.minLength(1), Validators.maxLength(250)],
      }),
    });
  }

  getDesignation(form: DesignationFormGroup): IDesignation | NewDesignation {
    return form.getRawValue() as IDesignation | NewDesignation;
  }

  resetForm(form: DesignationFormGroup, designation: DesignationFormGroupInput): void {
    const designationRawValue = { ...this.getFormDefaults(), ...designation };
    form.reset(
      {
        ...designationRawValue,
        id: { value: designationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DesignationFormDefaults {
    return {
      id: null,
    };
  }
}
