import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAttendance, NewAttendance } from '../attendance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttendance for edit and NewAttendanceFormGroupInput for create.
 */
type AttendanceFormGroupInput = IAttendance | PartialWithRequiredKeyOf<NewAttendance>;

type AttendanceFormDefaults = Pick<NewAttendance, 'id'>;

type AttendanceFormGroupContent = {
  id: FormControl<IAttendance['id'] | NewAttendance['id']>;
  year: FormControl<IAttendance['year']>;
  month: FormControl<IAttendance['month']>;
  absentDays: FormControl<IAttendance['absentDays']>;
  fractionDays: FormControl<IAttendance['fractionDays']>;
  compensatoryLeaveGained: FormControl<IAttendance['compensatoryLeaveGained']>;
  employee: FormControl<IAttendance['employee']>;
};

export type AttendanceFormGroup = FormGroup<AttendanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttendanceFormService {
  createAttendanceFormGroup(attendance: AttendanceFormGroupInput = { id: null }): AttendanceFormGroup {
    const attendanceRawValue = {
      ...this.getFormDefaults(),
      ...attendance,
    };
    return new FormGroup<AttendanceFormGroupContent>({
      id: new FormControl(
        { value: attendanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      year: new FormControl(attendanceRawValue.year, {
        validators: [Validators.min(1990), Validators.max(2099)],
      }),
      month: new FormControl(attendanceRawValue.month, {
        validators: [Validators.min(1), Validators.max(12)],
      }),
      absentDays: new FormControl(attendanceRawValue.absentDays),
      fractionDays: new FormControl(attendanceRawValue.fractionDays),
      compensatoryLeaveGained: new FormControl(attendanceRawValue.compensatoryLeaveGained),
      employee: new FormControl(attendanceRawValue.employee),
    });
  }

  getAttendance(form: AttendanceFormGroup): IAttendance | NewAttendance {
    return form.getRawValue() as IAttendance | NewAttendance;
  }

  resetForm(form: AttendanceFormGroup, attendance: AttendanceFormGroupInput): void {
    const attendanceRawValue = { ...this.getFormDefaults(), ...attendance };
    form.reset(
      {
        ...attendanceRawValue,
        id: { value: attendanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AttendanceFormDefaults {
    return {
      id: null,
    };
  }
}
