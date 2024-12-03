import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOfficeNotices, NewOfficeNotices } from '../office-notices.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOfficeNotices for edit and NewOfficeNoticesFormGroupInput for create.
 */
type OfficeNoticesFormGroupInput = IOfficeNotices | PartialWithRequiredKeyOf<NewOfficeNotices>;

type OfficeNoticesFormDefaults = Pick<NewOfficeNotices, 'id'>;

type OfficeNoticesFormGroupContent = {
  id: FormControl<IOfficeNotices['id'] | NewOfficeNotices['id']>;
  title: FormControl<IOfficeNotices['title']>;
  description: FormControl<IOfficeNotices['description']>;
  status: FormControl<IOfficeNotices['status']>;
  publishForm: FormControl<IOfficeNotices['publishForm']>;
  publishTo: FormControl<IOfficeNotices['publishTo']>;
  createdAt: FormControl<IOfficeNotices['createdAt']>;
  updatedAt: FormControl<IOfficeNotices['updatedAt']>;
  createdBy: FormControl<IOfficeNotices['createdBy']>;
  updatedBy: FormControl<IOfficeNotices['updatedBy']>;
};

export type OfficeNoticesFormGroup = FormGroup<OfficeNoticesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OfficeNoticesFormService {
  createOfficeNoticesFormGroup(officeNotices: OfficeNoticesFormGroupInput = { id: null }): OfficeNoticesFormGroup {
    const officeNoticesRawValue = {
      ...this.getFormDefaults(),
      ...officeNotices,
    };
    return new FormGroup<OfficeNoticesFormGroupContent>({
      id: new FormControl(
        { value: officeNoticesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(officeNoticesRawValue.title, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      description: new FormControl(officeNoticesRawValue.description, {
        validators: [CustomValidator.naturalTextValidator()]
      }),
      status: new FormControl(officeNoticesRawValue.status),
      publishForm: new FormControl(officeNoticesRawValue.publishForm),
      publishTo: new FormControl(officeNoticesRawValue.publishTo),
      createdAt: new FormControl(officeNoticesRawValue.createdAt),
      updatedAt: new FormControl(officeNoticesRawValue.updatedAt),
      createdBy: new FormControl(officeNoticesRawValue.createdBy),
      updatedBy: new FormControl(officeNoticesRawValue.updatedBy),
    });
  }

  getOfficeNotices(form: OfficeNoticesFormGroup): IOfficeNotices | NewOfficeNotices {
    return form.getRawValue() as IOfficeNotices | NewOfficeNotices;
  }

  resetForm(form: OfficeNoticesFormGroup, officeNotices: OfficeNoticesFormGroupInput): void {
    const officeNoticesRawValue = { ...this.getFormDefaults(), ...officeNotices };
    form.reset(
      {
        ...officeNoticesRawValue,
        id: { value: officeNoticesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OfficeNoticesFormDefaults {
    return {
      id: null,
    };
  }
}
