import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILeaveApplication, NewLeaveApplication } from '../leave-application.model';
import { CustomValidator } from '../../../validators/custom-validator';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILeaveApplication for edit and NewLeaveApplicationFormGroupInput for create.
 */
type LeaveApplicationFormGroupInput = ILeaveApplication | PartialWithRequiredKeyOf<NewLeaveApplication>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILeaveApplication | NewLeaveApplication> = Omit<T, 'sanctionedAt'> & {
  sanctionedAt?: string | null;
};

type LeaveApplicationFormRawValue = FormValueOf<ILeaveApplication>;

type NewLeaveApplicationFormRawValue = FormValueOf<NewLeaveApplication>;

type LeaveApplicationFormDefaults = Pick<
  NewLeaveApplication,
  'id' | 'isLineManagerApproved' | 'isHRApproved' | 'isRejected' | 'isHalfDay' | 'sanctionedAt'
>;

type LeaveApplicationFormGroupContent = {
  id: FormControl<LeaveApplicationFormRawValue['id'] | NewLeaveApplication['id']>;
  applicationDate: FormControl<LeaveApplicationFormRawValue['applicationDate']>;
  leaveType: FormControl<LeaveApplicationFormRawValue['leaveType']>;
  description: FormControl<LeaveApplicationFormRawValue['description']>;
  startDate: FormControl<LeaveApplicationFormRawValue['startDate']>;
  endDate: FormControl<LeaveApplicationFormRawValue['endDate']>;
  isLineManagerApproved: FormControl<LeaveApplicationFormRawValue['isLineManagerApproved']>;
  isHRApproved: FormControl<LeaveApplicationFormRawValue['isHRApproved']>;
  isRejected: FormControl<LeaveApplicationFormRawValue['isRejected']>;
  rejectionComment: FormControl<LeaveApplicationFormRawValue['rejectionComment']>;
  isHalfDay: FormControl<LeaveApplicationFormRawValue['isHalfDay']>;
  durationInDay: FormControl<LeaveApplicationFormRawValue['durationInDay']>;
  phoneNumberOnLeave: FormControl<LeaveApplicationFormRawValue['phoneNumberOnLeave']>;
  addressOnLeave: FormControl<LeaveApplicationFormRawValue['addressOnLeave']>;
  reason: FormControl<LeaveApplicationFormRawValue['reason']>;
  employeeId: FormControl<LeaveApplicationFormRawValue['employeeId']>
};

export type LeaveApplicationFormGroup = FormGroup<LeaveApplicationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LeaveApplicationFormService {
  createLeaveApplicationFormGroup(leaveApplication: LeaveApplicationFormGroupInput = { id: null }): LeaveApplicationFormGroup {
    const leaveApplicationRawValue = this.convertLeaveApplicationToLeaveApplicationRawValue({
      ...this.getFormDefaults(),
      ...leaveApplication,
    });
    return new FormGroup<LeaveApplicationFormGroupContent>({
      id: new FormControl(
        { value: leaveApplicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      applicationDate: new FormControl(leaveApplicationRawValue.applicationDate),
      leaveType: new FormControl(leaveApplicationRawValue.leaveType, {
        validators: [Validators.required],
      }),
      description: new FormControl(leaveApplicationRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      startDate: new FormControl(leaveApplicationRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(leaveApplicationRawValue.endDate, {
        validators: [Validators.required],
      }),
      isLineManagerApproved: new FormControl(leaveApplicationRawValue.isLineManagerApproved),
      isHRApproved: new FormControl(leaveApplicationRawValue.isHRApproved),
      isRejected: new FormControl(leaveApplicationRawValue.isRejected),
      rejectionComment: new FormControl(leaveApplicationRawValue.rejectionComment),
      isHalfDay: new FormControl(leaveApplicationRawValue.isHalfDay),
      durationInDay: new FormControl(leaveApplicationRawValue.durationInDay, {
        validators: [Validators.min(1)],
      }),
      phoneNumberOnLeave: new FormControl(leaveApplicationRawValue.phoneNumberOnLeave, {
       /*  validators: [Validators.pattern('^(?:\\+88|88)?(01[3-9]\\d{8})$')], */
        validators: CustomValidator.phoneNumberValidator()
      }),
      addressOnLeave: new FormControl(leaveApplicationRawValue.addressOnLeave, {
        validators: [Validators.maxLength(255), CustomValidator.naturalTextValidator()],
      }),
      reason: new FormControl(leaveApplicationRawValue.reason, {
       /*  validators: [Validators.maxLength(255), Validators.pattern("^[a-zA-Z0-9]+(?:[ ]?[a-zA-Z0-9]+)*|[ ]*$")], */
        validators: CustomValidator.alphaNumericValidator()
      }),
      employeeId: new FormControl(leaveApplicationRawValue.employeeId, {
        validators: [Validators.required],
      }),
    });
  }

  getLeaveApplication(form: LeaveApplicationFormGroup): ILeaveApplication | NewLeaveApplication {
    return this.convertLeaveApplicationRawValueToLeaveApplication(
      form.getRawValue() as LeaveApplicationFormRawValue | NewLeaveApplicationFormRawValue
    );
  }

  resetForm(form: LeaveApplicationFormGroup, leaveApplication: LeaveApplicationFormGroupInput): void {
    const leaveApplicationRawValue = this.convertLeaveApplicationToLeaveApplicationRawValue({
      ...this.getFormDefaults(),
      ...leaveApplication,
    });
    form.reset(
      {
        ...leaveApplicationRawValue,
        id: { value: leaveApplicationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LeaveApplicationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isLineManagerApproved: false,
      isHRApproved: false,
      isRejected: false,
      isHalfDay: false,
      sanctionedAt: currentTime,
    };
  }

  private convertLeaveApplicationRawValueToLeaveApplication(
    rawLeaveApplication: LeaveApplicationFormRawValue | NewLeaveApplicationFormRawValue
  ): ILeaveApplication | NewLeaveApplication {
    return {
      ...rawLeaveApplication,
      sanctionedAt: dayjs(rawLeaveApplication.sanctionedAt, DATE_TIME_FORMAT),
      //reason: rawLeaveApplication.id === undefined ? 'Automated Regularization by HR' : rawLeaveApplication.reason,
    };
  }

  private convertLeaveApplicationToLeaveApplicationRawValue(
    leaveApplication: ILeaveApplication | (Partial<NewLeaveApplication> & LeaveApplicationFormDefaults)
  ): LeaveApplicationFormRawValue | PartialWithRequiredKeyOf<NewLeaveApplicationFormRawValue> {
    return {
      ...leaveApplication,
      sanctionedAt: leaveApplication.sanctionedAt ? leaveApplication.sanctionedAt.format(DATE_TIME_FORMAT) : undefined,
      reason: leaveApplication.id === null ? 'Automated Regularization by HR' : leaveApplication.reason,
    };
  }
}
