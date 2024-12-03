import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAttendanceSyncCache, NewAttendanceSyncCache } from '../attendance-sync-cache.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttendanceSyncCache for edit and NewAttendanceSyncCacheFormGroupInput for create.
 */
type AttendanceSyncCacheFormGroupInput = IAttendanceSyncCache | PartialWithRequiredKeyOf<NewAttendanceSyncCache>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAttendanceSyncCache | NewAttendanceSyncCache> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type AttendanceSyncCacheFormRawValue = FormValueOf<IAttendanceSyncCache>;

type NewAttendanceSyncCacheFormRawValue = FormValueOf<NewAttendanceSyncCache>;

type AttendanceSyncCacheFormDefaults = Pick<NewAttendanceSyncCache, 'id' | 'timestamp'>;

type AttendanceSyncCacheFormGroupContent = {
  id: FormControl<AttendanceSyncCacheFormRawValue['id'] | NewAttendanceSyncCache['id']>;
  employeePin: FormControl<AttendanceSyncCacheFormRawValue['employeePin']>;
  timestamp: FormControl<AttendanceSyncCacheFormRawValue['timestamp']>;
  terminal: FormControl<AttendanceSyncCacheFormRawValue['terminal']>;
};

export type AttendanceSyncCacheFormGroup = FormGroup<AttendanceSyncCacheFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttendanceSyncCacheFormService {
  createAttendanceSyncCacheFormGroup(attendanceSyncCache: AttendanceSyncCacheFormGroupInput = { id: null }): AttendanceSyncCacheFormGroup {
    const attendanceSyncCacheRawValue = this.convertAttendanceSyncCacheToAttendanceSyncCacheRawValue({
      ...this.getFormDefaults(),
      ...attendanceSyncCache,
    });
    return new FormGroup<AttendanceSyncCacheFormGroupContent>({
      id: new FormControl(
        { value: attendanceSyncCacheRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employeePin: new FormControl(attendanceSyncCacheRawValue.employeePin),
      timestamp: new FormControl(attendanceSyncCacheRawValue.timestamp),
      terminal: new FormControl(attendanceSyncCacheRawValue.terminal),
    });
  }

  getAttendanceSyncCache(form: AttendanceSyncCacheFormGroup): IAttendanceSyncCache | NewAttendanceSyncCache {
    return this.convertAttendanceSyncCacheRawValueToAttendanceSyncCache(
      form.getRawValue() as AttendanceSyncCacheFormRawValue | NewAttendanceSyncCacheFormRawValue
    );
  }

  resetForm(form: AttendanceSyncCacheFormGroup, attendanceSyncCache: AttendanceSyncCacheFormGroupInput): void {
    const attendanceSyncCacheRawValue = this.convertAttendanceSyncCacheToAttendanceSyncCacheRawValue({
      ...this.getFormDefaults(),
      ...attendanceSyncCache,
    });
    form.reset(
      {
        ...attendanceSyncCacheRawValue,
        id: { value: attendanceSyncCacheRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AttendanceSyncCacheFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertAttendanceSyncCacheRawValueToAttendanceSyncCache(
    rawAttendanceSyncCache: AttendanceSyncCacheFormRawValue | NewAttendanceSyncCacheFormRawValue
  ): IAttendanceSyncCache | NewAttendanceSyncCache {
    return {
      ...rawAttendanceSyncCache,
      timestamp: dayjs(rawAttendanceSyncCache.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertAttendanceSyncCacheToAttendanceSyncCacheRawValue(
    attendanceSyncCache: IAttendanceSyncCache | (Partial<NewAttendanceSyncCache> & AttendanceSyncCacheFormDefaults)
  ): AttendanceSyncCacheFormRawValue | PartialWithRequiredKeyOf<NewAttendanceSyncCacheFormRawValue> {
    return {
      ...attendanceSyncCache,
      timestamp: attendanceSyncCache.timestamp ? attendanceSyncCache.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
