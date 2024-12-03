import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserFeedback, NewUserFeedback } from '../user-feedback.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserFeedback for edit and NewUserFeedbackFormGroupInput for create.
 */
type UserFeedbackFormGroupInput = IUserFeedback | PartialWithRequiredKeyOf<NewUserFeedback>;

type UserFeedbackFormDefaults = Pick<NewUserFeedback, 'id'>;

type UserFeedbackFormGroupContent = {
  id: FormControl<IUserFeedback['id'] | NewUserFeedback['id']>;
  rating: FormControl<IUserFeedback['rating']>;
  suggestion: FormControl<IUserFeedback['suggestion']>;
  user: FormControl<IUserFeedback['user']>;
};

export type UserFeedbackFormGroup = FormGroup<UserFeedbackFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserFeedbackFormService {
  createUserFeedbackFormGroup(userFeedback: UserFeedbackFormGroupInput = { id: null }): UserFeedbackFormGroup {
    const userFeedbackRawValue = {
      ...this.getFormDefaults(),
      ...userFeedback,
    };
    return new FormGroup<UserFeedbackFormGroupContent>({
      id: new FormControl(
        { value: userFeedbackRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      rating: new FormControl(userFeedbackRawValue.rating, {
        validators: [Validators.required, Validators.min(1), Validators.max(10)],
      }),
      suggestion: new FormControl(userFeedbackRawValue.suggestion, {
        validators: [Validators.minLength(0), Validators.maxLength(255)],
      }),
      user: new FormControl(userFeedbackRawValue.user),
    });
  }

  getUserFeedback(form: UserFeedbackFormGroup): IUserFeedback | NewUserFeedback {
    return form.getRawValue() as IUserFeedback | NewUserFeedback;
  }

  resetForm(form: UserFeedbackFormGroup, userFeedback: UserFeedbackFormGroupInput): void {
    const userFeedbackRawValue = { ...this.getFormDefaults(), ...userFeedback };
    form.reset(
      {
        ...userFeedbackRawValue,
        id: { value: userFeedbackRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserFeedbackFormDefaults {
    return {
      id: null,
    };
  }
}
