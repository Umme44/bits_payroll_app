import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWorkFromHomeApplication, NewWorkFromHomeApplication } from '../work-from-home-application.model';
import {Status} from "../../enumerations/status.model";
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkFromHomeApplication for edit and NewWorkFromHomeApplicationFormGroupInput for create.
 */
type WorkFromHomeApplicationFormGroupInput = IWorkFromHomeApplication | PartialWithRequiredKeyOf<NewWorkFromHomeApplication>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWorkFromHomeApplication | NewWorkFromHomeApplication> = Omit<T, 'updatedAt' | 'createdAt' | 'sanctionedAt' | 'approvedStartDate'> & {
  updatedAt?: string | null;
  createdAt?: string | null;
  sanctionedAt?: string | null;
  approvedStartDate?: string | null;
};

type WorkFromHomeApplicationFormRawValue = FormValueOf<IWorkFromHomeApplication>;

type NewWorkFromHomeApplicationFormRawValue = FormValueOf<NewWorkFromHomeApplication>;

type WorkFromHomeApplicationFormDefaults = Pick<NewWorkFromHomeApplication, 'id' | 'updatedAt' | 'createdAt' | 'sanctionedAt'>;

type WorkFromHomeApplicationFormGroupContent = {
  id: FormControl<WorkFromHomeApplicationFormRawValue['id'] | NewWorkFromHomeApplication['id']>;
  startDate: FormControl<WorkFromHomeApplicationFormRawValue['startDate']>;
  endDate: FormControl<WorkFromHomeApplicationFormRawValue['endDate']>;
  reason: FormControl<WorkFromHomeApplicationFormRawValue['reason']>;
  duration: FormControl<WorkFromHomeApplicationFormRawValue['duration']>;
  status: FormControl<WorkFromHomeApplicationFormRawValue['status']>;
  appliedAt: FormControl<WorkFromHomeApplicationFormRawValue['appliedAt']>;
  updatedAt: FormControl<WorkFromHomeApplicationFormRawValue['updatedAt']>;
  createdAt: FormControl<WorkFromHomeApplicationFormRawValue['createdAt']>;
  sanctionedAt: FormControl<WorkFromHomeApplicationFormRawValue['sanctionedAt']>;
  appliedById: FormControl<WorkFromHomeApplicationFormRawValue['appliedById']>;
  createdById: FormControl<WorkFromHomeApplicationFormRawValue['createdById']>;
  updatedById: FormControl<WorkFromHomeApplicationFormRawValue['updatedById']>;
  sanctionedById: FormControl<WorkFromHomeApplicationFormRawValue['sanctionedById']>;
  employeeId: FormControl<WorkFromHomeApplicationFormRawValue['employeeId']>;
};

export type WorkFromHomeApplicationFormGroup = FormGroup<WorkFromHomeApplicationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkFromHomeApplicationFormService {
  createWorkFromHomeApplicationFormGroup(
    workFromHomeApplication: WorkFromHomeApplicationFormGroupInput = { id: null }
  ): WorkFromHomeApplicationFormGroup {
    const workFromHomeApplicationRawValue = this.convertWorkFromHomeApplicationToWorkFromHomeApplicationRawValue({
      ...this.getFormDefaults(),
      ...workFromHomeApplication,
    });
    return new FormGroup<WorkFromHomeApplicationFormGroupContent>({
      id: new FormControl(
        { value: workFromHomeApplicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(workFromHomeApplicationRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(workFromHomeApplicationRawValue.endDate, {
        validators: [Validators.required],
      }),
      reason: new FormControl(workFromHomeApplicationRawValue.reason, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      duration: new FormControl(workFromHomeApplicationRawValue.duration),
      status: new FormControl(workFromHomeApplicationRawValue.status),
      appliedAt: new FormControl(workFromHomeApplicationRawValue.appliedAt),
      updatedAt: new FormControl(workFromHomeApplicationRawValue.updatedAt),
      createdAt: new FormControl(workFromHomeApplicationRawValue.createdAt),
      sanctionedAt: new FormControl(workFromHomeApplicationRawValue.sanctionedAt),
      appliedById: new FormControl(workFromHomeApplicationRawValue.appliedById),
      createdById: new FormControl(workFromHomeApplicationRawValue.createdById),
      updatedById: new FormControl(workFromHomeApplicationRawValue.updatedById),
      sanctionedById: new FormControl(workFromHomeApplicationRawValue.sanctionedById),
      employeeId: new FormControl(workFromHomeApplicationRawValue.employeeId, {
        validators: [Validators.required],
      }),
    });
  }

  getWorkFromHomeApplication(form: WorkFromHomeApplicationFormGroup): IWorkFromHomeApplication | NewWorkFromHomeApplication {
    return this.convertWorkFromHomeApplicationRawValueToWorkFromHomeApplication(
      form.getRawValue() as WorkFromHomeApplicationFormRawValue | NewWorkFromHomeApplicationFormRawValue
    );
  }

  resetForm(form: WorkFromHomeApplicationFormGroup, workFromHomeApplication: WorkFromHomeApplicationFormGroupInput): void {
    const workFromHomeApplicationRawValue = this.convertWorkFromHomeApplicationToWorkFromHomeApplicationRawValue({
      ...this.getFormDefaults(),
      ...workFromHomeApplication,
    });
    form.reset(
      {
        ...workFromHomeApplicationRawValue,
        id: { value: workFromHomeApplicationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WorkFromHomeApplicationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      updatedAt: currentTime,
      createdAt: currentTime,
      sanctionedAt: currentTime,
    };
  }

  private convertWorkFromHomeApplicationRawValueToWorkFromHomeApplication(
    rawWorkFromHomeApplication: WorkFromHomeApplicationFormRawValue | NewWorkFromHomeApplicationFormRawValue
  ): IWorkFromHomeApplication | NewWorkFromHomeApplication {
    return {
      ...rawWorkFromHomeApplication,
      updatedAt: dayjs(rawWorkFromHomeApplication.updatedAt, DATE_TIME_FORMAT),
      createdAt: dayjs(rawWorkFromHomeApplication.createdAt, DATE_TIME_FORMAT),
      sanctionedAt: dayjs(rawWorkFromHomeApplication.sanctionedAt, DATE_TIME_FORMAT),
      approvedStartDate: dayjs(rawWorkFromHomeApplication.approvedStartDate, DATE_TIME_FORMAT),
      status: Status.PENDING
    };
  }

  private convertWorkFromHomeApplicationToWorkFromHomeApplicationRawValue(
    workFromHomeApplication: IWorkFromHomeApplication | (Partial<NewWorkFromHomeApplication> & WorkFromHomeApplicationFormDefaults)
  ): WorkFromHomeApplicationFormRawValue | PartialWithRequiredKeyOf<NewWorkFromHomeApplicationFormRawValue> {
    return {
      ...workFromHomeApplication,
      updatedAt: workFromHomeApplication.updatedAt ? workFromHomeApplication.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      createdAt: workFromHomeApplication.createdAt ? workFromHomeApplication.createdAt.format(DATE_TIME_FORMAT) : undefined,
      sanctionedAt: workFromHomeApplication.sanctionedAt ? workFromHomeApplication.sanctionedAt.format(DATE_TIME_FORMAT) : undefined,
      approvedStartDate: workFromHomeApplication.approvedStartDate ? workFromHomeApplication.approvedStartDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
