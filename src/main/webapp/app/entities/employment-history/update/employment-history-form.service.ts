import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmploymentHistory, NewEmploymentHistory } from '../employment-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmploymentHistory for edit and NewEmploymentHistoryFormGroupInput for create.
 */
type EmploymentHistoryFormGroupInput = IEmploymentHistory | PartialWithRequiredKeyOf<NewEmploymentHistory>;

type EmploymentHistoryFormDefaults = Pick<NewEmploymentHistory, 'id' | 'isModifiable'>;

type EmploymentHistoryFormGroupContent = {
  id: FormControl<IEmploymentHistory['id'] | NewEmploymentHistory['id']>;
  referenceId: FormControl<IEmploymentHistory['referenceId']>;
  pin: FormControl<IEmploymentHistory['pin']>;
  eventType: FormControl<IEmploymentHistory['eventType']>;
  effectiveDate: FormControl<IEmploymentHistory['effectiveDate']>;
  previousMainGrossSalary: FormControl<IEmploymentHistory['previousMainGrossSalary']>;
  currentMainGrossSalary: FormControl<IEmploymentHistory['currentMainGrossSalary']>;
  previousWorkingHour: FormControl<IEmploymentHistory['previousWorkingHour']>;
  changedWorkingHour: FormControl<IEmploymentHistory['changedWorkingHour']>;
  isModifiable: FormControl<IEmploymentHistory['isModifiable']>;
  previousDesignation: FormControl<IEmploymentHistory['previousDesignation']>;
  changedDesignation: FormControl<IEmploymentHistory['changedDesignation']>;
  previousDepartment: FormControl<IEmploymentHistory['previousDepartment']>;
  changedDepartment: FormControl<IEmploymentHistory['changedDepartment']>;
  previousReportingTo: FormControl<IEmploymentHistory['previousReportingTo']>;
  changedReportingTo: FormControl<IEmploymentHistory['changedReportingTo']>;
  employee: FormControl<IEmploymentHistory['employee']>;
  previousUnit: FormControl<IEmploymentHistory['previousUnit']>;
  changedUnit: FormControl<IEmploymentHistory['changedUnit']>;
  previousBand: FormControl<IEmploymentHistory['previousBand']>;
  changedBand: FormControl<IEmploymentHistory['changedBand']>;
};

export type EmploymentHistoryFormGroup = FormGroup<EmploymentHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmploymentHistoryFormService {
  createEmploymentHistoryFormGroup(employmentHistory: EmploymentHistoryFormGroupInput = { id: null }): EmploymentHistoryFormGroup {
    const employmentHistoryRawValue = {
      ...this.getFormDefaults(),
      ...employmentHistory,
    };
    return new FormGroup<EmploymentHistoryFormGroupContent>({
      id: new FormControl(
        { value: employmentHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      referenceId: new FormControl(employmentHistoryRawValue.referenceId),
      pin: new FormControl(employmentHistoryRawValue.pin),
      eventType: new FormControl(employmentHistoryRawValue.eventType),
      effectiveDate: new FormControl(employmentHistoryRawValue.effectiveDate),
      previousMainGrossSalary: new FormControl(employmentHistoryRawValue.previousMainGrossSalary),
      currentMainGrossSalary: new FormControl(employmentHistoryRawValue.currentMainGrossSalary),
      previousWorkingHour: new FormControl(employmentHistoryRawValue.previousWorkingHour),
      changedWorkingHour: new FormControl(employmentHistoryRawValue.changedWorkingHour),
      isModifiable: new FormControl(employmentHistoryRawValue.isModifiable),
      previousDesignation: new FormControl(employmentHistoryRawValue.previousDesignation),
      changedDesignation: new FormControl(employmentHistoryRawValue.changedDesignation),
      previousDepartment: new FormControl(employmentHistoryRawValue.previousDepartment),
      changedDepartment: new FormControl(employmentHistoryRawValue.changedDepartment),
      previousReportingTo: new FormControl(employmentHistoryRawValue.previousReportingTo),
      changedReportingTo: new FormControl(employmentHistoryRawValue.changedReportingTo),
      employee: new FormControl(employmentHistoryRawValue.employee),
      previousUnit: new FormControl(employmentHistoryRawValue.previousUnit),
      changedUnit: new FormControl(employmentHistoryRawValue.changedUnit),
      previousBand: new FormControl(employmentHistoryRawValue.previousBand),
      changedBand: new FormControl(employmentHistoryRawValue.changedBand),
    });
  }

  getEmploymentHistory(form: EmploymentHistoryFormGroup): IEmploymentHistory | NewEmploymentHistory {
    return form.getRawValue() as IEmploymentHistory | NewEmploymentHistory;
  }

  resetForm(form: EmploymentHistoryFormGroup, employmentHistory: EmploymentHistoryFormGroupInput): void {
    const employmentHistoryRawValue = { ...this.getFormDefaults(), ...employmentHistory };
    form.reset(
      {
        ...employmentHistoryRawValue,
        id: { value: employmentHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmploymentHistoryFormDefaults {
    return {
      id: null,
      isModifiable: false,
    };
  }
}
