import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReferences, NewReferences } from '../references.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReferences for edit and NewReferencesFormGroupInput for create.
 */
type ReferencesFormGroupInput = IReferences | PartialWithRequiredKeyOf<NewReferences>;

type ReferencesFormDefaults = Pick<NewReferences, 'id'>;

type ReferencesFormGroupContent = {
  id: FormControl<IReferences['id'] | NewReferences['id']>;
  name: FormControl<IReferences['name']>;
  institute: FormControl<IReferences['institute']>;
  designation: FormControl<IReferences['designation']>;
  relationshipWithEmployee: FormControl<IReferences['relationshipWithEmployee']>;
  email: FormControl<IReferences['email']>;
  contactNumber: FormControl<IReferences['contactNumber']>;
  employeeId: FormControl<IReferences['employeeId']>;
};

export type ReferencesFormGroup = FormGroup<ReferencesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReferencesFormService {
  createReferencesFormGroup(references: ReferencesFormGroupInput = { id: null }): ReferencesFormGroup {
    const referencesRawValue = {
      ...this.getFormDefaults(),
      ...references,
    };
    return new FormGroup<ReferencesFormGroupContent>({
      id: new FormControl(
        { value: referencesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(referencesRawValue.name, {
        validators: [CustomValidator.naturalTextValidator()]
      }),
      institute: new FormControl(referencesRawValue.institute, {
        validators: [CustomValidator.naturalTextValidator()]
      }),
      designation: new FormControl(referencesRawValue.designation, {
        validators: [CustomValidator.naturalTextValidator()]
      }),
      relationshipWithEmployee: new FormControl(referencesRawValue.relationshipWithEmployee),
      email: new FormControl(referencesRawValue.email, {
        validators: [Validators.email]
      }),
      contactNumber: new FormControl(referencesRawValue.contactNumber, {
        validators: [CustomValidator.phoneNumberValidator()]
      }),
      employeeId: new FormControl(referencesRawValue.employeeId, {
        validators: [Validators.required],
      }),
    });
  }

  getReferences(form: ReferencesFormGroup): IReferences | NewReferences {
    return form.getRawValue() as IReferences | NewReferences;
  }

  resetForm(form: ReferencesFormGroup, references: ReferencesFormGroupInput): void {
    const referencesRawValue = { ...this.getFormDefaults(), ...references };
    form.reset(
      {
        ...referencesRawValue,
        id: { value: referencesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ReferencesFormDefaults {
    return {
      id: null,
    };
  }
}
