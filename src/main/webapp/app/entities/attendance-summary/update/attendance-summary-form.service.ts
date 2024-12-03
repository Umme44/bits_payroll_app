import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAttendanceSummary, NewAttendanceSummary } from '../attendance-summary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttendanceSummary for edit and NewAttendanceSummaryFormGroupInput for create.
 */
type AttendanceSummaryFormGroupInput = IAttendanceSummary | PartialWithRequiredKeyOf<NewAttendanceSummary>;

type AttendanceSummaryFormDefaults = Pick<NewAttendanceSummary, 'id'>;

type AttendanceSummaryFormGroupContent = {
  id: FormControl<IAttendanceSummary['id'] | NewAttendanceSummary['id']>;
  month: FormControl<IAttendanceSummary['month']>;
  year: FormControl<IAttendanceSummary['year']>;
  totalWorkingDays: FormControl<IAttendanceSummary['totalWorkingDays']>;
  totalLeaveDays: FormControl<IAttendanceSummary['totalLeaveDays']>;
  totalAbsentDays: FormControl<IAttendanceSummary['totalAbsentDays']>;
  totalFractionDays: FormControl<IAttendanceSummary['totalFractionDays']>;
  attendanceRegularisationStartDate: FormControl<IAttendanceSummary['attendanceRegularisationStartDate']>;
  attendanceRegularisationEndDate: FormControl<IAttendanceSummary['attendanceRegularisationEndDate']>;
  employeeId: FormControl<IAttendanceSummary['employeeId']>;
};

export type AttendanceSummaryFormGroup = FormGroup<AttendanceSummaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttendanceSummaryFormService {
  createAttendanceSummaryFormGroup(attendanceSummary: AttendanceSummaryFormGroupInput = { id: null }): AttendanceSummaryFormGroup {
    const attendanceSummaryRawValue = {
      ...this.getFormDefaults(),
      ...attendanceSummary,
    };
    return new FormGroup<AttendanceSummaryFormGroupContent>({
      id: new FormControl(
        { value: attendanceSummaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),

      month: new FormControl(attendanceSummaryRawValue.month, [Validators.required]),

      year: new FormControl(attendanceSummaryRawValue.year, { validators: [Validators.required] }),

      totalWorkingDays: new FormControl(0, [Validators.required, Validators.max(31), Validators.min(0)]),
      totalLeaveDays: new FormControl(0, [Validators.required, Validators.max(31), Validators.min(0)]),
      totalAbsentDays: new FormControl(0, [Validators.required, Validators.max(31), Validators.min(0)]),
      totalFractionDays: new FormControl(0, [Validators.required, Validators.max(31), Validators.min(0)]),
      attendanceRegularisationStartDate: new FormControl(attendanceSummaryRawValue.attendanceRegularisationStartDate),
      attendanceRegularisationEndDate: new FormControl(attendanceSummaryRawValue.attendanceRegularisationEndDate),
      employeeId: new FormControl(attendanceSummaryRawValue.employeeId, [Validators.required]),

      // totalWorkingDays: new FormControl(0, [Validators.required, Validators.max(31), Validators.min(0)]),
      // totalLeaveDays: new FormControl(attendanceSummaryRawValue.totalLeaveDays),
      // totalAbsentDays: new FormControl(attendanceSummaryRawValue.totalAbsentDays),
      // totalFractionDays: new FormControl(attendanceSummaryRawValue.totalFractionDays),
      // attendanceRegularisationStartDate: new FormControl(attendanceSummaryRawValue.attendanceRegularisationStartDate),
      // attendanceRegularisationEndDate: new FormControl(attendanceSummaryRawValue.attendanceRegularisationEndDate),
      // employee: new FormControl(attendanceSummaryRawValue.employee),
    });
  }

  getAttendanceSummary(form: AttendanceSummaryFormGroup): IAttendanceSummary | NewAttendanceSummary {
    return form.getRawValue() as IAttendanceSummary | NewAttendanceSummary;
  }

  resetForm(form: AttendanceSummaryFormGroup, attendanceSummary: AttendanceSummaryFormGroupInput): void {
    const attendanceSummaryRawValue = { ...this.getFormDefaults(), ...attendanceSummary };
    form.reset(
      {
        ...attendanceSummaryRawValue,
        id: { value: attendanceSummaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AttendanceSummaryFormDefaults {
    return {
      id: null,
    };
  }
}
