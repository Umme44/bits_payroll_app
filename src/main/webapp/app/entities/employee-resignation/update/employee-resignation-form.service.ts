import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployeeResignation, NewEmployeeResignation } from '../employee-resignation.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeResignation for edit and NewEmployeeResignationFormGroupInput for create.
 */
type EmployeeResignationFormGroupInput = IEmployeeResignation | PartialWithRequiredKeyOf<NewEmployeeResignation>;

type EmployeeResignationFormDefaults = Pick<NewEmployeeResignation, 'id' | 'isSalaryHold' | 'isFestivalBonusHold'>;

type EmployeeResignationFormGroupContent = {
  id: FormControl<IEmployeeResignation['id'] | NewEmployeeResignation['id']>;
  createdAt: FormControl<IEmployeeResignation['createdAt']>;
  updatedAt: FormControl<IEmployeeResignation['updatedAt']>;
  resignationDate: FormControl<IEmployeeResignation['resignationDate']>;
  approvalStatus: FormControl<IEmployeeResignation['approvalStatus']>;
  approvalComment: FormControl<IEmployeeResignation['approvalComment']>;
  isSalaryHold: FormControl<IEmployeeResignation['isSalaryHold']>;
  isFestivalBonusHold: FormControl<IEmployeeResignation['isFestivalBonusHold']>;
  resignationReason: FormControl<IEmployeeResignation['resignationReason']>;
  lastWorkingDay: FormControl<IEmployeeResignation['lastWorkingDay']>;
  createdById: FormControl<IEmployeeResignation['createdById']>;
  uodatedById: FormControl<IEmployeeResignation['uodatedById']>;
  employeeId: FormControl<IEmployeeResignation['employeeId']>;
};

export type EmployeeResignationFormGroup = FormGroup<EmployeeResignationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeResignationFormService {
  createEmployeeResignationFormGroup(employeeResignation: EmployeeResignationFormGroupInput = { id: null }): EmployeeResignationFormGroup {
    const employeeResignationRawValue = {
      ...this.getFormDefaults(),
      ...employeeResignation,
    };
    return new FormGroup<EmployeeResignationFormGroupContent>({
      id: new FormControl(
        { value: employeeResignationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdAt: new FormControl(employeeResignationRawValue.createdAt),
      updatedAt: new FormControl(employeeResignationRawValue.updatedAt),
      resignationDate: new FormControl(employeeResignationRawValue.resignationDate),
      approvalStatus: new FormControl(employeeResignationRawValue.approvalStatus),
      approvalComment: new FormControl(employeeResignationRawValue.approvalComment),
      isSalaryHold: new FormControl(employeeResignationRawValue.isSalaryHold),
      isFestivalBonusHold: new FormControl(employeeResignationRawValue.isFestivalBonusHold),
      resignationReason: new FormControl(employeeResignationRawValue.resignationReason, {
        validators: [CustomValidator.naturalTextValidator()]
      }),
      lastWorkingDay: new FormControl(employeeResignationRawValue.lastWorkingDay, {
        validators: [Validators.required],
      }),
      createdById: new FormControl(employeeResignationRawValue.createdById),
      uodatedById: new FormControl(employeeResignationRawValue.uodatedById),
      employeeId: new FormControl(employeeResignationRawValue.employeeId),
    });
  }

  getEmployeeResignation(form: EmployeeResignationFormGroup): IEmployeeResignation | NewEmployeeResignation {
    return form.getRawValue() as IEmployeeResignation | NewEmployeeResignation;
  }

  resetForm(form: EmployeeResignationFormGroup, employeeResignation: EmployeeResignationFormGroupInput): void {
    const employeeResignationRawValue = { ...this.getFormDefaults(), ...employeeResignation };
    form.reset(
      {
        ...employeeResignationRawValue,
        id: { value: employeeResignationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeResignationFormDefaults {
    return {
      id: null,
      isSalaryHold: false,
      isFestivalBonusHold: false,
    };
  }
}
