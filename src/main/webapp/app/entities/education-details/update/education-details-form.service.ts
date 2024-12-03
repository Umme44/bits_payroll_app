import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEducationDetails, NewEducationDetails } from '../education-details.model';
import { CustomValidator } from '../../../validators/custom-validator';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEducationDetails for edit and NewEducationDetailsFormGroupInput for create.
 */
type EducationDetailsFormGroupInput = IEducationDetails | PartialWithRequiredKeyOf<NewEducationDetails>;

type EducationDetailsFormDefaults = Pick<NewEducationDetails, 'id'>;

type EducationDetailsFormGroupContent = {
  id: FormControl<IEducationDetails['id'] | NewEducationDetails['id']>;
  nameOfDegree: FormControl<IEducationDetails['nameOfDegree']>;
  subject: FormControl<IEducationDetails['subject']>;
  institute: FormControl<IEducationDetails['institute']>;
  yearOfDegreeCompletion: FormControl<IEducationDetails['yearOfDegreeCompletion']>;
  employeeId: FormControl<IEducationDetails['employeeId']>;
};

export type EducationDetailsFormGroup = FormGroup<EducationDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EducationDetailsFormService {
  createEducationDetailsFormGroup(educationDetails: EducationDetailsFormGroupInput = { id: null }): EducationDetailsFormGroup {
    const educationDetailsRawValue = {
      ...this.getFormDefaults(),
      ...educationDetails,
    };
    return new FormGroup<EducationDetailsFormGroupContent>({
      id: new FormControl(
        { value: educationDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nameOfDegree: new FormControl(educationDetailsRawValue.nameOfDegree, {
        validators: [CustomValidator.naturalTextValidator(), Validators.required],
      }),
      subject: new FormControl(educationDetailsRawValue.subject, {
        validators: [CustomValidator.naturalTextValidator(), Validators.required],
      }),
      institute: new FormControl(educationDetailsRawValue.institute, {
        validators: [CustomValidator.naturalTextValidator(), Validators.required],
      }),
      yearOfDegreeCompletion: new FormControl(educationDetailsRawValue.yearOfDegreeCompletion),
      employeeId: new FormControl(educationDetailsRawValue.employeeId),
    });
  }

  getEducationDetails(form: EducationDetailsFormGroup): IEducationDetails | NewEducationDetails {
    return form.getRawValue() as IEducationDetails | NewEducationDetails;
  }

  resetForm(form: EducationDetailsFormGroup, educationDetails: EducationDetailsFormGroupInput): void {
    const educationDetailsRawValue = { ...this.getFormDefaults(), ...educationDetails };
    form.reset(
      {
        ...educationDetailsRawValue,
        id: { value: educationDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EducationDetailsFormDefaults {
    return {
      id: null,
    };
  }
}
