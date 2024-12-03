import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWorkingExperience, NewWorkingExperience } from '../working-experience.model';
import { CustomValidator } from '../../../validators/custom-validator';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkingExperience for edit and NewWorkingExperienceFormGroupInput for create.
 */
type WorkingExperienceFormGroupInput = IWorkingExperience | PartialWithRequiredKeyOf<NewWorkingExperience>;

type WorkingExperienceFormDefaults = Pick<NewWorkingExperience, 'id'>;

type WorkingExperienceFormGroupContent = {
  id: FormControl<IWorkingExperience['id'] | NewWorkingExperience['id']>;
  lastDesignation: FormControl<IWorkingExperience['lastDesignation']>;
  organizationName: FormControl<IWorkingExperience['organizationName']>;
  dojOfLastOrganization: FormControl<IWorkingExperience['dojOfLastOrganization']>;
  dorOfLastOrganization: FormControl<IWorkingExperience['dorOfLastOrganization']>;
  employeeId: FormControl<IWorkingExperience['employeeId']>;
};

export type WorkingExperienceFormGroup = FormGroup<WorkingExperienceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkingExperienceFormService {
  createWorkingExperienceFormGroup(workingExperience: WorkingExperienceFormGroupInput = { id: null }): WorkingExperienceFormGroup {
    const workingExperienceRawValue = {
      ...this.getFormDefaults(),
      ...workingExperience,
    };
    return new FormGroup<WorkingExperienceFormGroupContent>({
      id: new FormControl(
        { value: workingExperienceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      lastDesignation: new FormControl(workingExperienceRawValue.lastDesignation, {
        validators: [CustomValidator.naturalTextValidator(), Validators.required],
      }),
      organizationName: new FormControl(workingExperienceRawValue.organizationName, {
        validators: [CustomValidator.naturalTextValidator(), Validators.required],
      }),
      dojOfLastOrganization: new FormControl(workingExperienceRawValue.dojOfLastOrganization),
      dorOfLastOrganization: new FormControl(workingExperienceRawValue.dorOfLastOrganization),
      employeeId: new FormControl(workingExperienceRawValue.employeeId),
    });
  }

  getWorkingExperience(form: WorkingExperienceFormGroup): IWorkingExperience | NewWorkingExperience {
    return form.getRawValue() as IWorkingExperience | NewWorkingExperience;
  }

  resetForm(form: WorkingExperienceFormGroup, workingExperience: WorkingExperienceFormGroupInput): void {
    const workingExperienceRawValue = { ...this.getFormDefaults(), ...workingExperience };
    form.reset(
      {
        ...workingExperienceRawValue,
        id: { value: workingExperienceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WorkingExperienceFormDefaults {
    return {
      id: null,
    };
  }
}
