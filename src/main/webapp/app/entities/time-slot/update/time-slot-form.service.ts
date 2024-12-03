import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT, TIME_FORMAT } from 'app/config/input.constants';
import { ITimeSlot, NewTimeSlot } from '../time-slot.model';
import { CustomValidator } from '../../../validators/custom-validator';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITimeSlot for edit and NewTimeSlotFormGroupInput for create.
 */
type TimeSlotFormGroupInput = ITimeSlot | PartialWithRequiredKeyOf<NewTimeSlot>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITimeSlot | NewTimeSlot> = Omit<T, 'inTime' | 'outTime'> & {
  inTime?: string | null;
  outTime?: string | null;
};

type TimeSlotFormRawValue = FormValueOf<ITimeSlot>;

type NewTimeSlotFormRawValue = FormValueOf<NewTimeSlot>;

type TimeSlotFormDefaults = Pick<NewTimeSlot, 'id' | 'inTime' | 'outTime' | 'isApplicableByEmployee' | 'isDefaultShift'>;

type TimeSlotFormGroupContent = {
  id: FormControl<TimeSlotFormRawValue['id'] | NewTimeSlot['id']>;
  title: FormControl<TimeSlotFormRawValue['title']>;
  inTime: FormControl<TimeSlotFormRawValue['inTime']>;
  outTime: FormControl<TimeSlotFormRawValue['outTime']>;
  isApplicableByEmployee: FormControl<TimeSlotFormRawValue['isApplicableByEmployee']>;
  isDefaultShift: FormControl<TimeSlotFormRawValue['isDefaultShift']>;
  code: FormControl<TimeSlotFormRawValue['code']>;
  weekEnds: FormControl<TimeSlotFormRawValue['weekEnds']>;
  weekEndList: FormControl<TimeSlotFormRawValue['weekEndList']>;
};

export type TimeSlotFormGroup = FormGroup<TimeSlotFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TimeSlotFormService {
  createTimeSlotFormGroup(timeSlot: TimeSlotFormGroupInput = { id: null }): TimeSlotFormGroup {
    const timeSlotRawValue = this.convertTimeSlotToTimeSlotRawValue({
      ...this.getFormDefaults(),
      ...timeSlot,
    });
    return new FormGroup<TimeSlotFormGroupContent>({
      id: new FormControl(
        { value: timeSlotRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(timeSlotRawValue.title, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      inTime: new FormControl(timeSlotRawValue.inTime, {
        validators: [Validators.required],
      }),
      outTime: new FormControl(timeSlotRawValue.outTime, {
        validators: [Validators.required],
      }),
      isApplicableByEmployee: new FormControl(timeSlotRawValue.isApplicableByEmployee),
      isDefaultShift: new FormControl(timeSlotRawValue.isDefaultShift),
      code: new FormControl(timeSlotRawValue.code, {
        validators: [Validators.minLength(0), Validators.maxLength(50)],
      }),
      weekEnds: new FormControl(timeSlotRawValue.weekEnds, {
        validators: [Validators.minLength(0), Validators.maxLength(500)],
      }),
      weekEndList: new FormControl(timeSlotRawValue.weekEndList, {
        validators: [Validators.minLength(0), Validators.maxLength(500)],
      }),
    });
  }

  getTimeSlot(form: TimeSlotFormGroup): ITimeSlot | NewTimeSlot {
    return this.convertTimeSlotRawValueToTimeSlot(form.getRawValue() as TimeSlotFormRawValue | NewTimeSlotFormRawValue);
  }

  resetForm(form: TimeSlotFormGroup, timeSlot: TimeSlotFormGroupInput): void {
    const timeSlotRawValue = this.convertTimeSlotToTimeSlotRawValue({ ...this.getFormDefaults(), ...timeSlot });
    form.reset(
      {
        ...timeSlotRawValue,
        id: { value: timeSlotRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TimeSlotFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      inTime: currentTime,
      outTime: currentTime,
      isApplicableByEmployee: false,
      isDefaultShift: false,
    };
  }

  private convertTimeSlotRawValueToTimeSlot(rawTimeSlot: TimeSlotFormRawValue | NewTimeSlotFormRawValue): ITimeSlot | NewTimeSlot {
    return {
      ...rawTimeSlot,
      inTime: dayjs(rawTimeSlot.inTime, TIME_FORMAT),
      outTime: dayjs(rawTimeSlot.outTime, TIME_FORMAT),
    };
  }

  private convertTimeSlotToTimeSlotRawValue(
    timeSlot: ITimeSlot | (Partial<NewTimeSlot> & TimeSlotFormDefaults)
  ): TimeSlotFormRawValue | PartialWithRequiredKeyOf<NewTimeSlotFormRawValue> {
    return {
      ...timeSlot,
      inTime: timeSlot.inTime ? timeSlot.inTime.format(TIME_FORMAT) : undefined,
      outTime: timeSlot.outTime ? timeSlot.outTime.format(TIME_FORMAT) : undefined,
    };
  }
}
