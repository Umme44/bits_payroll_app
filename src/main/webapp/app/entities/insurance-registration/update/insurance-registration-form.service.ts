import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInsuranceRegistration, NewInsuranceRegistration } from '../insurance-registration.model';
import { InsuranceStatus } from '../../enumerations/insurance-status.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInsuranceRegistration for edit and NewInsuranceRegistrationFormGroupInput for create.
 */
type InsuranceRegistrationFormGroupInput = IInsuranceRegistration | PartialWithRequiredKeyOf<NewInsuranceRegistration>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInsuranceRegistration | NewInsuranceRegistration> = Omit<T, 'updatedAt' | 'approvedAt' | 'createdAt'> & {
  updatedAt?: string | null;
  approvedAt?: string | null;
  createdAt?: string | null;
};

type InsuranceRegistrationFormRawValue = FormValueOf<IInsuranceRegistration>;

type NewInsuranceRegistrationFormRawValue = FormValueOf<NewInsuranceRegistration>;

type InsuranceRegistrationFormDefaults = Pick<
  NewInsuranceRegistration,
  'id' | 'updatedAt' | 'approvedAt' | 'createdAt' | 'insuranceStatus'
>;

type InsuranceRegistrationFormGroupContent = {
  id: FormControl<InsuranceRegistrationFormRawValue['id'] | NewInsuranceRegistration['id']>;
  name: FormControl<InsuranceRegistrationFormRawValue['name']>;
  dateOfBirth: FormControl<InsuranceRegistrationFormRawValue['dateOfBirth']>;
  age: FormControl<InsuranceRegistrationFormRawValue['age']>;
  photo: FormControl<InsuranceRegistrationFormRawValue['photo']>;
  insuranceRelation: FormControl<InsuranceRegistrationFormRawValue['insuranceRelation']>;
  insuranceStatus: FormControl<InsuranceRegistrationFormRawValue['insuranceStatus']>;
  unapprovalReason: FormControl<InsuranceRegistrationFormRawValue['unapprovalReason']>;
  availableBalance: FormControl<InsuranceRegistrationFormRawValue['availableBalance']>;
  updatedAt: FormControl<InsuranceRegistrationFormRawValue['updatedAt']>;
  approvedAt: FormControl<InsuranceRegistrationFormRawValue['approvedAt']>;
  insuranceId: FormControl<InsuranceRegistrationFormRawValue['insuranceId']>;
  createdAt: FormControl<InsuranceRegistrationFormRawValue['createdAt']>;
  employeeId: FormControl<InsuranceRegistrationFormRawValue['employeeId']>;
  employeeName: FormControl<InsuranceRegistrationFormRawValue['employeeName']>;
  approvedById: FormControl<InsuranceRegistrationFormRawValue['approvedById']>;
  updatedById: FormControl<InsuranceRegistrationFormRawValue['updatedById']>;
  createdById: FormControl<InsuranceRegistrationFormRawValue['createdById']>;
};

export type InsuranceRegistrationFormGroup = FormGroup<InsuranceRegistrationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InsuranceRegistrationFormService {
  createInsuranceRegistrationFormGroup(
    insuranceRegistration: InsuranceRegistrationFormGroupInput = { id: null }
  ): InsuranceRegistrationFormGroup {
    const insuranceRegistrationRawValue = this.convertInsuranceRegistrationToInsuranceRegistrationRawValue({
      ...this.getFormDefaults(),
      ...insuranceRegistration,
    });
    return new FormGroup<InsuranceRegistrationFormGroupContent>({
      id: new FormControl(
        { value: insuranceRegistrationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(insuranceRegistrationRawValue.name, {
        validators: [Validators.minLength(0), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      age: new FormControl(insuranceRegistrationRawValue.age, {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl(insuranceRegistrationRawValue.dateOfBirth, {
        validators: [Validators.required],
      }),
      photo: new FormControl(insuranceRegistrationRawValue.photo),
      insuranceRelation: new FormControl(insuranceRegistrationRawValue.insuranceRelation, {
        validators: [Validators.required],
      }),
      insuranceStatus: new FormControl(insuranceRegistrationRawValue.insuranceStatus),
      unapprovalReason: new FormControl(insuranceRegistrationRawValue.unapprovalReason),
      availableBalance: new FormControl(insuranceRegistrationRawValue.availableBalance),
      updatedAt: new FormControl(insuranceRegistrationRawValue.updatedAt),
      approvedAt: new FormControl(insuranceRegistrationRawValue.approvedAt),
      insuranceId: new FormControl(insuranceRegistrationRawValue.insuranceId),
      createdAt: new FormControl(insuranceRegistrationRawValue.createdAt),
      employeeId: new FormControl(insuranceRegistrationRawValue.employeeId, {
        validators: [Validators.required],
      }),
      employeeName: new FormControl(insuranceRegistrationRawValue.employeeName),
      approvedById: new FormControl(insuranceRegistrationRawValue.approvedById),
      updatedById: new FormControl(insuranceRegistrationRawValue.updatedById),
      createdById: new FormControl(insuranceRegistrationRawValue.createdById),
    });
  }

  getInsuranceRegistration(form: InsuranceRegistrationFormGroup): IInsuranceRegistration | NewInsuranceRegistration {
    return this.convertInsuranceRegistrationRawValueToInsuranceRegistration(
      form.getRawValue() as InsuranceRegistrationFormRawValue | NewInsuranceRegistrationFormRawValue
    );
  }

  resetForm(form: InsuranceRegistrationFormGroup, insuranceRegistration: InsuranceRegistrationFormGroupInput): void {
    const insuranceRegistrationRawValue = this.convertInsuranceRegistrationToInsuranceRegistrationRawValue({
      ...this.getFormDefaults(),
      ...insuranceRegistration,
    });
    form.reset(
      {
        ...insuranceRegistrationRawValue,
        id: { value: insuranceRegistrationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InsuranceRegistrationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      updatedAt: currentTime,
      approvedAt: currentTime,
      createdAt: currentTime,
      insuranceStatus: InsuranceStatus.PENDING,
    };
  }

  private convertInsuranceRegistrationRawValueToInsuranceRegistration(
    rawInsuranceRegistration: InsuranceRegistrationFormRawValue | NewInsuranceRegistrationFormRawValue
  ): IInsuranceRegistration | NewInsuranceRegistration {
    return {
      ...rawInsuranceRegistration,
      updatedAt: dayjs(rawInsuranceRegistration.updatedAt, DATE_TIME_FORMAT),
      approvedAt: dayjs(rawInsuranceRegistration.approvedAt, DATE_TIME_FORMAT),
      createdAt: dayjs(rawInsuranceRegistration.createdAt, DATE_TIME_FORMAT),
      insuranceStatus: InsuranceStatus.PENDING,
    };
  }

  private convertInsuranceRegistrationToInsuranceRegistrationRawValue(
    insuranceRegistration: IInsuranceRegistration | (Partial<NewInsuranceRegistration> & InsuranceRegistrationFormDefaults)
  ): InsuranceRegistrationFormRawValue | PartialWithRequiredKeyOf<NewInsuranceRegistrationFormRawValue> {
    return {
      ...insuranceRegistration,
      updatedAt: insuranceRegistration.updatedAt ? insuranceRegistration.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      approvedAt: insuranceRegistration.approvedAt ? insuranceRegistration.approvedAt.format(DATE_TIME_FORMAT) : undefined,
      createdAt: insuranceRegistration.createdAt ? insuranceRegistration.createdAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
