import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT, TIME_FORMAT } from 'app/config/input.constants';
import { IAttendanceEntry, NewAttendanceEntry } from '../attendance-entry.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttendanceEntry for edit and NewAttendanceEntryFormGroupInput for create.
 */
type AttendanceEntryFormGroupInput = IAttendanceEntry | PartialWithRequiredKeyOf<NewAttendanceEntry>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAttendanceEntry | NewAttendanceEntry> = Omit<T, 'inTime' | 'outTime'> & {
  inTime?: string | null;
  outTime?: string | null;
};


type AttendanceEntryFormRawValue = FormValueOf<IAttendanceEntry>;

type NewAttendanceEntryFormRawValue = FormValueOf<NewAttendanceEntry>;

type AttendanceEntryFormDefaults = Pick<NewAttendanceEntry, 'id' | 'inTime' | 'outTime'>;

type AttendanceEntryFormGroupContent = {
  id: FormControl<AttendanceEntryFormRawValue['id'] | NewAttendanceEntry['id']>;
  date: FormControl<AttendanceEntryFormRawValue['date']>;
  inTime: FormControl<AttendanceEntryFormRawValue['inTime']>;
  inNote: FormControl<AttendanceEntryFormRawValue['inNote']>;
  outTime: FormControl<AttendanceEntryFormRawValue['outTime']>;
  outNote: FormControl<AttendanceEntryFormRawValue['outNote']>;
  status: FormControl<AttendanceEntryFormRawValue['status']>;
  employeeId: FormControl<AttendanceEntryFormRawValue['employeeId']>;
};

export type AttendanceEntryFormGroup = FormGroup<AttendanceEntryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttendanceEntryFormService {
  createAttendanceEntryFormGroup(attendanceEntry: AttendanceEntryFormGroupInput = { id: null }): AttendanceEntryFormGroup {
    const attendanceEntryRawValue = this.convertAttendanceEntryToAttendanceEntryRawValue({
      ...this.getFormDefaults(),
      ...attendanceEntry,
    });
    return new FormGroup<AttendanceEntryFormGroupContent>({
      id: new FormControl(
        { value: attendanceEntryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(attendanceEntryRawValue.date, {
        validators: [Validators.required],
      }),
      inTime: new FormControl(attendanceEntryRawValue.inTime, {
        validators: [Validators.required],
      }),
      inNote: new FormControl(attendanceEntryRawValue.inNote, {
        validators: [Validators.maxLength(255), CustomValidator.naturalTextValidator()],
      }),
      outTime: new FormControl(attendanceEntryRawValue.outTime, {
        validators: [Validators.required],
      }),
      outNote: new FormControl(attendanceEntryRawValue.outNote, {
        validators: [Validators.maxLength(255), CustomValidator.naturalTextValidator()],
      }),
      status: new FormControl(attendanceEntryRawValue.status),
      employeeId: new FormControl(attendanceEntryRawValue.employeeId, {
        validators: [Validators.required],
      }),
    });
  }

  getAttendanceEntry(form: AttendanceEntryFormGroup): IAttendanceEntry | NewAttendanceEntry {
    return this.convertAttendanceEntryRawValueToAttendanceEntry(
      form.getRawValue() as AttendanceEntryFormRawValue | NewAttendanceEntryFormRawValue
    );
  }

  resetForm(form: AttendanceEntryFormGroup, attendanceEntry: AttendanceEntryFormGroupInput): void {
    const attendanceEntryRawValue = this.convertAttendanceEntryToAttendanceEntryRawValue({ ...this.getFormDefaults(), ...attendanceEntry });
    form.reset(
      {
        ...attendanceEntryRawValue,
        id: { value: attendanceEntryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AttendanceEntryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      inTime: null,
      outTime: null,
    };
  }

  private convertAttendanceEntryRawValueToAttendanceEntry(
    rawAttendanceEntry: AttendanceEntryFormRawValue | NewAttendanceEntryFormRawValue
  ): IAttendanceEntry | NewAttendanceEntry {
    return {
      ...rawAttendanceEntry,
      inTime: dayjs(rawAttendanceEntry.inTime, TIME_FORMAT),
      outTime: dayjs(rawAttendanceEntry.outTime, TIME_FORMAT),
    };
  }

  private convertAttendanceEntryToAttendanceEntryRawValue(
    attendanceEntry: IAttendanceEntry | (Partial<NewAttendanceEntry> & AttendanceEntryFormDefaults)
  ): AttendanceEntryFormRawValue | PartialWithRequiredKeyOf<NewAttendanceEntryFormRawValue> {
    return {
      ...attendanceEntry,
      inTime: attendanceEntry.inTime ? attendanceEntry.inTime.format(TIME_FORMAT) : undefined,
      outTime: attendanceEntry.outTime ? attendanceEntry.outTime.format(TIME_FORMAT) : undefined,
    };
  }
}
