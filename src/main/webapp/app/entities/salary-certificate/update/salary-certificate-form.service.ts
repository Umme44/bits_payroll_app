import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISalaryCertificate, NewSalaryCertificate } from '../salary-certificate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISalaryCertificate for edit and NewSalaryCertificateFormGroupInput for create.
 */
type SalaryCertificateFormGroupInput = ISalaryCertificate | PartialWithRequiredKeyOf<NewSalaryCertificate>;

type SalaryCertificateFormDefaults = Pick<NewSalaryCertificate, 'id'>;

type SalaryCertificateFormGroupContent = {
  id: FormControl<ISalaryCertificate['id'] | NewSalaryCertificate['id']>;
  purpose: FormControl<ISalaryCertificate['purpose']>;
  remarks: FormControl<ISalaryCertificate['remarks']>;
  status: FormControl<ISalaryCertificate['status']>;
  createdAt: FormControl<ISalaryCertificate['createdAt']>;
  updatedAt: FormControl<ISalaryCertificate['updatedAt']>;
  sanctionAt: FormControl<ISalaryCertificate['sanctionAt']>;
  month: FormControl<ISalaryCertificate['month']>;
  year: FormControl<ISalaryCertificate['year']>;
  referenceNumber: FormControl<ISalaryCertificate['referenceNumber']>;
  createdById: FormControl<ISalaryCertificate['createdById']>;
  updatedById: FormControl<ISalaryCertificate['updatedById']>;
  sanctionById: FormControl<ISalaryCertificate['sanctionById']>;
  employeeId: FormControl<ISalaryCertificate['employeeId']>;
  signatoryPersonId: FormControl<ISalaryCertificate['signatoryPersonId']>;
};

export type SalaryCertificateFormGroup = FormGroup<SalaryCertificateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SalaryCertificateFormService {
  createSalaryCertificateFormGroup(salaryCertificate: SalaryCertificateFormGroupInput = { id: null }): SalaryCertificateFormGroup {
    const salaryCertificateRawValue = {
      ...this.getFormDefaults(),
      ...salaryCertificate,
    };
    return new FormGroup<SalaryCertificateFormGroupContent>({
      id: new FormControl(
        { value: salaryCertificateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      purpose: new FormControl(salaryCertificateRawValue.purpose, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250)],
      }),
      remarks: new FormControl(salaryCertificateRawValue.remarks, {
        validators: [Validators.minLength(3), Validators.maxLength(250)],
      }),
      status: new FormControl(salaryCertificateRawValue.status),
      createdAt: new FormControl(salaryCertificateRawValue.createdAt),
      updatedAt: new FormControl(salaryCertificateRawValue.updatedAt),
      sanctionAt: new FormControl(salaryCertificateRawValue.sanctionAt),
      month: new FormControl(salaryCertificateRawValue.month, {
        validators: [Validators.required],
      }),
      year: new FormControl(salaryCertificateRawValue.year),
      referenceNumber: new FormControl(salaryCertificateRawValue.referenceNumber),
      createdById: new FormControl(salaryCertificateRawValue.createdById),
      updatedById: new FormControl(salaryCertificateRawValue.updatedById),
      sanctionById: new FormControl(salaryCertificateRawValue.sanctionById),
      employeeId: new FormControl(salaryCertificateRawValue.employeeId, {
        validators: [Validators.required],
      }),
      signatoryPersonId: new FormControl(salaryCertificateRawValue.signatoryPersonId),
    });
  }

  getSalaryCertificate(form: SalaryCertificateFormGroup): ISalaryCertificate | NewSalaryCertificate {
    return form.getRawValue() as ISalaryCertificate | NewSalaryCertificate;
  }

  resetForm(form: SalaryCertificateFormGroup, salaryCertificate: SalaryCertificateFormGroupInput): void {
    const salaryCertificateRawValue = { ...this.getFormDefaults(), ...salaryCertificate };
    form.reset(
      {
        ...salaryCertificateRawValue,
        id: { value: salaryCertificateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SalaryCertificateFormDefaults {
    return {
      id: null,
    };
  }
}
