import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployeePinConfiguration, NewEmployeePinConfiguration } from '../employee-pin-configuration.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeePinConfiguration for edit and NewEmployeePinConfigurationFormGroupInput for create.
 */
type EmployeePinConfigurationFormGroupInput = IEmployeePinConfiguration | PartialWithRequiredKeyOf<NewEmployeePinConfiguration>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployeePinConfiguration | NewEmployeePinConfiguration> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type EmployeePinConfigurationFormRawValue = FormValueOf<IEmployeePinConfiguration>;

type NewEmployeePinConfigurationFormRawValue = FormValueOf<NewEmployeePinConfiguration>;

type EmployeePinConfigurationFormDefaults = Pick<NewEmployeePinConfiguration, 'id' | 'hasFullFilled' | 'createdAt' | 'updatedAt'>;

type EmployeePinConfigurationFormGroupContent = {
  id: FormControl<EmployeePinConfigurationFormRawValue['id'] | NewEmployeePinConfiguration['id']>;
  employeeCategory: FormControl<EmployeePinConfigurationFormRawValue['employeeCategory']>;
  sequenceStart: FormControl<EmployeePinConfigurationFormRawValue['sequenceStart']>;
  sequenceEnd: FormControl<EmployeePinConfigurationFormRawValue['sequenceEnd']>;
  lastSequence: FormControl<EmployeePinConfigurationFormRawValue['lastSequence']>;
  hasFullFilled: FormControl<EmployeePinConfigurationFormRawValue['hasFullFilled']>;
  createdAt: FormControl<EmployeePinConfigurationFormRawValue['createdAt']>;
  updatedAt: FormControl<EmployeePinConfigurationFormRawValue['updatedAt']>;
  lastCreatedPin: FormControl<EmployeePinConfigurationFormRawValue['lastCreatedPin']>;
  createdById: FormControl<EmployeePinConfigurationFormRawValue['createdById']>;
  updatedById: FormControl<EmployeePinConfigurationFormRawValue['updatedById']>;
};

export type EmployeePinConfigurationFormGroup = FormGroup<EmployeePinConfigurationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeePinConfigurationFormService {
  createEmployeePinConfigurationFormGroup(
    employeePinConfiguration: EmployeePinConfigurationFormGroupInput = { id: null }
  ): EmployeePinConfigurationFormGroup {
    const employeePinConfigurationRawValue = this.convertEmployeePinConfigurationToEmployeePinConfigurationRawValue({
      ...this.getFormDefaults(),
      ...employeePinConfiguration,
    });
    return new FormGroup<EmployeePinConfigurationFormGroupContent>({
      id: new FormControl(
        { value: employeePinConfigurationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employeeCategory: new FormControl(employeePinConfigurationRawValue.employeeCategory, {
        validators: [Validators.required],
      }),
      sequenceStart: new FormControl(employeePinConfigurationRawValue.sequenceStart, {
        validators: [Validators.required,Validators.pattern('^[0-9]*$')],
      }),
      sequenceEnd: new FormControl(employeePinConfigurationRawValue.sequenceEnd, {
        validators: [Validators.required,Validators.pattern('^[0-9]*$')],
      }),
      lastSequence: new FormControl(employeePinConfigurationRawValue.lastSequence),
      hasFullFilled: new FormControl(employeePinConfigurationRawValue.hasFullFilled),
      createdAt: new FormControl(employeePinConfigurationRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(employeePinConfigurationRawValue.updatedAt),
      lastCreatedPin: new FormControl(employeePinConfigurationRawValue.lastCreatedPin),
      createdById: new FormControl(employeePinConfigurationRawValue.createdById),
      updatedById: new FormControl(employeePinConfigurationRawValue.updatedById),
    });
  }

  getEmployeePinConfiguration(form: EmployeePinConfigurationFormGroup): IEmployeePinConfiguration | NewEmployeePinConfiguration {
    return this.convertEmployeePinConfigurationRawValueToEmployeePinConfiguration(
      form.getRawValue() as EmployeePinConfigurationFormRawValue | NewEmployeePinConfigurationFormRawValue
    );
  }

  resetForm(form: EmployeePinConfigurationFormGroup, employeePinConfiguration: EmployeePinConfigurationFormGroupInput): void {
    const employeePinConfigurationRawValue = this.convertEmployeePinConfigurationToEmployeePinConfigurationRawValue({
      ...this.getFormDefaults(),
      ...employeePinConfiguration,
    });
    form.reset(
      {
        ...employeePinConfigurationRawValue,
        id: { value: employeePinConfigurationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeePinConfigurationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      hasFullFilled: false,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertEmployeePinConfigurationRawValueToEmployeePinConfiguration(
    rawEmployeePinConfiguration: EmployeePinConfigurationFormRawValue | NewEmployeePinConfigurationFormRawValue
  ): IEmployeePinConfiguration | NewEmployeePinConfiguration {
    return {
      ...rawEmployeePinConfiguration,
      createdAt: dayjs(rawEmployeePinConfiguration.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawEmployeePinConfiguration.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeePinConfigurationToEmployeePinConfigurationRawValue(
    employeePinConfiguration: IEmployeePinConfiguration | (Partial<NewEmployeePinConfiguration> & EmployeePinConfigurationFormDefaults)
  ): EmployeePinConfigurationFormRawValue | PartialWithRequiredKeyOf<NewEmployeePinConfigurationFormRawValue> {
    return {
      ...employeePinConfiguration,
      createdAt: employeePinConfiguration.createdAt ? employeePinConfiguration.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: employeePinConfiguration.updatedAt ? employeePinConfiguration.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
