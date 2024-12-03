import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmploymentCertificate, NewEmploymentCertificate } from '../employment-certificate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmploymentCertificate for edit and NewEmploymentCertificateFormGroupInput for create.
 */
type EmploymentCertificateFormGroupInput = IEmploymentCertificate | PartialWithRequiredKeyOf<NewEmploymentCertificate>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmploymentCertificate | NewEmploymentCertificate> = Omit<T, 'createdAt' | 'updatedAt' | 'generatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  generatedAt?: string | null;
};

type EmploymentCertificateFormRawValue = FormValueOf<IEmploymentCertificate>;

type NewEmploymentCertificateFormRawValue = FormValueOf<NewEmploymentCertificate>;

type EmploymentCertificateFormDefaults = Pick<NewEmploymentCertificate, 'id' | 'createdAt' | 'updatedAt' | 'generatedAt'>;

type EmploymentCertificateFormGroupContent = {
  id: FormControl<EmploymentCertificateFormRawValue['id'] | NewEmploymentCertificate['id']>;
  certificateStatus: FormControl<EmploymentCertificateFormRawValue['certificateStatus']>;
  referenceNumber: FormControl<EmploymentCertificateFormRawValue['referenceNumber']>;
  issueDate: FormControl<EmploymentCertificateFormRawValue['issueDate']>;
  reason: FormControl<EmploymentCertificateFormRawValue['reason']>;
  createdAt: FormControl<EmploymentCertificateFormRawValue['createdAt']>;
  updatedAt: FormControl<EmploymentCertificateFormRawValue['updatedAt']>;
  generatedAt: FormControl<EmploymentCertificateFormRawValue['generatedAt']>;
  employee: FormControl<EmploymentCertificateFormRawValue['employee']>;
  signatoryPerson: FormControl<EmploymentCertificateFormRawValue['signatoryPerson']>;
  createdBy: FormControl<EmploymentCertificateFormRawValue['createdBy']>;
  updatedBy: FormControl<EmploymentCertificateFormRawValue['updatedBy']>;
  generatedBy: FormControl<EmploymentCertificateFormRawValue['generatedBy']>;
};

export type EmploymentCertificateFormGroup = FormGroup<EmploymentCertificateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmploymentCertificateFormService {
  createEmploymentCertificateFormGroup(
    employmentCertificate: EmploymentCertificateFormGroupInput = { id: null }
  ): EmploymentCertificateFormGroup {
    const employmentCertificateRawValue = this.convertEmploymentCertificateToEmploymentCertificateRawValue({
      ...this.getFormDefaults(),
      ...employmentCertificate,
    });
    return new FormGroup<EmploymentCertificateFormGroupContent>({
      id: new FormControl(
        { value: employmentCertificateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      certificateStatus: new FormControl(employmentCertificateRawValue.certificateStatus, {
        validators: [Validators.required],
      }),
      referenceNumber: new FormControl(employmentCertificateRawValue.referenceNumber),
      issueDate: new FormControl(employmentCertificateRawValue.issueDate),
      reason: new FormControl(employmentCertificateRawValue.reason),
      createdAt: new FormControl(employmentCertificateRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(employmentCertificateRawValue.updatedAt),
      generatedAt: new FormControl(employmentCertificateRawValue.generatedAt),
      employee: new FormControl(employmentCertificateRawValue.employee, {
        validators: [Validators.required],
      }),
      signatoryPerson: new FormControl(employmentCertificateRawValue.signatoryPerson),
      createdBy: new FormControl(employmentCertificateRawValue.createdBy, {
        validators: [Validators.required],
      }),
      updatedBy: new FormControl(employmentCertificateRawValue.updatedBy),
      generatedBy: new FormControl(employmentCertificateRawValue.generatedBy),
    });
  }

  getEmploymentCertificate(form: EmploymentCertificateFormGroup): IEmploymentCertificate | NewEmploymentCertificate {
    return this.convertEmploymentCertificateRawValueToEmploymentCertificate(
      form.getRawValue() as EmploymentCertificateFormRawValue | NewEmploymentCertificateFormRawValue
    );
  }

  resetForm(form: EmploymentCertificateFormGroup, employmentCertificate: EmploymentCertificateFormGroupInput): void {
    const employmentCertificateRawValue = this.convertEmploymentCertificateToEmploymentCertificateRawValue({
      ...this.getFormDefaults(),
      ...employmentCertificate,
    });
    form.reset(
      {
        ...employmentCertificateRawValue,
        id: { value: employmentCertificateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmploymentCertificateFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
      generatedAt: currentTime,
    };
  }

  private convertEmploymentCertificateRawValueToEmploymentCertificate(
    rawEmploymentCertificate: EmploymentCertificateFormRawValue | NewEmploymentCertificateFormRawValue
  ): IEmploymentCertificate | NewEmploymentCertificate {
    return {
      ...rawEmploymentCertificate,
      createdAt: dayjs(rawEmploymentCertificate.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawEmploymentCertificate.updatedAt, DATE_TIME_FORMAT),
      generatedAt: dayjs(rawEmploymentCertificate.generatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEmploymentCertificateToEmploymentCertificateRawValue(
    employmentCertificate: IEmploymentCertificate | (Partial<NewEmploymentCertificate> & EmploymentCertificateFormDefaults)
  ): EmploymentCertificateFormRawValue | PartialWithRequiredKeyOf<NewEmploymentCertificateFormRawValue> {
    return {
      ...employmentCertificate,
      createdAt: employmentCertificate.createdAt ? employmentCertificate.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: employmentCertificate.updatedAt ? employmentCertificate.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      generatedAt: employmentCertificate.generatedAt ? employmentCertificate.generatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
