import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFlexSchedule, NewFlexSchedule } from '../flex-schedule.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFlexSchedule for edit and NewFlexScheduleFormGroupInput for create.
 */
type FlexScheduleFormGroupInput = IFlexSchedule | PartialWithRequiredKeyOf<NewFlexSchedule>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFlexSchedule | NewFlexSchedule> = Omit<T, 'inTime' | 'outTime'> & {
  inTime?: string | null;
  outTime?: string | null;
};

type FlexScheduleFormRawValue = FormValueOf<IFlexSchedule>;

type NewFlexScheduleFormRawValue = FormValueOf<NewFlexSchedule>;

type FlexScheduleFormDefaults = Pick<NewFlexSchedule, 'id' | 'inTime' | 'outTime'>;

type FlexScheduleFormGroupContent = {
  id: FormControl<FlexScheduleFormRawValue['id'] | NewFlexSchedule['id']>;
  effectiveDate: FormControl<FlexScheduleFormRawValue['effectiveDate']>;
  inTime: FormControl<FlexScheduleFormRawValue['inTime']>;
  outTime: FormControl<FlexScheduleFormRawValue['outTime']>;
  employee: FormControl<FlexScheduleFormRawValue['employee']>;
  createdBy: FormControl<FlexScheduleFormRawValue['createdBy']>;
};

export type FlexScheduleFormGroup = FormGroup<FlexScheduleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FlexScheduleFormService {
  createFlexScheduleFormGroup(flexSchedule: FlexScheduleFormGroupInput = { id: null }): FlexScheduleFormGroup {
    const flexScheduleRawValue = this.convertFlexScheduleToFlexScheduleRawValue({
      ...this.getFormDefaults(),
      ...flexSchedule,
    });
    return new FormGroup<FlexScheduleFormGroupContent>({
      id: new FormControl(
        { value: flexScheduleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      effectiveDate: new FormControl(flexScheduleRawValue.effectiveDate, {
        validators: [Validators.required],
      }),
      inTime: new FormControl(flexScheduleRawValue.inTime, {
        validators: [Validators.required],
      }),
      outTime: new FormControl(flexScheduleRawValue.outTime, {
        validators: [Validators.required],
      }),
      employee: new FormControl(flexScheduleRawValue.employee, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(flexScheduleRawValue.createdBy, {
        validators: [Validators.required],
      }),
    });
  }

  getFlexSchedule(form: FlexScheduleFormGroup): IFlexSchedule | NewFlexSchedule {
    return this.convertFlexScheduleRawValueToFlexSchedule(form.getRawValue() as FlexScheduleFormRawValue | NewFlexScheduleFormRawValue);
  }

  resetForm(form: FlexScheduleFormGroup, flexSchedule: FlexScheduleFormGroupInput): void {
    const flexScheduleRawValue = this.convertFlexScheduleToFlexScheduleRawValue({ ...this.getFormDefaults(), ...flexSchedule });
    form.reset(
      {
        ...flexScheduleRawValue,
        id: { value: flexScheduleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FlexScheduleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      inTime: currentTime,
      outTime: currentTime,
    };
  }

  private convertFlexScheduleRawValueToFlexSchedule(
    rawFlexSchedule: FlexScheduleFormRawValue | NewFlexScheduleFormRawValue
  ): IFlexSchedule | NewFlexSchedule {
    return {
      ...rawFlexSchedule,
      inTime: dayjs(rawFlexSchedule.inTime, DATE_TIME_FORMAT),
      outTime: dayjs(rawFlexSchedule.outTime, DATE_TIME_FORMAT),
    };
  }

  private convertFlexScheduleToFlexScheduleRawValue(
    flexSchedule: IFlexSchedule | (Partial<NewFlexSchedule> & FlexScheduleFormDefaults)
  ): FlexScheduleFormRawValue | PartialWithRequiredKeyOf<NewFlexScheduleFormRawValue> {
    return {
      ...flexSchedule,
      inTime: flexSchedule.inTime ? flexSchedule.inTime.format(DATE_TIME_FORMAT) : undefined,
      outTime: flexSchedule.outTime ? flexSchedule.outTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
