import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { IUserWorkFromHomeApplication, UserWorkFromHomeApplication } from '../user-work-from-home-application.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import {CustomValidator} from "../../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserWorkFromHomeApplication for edit and UserWorkFromHomeApplicationFormGroupInput for create.
 */
type UserWorkFromHomeApplicationFormGroupInput = IUserWorkFromHomeApplication | PartialWithRequiredKeyOf<UserWorkFromHomeApplication>;

type UserWorkFromHomeApplicationFormDefaults = Pick<UserWorkFromHomeApplication, 'id'>;

type UserWorkFromHomeApplicationFormGroupContent = {
  id: FormControl<IUserWorkFromHomeApplication['id'] | UserWorkFromHomeApplication['id']>;
  startDate: FormControl<IUserWorkFromHomeApplication['startDate']>;
  endDate: FormControl<IUserWorkFromHomeApplication['endDate']>;
  reason: FormControl<IUserWorkFromHomeApplication['reason']>;
  duration: FormControl<IUserWorkFromHomeApplication['duration']>;
  status: FormControl<IUserWorkFromHomeApplication['status']>;
  appliedAt: FormControl<IUserWorkFromHomeApplication['appliedAt']>;
  updatedAt: FormControl<IUserWorkFromHomeApplication['updatedAt']>;
  createdAt: FormControl<IUserWorkFromHomeApplication['createdAt']>;
  sanctionedAt: FormControl<IUserWorkFromHomeApplication['sanctionedAt']>;
  appliedById: FormControl<IUserWorkFromHomeApplication['appliedById']>;
  createdById: FormControl<IUserWorkFromHomeApplication['createdById']>;
  updatedById: FormControl<IUserWorkFromHomeApplication['updatedById']>;
  sanctionedById: FormControl<IUserWorkFromHomeApplication['sanctionedById']>;
  employeeId: FormControl<IUserWorkFromHomeApplication['employeeId']>;
};

export type UserWorkFromHomeApplicationFormGroup = FormGroup<UserWorkFromHomeApplicationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserWorkFromHomeApplicationFormService {
  createUserWorkFromHomeApplicationFormGroup(
    userWorkFromHomeApplication: UserWorkFromHomeApplicationFormGroupInput = { id: null }
  ): UserWorkFromHomeApplicationFormGroup {
    const userWorkFromHomeApplicationRawValue = {
      ...this.getFormDefaults(),
      ...userWorkFromHomeApplication,
    };
    return new FormGroup<UserWorkFromHomeApplicationFormGroupContent>({
      id: new FormControl(
        { value: userWorkFromHomeApplicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(userWorkFromHomeApplicationRawValue.startDate, {
        validators: [Validators.required]
      }),
      endDate: new FormControl(userWorkFromHomeApplicationRawValue.endDate, {
        validators: [Validators.required]
      }),
      reason: new FormControl('', [Validators.required, Validators.minLength(0), Validators.maxLength(250), CustomValidator.naturalTextValidator()]),
      duration: new FormControl(userWorkFromHomeApplicationRawValue.duration),
      status: new FormControl(Status.PENDING),
      appliedAt: new FormControl(userWorkFromHomeApplicationRawValue.appliedAt),
      updatedAt: new FormControl(userWorkFromHomeApplicationRawValue.updatedAt),
      createdAt: new FormControl(userWorkFromHomeApplicationRawValue.createdAt),
      sanctionedAt: new FormControl(userWorkFromHomeApplicationRawValue.sanctionedAt),
      appliedById: new FormControl(userWorkFromHomeApplicationRawValue.appliedById),
      createdById: new FormControl(userWorkFromHomeApplicationRawValue.createdById),
      updatedById: new FormControl(userWorkFromHomeApplicationRawValue.updatedById),
      sanctionedById: new FormControl(userWorkFromHomeApplicationRawValue.sanctionedById),
      employeeId: new FormControl(userWorkFromHomeApplicationRawValue.employeeId),
    });
  }

  getUserWorkFromHomeApplication(form: UserWorkFromHomeApplicationFormGroup): IUserWorkFromHomeApplication | UserWorkFromHomeApplication {
    return form.getRawValue() as IUserWorkFromHomeApplication | UserWorkFromHomeApplication;
  }

  resetForm(form: UserWorkFromHomeApplicationFormGroup, userWorkFromHomeApplication: UserWorkFromHomeApplicationFormGroupInput): void {
    const userWorkFromHomeApplicationRawValue = { ...this.getFormDefaults(), ...userWorkFromHomeApplication };
    form.reset(
      {
        ...userWorkFromHomeApplicationRawValue,
        id: { value: userWorkFromHomeApplicationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserWorkFromHomeApplicationFormDefaults {
    return {
      id: null,
    };
  }
}
