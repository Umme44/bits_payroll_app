import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISpecialShiftTiming, NewSpecialShiftTiming } from '../special-shift-timing.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISpecialShiftTiming for edit and NewSpecialShiftTimingFormGroupInput for create.
 */
type SpecialShiftTimingFormGroupInput = ISpecialShiftTiming | PartialWithRequiredKeyOf<NewSpecialShiftTiming>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISpecialShiftTiming | NewSpecialShiftTiming> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type SpecialShiftTimingFormRawValue = FormValueOf<ISpecialShiftTiming>;

type NewSpecialShiftTimingFormRawValue = FormValueOf<NewSpecialShiftTiming>;

type SpecialShiftTimingFormDefaults = Pick<
  NewSpecialShiftTiming,
  'id' | 'overrideRoaster' | 'overrideFlexSchedule' | 'createdAt' | 'updatedAt'
>;

type SpecialShiftTimingFormGroupContent = {
  id: FormControl<SpecialShiftTimingFormRawValue['id'] | NewSpecialShiftTiming['id']>;
  startDate: FormControl<SpecialShiftTimingFormRawValue['startDate']>;
  endDate: FormControl<SpecialShiftTimingFormRawValue['endDate']>;
  overrideRoaster: FormControl<SpecialShiftTimingFormRawValue['overrideRoaster']>;
  overrideFlexSchedule: FormControl<SpecialShiftTimingFormRawValue['overrideFlexSchedule']>;
  remarks: FormControl<SpecialShiftTimingFormRawValue['remarks']>;
  createdAt: FormControl<SpecialShiftTimingFormRawValue['createdAt']>;
  updatedAt: FormControl<SpecialShiftTimingFormRawValue['updatedAt']>;
  reason: FormControl<SpecialShiftTimingFormRawValue['reason']>;
  timeSlotId: FormControl<SpecialShiftTimingFormRawValue['timeSlotId']>;
  createdById: FormControl<SpecialShiftTimingFormRawValue['createdById']>;
  updatedById: FormControl<SpecialShiftTimingFormRawValue['updatedById']>;
};

export type SpecialShiftTimingFormGroup = FormGroup<SpecialShiftTimingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SpecialShiftTimingFormService {
  createSpecialShiftTimingFormGroup(specialShiftTiming: SpecialShiftTimingFormGroupInput = { id: null }): SpecialShiftTimingFormGroup {
    const specialShiftTimingRawValue = this.convertSpecialShiftTimingToSpecialShiftTimingRawValue({
      ...this.getFormDefaults(),
      ...specialShiftTiming,
    });
    return new FormGroup<SpecialShiftTimingFormGroupContent>({
      id: new FormControl(
        { value: specialShiftTimingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(specialShiftTimingRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(specialShiftTimingRawValue.endDate, {
        validators: [Validators.required],
      }),
      overrideRoaster: new FormControl(specialShiftTimingRawValue.overrideRoaster, {
        validators: [Validators.required],
      }),
      overrideFlexSchedule: new FormControl(specialShiftTimingRawValue.overrideFlexSchedule, {
        validators: [Validators.required],
      }),
      remarks: new FormControl(specialShiftTimingRawValue.remarks,
        {validators: [Validators.minLength(0), Validators.maxLength(250), CustomValidator.naturalTextValidator()]}
        ),
      createdAt: new FormControl(specialShiftTimingRawValue.createdAt),
      updatedAt: new FormControl(specialShiftTimingRawValue.updatedAt),
      reason: new FormControl(specialShiftTimingRawValue.reason, {
        validators: [Validators.minLength(3), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      timeSlotId: new FormControl(specialShiftTimingRawValue.timeSlotId, {
        validators: [Validators.required],
      }),
      createdById: new FormControl(specialShiftTimingRawValue.createdById),
      updatedById: new FormControl(specialShiftTimingRawValue.updatedById),
    });
  }

  getSpecialShiftTiming(form: SpecialShiftTimingFormGroup): ISpecialShiftTiming | NewSpecialShiftTiming {
    return this.convertSpecialShiftTimingRawValueToSpecialShiftTiming(
      form.getRawValue() as SpecialShiftTimingFormRawValue | NewSpecialShiftTimingFormRawValue
    );
  }

  resetForm(form: SpecialShiftTimingFormGroup, specialShiftTiming: SpecialShiftTimingFormGroupInput): void {
    const specialShiftTimingRawValue = this.convertSpecialShiftTimingToSpecialShiftTimingRawValue({
      ...this.getFormDefaults(),
      ...specialShiftTiming,
    });
    form.reset(
      {
        ...specialShiftTimingRawValue,
        id: { value: specialShiftTimingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SpecialShiftTimingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      overrideRoaster: false,
      overrideFlexSchedule: false,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertSpecialShiftTimingRawValueToSpecialShiftTiming(
    rawSpecialShiftTiming: SpecialShiftTimingFormRawValue | NewSpecialShiftTimingFormRawValue
  ): ISpecialShiftTiming | NewSpecialShiftTiming {
    return {
      ...rawSpecialShiftTiming,
      createdAt: dayjs(rawSpecialShiftTiming.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawSpecialShiftTiming.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertSpecialShiftTimingToSpecialShiftTimingRawValue(
    specialShiftTiming: ISpecialShiftTiming | (Partial<NewSpecialShiftTiming> & SpecialShiftTimingFormDefaults)
  ): SpecialShiftTimingFormRawValue | PartialWithRequiredKeyOf<NewSpecialShiftTimingFormRawValue> {
    return {
      ...specialShiftTiming,
      createdAt: specialShiftTiming.createdAt ? specialShiftTiming.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: specialShiftTiming.updatedAt ? specialShiftTiming.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
