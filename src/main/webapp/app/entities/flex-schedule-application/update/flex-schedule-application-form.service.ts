import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFlexScheduleApplication, NewFlexScheduleApplication } from '../flex-schedule-application.model';
import { Status } from '../../enumerations/status.model';
import { CustomValidator } from '../../../validators/custom-validator';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFlexScheduleApplication for edit and NewFlexScheduleApplicationFormGroupInput for create.
 */
type FlexScheduleApplicationFormGroupInput = IFlexScheduleApplication | PartialWithRequiredKeyOf<NewFlexScheduleApplication>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFlexScheduleApplication | NewFlexScheduleApplication> = Omit<T, 'sanctionedAt' | 'createdAt' | 'updatedAt'> & {
  sanctionedAt?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

type FlexScheduleApplicationFormRawValue = FormValueOf<IFlexScheduleApplication>;

type NewFlexScheduleApplicationFormRawValue = FormValueOf<NewFlexScheduleApplication>;

type FlexScheduleApplicationFormDefaults = Pick<NewFlexScheduleApplication, 'id' | 'sanctionedAt' | 'createdAt' | 'updatedAt'>;

type FlexScheduleApplicationFormGroupContent = {
  id: FormControl<FlexScheduleApplicationFormRawValue['id'] | NewFlexScheduleApplication['id']>;
  effectiveFrom: FormControl<FlexScheduleApplicationFormRawValue['effectiveFrom']>;
  effectiveTo: FormControl<FlexScheduleApplicationFormRawValue['effectiveTo']>;
  reason: FormControl<FlexScheduleApplicationFormRawValue['reason']>;
  status: FormControl<FlexScheduleApplicationFormRawValue['status']>;
  sanctionedAt: FormControl<FlexScheduleApplicationFormRawValue['sanctionedAt']>;
  appliedAt: FormControl<FlexScheduleApplicationFormRawValue['appliedAt']>;
  createdAt: FormControl<FlexScheduleApplicationFormRawValue['createdAt']>;
  updatedAt: FormControl<FlexScheduleApplicationFormRawValue['updatedAt']>;
  requesterId: FormControl<FlexScheduleApplicationFormRawValue['requesterId']>;
  sanctionedById: FormControl<FlexScheduleApplicationFormRawValue['sanctionedById']>;
  appliedById: FormControl<FlexScheduleApplicationFormRawValue['appliedById']>;
  createdById: FormControl<FlexScheduleApplicationFormRawValue['createdById']>;
  updatedById: FormControl<FlexScheduleApplicationFormRawValue['updatedById']>;
  timeSlotId: FormControl<FlexScheduleApplicationFormRawValue['timeSlotId']>;
};

export type FlexScheduleApplicationFormGroup = FormGroup<FlexScheduleApplicationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FlexScheduleApplicationFormService {
  createFlexScheduleApplicationFormGroup(
    flexScheduleApplication: FlexScheduleApplicationFormGroupInput = { id: null }
  ): FlexScheduleApplicationFormGroup {
    const flexScheduleApplicationRawValue = this.convertFlexScheduleApplicationToFlexScheduleApplicationRawValue({
      ...this.getFormDefaults(),
      ...flexScheduleApplication,
    });
    return new FormGroup<FlexScheduleApplicationFormGroupContent>({
      id: new FormControl(
        { value: flexScheduleApplicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      effectiveFrom: new FormControl(flexScheduleApplicationRawValue.effectiveFrom, {
        validators: [Validators.required],
      }),
      effectiveTo: new FormControl(flexScheduleApplicationRawValue.effectiveTo, {
        validators: [Validators.required],
      }),
      reason: new FormControl(flexScheduleApplicationRawValue.reason,
        {
          validators: CustomValidator.naturalTextValidator()
        }),
      status: new FormControl(flexScheduleApplicationRawValue.status || Status.PENDING, {
        validators: [Validators.required],
      }),
      sanctionedAt: new FormControl(flexScheduleApplicationRawValue.sanctionedAt),
      appliedAt: new FormControl(flexScheduleApplicationRawValue.appliedAt),
      createdAt: new FormControl(flexScheduleApplicationRawValue.createdAt),
      updatedAt: new FormControl(flexScheduleApplicationRawValue.updatedAt),
      requesterId: new FormControl(flexScheduleApplicationRawValue.requesterId, {
        validators: [Validators.required],
      }),
      sanctionedById: new FormControl(flexScheduleApplicationRawValue.sanctionedById),
      appliedById: new FormControl(flexScheduleApplicationRawValue.appliedById),
      createdById: new FormControl(flexScheduleApplicationRawValue.createdById),
      updatedById: new FormControl(flexScheduleApplicationRawValue.updatedById),
      timeSlotId: new FormControl(flexScheduleApplicationRawValue.timeSlotId),
    });
  }

  getFlexScheduleApplication(form: FlexScheduleApplicationFormGroup): IFlexScheduleApplication | NewFlexScheduleApplication {
    return this.convertFlexScheduleApplicationRawValueToFlexScheduleApplication(
      form.getRawValue() as FlexScheduleApplicationFormRawValue | NewFlexScheduleApplicationFormRawValue
    );
  }

  resetForm(form: FlexScheduleApplicationFormGroup, flexScheduleApplication: FlexScheduleApplicationFormGroupInput): void {
    const flexScheduleApplicationRawValue = this.convertFlexScheduleApplicationToFlexScheduleApplicationRawValue({
      ...this.getFormDefaults(),
      ...flexScheduleApplication,
    });
    form.reset(
      {
        ...flexScheduleApplicationRawValue,
        id: { value: flexScheduleApplicationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FlexScheduleApplicationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      sanctionedAt: currentTime,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertFlexScheduleApplicationRawValueToFlexScheduleApplication(
    rawFlexScheduleApplication: FlexScheduleApplicationFormRawValue | NewFlexScheduleApplicationFormRawValue
  ): IFlexScheduleApplication | NewFlexScheduleApplication {
    return {
      ...rawFlexScheduleApplication,
      sanctionedAt: dayjs(rawFlexScheduleApplication.sanctionedAt, DATE_TIME_FORMAT),
      createdAt: dayjs(rawFlexScheduleApplication.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawFlexScheduleApplication.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertFlexScheduleApplicationToFlexScheduleApplicationRawValue(
    flexScheduleApplication: IFlexScheduleApplication | (Partial<NewFlexScheduleApplication> & FlexScheduleApplicationFormDefaults)
  ): FlexScheduleApplicationFormRawValue | PartialWithRequiredKeyOf<NewFlexScheduleApplicationFormRawValue> {
    return {
      ...flexScheduleApplication,
      sanctionedAt: flexScheduleApplication.sanctionedAt ? flexScheduleApplication.sanctionedAt.format(DATE_TIME_FORMAT) : undefined,
      createdAt: flexScheduleApplication.createdAt ? flexScheduleApplication.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: flexScheduleApplication.updatedAt ? flexScheduleApplication.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
