import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployeePin, NewEmployeePin } from '../employee-pin.model';
import { EmployeePinStatus } from '../../enumerations/employee-pin-status.model';
import { CustomValidator } from '../../../validators/custom-validator';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeePin for edit and NewEmployeePinFormGroupInput for create.
 */
type EmployeePinFormGroupInput = IEmployeePin | PartialWithRequiredKeyOf<NewEmployeePin>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployeePin | NewEmployeePin> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type EmployeePinFormRawValue = FormValueOf<IEmployeePin>;

type NewEmployeePinFormRawValue = FormValueOf<NewEmployeePin>;

type EmployeePinFormDefaults = Pick<NewEmployeePin, 'id' | 'createdAt' | 'updatedAt' | 'employeePinStatus'>;

type EmployeePinFormGroupContent = {
  id: FormControl<EmployeePinFormRawValue['id'] | NewEmployeePin['id']>;
  pin: FormControl<EmployeePinFormRawValue['pin']>;
  fullName: FormControl<EmployeePinFormRawValue['fullName']>;
  employeeCategory: FormControl<EmployeePinFormRawValue['employeeCategory']>;
  employeePinStatus: FormControl<EmployeePinFormRawValue['employeePinStatus']>;
  createdAt: FormControl<EmployeePinFormRawValue['createdAt']>;
  updatedAt: FormControl<EmployeePinFormRawValue['updatedAt']>;
  departmentId: FormControl<EmployeePinFormRawValue['departmentId']>;
  designationId: FormControl<EmployeePinFormRawValue['designationId']>;
  unitId: FormControl<EmployeePinFormRawValue['unitId']>;
  updatedById: FormControl<EmployeePinFormRawValue['updatedById']>;
  createdById: FormControl<EmployeePinFormRawValue['createdById']>;
  recruitmentRequisitionFormId: FormControl<EmployeePinFormRawValue['recruitmentRequisitionFormId']>;
};

export type EmployeePinFormGroup = FormGroup<EmployeePinFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeePinFormService {
  createEmployeePinFormGroup(employeePin: EmployeePinFormGroupInput = { id: null }): EmployeePinFormGroup {
    const employeePinRawValue = this.convertEmployeePinToEmployeePinRawValue({
      ...this.getFormDefaults(),
      ...employeePin,
    });
    return new FormGroup<EmployeePinFormGroupContent>({
      id: new FormControl(
        { value: employeePinRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      pin: new FormControl(employeePinRawValue.pin, {
        validators: [Validators.required, CustomValidator.numberValidator()],
      }),
      fullName: new FormControl(employeePinRawValue.fullName, {
        validators: [Validators.required, CustomValidator.naturalTextValidator()],
      }),
      employeeCategory: new FormControl(employeePinRawValue.employeeCategory, {
        validators: [Validators.required],
      }),
      employeePinStatus: new FormControl(employeePinRawValue.employeePinStatus),
      createdAt: new FormControl(employeePinRawValue.createdAt),
      updatedAt: new FormControl(employeePinRawValue.updatedAt),
      departmentId: new FormControl(employeePinRawValue.departmentId, {
        validators: [Validators.required],
      }),
      designationId: new FormControl(employeePinRawValue.designationId, {
        validators: [Validators.required],
      }),
      unitId: new FormControl(employeePinRawValue.unitId, {
        validators: [Validators.required],
      }),
      updatedById: new FormControl(employeePinRawValue.updatedById),
      createdById: new FormControl(employeePinRawValue.createdById),
      recruitmentRequisitionFormId: new FormControl(employeePinRawValue.recruitmentRequisitionFormId),
    });
  }

  getEmployeePin(form: EmployeePinFormGroup): IEmployeePin | NewEmployeePin {
    return this.convertEmployeePinRawValueToEmployeePin(form.getRawValue() as EmployeePinFormRawValue | NewEmployeePinFormRawValue);
  }

  resetForm(form: EmployeePinFormGroup, employeePin: EmployeePinFormGroupInput): void {
    const employeePinRawValue = this.convertEmployeePinToEmployeePinRawValue({ ...this.getFormDefaults(), ...employeePin });
    form.reset(
      {
        ...employeePinRawValue,
        id: { value: employeePinRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeePinFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
      employeePinStatus: EmployeePinStatus.CREATED,
    };
  }

  private convertEmployeePinRawValueToEmployeePin(
    rawEmployeePin: EmployeePinFormRawValue | NewEmployeePinFormRawValue
  ): IEmployeePin | NewEmployeePin {
    return {
      ...rawEmployeePin,
      createdAt: dayjs(rawEmployeePin.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawEmployeePin.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeePinToEmployeePinRawValue(
    employeePin: IEmployeePin | (Partial<NewEmployeePin> & EmployeePinFormDefaults)
  ): EmployeePinFormRawValue | PartialWithRequiredKeyOf<NewEmployeePinFormRawValue> {
    return {
      ...employeePin,
      createdAt: employeePin.createdAt ? employeePin.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: employeePin.updatedAt ? employeePin.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
