import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IManualAttendanceEntry, NewManualAttendanceEntry } from '../manual-attendance-entry.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IManualAttendanceEntry for edit and NewManualAttendanceEntryFormGroupInput for create.
 */
type ManualAttendanceEntryFormGroupInput = IManualAttendanceEntry | PartialWithRequiredKeyOf<NewManualAttendanceEntry>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IManualAttendanceEntry | NewManualAttendanceEntry> = Omit<T, 'inTime' | 'outTime'> & {
  inTime?: string | null;
  outTime?: string | null;
};

type ManualAttendanceEntryFormRawValue = FormValueOf<IManualAttendanceEntry>;

type NewManualAttendanceEntryFormRawValue = FormValueOf<NewManualAttendanceEntry>;

type ManualAttendanceEntryFormDefaults = Pick<
  NewManualAttendanceEntry,
  'id' | 'inTime' | 'outTime' | 'isLineManagerApproved' | 'isHRApproved' | 'isRejected'
>;

type ManualAttendanceEntryFormGroupContent = {
  id: FormControl<ManualAttendanceEntryFormRawValue['id'] | NewManualAttendanceEntry['id']>;
  date: FormControl<ManualAttendanceEntryFormRawValue['date']>;
  inTime: FormControl<ManualAttendanceEntryFormRawValue['inTime']>;
  inNote: FormControl<ManualAttendanceEntryFormRawValue['inNote']>;
  outTime: FormControl<ManualAttendanceEntryFormRawValue['outTime']>;
  outNote: FormControl<ManualAttendanceEntryFormRawValue['outNote']>;
  isLineManagerApproved: FormControl<ManualAttendanceEntryFormRawValue['isLineManagerApproved']>;
  isHRApproved: FormControl<ManualAttendanceEntryFormRawValue['isHRApproved']>;
  isRejected: FormControl<ManualAttendanceEntryFormRawValue['isRejected']>;
  rejectionComment: FormControl<ManualAttendanceEntryFormRawValue['rejectionComment']>;
  note: FormControl<ManualAttendanceEntryFormRawValue['note']>;
  employee: FormControl<ManualAttendanceEntryFormRawValue['employee']>;
};

export type ManualAttendanceEntryFormGroup = FormGroup<ManualAttendanceEntryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ManualAttendanceEntryFormService {
  createManualAttendanceEntryFormGroup(
    manualAttendanceEntry: ManualAttendanceEntryFormGroupInput = { id: null }
  ): ManualAttendanceEntryFormGroup {
    const manualAttendanceEntryRawValue = this.convertManualAttendanceEntryToManualAttendanceEntryRawValue({
      ...this.getFormDefaults(),
      ...manualAttendanceEntry,
    });
    return new FormGroup<ManualAttendanceEntryFormGroupContent>({
      id: new FormControl(
        { value: manualAttendanceEntryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(manualAttendanceEntryRawValue.date),
      inTime: new FormControl(manualAttendanceEntryRawValue.inTime),
      inNote: new FormControl(manualAttendanceEntryRawValue.inNote),
      outTime: new FormControl(manualAttendanceEntryRawValue.outTime),
      outNote: new FormControl(manualAttendanceEntryRawValue.outNote),
      isLineManagerApproved: new FormControl(manualAttendanceEntryRawValue.isLineManagerApproved),
      isHRApproved: new FormControl(manualAttendanceEntryRawValue.isHRApproved),
      isRejected: new FormControl(manualAttendanceEntryRawValue.isRejected),
      rejectionComment: new FormControl(manualAttendanceEntryRawValue.rejectionComment),
      note: new FormControl(manualAttendanceEntryRawValue.note, {
        validators: [Validators.minLength(0), Validators.maxLength(250)],
      }),
      employee: new FormControl(manualAttendanceEntryRawValue.employee),
    });
  }

  getManualAttendanceEntry(form: ManualAttendanceEntryFormGroup): IManualAttendanceEntry | NewManualAttendanceEntry {
    return this.convertManualAttendanceEntryRawValueToManualAttendanceEntry(
      form.getRawValue() as ManualAttendanceEntryFormRawValue | NewManualAttendanceEntryFormRawValue
    );
  }

  resetForm(form: ManualAttendanceEntryFormGroup, manualAttendanceEntry: ManualAttendanceEntryFormGroupInput): void {
    const manualAttendanceEntryRawValue = this.convertManualAttendanceEntryToManualAttendanceEntryRawValue({
      ...this.getFormDefaults(),
      ...manualAttendanceEntry,
    });
    form.reset(
      {
        ...manualAttendanceEntryRawValue,
        id: { value: manualAttendanceEntryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ManualAttendanceEntryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      inTime: currentTime,
      outTime: currentTime,
      isLineManagerApproved: false,
      isHRApproved: false,
      isRejected: false,
    };
  }

  private convertManualAttendanceEntryRawValueToManualAttendanceEntry(
    rawManualAttendanceEntry: ManualAttendanceEntryFormRawValue | NewManualAttendanceEntryFormRawValue
  ): IManualAttendanceEntry | NewManualAttendanceEntry {
    return {
      ...rawManualAttendanceEntry,
      inTime: dayjs(rawManualAttendanceEntry.inTime, DATE_TIME_FORMAT),
      outTime: dayjs(rawManualAttendanceEntry.outTime, DATE_TIME_FORMAT),
    };
  }

  private convertManualAttendanceEntryToManualAttendanceEntryRawValue(
    manualAttendanceEntry: IManualAttendanceEntry | (Partial<NewManualAttendanceEntry> & ManualAttendanceEntryFormDefaults)
  ): ManualAttendanceEntryFormRawValue | PartialWithRequiredKeyOf<NewManualAttendanceEntryFormRawValue> {
    return {
      ...manualAttendanceEntry,
      inTime: manualAttendanceEntry.inTime ? manualAttendanceEntry.inTime.format(DATE_TIME_FORMAT) : undefined,
      outTime: manualAttendanceEntry.outTime ? manualAttendanceEntry.outTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
